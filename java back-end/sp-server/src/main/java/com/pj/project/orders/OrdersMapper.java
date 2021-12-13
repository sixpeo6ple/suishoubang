package com.pj.project.orders;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pj.utils.so.*;
import org.springframework.stereotype.Repository;

/**
 * Mapper: orders -- 订单表
 * @author xyy 
 */

@Mapper
@Repository
public interface OrdersMapper {

	/**
	 * 增  
	 * @param o 实体对象 
	 * @return 受影响行数 
	 */
	int add(Orders o);

	/**
	 * 删  
	 * @param id 要删除的数据id  
	 * @return 受影响行数 
	 */
	int delete(Integer id);	 

	/** 
	 * 改  
	 * @param o 实体对象 
	 * @return 受影响行数 
	 */
	int update(Orders o);

	/** 
	 * 查 - 根据id  
	 * @param id 要查询的数据id 
	 * @return 实体对象 
	 */
	Orders getById(Integer id);	 

	/**
	 * 查集合 - 根据条件（参数为空时代表忽略指定条件）
	 * @param so 参数集合 
	 * @return 数据列表 
	 */
	List<Orders> getList(SoMap so);

	List<OrderCnt> cnt();
}
