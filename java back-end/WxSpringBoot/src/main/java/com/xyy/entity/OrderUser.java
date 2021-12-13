package com.xyy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author WhiteRunner
 * @create 2021-12-03 14:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUser {
    private Integer id; //订单id
    private String outTradeNo; //微信支付id
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
    private String picUrl;
    private String info;
    private BigDecimal price;
    private String type;
    private Integer status; //订单状态 1无人接单 2有人接单 3完成
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime deliverTime;
    private String deliverType;
    private String deliverPlace;

    private String payerName;
    private String payerSid;
    private String payerPlace;
    private String payerPhone;
    private String payerAvatarUrl;

    private String receiverName;
    private String receiverSid;
    private String receiverPlace;
    private String receiverPhone;
    private String receiverAvatarUrl;
}
