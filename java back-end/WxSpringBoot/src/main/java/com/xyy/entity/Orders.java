package com.xyy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author WhiteRunner
 * @create 2021-12-03 13:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    private Integer id;
    private String outTradeNo;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    private String payerOpenid;
    private String receiverOpenid;
    private String picUrl;
    private String info;
    private BigDecimal price;
    private String type;
    private Integer status; //订单状态 1无人接单 2有人接单 3完成 4退款
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliverTime;
    private String deliverType;
    private String deliverPlace;

}
