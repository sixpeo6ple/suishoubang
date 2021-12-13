package com.xyy.schedule;

import com.xyy.mapper.OrdersMapper;
import com.xyy.service.OrdersService;
import org.junit.jupiter.api.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author WhiteRunner
 * @create 2021-12-07 11:17
 */
@Component
public class TimingTask {
    @Resource
    OrdersMapper mapper;

    //@Scheduled(cron = "*/10 * * * * ?")
    //public void updatePicUrl(){
    //    boolean b=mapper.updatePicUrl();
    //    //System.out.println("图片过期处理");
    //}

    @Scheduled(cron = "0 */1 * * * ?")
    public void updateStatus(){
        boolean b=mapper.updateStatus();
        //System.out.println("订单默认确认处理");
    }
}
