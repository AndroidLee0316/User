package com.pasc.business.user.net;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public class UrlManager {
    public static final String URL_CHANGE_PHONE_SEND_SMS = "/api/platform/user/sendVerifyCode";
    public static final String URL_CHANGE_PHONE_LEGALITY = "/api/platform/user/legalityOfMobile";
    public static final String URL_CHANGE_PHONE_CHANGE = "/api/platform/user/rebindingMobile";
    public static final String URL_CHANGE_PHONE_VERFY = "/api/platform//user/verifyMobile";

    public static final String URL_ACCOUT_CALCE_PAY = "/api/platform/accountCancel/dudgePaymentAccount";
    public static final String URL_ACCOUT_CALCE_COMMIT = "/api/platform/accountCancel/updateState";

    public static final String URL_NEW_PHONE_SEND_SMS="/api/platform/retrieveAccount/sendCheckMessage";
    public static final String URL_NEW_PHONE_SET="/api/platform/retrieveAccount/resetMobile";


}
