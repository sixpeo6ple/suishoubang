package com.xyy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyy.entity.OrderUser;
import com.xyy.entity.Orders;

import java.util.List;

/**
 * @author WhiteRunner
 * @create 2021-12-03 13:46
 */
public interface OrdersMapper extends BaseMapper<Orders> {
    List<OrderUser> listAllOrders(Integer status,Integer id,String payerOpenid,String receiverOpenid,String type);
    IPage<OrderUser> listAllOrdersWithPage(Integer status,Integer id,
                                           String payerOpenid,
                                           String receiverOpenid,
                                           Page<OrderUser> page,
                                           String type);
    //定时任务
    boolean updatePicUrl();
    boolean updateStatus();
}
