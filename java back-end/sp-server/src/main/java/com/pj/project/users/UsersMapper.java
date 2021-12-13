package com.pj.project.users;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pj.utils.so.*;
import org.springframework.stereotype.Repository;

/**
 * Mapper: users -- 用户表
 * @author xyy 
 */

@Mapper
@Repository
public interface UsersMapper {


	List<UserCnt> cnt();


	/**
	 * 增  
	 * @param u 实体对象 
	 * @return 受影响行数 
	 */
	int add(Users u);


	/**
	 * 删  
	 * @param id 要删除的数据id  
	 * @return 受影响行数 
	 */
	int delete(Integer id);	 

	/** 
	 * 改  
	 * @param u 实体对象 
	 * @return 受影响行数 
	 */
	int update(Users u);

	/** 
	 * 查 - 根据id  
	 * @param id 要查询的数据id 
	 * @return 实体对象 
	 */
	Users getById(Integer id);	 

	/**
	 * 查集合 - 根据条件（参数为空时代表忽略指定条件）
	 * @param so 参数集合 
	 * @return 数据列表 
	 */
	List<Users> getList(SoMap so);


}
