package com.xyy.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xyy.entity.User;
import com.xyy.pojo.WxloginRes;
import com.xyy.service.UserService;
import com.xyy.utils.ResponseResult;
import com.xyy.utils.WeiXinLoginUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author WhiteRunner
 * @create 2021-11-21 14:37
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    private UserService service;
    @Resource
    private WeiXinLoginUtils WxUtils;

    @GetMapping("/doLogin")
    public Object doLogin(String code){

        if (code==null){
            return ResponseResult.error("没有收到code");
        }
        //根据code获取用户openid
        WxloginRes loginResult = WxUtils.getLoginResult(code);
        if(loginResult.getErrCode()!=null) {
            return ResponseResult.error(
                    loginResult, "获取openid失败");
        }else {
            String openid = loginResult.getOpenid();
            //根据openId从数据库里找该用户
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getOpenid,openid);
            User user = service.getOne(wrapper);
            //如果user为null，将openId插入数据库
            if (user == null) {
                User newUser = new User();
                newUser.setOpenid(openid);
                service.save(newUser);
            }
            //根据openId生成token,自动保存到redis中
            StpUtil.login(openid);
            //获取token的值保存到map中
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            HashMap<String, String> map = new HashMap<>();
            map.put("tokenName",tokenInfo.getTokenName());
            map.put("tokenValue",tokenInfo.getTokenValue());
            map.put("session_key", loginResult.getSessionKey());
            map.put("openid", loginResult.getOpenid());
            //封装返回值
            return ResponseResult.success(map,"登录成功");
        }

    }

    // 查询登录状态
    @GetMapping("/isLogin")
    public Object isLogin() {
        if(StpUtil.isLogin()){
            if(StpUtil.hasRole("student")){
                return ResponseResult.success("学生用户登录");
            }else {
                return ResponseResult.success("普通用户登录");
            }
        }else {
            return ResponseResult.error("未登录");
        }
    }

    @PutMapping("/StudentAuth")
    public Object StudentAuth(){
        String openid = StpUtil.getLoginIdAsString();
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getOpenid,openid);
        wrapper.set(User::getIsStudent,1);
        service.update(wrapper);
        return ResponseResult.success("切换为学生用户");
    }

    @PutMapping("/CancelStudentAuth")
    public Object CancelStudentAuth(){
        String openid = StpUtil.getLoginIdAsString();
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getOpenid,openid);
        wrapper.set(User::getIsStudent,0);
        service.update(wrapper);
        return ResponseResult.success("切换为普通用户");
    }

}
