package com.pj.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.project.orders.OrdersMapper;
import com.pj.project.users.UsersMapper;
import com.pj.project4sp.public4mapper.PublicMapper;
import com.pj.project4sp.public4mapper.PublicService;

/**
 * SpringBean依赖清单，项目中所有Bean在此定义
 */
@Component
public class FC {

	// ======================================== 所有Mapper ============================================== 

	public static OrdersMapper ordersMapper;		// Mapper依赖：订单表
	public static UsersMapper usersMapper;		// Mapper依赖：用户表
	public static PublicMapper publicMapper;					// Mapper: 公共Mapper 



	// ======================================== 所有Service ============================================== 

	public static PublicService publicService;						// Service：公共service



	// ======================================== 所有注入所有Bean ============================================== 
	
	// 注入 
	@Autowired
	public void setBean(
			OrdersMapper ordersMapper, 
			UsersMapper usersMapper, 
			PublicMapper publicMapper,
			PublicService publicService
			) {
			FC.ordersMapper = ordersMapper;
			FC.usersMapper = usersMapper;
			FC.publicMapper = publicMapper;
			FC.publicService = publicService;
	}


}