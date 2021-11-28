package com.xyy.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.wx.v3.api.WxPayConfigStorage;
import com.egzosn.pay.wx.v3.api.WxPayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import com.xyy.pojo.WxLoginParams;
import com.xyy.pojo.WxPayArgs;
import com.xyy.utils.ResponseResult;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * @author WhiteRunner
 * @create 2021-11-21 19:38
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @GetMapping("/doPay")
    public Object doPay() throws Exception {
        if(!StpUtil.isLogin()){
            return ResponseResult.error("请登录后重试");
        }
        String openid = StpUtil.getLoginIdAsString();
        PrivateKey merchantPrivateKey = PemUtil.
                loadPrivateKey(new FileInputStream(ResourceUtils.getFile(WxPayArgs.PRIVATE_KEY_PATH)));

        //使用自动更新的签名验证器，不需要传入证书
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(WxPayArgs.MCH_ID, new PrivateKeySigner(WxPayArgs.MCH_SERIAL_NO, merchantPrivateKey)),
                WxPayArgs.API_V3KEY.getBytes(StandardCharsets.UTF_8));

        CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(WxPayArgs.MCH_ID, WxPayArgs.MCH_SERIAL_NO, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier))
                .build();

        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type","application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid", WxPayArgs.MCH_ID)
                .put("appid", WxPayArgs.APP_ID)
                .put("notify_url", WxPayArgs.NOTIFY_URL)
                .put("description", "test")
                .put("out_trade_no", System.currentTimeMillis()+"");
        rootNode.putObject("amount")//付了多少钱(单位是分)
                .put("total", 1);
        rootNode.putObject("payer")//付钱的人的openid
                .put("openid", openid);

        objectMapper.writeValue(bos, rootNode);

        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String bodyAsString = EntityUtils.toString(response.getEntity());
        JSONObject bodyAsJson = JSONObject.parseObject(bodyAsString);
        if(bodyAsJson.containsKey("code")) {
            return ResponseResult.success(bodyAsJson.getString("message"));
        }

        final String prepay_id = bodyAsJson.getString("prepay_id");
        long time = System.currentTimeMillis();
        final String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(time));
        final String nonceStr = RandomUtil.randomString(32);

        String str = WxPayArgs.APP_ID + "\n" +
                timeStamp + "\n" +
                nonceStr + "\n" +
                "prepay_id=" + prepay_id + "\n";
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(merchantPrivateKey);
        signature.update(str.getBytes(StandardCharsets.UTF_8));
        byte[] signBytes = signature.sign();
        String paySign = Base64.getEncoder().encodeToString(signBytes);

        JSONObject params = new JSONObject();
        params.put("appId", WxPayArgs.APP_ID);
        params.put("timeStamp", timeStamp);
        params.put("nonceStr", nonceStr);
        params.put("package", "prepay_id="+prepay_id);
        params.put("signType", "RSA");
        params.put("paySign", paySign);
        return ResponseResult.success(params,"ok");
    }

}
