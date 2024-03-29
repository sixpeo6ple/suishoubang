package com.xyy.service.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.egzosn.pay.common.api.DefaultPayMessageHandler;
import com.egzosn.pay.common.api.PayMessageHandler;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.wx.v3.bean.response.WxPayMessage;

/**
 * 微信支付回调处理器
 * Created by ZaoSheng on 2016/6/1.
 */
public class WxV3PayMessageHandler implements PayMessageHandler<WxPayMessage, PayService> {

    private final Logger LOG = LoggerFactory.getLogger(DefaultPayMessageHandler.class);

    @Override
    public PayOutMessage handle(WxPayMessage payMessage, Map<String, Object> context, PayService payService) throws PayErrorException {
        String s = JSON.toJSONString(payMessage);
        LOG.info("map中的内容：{}", context);
        LOG.info("回调支付消息处理器，回调消息：{}", JSON.toJSONString(payMessage));
        return payService.successPayOutMessage(payMessage);
    }
}
