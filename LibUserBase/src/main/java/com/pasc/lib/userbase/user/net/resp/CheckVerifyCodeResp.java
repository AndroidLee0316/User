package com.pasc.lib.userbase.user.net.resp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kuangxiangkui192 on 2018/12/21.
 */
public class CheckVerifyCodeResp {
    @SerializedName("mobile") public String mobile;
    @SerializedName("verificationCode") public String verificationCode;
    @SerializedName("verificationType") public String verificationType;
    @SerializedName("validateCode") public String validateCode;
}
