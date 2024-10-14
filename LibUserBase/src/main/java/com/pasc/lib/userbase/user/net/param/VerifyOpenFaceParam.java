package com.pasc.lib.userbase.user.net.param;

import com.google.gson.annotations.SerializedName;

public class VerifyOpenFaceParam {
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("verificationType")
    public String verificationType;
    @SerializedName("verificationCode")
    public String verificationCode;

    public VerifyOpenFaceParam(String mobile, String verificationType, String verificationCode) {
        this.mobile = mobile;
        this.verificationType = verificationType;
        this.verificationCode = verificationCode;
    }
}
