package com.pasc.lib.userbase.user.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by duyuan797 on 17/10/25.
 */

public class OtpLoginParam {

    public OtpLoginParam(String mobile, String verifyCode, String verificationType, Env env) {
        this.mobile = mobile;
        this.verificationCode = verifyCode;
        this.verificationType = verificationType;
        this.env = env;
    }

    @SerializedName("mobile")
    public String mobile;
    @SerializedName("verificationCode")
    public String verificationCode;
    @SerializedName("verificationType")
    public String verificationType;
    @SerializedName("env")
    public Env env;


    public static class Env {
        @SerializedName("osType")
        public String osType;
        @SerializedName("deviceId")
        public String deviceId;

        public Env(String osType, String deviceId) {
            this.osType = osType;
            this.deviceId = deviceId;
        }
    }
}
