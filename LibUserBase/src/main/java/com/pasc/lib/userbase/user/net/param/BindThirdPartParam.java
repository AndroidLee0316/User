package com.pasc.lib.userbase.user.net.param;

import com.google.gson.annotations.SerializedName;

import com.pasc.lib.userbase.base.data.BaseLoginParam;
import com.pasc.lib.userbase.base.data.user.ThirdUserData;

import java.io.Serializable;

/**
 * 绑定第三方账号
 */
public class BindThirdPartParam implements Serializable{

    @SerializedName("mobile") public String mobile = ""; //手机号
    @SerializedName("accessToken") public String accessToken = "";
    @SerializedName("openid") public String openid = "";
    @SerializedName("loginType") public String loginType = ""; //1 微信 2 QQ
    @SerializedName("osType") public String osType = "2";   //终端类型 1：ios 2：android
    @SerializedName("code") public String code = "";
    @SerializedName("verificationCode") public String verificationCode = "";
    @SerializedName("isLogin") public String isLogin = "";

    public BindThirdPartParam() {
    }

    public BindThirdPartParam(String mobile, String accessToken, String openid,
                              String loginType, String code, String verificationCode, String isLogin) {
        this.mobile = mobile;
        this.accessToken = accessToken;
        this.openid = openid;
        this.loginType = loginType;
        this.osType = "2";
        this.code = code;
        this.verificationCode = verificationCode;
        this.isLogin = isLogin;
    }
}
