package com.xyy.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyy.entity.OrderUser;
import com.xyy.entity.Orders;
import com.xyy.mapper.OrdersMapper;
import com.xyy.service.OrdersService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
        implements OrdersService {

    @Resource
    OrdersMapper mapper;

    //@Transactional
    //public List<OrderUser> listAllOrders(Integer status,Integer id,String payerOpenid,String receiverOpenid){
    //    return mapper.listAllOrders(status,id,payerOpenid,receiverOpenid);
    //}

    @Override
    public List<OrderUser> listOrders(Integer status,String type) {
        return mapper.listAllOrders(status,-1,"","",type);
    }

    @Override
    public List<OrderUser> listMyOrders(Integer status, String openid,String orderType,String personType) {
        if("payer".equals(personType)){
            return mapper.listAllOrders(status,-1,openid,"",orderType);
        }else if ("receiver".equals(personType)) {
            return mapper.listAllOrders(status,-1,"",openid,orderType);
        }else{
            return null;
        }
    }

    @Override
    public List<OrderUser> getOneById(Integer id) {
        return mapper.listAllOrders(0,id,"","","");
    }



    @Override
    public IPage<OrderUser> listOrdersWithPage(Integer status, int size, int currentPage,String type) {
        Page<OrderUser> page=new Page<>();
        page.setSize(size);
        page.setCurrent(currentPage);
        return mapper.listAllOrdersWithPage(status,-1,"","",page,type);
    }

    @Override
    public IPage<OrderUser> listMyOrdersWithPage(Integer status, String openid, int size, int currentPage,String orderType,String personType) {
        Page<OrderUser> page=new Page<>();
        page.setSize(size);
        page.setCurrent(currentPage);
        if("payer".equals(personType)){
            return mapper.listAllOrdersWithPage(status,-1,openid,"",page,orderType);
        }else if ("receiver".equals(personType)) {
            return mapper.listAllOrdersWithPage(status,-1,"",openid,page,orderType);
        }else{
            return null;
        }
    }

}