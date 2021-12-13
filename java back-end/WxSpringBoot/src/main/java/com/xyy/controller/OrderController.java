package com.xyy.controller;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.egzosn.pay.common.bean.*;
import com.egzosn.pay.wx.v3.bean.WxTransactionType;
import com.xyy.entity.OrderUser;
import com.xyy.entity.Orders;
import com.xyy.pojo.QueryOrder;
import com.xyy.pojo.WxPayArgs;
import com.xyy.service.OrdersService;
import com.xyy.service.handler.WxV3PayMessageHandler;
import com.xyy.utils.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.egzosn.pay.web.support.HttpRequestNoticeParams;
import com.egzosn.pay.wx.v3.api.WxPayConfigStorage;
import com.egzosn.pay.wx.v3.api.WxPayService;

/**
 * 微信V3发起支付入口
 *
 * @author egan
 * email egzosn@gmail.com
 * date 2016/11/18 0:25
 */
@RestController
@RequestMapping("order")
public class OrderController {

    private WxPayService service = null;
    @Resource
    private OrdersService ordersService;


    @PostConstruct
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
        //商户API证书
        wxPayConfigStorage.setApiClientKeyP12("apiclient_cert.p12");
        wxPayConfigStorage.setCertStoreType(CertStoreType.CLASS_PATH);
        service = new WxPayService(wxPayConfigStorage);
        //设置回调消息处理
        //TODO {@link com.egzosn.pay.demo.controller.WxPayController#payBack}
        service.setPayMessageHandler(new WxV3PayMessageHandler());
    }


    @RequestMapping(value = "create")
    public Object createPay(Orders orders) {
        if(!StpUtil.hasRole("student")){
            throw new RuntimeException("没有通过学生认证");
        }
        String payerOpenid = StpUtil.getLoginIdAsString();
        String outTradeNo=UUID.randomUUID().toString().replace("-", "");
        //保存到数据库
        orders.setPayerOpenid(payerOpenid);
        orders.setOutTradeNo(outTradeNo);
        orders.setCreateTime(LocalDateTime.now());
        orders.setStatus(1);
        ordersService.save(orders);
        //下单
        PayOrder payOrder = new PayOrder(
                "订单title",
                orders.getType(),
                orders.getPrice(),
                outTradeNo,
                WxTransactionType.JSAPI);
        payOrder.setOpenid(payerOpenid);
        Map orderInfo = service.orderInfo(payOrder);
        orderInfo.put("outTradeNo",outTradeNo);
        return ResponseResult.success(orderInfo,"下单成功");
    }

    @RequestMapping(value = "updatePic") //id + picUrl
    public Object uploadPic(Orders orders){
        boolean b = ordersService.updateById(orders);
        if(b){
            return ResponseResult.success("成功更新身份码图片");
        }else{
            throw new RuntimeException("更新失败");
        }
    }

    @RequestMapping(value = "receiveOrder")
    public Object receiveOrder(Integer id){
        if(!StpUtil.hasRole("student")){
            throw new RuntimeException("没有通过学生认证");
        }
        String openid = StpUtil.getLoginIdAsString();
        LambdaUpdateWrapper<Orders> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Orders::getId,id);
        wrapper.set(Orders::getStatus,2);
        wrapper.set(Orders::getReceiverOpenid,openid);
        ordersService.update(wrapper);
        return ResponseResult.success("接单成功");
    }

    @RequestMapping(value = "finishOrder")
    public Object finishOrder(Integer id){
        if(!StpUtil.hasRole("student")){
            throw new RuntimeException("没有通过学生认证");
        }
        String openid = StpUtil.getLoginIdAsString();
        LambdaUpdateWrapper<Orders> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Orders::getId,id);
        wrapper.eq(Orders::getPayerOpenid,openid);
        wrapper.set(Orders::getStatus,3);
        ordersService.update(wrapper);
        HashMap<String, String> map = new HashMap<>();
        map.put("status","3");
        return ResponseResult.success(map,"修改订单为完成状态成功");
    }

    @RequestMapping(value = "delOrder")
    public Object delOrder(String outTradeNo){
        if(!StpUtil.hasRole("student")){
            throw new RuntimeException("没有通过学生认证");
        }
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getOutTradeNo,outTradeNo);
        ordersService.remove(wrapper);
        return ResponseResult.success("删除订单outTradeNo="+outTradeNo);
    }

    /**
     * 展示订单
     */
    @RequestMapping(value = "showOrderById")
    public Object showOrderById(Integer id){
        if(!StpUtil.hasRole("student")){
            throw new RuntimeException("没有通过学生认证");
        }
        //List<OrderUser> orderUsers = ordersService.listAllOrders(0,id,"","");
        List<OrderUser> one = ordersService.getOneById(id);
        return ResponseResult.success(one, "返回指定id订单成功");
    }

    @RequestMapping(value = "showMyOrder")
    public Object showMyOrder(Integer status,
                              @RequestParam(required = false,defaultValue = "") String personType,
                              String orderType){
        if(!StpUtil.hasRole("student")){
            throw new RuntimeException("没有通过学生认证");
        }
        String openid = StpUtil.getLoginIdAsString();
        List<OrderUser> orderUsers = ordersService.listMyOrders(status, openid, orderType,personType);
        if (orderUsers != null) {
            return ResponseResult.success(orderUsers,"成功");
        }else {
            throw new RuntimeException("获取用户订单失败");
        }
    }

    @RequestMapping(value = "showAll")
    public Object showAll(Integer status,@RequestParam(required = false,defaultValue = "") String type){
        //List<OrderUser> orderUsers = ordersService.listAllOrders(status,-1,"","");
        List<OrderUser> orderUsers = ordersService.listOrders(status,type);
        return ResponseResult.success(orderUsers,"成功返回");
    }

    @RequestMapping(value = "showMyOrderWithPage")
    public Object showMyOrder(Integer status,Integer size,Integer currentPage,
                              @RequestParam(required = false,defaultValue = "") String orderType,String personType){
        if(!StpUtil.hasRole("student")){
            throw new RuntimeException("没有通过学生认证");
        }
        String openid = StpUtil.getLoginIdAsString();
        IPage<OrderUser> page = ordersService.listMyOrdersWithPage(status, openid, currentPage, size, orderType,personType);
        if (page.getRecords() != null) {
            return ResponseResult.success(page.getRecords(),"成功");
        }else {
            throw new RuntimeException("获取用户订单失败");
        }
    }

    @RequestMapping(value = "showAllWithPage")
    public Object showAll(Integer status,Integer size,Integer currentPage,@RequestParam(required = false,defaultValue = "") String type){
        //List<OrderUser> orderUsers = ordersService.listAllOrders(status,-1,"","");
        IPage<OrderUser> page = ordersService.listOrdersWithPage(status, size, currentPage,type);
        return ResponseResult.success(page.getRecords(),"成功返回");
    }








    /**
     * 支付回调地址
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
     //* @param order 订单的请求体
     * @return 返回支付方申请退款后的结果
     */
    @RequestMapping("refund")//RefundOrder order
    public RefundResult refund(String id) {
        //改变订单状态
        Orders order = ordersService.getById(id);
        order.setStatus(4);
        ordersService.updateById(order);
        //退款单信息
        RefundOrder rOrder = new RefundOrder();
        rOrder.setOutTradeNo(order.getOutTradeNo());
        rOrder.setRefundAmount(order.getPrice());
        rOrder.setTotalAmount(order.getPrice());
        //退款单号
        String refundNo = RandomUtil.randomString(16);
        rOrder.setRefundNo(refundNo);
        return service.refund(rOrder);
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




}
