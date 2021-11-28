package com.xyy.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import cn.dev33.satoken.stp.StpUtil;
import com.xyy.pojo.QueryOrder;
import com.xyy.pojo.WxPayArgs;
import com.xyy.service.handler.WxV3PayMessageHandler;
import com.xyy.utils.ResponseResult;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.RefundResult;
import com.egzosn.pay.web.support.HttpRequestNoticeParams;
import com.egzosn.pay.wx.v3.api.WxPayConfigStorage;
import com.egzosn.pay.wx.v3.api.WxPayService;
import com.egzosn.pay.wx.v3.bean.WxTransactionType;
import com.egzosn.pay.wx.v3.bean.order.H5Info;
import com.egzosn.pay.wx.v3.bean.order.SceneInfo;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 微信V3发起支付入口
 *
 * @author egan
 * email egzosn@gmail.com
 * date 2016/11/18 0:25
 */
@RestController
@RequestMapping("wxV3")
public class WxV3PayController {

    private WxPayService service = null;


    @PostConstruct  //没有证书的情况下注释掉，避免启动报错
    public void init() {
        //Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
        wxPayConfigStorage.setAppId(WxPayArgs.APP_ID);
        wxPayConfigStorage.setMchId(WxPayArgs.MCH_ID);
        //wxPayConfigStorage.setSignType("SHA256withRSA");
        //V3密钥 https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay3_2.shtml
        wxPayConfigStorage.setV3ApiKey(WxPayArgs.API_V3KEY);
        wxPayConfigStorage.setNotifyUrl(WxPayArgs.NOTIFY_URL);
        wxPayConfigStorage.setReturnUrl(WxPayArgs.RETURN_URL);
        wxPayConfigStorage.setInputCharset("utf-8");
        //使用证书时设置为true
        wxPayConfigStorage.setCertSign(true);
        //商户API证书 https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay3_1.shtml
        wxPayConfigStorage.setApiClientKeyP12("apiclient_cert.p12");
        wxPayConfigStorage.setCertStoreType(CertStoreType.CLASS_PATH);
        service = new WxPayService(wxPayConfigStorage);
        //设置回调消息处理
        //TODO {@link com.egzosn.pay.demo.controller.WxPayController#payBack}
        service.setPayMessageHandler(new WxV3PayMessageHandler());
    }


    /**
     * 公众号(微信小程序)支付
     *
     //* @param openid openid
     //* @param price  金额
     * @return 返回jsapi所需参数
     */
    @RequestMapping(value = "jsapi")
    //public Map toPay(String openid, BigDecimal price) {
    public Object toPay() {
        String openid = StpUtil.getLoginIdAsString();
        BigDecimal price=BigDecimal.valueOf(0.01);
        PayOrder order = new PayOrder("订单title", "商品详情",
                null == price ? BigDecimal.valueOf(0.01) : price,
                UUID.randomUUID().toString().replace("-", ""),
                WxTransactionType.JSAPI);
        order.setOpenid(openid);

        Map orderInfo = service.orderInfo(order);

        return ResponseResult.success(orderInfo);
    }



    /**
     * 支付回调地址
     *
     * @param request 请求
     * @return 是否成功
     * <p>
     * 业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link com.egzosn.pay.common.api.PayService#setPayMessageHandler(com.egzosn.pay.common.api.PayMessageHandler)}
     * <p>
     * 如果未设置 {@link com.egzosn.pay.common.api.PayMessageHandler} 那么会使用默认的 {@link com.egzosn.pay.common.api.DefaultPayMessageHandler}
     */
    @RequestMapping(value = "payBack.json")
    public String payBack(HttpServletRequest request) {
        //业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看com.egzosn.pay.common.api.PayService.setPayMessageHandler()
        return service.payBack(new HttpRequestNoticeParams(request)).toMessage();
    }


    /**
     * 查询
     *
     * @param order 订单的请求体
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @RequestMapping("query")
    public Map<String, Object> query(QueryOrder order) {
        //tradeNo微信支付单号  outTradeNo商户订单号
        System.out.println(order);
        return service.query(new AssistOrder(order.getTradeNo(),
                order.getOutTradeNo()));
    }


    /**
     * 交易关闭接口
     *
     * @param order 订单的请求体
     * @return 返回支付方交易关闭后的结果
     */
    @RequestMapping("close")
    public Map<String, Object> close(QueryOrder order) {
        return service.close(new AssistOrder(order.getTradeNo(), order.getOutTradeNo()));
    }

    /**
     * 申请退款接口
     *
     * @param order 订单的请求体
     * @return 返回支付方申请退款后的结果
     */
    @RequestMapping("refund")
    public RefundResult refund(RefundOrder order) {

        return service.refund(order);
    }

    /**
     * 查询退款
     *
     * @param order 订单的请求体
     * @return 返回支付方查询退款后的结果
     */
    @RequestMapping("refundquery")
    public Map<String, Object> refundquery(RefundOrder order) {
        return service.refundquery(order);
    }

    /**
     * 下载对账单
     *
     * @param order 订单的请求体
     * @return 返回支付方下载对账单的结果
     */
    @RequestMapping("downloadbill")
    public Object downloadBill(QueryOrder order) {
        return service.downloadBill(order.getBillDate(), order.getBillType());
    }


}
