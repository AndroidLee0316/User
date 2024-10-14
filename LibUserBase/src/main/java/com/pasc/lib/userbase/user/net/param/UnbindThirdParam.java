package com.pasc.lib.userbase.user.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * 解绑第三方账号
 */
public class UnbindThirdParam {

    @SerializedName("loginType") public String loginType; //第三方授权登录类型（1：微信，2：qq）
    @SerializedName("osType") public String osType;   //终端类型 2：android，1：ios

    public UnbindThirdParam(String loginType) {
        this.loginType = loginType;
        this.osType = "2";
    }
}
