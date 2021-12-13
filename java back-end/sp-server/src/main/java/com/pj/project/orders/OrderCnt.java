package com.pj.project.orders;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author WhiteRunner
 * @create 2021-12-11 19:29
 */
@Data
public class OrderCnt {
    //private String orderType;
    private String orderStatus;
    private double priceSum;
    private Integer cnt;
}
