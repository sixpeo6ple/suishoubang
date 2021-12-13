package com.xyy.config;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xyy.entity.Users;
import com.xyy.service.UsersService;
import org.springframework.stereotype.Component;
import cn.dev33.satoken.stp.StpInterface;

import javax.annotation.Resource;

/**
 * 自定义权限验证接口扩展 
 */
@Component    // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展 
public class PermissionValidationConfig implements StpInterface {

    @Resource
    private UsersService service;

    /**
     * 返回一个账号所拥有的权限码集合 
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        List<String> list = new ArrayList<>();
        //list.add("101");
        //list.add("user-add");
        //list.add("user-delete");
        //list.add("user-update");
        //list.add("user-get");
        //list.add("article-get");
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {

        //System.out.println(loginId+","+loginType);
        List<String> list = new ArrayList<>();
        //查询当前用户信息 根据有没有学生认证添加权限
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getOpenid,loginId);
        Users users = service.getOne(wrapper);
        if(users.getIsStudent()){
            list.add("student");
            return list;
        }
        return list;
    }

}
