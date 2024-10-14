package com.pasc.lib.userbase.user.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kuangxiangkui192 on 2018/12/21.
 */

public class ForgetPWDParam {
    @SerializedName("mobileNo")
    public String mobileNo;
    @SerializedName("password")
    public String password;
    @SerializedName("envSafeInfo")
    public EnvSafeInfo envSafeInfo;

    public ForgetPWDParam(String mobileNo, String password, String validateCode) {
        this.mobileNo = mobileNo;
        this.password = password;
        this.envSafeInfo = new EnvSafeInfo(validateCode);
    }

    public static class EnvSafeInfo {
        @SerializedName("validateCode")
        public String validateCode;

        public EnvSafeInfo(String validateCode) {
            this.validateCode = validateCode;
        }
    }
}
