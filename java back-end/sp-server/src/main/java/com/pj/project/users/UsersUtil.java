package com.pj.project.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.utils.sg.*;

/**
 * 工具类：users -- 用户表
 * @author xyy 
 *
 */
@Component
public class UsersUtil {

	
	/** 底层 Mapper 对象 */
	public static UsersMapper usersMapper;
	@Autowired
	private void setUsersMapper(UsersMapper usersMapper) {
		UsersUtil.usersMapper = usersMapper;
	}
	
	
	/** 
	 * 将一个 Users 对象进行进行数据完整性校验 (方便add/update等接口数据校验) [G] 
	 */
	static void check(Users u) {
		AjaxError.throwByIsNull(u.id, "[学生id] 不能为空");		// 验证: 学生id 
		AjaxError.throwByIsNull(u.avatarUrl, "[用户头像] 不能为空");		// 验证: 用户头像
		AjaxError.throwByIsNull(u.openid, "[学生openid] 不能为空");		// 验证: 学生openid
		AjaxError.throwByIsNull(u.isStudent, "[用户状态] 不能为空");		// 验证: 用户状态 (0=普通用户, 1=学生用户) 
		AjaxError.throwByIsNull(u.picUrl, "[学生证照片] 不能为空");		// 验证: 学生证照片 
		AjaxError.throwByIsNull(u.name, "[姓名] 不能为空");		// 验证: 姓名 
		AjaxError.throwByIsNull(u.sid, "[学号] 不能为空");		// 验证: 学号 
		AjaxError.throwByIsNull(u.place, "[地点] 不能为空");		// 验证: 地点 
		AjaxError.throwByIsNull(u.phone, "[电话] 不能为空");		// 验证: 电话 
		AjaxError.throwByIsNull(u.reviewStatus, "[审核状态] 不能为空");		// 验证: 审核状态 (0=无状态, 1=审核中, 2=审核成功, 3=审核失败) 
	}

	/** 
	 * 获取一个Users (方便复制代码用) [G] 
	 */
	static Users getUsers() {
		Users u = new Users();	// 声明对象 
		u.id = 0;		// 学生id 
		u.avatarUrl = "";		// 用户头像
		u.openid = "";		// 学生openid
		u.isStudent = "";		// 用户状态 (0=普通用户, 1=学生用户) 
		u.picUrl = "";		// 学生证照片 
		u.name = "";		// 姓名 
		u.sid = "";		// 学号 
		u.place = "";		// 地点 
		u.phone = "";		// 电话 
		u.reviewStatus = 0;		// 审核状态 (0=无状态, 1=审核中, 2=审核成功, 3=审核失败) 
		return u;
	}
	
	
	
	
	
}
