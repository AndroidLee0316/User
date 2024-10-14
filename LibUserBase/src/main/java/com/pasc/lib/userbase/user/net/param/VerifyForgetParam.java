package com.pasc.lib.userbase.user.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lingchun147 on 2017/11/30.
 */

public class VerifyForgetParam {
    @SerializedName("mobile")
    public String mobile;

    @SerializedName("verificationType")
    public String verificationType;

    @SerializedName("verificationCode")
    public String verificationCode;

    public VerifyForgetParam(String mobile, String verificationType, String verificationCode) {
        this.mobile = mobile;
        this.verificationType = verificationType;
        this.verificationCode = verificationCode;
    }
}
