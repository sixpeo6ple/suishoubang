package com.pj.project.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.utils.sg.*;
import java.util.*;

/**
 * 工具类：orders -- 订单表
 * @author xyy 
 *
 */
@Component
public class OrdersUtil {

	
	/** 底层 Mapper 对象 */
	public static OrdersMapper ordersMapper;
	@Autowired
	private void setOrdersMapper(OrdersMapper ordersMapper) {
		OrdersUtil.ordersMapper = ordersMapper;
	}
	
	
	/** 
	 * 将一个 Orders 对象进行进行数据完整性校验 (方便add/update等接口数据校验) [G] 
	 */
	static void check(Orders o) {
		AjaxError.throwByIsNull(o.id, "[订单id] 不能为空");		// 验证: 订单id 
		AjaxError.throwByIsNull(o.outTradeNo, "[微信支付id] 不能为空");		// 验证: 微信支付id 
		AjaxError.throwByIsNull(o.createTime, "[创建日期] 不能为空");		// 验证: 创建日期 
		AjaxError.throwByIsNull(o.payerOpenid, "[支付者openid] 不能为空");		// 验证: 支付者openid 
		AjaxError.throwByIsNull(o.receiverOpenid, "[接单者openid] 不能为空");		// 验证: 接单者openid 
		AjaxError.throwByIsNull(o.picUrl, "[身份码照片] 不能为空");		// 验证: 身份码照片 
		AjaxError.throwByIsNull(o.info, "[取货码] 不能为空");		// 验证: 取货码 
		AjaxError.throwByIsNull(o.price, "[价格] 不能为空");		// 验证: 价格 
		AjaxError.throwByIsNull(o.deliverPlace, "[送货地址] 不能为空");		// 验证: 送货地址 
		AjaxError.throwByIsNull(o.deliverTime, "[送货日期] 不能为空");		// 验证: 送货日期 
		AjaxError.throwByIsNull(o.deliverType, "[送货方式] 不能为空");		// 验证: 送货方式 
		AjaxError.throwByIsNull(o.type, "[种类] 不能为空");		// 验证: 种类 
		AjaxError.throwByIsNull(o.status, "[状态] 不能为空");		// 验证: 状态 (1=订单创建, 2=订单进行, 3=订单完成) 
	}

	/** 
	 * 获取一个Orders (方便复制代码用) [G] 
	 */ 
	static Orders getOrders() {
		Orders o = new Orders();	// 声明对象 
		o.id = 0;		// 订单id 
		o.outTradeNo = "";		// 微信支付id 
		o.createTime = new Date();		// 创建日期 
		o.payerOpenid = "";		// 支付者openid 
		o.receiverOpenid = "";		// 接单者openid 
		o.picUrl = "";		// 身份码照片 
		o.info = "";		// 取货码 
		o.price = 0.0;		// 价格 
		o.deliverPlace = "";		// 送货地址 
		o.deliverTime = new Date();		// 送货日期 
		o.deliverType = "";		// 送货方式 
		o.type = "";		// 种类 
		o.status = 0;		// 状态 (1=订单创建, 2=订单进行, 3=订单完成) 
		return o;
	}
	
	
	
	
	
}
