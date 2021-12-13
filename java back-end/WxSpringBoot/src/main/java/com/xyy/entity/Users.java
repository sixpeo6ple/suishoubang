package com.xyy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WhiteRunner
 * @create 2021-11-12 11:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private Integer id;
    private String openid;
    private Boolean isStudent;
    private String picUrl; //学生证
    private String name;
    private String sid;
    private String place;
    private String phone;
    private String avatarUrl; //头像
    private Integer reviewStatus; //审核状态 0=无状态, 1=审核中, 2=审核成功, 3=审核失败
}
