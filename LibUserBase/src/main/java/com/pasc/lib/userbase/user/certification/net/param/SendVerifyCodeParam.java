package com.pasc.lib.userbase.user.certification.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * 银行卡认证获取验证码
 */

public class SendVerifyCodeParam {
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("verificationType")
    public String verificationType;


    public SendVerifyCodeParam(String mobile, String type) {
        this.mobile = mobile;
        this.verificationType = type;
    }
}
