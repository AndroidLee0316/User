package com.pasc.business.user;

/**
 * 功能：对外提供的配置文件
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/22
 */
public class PascUserConfig {

    /**
     * 用户信息：用户ID
     */
    public static final String USER_INFO_KEY_USER_ID = "userID";
    /**
     * 用户名称
     */
    public static final String USER_INFO_KEY_USER_NAME = "userName";
    /**
     * token
     */
    public static final String USER_INFO_KEY_TOKEN = "token";
    /**
     * 手机号
     */
    public static final String USER_INFO_KEY_PHONE = "phoneNum";
    /**
     * 是否已登陆
     */
    public static final String USER_INFO_KEY_IS_LOGIN = "isLogin";
    /**
     * 头像
     */
    public static final String USER_INFO_KEY_HEAD_IMG = "headImg";

    /**
     * 生日
     */
    public static final String USER_INFO_KEY_BIRTHDAY = "birthday";


    /**
     * 地址
     */
    public static final String USER_INFO_KEY_ADDRESS = "address";

    /**
     * 认证类型
     */
    public static final String USER_INFO_KEY_CERTIFICATION_TYPE = "certificationType";


    //---------盐城项目用到----------
    /**
     * 支付号
     */
    public static final String USER_INFO_KEY_PAY_ACCOUNT_ID = "payAccountId";


    /**
     * 认证列表页面
     */
    public static final int CERTIFICATION_TYPE_ALL = 0;

    /**
     * 认证列表页面，当认证成功后关闭认证列表页
     */
    public static final int CERTIFICATION_TYPE_ALL_AND_FINISH_WHEN_SUCCESS = 1;

    /**
     * 银行卡认证页面
     */
    public static final int CERTIFICATION_TYPE_BANK = 2;

    /**
     * 平安人脸认证页面
     */
    public static final int CERTIFICATION_TYPE_FACE_PA = 3;

}
