package com.xyy.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.xyy.pojo.PicUploadArgs;
import com.xyy.service.UsersService;
import com.xyy.utils.QCloudUtils;
import com.xyy.utils.ResponseResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author WhiteRunner
 * @create 2021-11-29 11:57
 */
@RestController
@RequestMapping("/pic")
public class PictureController {
    @Resource
    private QCloudUtils QcUtils;
    @Resource
    private UsersService service;

    @PostMapping("/uploadPic")
    public Object loadPic(PicUploadArgs picUploadArgs) throws IOException {
        //登录验证
        if(!StpUtil.isLogin()){
            throw new RuntimeException("请登录后再上传图片!");
        }
        // 指定要上传的文件
        String openid=StpUtil.getLoginIdAsString();
        String pic = picUploadArgs.getPic();
        String name = picUploadArgs.getName(); //存储桶路径
        //String type = picUploadArgs.getType(); //图片类型
        byte[] bytes = Base64.decodeBase64(pic);
        File file = File.createTempFile("tmp", ".jpg");
        FileUtils.writeByteArrayToFile(file,bytes);
        //上传图片并接收路径
        String url = QcUtils.uploadItem(file, name);
        if(StringUtils.hasText(url)){
            //写入数据库
            //String openid = StpUtil.getLoginIdAsString();
            //if("StuCard".equals(type)){
            //    LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
            //    wrapper.eq(User::getOpenid,openid);
            //    wrapper.set(User::getPicUrl,url);
            //    service.update(wrapper);
            //}
            //返回url
            HashMap<String, String> map = new HashMap<>();
            map.put("URL",url);
            return ResponseResult.success(map);
        }
        return ResponseResult.error("上传失败");

    }

    @PostMapping("/delPic")
    public Object delPic(String name) throws IOException{
        boolean isDelete = QcUtils.deleteItem(name);
        if(isDelete){
            return ResponseResult.success("成功删除 "+name);
        }else {
            return ResponseResult.error("删除失败,不存在该文件");
        }
    }
}
