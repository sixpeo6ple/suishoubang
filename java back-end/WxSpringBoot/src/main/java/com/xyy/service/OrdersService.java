package com.xyy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyy.entity.OrderUser;
import com.xyy.entity.Orders;

import java.util.List;

public interface OrdersService extends IService<Orders> {
    //List<OrderUser> listAllOrders(Integer status,Integer id,String payerOpenid,String receiverOpenid);
    List<OrderUser> listOrders(Integer status,String type);
    List<OrderUser> listMyOrders(Integer status,String openid,String orderType,String personType);
    List<OrderUser> getOneById(Integer id);
    IPage<OrderUser> listOrdersWithPage(Integer status,int size,int currentPage,String type);
    IPage<OrderUser> listMyOrdersWithPage(Integer status,String openid,int size,int currentPage,String orderType,String personType);
}