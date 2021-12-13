package com.xyy.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WhiteRunner
 * @create 2021-11-12 15:55
 */
@Component
public class QCloudUtils {

    COSClient cosClient = null;
    String bucketName="wxapp-1303032424";
    String baseURL="https://"+bucketName+".cos.ap-shanghai.myqcloud.com/";

    public QCloudUtils() {
        // 1 初始化用户身份信息（secretId, secretKey）。
        String secretId = "AKIDJTOZT9SZ8PAYgLl4AzWjBMHxNt8BcZcP";
        String secretKey = "gFK9rbF1XUB5pdATZXHuxukV1IIAzGHD";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法
        Region region = new Region("ap-shanghai");
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端
        cosClient = new COSClient(cred, clientConfig);
    }

    /**
     * @param file 要上传的文件
     * @param key 存储桶内路径 如 user/a.jpg
     * @return 文件地址
     */
    public String uploadItem(File file, String key){
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        cosClient.putObject(putObjectRequest);
        return baseURL+key;
    }


    /**
     * @param key 存储桶内路径 如 user/a.jpg
     */
    public boolean deleteItem(String key){
        //key: SUFE/2019111327/StuCard.jpg
        // 查询当前用户文件夹内的文件
        int i = key.lastIndexOf("/");
        String prefix = key.substring(0,i+1);
        ArrayList<String> fileList = listItems(prefix);
        //如果有该文件则删除该文件
        if(fileList.contains(key)){
            cosClient.deleteObject(bucketName, key);
            return true;
        }else {
            return false;
        }
    }

    public ArrayList<String> listItems(String prefix) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置bucket名称
        listObjectsRequest.setBucketName(bucketName);
        // prefix表示列出的object的key以prefix开始 images/
        listObjectsRequest.setPrefix(prefix);
        // deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
        listObjectsRequest.setDelimiter("/");
        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);

        ObjectListing objectListing = null;
        ArrayList<String> keys = new ArrayList<>();
        do{
            objectListing = cosClient.listObjects(listObjectsRequest);
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                String key = cosObjectSummary.getKey();
                keys.add(key);
            }
            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        }while (objectListing.isTruncated());

        return keys;
    }

}
