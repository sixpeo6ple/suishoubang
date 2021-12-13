package com.pj.project.users;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Model: users -- 用户表
 * @author xyy 
 */
@Data
@Accessors(chain = true)
public class Users implements Serializable {

	// ---------- 模块常量 ----------
	/**
	 * 序列化版本id 
	 */
	private static final long serialVersionUID = 1L;	
	/**
	 * 此模块对应的表名 
	 */
	public static final String TABLE_NAME = "users";	
	/**
	 * 此模块对应的权限码 
	 */
	public static final String PERMISSION_CODE = "users";	


	// ---------- 表中字段 ----------
	/**
	 * 学生id 
	 */
	public Integer id;

	/**
	 * 用户头像
	 */
	public String avatarUrl;

	/**
	 * 学生openid 
	 */
	public String openid;	

	/**
	 * 用户状态 (0=普通用户, 1=学生用户) 
	 */
	public String isStudent;	

	/**
	 * 学生证照片 
	 */
	public String picUrl;	

	/**
	 * 姓名 
	 */
	public String name;	

	/**
	 * 学号 
	 */
	public String sid;	

	/**
	 * 地点 
	 */
	public String place;	

	/**
	 * 电话 
	 */
	public String phone;	

	/**
	 * 审核状态 (0=无状态, 1=审核中, 2=审核成功, 3=审核失败) 
	 */
	public Integer reviewStatus;	
	
}
