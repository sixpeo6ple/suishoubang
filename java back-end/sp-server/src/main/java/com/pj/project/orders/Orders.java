package com.pj.project.orders;

import java.io.Serializable;
import java.util.*;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Model: orders -- 订单表
 * @author xyy 
 */
@Data
@Accessors(chain = true)
public class Orders implements Serializable {

	// ---------- 模块常量 ----------
	/**
	 * 序列化版本id 
	 */
	private static final long serialVersionUID = 1L;	
	/**
	 * 此模块对应的表名 
	 */
	public static final String TABLE_NAME = "orders";	
	/**
	 * 此模块对应的权限码 
	 */
	public static final String PERMISSION_CODE = "orders";	


	// ---------- 表中字段 ----------
	/**
	 * 订单id 
	 */
	public Integer id;	

	/**
	 * 微信支付id 
	 */
	public String outTradeNo;	

	/**
	 * 创建日期 
	 */
	public Date createTime;	

	/**
	 * 支付者openid 
	 */
	public String payerOpenid;	

	/**
	 * 接单者openid 
	 */
	public String receiverOpenid;	

	/**
	 * 身份码照片 
	 */
	public String picUrl;	

	/**
	 * 取货码 
	 */
	public String info;	

	/**
	 * 价格 
	 */
	public Double price;	

	/**
	 * 送货地址 
	 */
	public String deliverPlace;	

	/**
	 * 送货日期 
	 */
	public Date deliverTime;	

	/**
	 * 送货方式 
	 */
	public String deliverType;	

	/**
	 * 种类 
	 */
	public String type;	

	/**
	 * 状态 (1=订单创建, 2=订单进行, 3=订单完成) 
	 */
	public Integer status;	





	


}
