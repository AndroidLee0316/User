package com.pasc.lib.userbase.base.data;

/**
 * Created by kuangxiangkui192 on 2018/12/25.
 * 常用常量值
 */
public class Constant {
    public static final String USER_HAS_PWD = "1"; //是否设置了密码
    public static final String MOBILE_NUMBER = "MOBILE_NUMBER";

    /**
     * 用户已经开启了人脸识别
     */
    public static final String USER_FACE_OPEND = "1";
    /**
     * 用户未开启人脸识别
     */
    public static final String USER_FACE_UN_OPEN = "0";

    public static final String SMS_REGISTER = "SMS_REGISTER"; //注册
    public static final String SMS_MOBILE_LOGIN = "SMS_MOBILE_LOGIN"; //验证码登录
    public static final String SMS_UPDATE_PSD = "SMS_UPDATE_PSD"; //忘记 or 修改密码
    public static final String SMS_REAL_NAME_AUTH = "SMS_REAL_NAME_AUTH"; //实名认证
    public static final String SMS_BIND_MOBILE = "SMS_BIND_MOBILE"; //绑定三方账号

    public static final String LOGIN_TYPE_WX = "1"; //微信登录
    public static final String LOGIN_TYPE_QQ = "2"; //QQ登录
    public static final String LOGIN_TYPE_ALIPAY = "3"; //支付宝登录

//      remove by lcb，第三方绑定状态放到了User类里面
//    public static final String THIRD_BIND_WX_QQ = "1"; //微信和QQ都绑定了
//    public static final String THIRD_BIND_QQ = "2"; //只绑定QQ
//    public static final String THIRD_BIND_WX = "3"; //只绑定微信
//    public static final String THIRD_BIND_Alipay = "4"; //只绑定微信
//    public static final String THIRD_BIND_NONE = "4"; //QQ和微信均未绑定

    public static final String LOGIN_TYPE = "LOGIN_TYPE";
    public static final String LOGIN_TYPE_SWITCH_ACCOUNT = "LOGIN_TYPE_SWITCH_ACCOUNT";

    public static final String CERT_TYPE = "CERT_TYPE";
    public static final int CERT_TYPE_UNKNOW = -1;
    public static final int CERT_TYPE_BANK = 0;
    public static final int CERT_TYPE_FACE = 1;
    public static final int CERT_TYPE_ALIPAY = 2;
    public static final String CERT_FAIL_MSG = "CERT_FAIL_MSG";
    public static final String CERT_FAIL_REMAIN_COUNT = "CERT_FAIL_REMAIN_COUNT";

    public static final String ACCOUNT_LOGIN_START = "account_login_start";
    public static final String ACCOUNT_LOGIN_TYPE = "account_login_type";
    public static final String ACCOUNT_LOGIN_FAIL = "account_login_fail";
    public static final String ACCOUNT_LOGIN_SUCCESS = "account_login_sucess";
    public static final String ACCOUNT_CERT_START = "account_cert_start";
    public static final String ACCOUNT_CERT_FAIL = "account_cert_fail";
    public static final String ACCOUNT_CERT_SUCCESS = "account_cert_sucess";
    public static final String ACCOUNT_FIND_PASSWORD = "account_find_password";
    public static final String ACCOUNT_SECURITY = "account_security";
}
