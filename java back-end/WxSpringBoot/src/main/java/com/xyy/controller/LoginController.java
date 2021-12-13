package com.xyy.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xyy.entity.Users;
import com.xyy.pojo.WxloginRes;
import com.xyy.service.UsersService;
import com.xyy.utils.ResponseResult;
import com.xyy.utils.WeiXinLoginUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author WhiteRunner
 * @create 2021-11-21 14:37
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    private UsersService service;
    @Resource
    private WeiXinLoginUtils WxUtils;

    @GetMapping("/doLogin")
    public Object doLogin(String code,@RequestParam(required = false,defaultValue = "") String avatarUrl){

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
            LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Users::getOpenid,openid);
            Users users = service.getOne(wrapper);
            //如果user为null，将openId插入数据库
            if (users == null) {
                Users newUsers = new Users();
                newUsers.setOpenid(openid);
                newUsers.setAvatarUrl(avatarUrl);
                service.save(newUsers);
            }else if(!Objects.equals(avatarUrl, "")){
                //如果传入了头像url,则更新数据库里的头像url
                LambdaUpdateWrapper<Users> uWrapper = new LambdaUpdateWrapper<>();
                uWrapper.eq(Users::getOpenid,openid);
                uWrapper.set(Users::getAvatarUrl,avatarUrl);
                service.update(uWrapper);
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

    @PostMapping("/StudentAuth")
    public Object StudentAuth(Users users){
        //构造update wrapper
        String openid = StpUtil.getLoginIdAsString();
        LambdaUpdateWrapper<Users> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Users::getOpenid,openid);
        //置审核状态为1
        users.setReviewStatus(1);
        //写入数据库
        service.update(users,wrapper);
        return ResponseResult.success("学生信息登记成功,进入审核状态");
    }

    @GetMapping("/getInfo")
    public Object CancelStudentAuth(){
        String openid = StpUtil.getLoginIdAsString();
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getOpenid,openid);
        Users users = service.getOne(wrapper);
        return ResponseResult.success(users,"成功获取用户信息");
    }

}
