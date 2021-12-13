package com.xyy.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.xyy.utils.ResponseResult;
import com.xyy.service.UsersService;
import com.xyy.utils.QCloudUtils;
import com.xyy.utils.WeiXinLoginUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author WhiteRunner
 * @create 2021-11-11 18:20
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private UsersService service;
    @Resource
    private QCloudUtils QcUtils;
    @Resource
    private WeiXinLoginUtils WxUtils;
    //private final Logger LOG = LoggerFactory.getLogger(DefaultPayMessageHandler.class);



    @GetMapping("/doAdminLogin") //使用网页端
    public Object doAdminLogin(String id,String password){
        if(id.equals("sixpeo6ple") && password.equals("sixpeo6ple")){
            //根据openId生成token
            StpUtil.login(id);
            //获取token的值保存到map中
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            HashMap<String, String> map = new HashMap<>();
            map.put("tokenName",tokenInfo.getTokenName());
            map.put("tokenValue",tokenInfo.getTokenValue());
            //封装返回值
            return ResponseResult.success(map,"admin登录成功");
        }else {
            return ResponseResult.error("admin登录失败");
        }
    }


    @GetMapping("/hello")
    public String hello() {
        //LOG.info("log：你好世界");
        //System.out.println("print：你好世界");
        return "hello world";
    }

}
