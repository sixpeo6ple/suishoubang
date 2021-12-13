package com.xyy.pojo;

/**
 * @author WhiteRunner
 * @create 2021-11-21 18:47
 */
public class WxPayArgs {
    public static final String NOTIFY_URL = "https://api.suishoubang.myrating.cn/wxV3/payBack.json";//异步回调地址
    public static final String RETURN_URL = "https://api.suishoubang.myrating.cn/wxV3/payBack.json";//同步回调地址
    public static final String MCH_ID = "1616688547";//商户号
    public static final String MCH_SERIAL_NO = "1F6376C09E25E5215F5E9F61143650077C4C46E6";//商户证书序列号
    public static final String API_V3KEY = "X3TYX54EtvM62b8R6baBATsSmkrndIx8";//apiv3秘钥
    public static final String API_KEY = "LxFFsTJuP2KnnBILlWSJ1ou8IN2hvRxe";//apiv2秘钥
    public static final String APP_ID = "wx1ecc4a70f46c0314";//appid
    public static final String PRIVATE_KEY_PATH="classpath:apiclient_key.pem";
}
