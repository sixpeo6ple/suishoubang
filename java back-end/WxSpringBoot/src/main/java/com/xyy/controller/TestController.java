package com.xyy.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.api.DefaultPayMessageHandler;
import com.xyy.utils.ResponseResult;
import com.xyy.service.UserService;
import com.xyy.utils.QCloudUtils;
import com.xyy.utils.WeiXinLoginUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author WhiteRunner
 * @create 2021-11-11 18:20
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private UserService service;
    @Resource
    private QCloudUtils QcUtils;
    @Resource
    private WeiXinLoginUtils WxUtils;
    //private final Logger LOG = LoggerFactory.getLogger(DefaultPayMessageHandler.class);

    @PostMapping("/loadPic")
    public Object loadPic(@RequestBody JSONObject jsonObject) throws IOException {
        // 指定要上传的文件
        String picBase64 = jsonObject.getString("pic");
        byte[] bytes = Base64.decodeBase64(picBase64);
        File file = File.createTempFile("tmp", ".jpg");
        FileUtils.writeByteArrayToFile(file,bytes);
        //存储桶路径
        String key = "test/pic.jpg";
        //接收图片路径
        String url = QcUtils.uploadPic(file, key);
        if(StringUtils.hasText(url)){
            HashMap<String, String> map = new HashMap<>();
            map.put("URL",url);
            return ResponseResult.success(map);
        }
        return ResponseResult.error("上传失败");

    }


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
