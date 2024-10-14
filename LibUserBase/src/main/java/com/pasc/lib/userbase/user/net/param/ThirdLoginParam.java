package com.pasc.lib.userbase.user.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * 绑定第三方账号
 */
public class ThirdLoginParam {

    @SerializedName("accessToken") public String accessToken; //
    @SerializedName("openid") public String openid; //
    @SerializedName("loginType") public String loginType; //第三方授权登录类型（1：微信，2：qq，3：支付宝）
    @SerializedName("osType") public String osType;   //终端类型 2：android，1：ios
    @SerializedName("code") public String code;   //

    public ThirdLoginParam(String accessToken, String openid, String loginType, String code) {
        this.accessToken = accessToken;
        this.openid = openid;
        this.loginType = loginType;
        this.osType = "2";
        this.code = code;
    }
}
