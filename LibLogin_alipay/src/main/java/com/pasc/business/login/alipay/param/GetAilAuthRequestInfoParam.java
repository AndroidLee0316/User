package com.pasc.business.login.alipay.param;

import com.google.gson.annotations.SerializedName;
import com.pasc.lib.userbase.user.LoginConstant;

/**
 * 获取支付宝SDK调用加密串
 */
public class GetAilAuthRequestInfoParam {

    @SerializedName("loginType") public String loginType; //登录类型   1为微信 2为QQ 3为支付宝
    @SerializedName("osType") public String osType;   //终端类型 2：android，1：ios

    public GetAilAuthRequestInfoParam() {
        this.loginType = LoginConstant.LOGIN_TYPE_ALIPAY;
        this.osType = "2";
    }
}
