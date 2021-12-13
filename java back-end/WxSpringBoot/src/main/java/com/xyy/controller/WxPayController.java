
package com.xyy.controller;


import cn.hutool.core.util.RandomUtil;
import com.egzosn.pay.common.bean.*;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.web.support.HttpRequestNoticeParams;
import com.egzosn.pay.wx.api.WxPayConfigStorage;
import com.egzosn.pay.wx.api.WxPayService;
import com.egzosn.pay.wx.bean.*;
import com.xyy.pojo.QueryOrder;
import com.xyy.pojo.WxPayArgs;
import com.xyy.utils.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 发起支付入口
 *
 * @author egan
 * email egzosn@gmail.com
 * date 2016/11/18 0:25
 */
@RestController
@RequestMapping("wx")
public class WxPayController {

    private WxPayService service = null;



    ////ssl 退款证书相关 不使用可注释
    //private static String KEYSTORE = "ssl 退款证书";

    @PostConstruct
    public void init() {
        WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
        wxPayConfigStorage.setAppId(WxPayArgs.APP_ID);

        wxPayConfigStorage.setMchId(WxPayArgs.MCH_ID);
        //以下两个参数在 服务商版模式中必填--------
//        wxPayConfigStorage.setSubAppid("子商户公众账号ID ");
//        wxPayConfigStorage.setSubMchId("微信支付分配的子商户号 ");
        //-----------------------------------------------
        wxPayConfigStorage.setKeyPublic("");
        wxPayConfigStorage.setSecretKey(WxPayArgs.API_KEY);
        wxPayConfigStorage.setNotifyUrl(WxPayArgs.NOTIFY_URL);
        wxPayConfigStorage.setReturnUrl(WxPayArgs.RETURN_URL);
        wxPayConfigStorage.setSignType("MD5");
        wxPayConfigStorage.setInputCharset("utf-8");


        service = new WxPayService(wxPayConfigStorage);

        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();

        //ssl 退款证书相关 不使用可注释
        //httpConfigStorage.setKeystore(WxPayController.class.getResourceAsStream("D:\\programming\\java\\code\\WxSpringBoot\\src\\main\\resources\\apiclient_cert.p12"));
        //httpConfigStorage.setStorePassword(WxPayArgs.MCH_ID);
        ////设置ssl证书对应的存储方式，这里默认为文件地址
        //httpConfigStorage.setCertStoreType(CertStoreType.PATH);



        //请求连接池配置
        //最大连接数
        httpConfigStorage.setMaxTotal(20);
        //默认的每个路由的最大连接数
        httpConfigStorage.setDefaultMaxPerRoute(10);
        service.setRequestTemplateConfigStorage(httpConfigStorage);

        //设置回调消息处理
        //TODO {@link com.egzosn.pay.demo.controller.WxPayController#payBack}
//        service.setPayMessageHandler(new WxPayMessageHandler(null));
    }





    /**
     * 公众号支付
     *
     *
     * @return 返回jsapi所需参数
     */
    @RequestMapping(value = "jsapi" )
    public Object toPay() {

        PayOrder order = new PayOrder("订单title", "摘要", BigDecimal.valueOf(0.01) , UUID.randomUUID().toString().replace("-", ""), WxTransactionType.JSAPI);
        order.setOpenid("onLBk5Wz3Nn48W-9IEqLF-eOQ1aM");

        Map orderInfo = service.orderInfo(order);
        orderInfo.put("code", 0);

        return ResponseResult.success(orderInfo);
    }







    /**
     * 转账到余额
     *
     * @param order 转账订单
     *
     * @return 对应的转账结果
     */
    @RequestMapping("transfer")
    public Map<String, Object> transfer(TransferOrder order) {
        String outNo = RandomUtil.randomString(16);
        order.setOutNo(outNo);
        order.setPayeeAccount("onLBk5Wz3Nn48W-9IEqLF-eOQ1aM");
        //order.setPayeeName("收款用户姓名， 非必填，如果填写将强制验证收款人姓名");
        order.setRemark("转账备注, 非必填");
        order.setAmount(new BigDecimal("0.01"));

        //转账到余额，这里默认值是转账到银行卡
        order.setTransferType(WxTransferType.TRANSFERS);

        return service.transfer(order);
    }





}
