package com.xyy.pojo;

import lombok.Data;
import lombok.ToString;

@Data
public class WxloginRes {
    private String openid;
    private String sessionKey;
    private String errCode;
    private String errMsg;
}