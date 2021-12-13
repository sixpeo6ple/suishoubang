package com.xyy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyy.entity.OrderUser;
import com.xyy.mapper.OrdersMapper;
import com.xyy.pojo.WxPayArgs;
import com.xyy.service.OrdersService;
import com.xyy.utils.QCloudUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
class WxSpringBootApplicationTests {
    @Resource
    private OrdersService service;
    @Resource
    private OrdersMapper mapper;
    @Resource
    private QCloudUtils utils;
    @Resource
    private StringRedisTemplate redis;

    @Test
    void TestTransfer(){

    }

    @Test
    void TestRedis() {
        //redis.opsForValue().set("key","value");
        //redis.opsForValue().set(new Duration);
    }

    @Test
    void TestQCloud() throws IOException {
        ArrayList<String> keys = utils.listItems("SUFE/2019111327/");
        System.out.println(keys);
    }

    @Test
    void TestMySql() {
        //for (OrderUser listAllOrder : service.listAllOrders
        //        (0,"","onLBk5Wz3Nn48W-9IEqLF-eOQ1aM")) {
        //    System.out.println(listAllOrder);
        //}
        //System.out.println(service.updatePicUrl());
        Page<OrderUser> page=new Page<>();
        //page.setSize(3);//设置每页查询数量
        //page.setCurrent(2);//设置当前页
        mapper.listAllOrdersWithPage(0,-1,"","",page,"");
        for (OrderUser record : page.getRecords()) {
            System.out.println(record);
        }

    }

}
