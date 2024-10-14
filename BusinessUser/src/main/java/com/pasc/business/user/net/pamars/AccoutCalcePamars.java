package com.pasc.business.user.net.pamars;

import com.google.gson.annotations.SerializedName;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/31
 * 更改时间：2019/10/31
 */
public class AccoutCalcePamars {
    @SerializedName("TYPE_FACE")
    public static final String TYPE_FACE = "2";
    @SerializedName("TYPE_SMS_CODE")
    public static final String TYPE_SMS_CODE = "1";

    public AccoutCalcePamars(String checkType, String verificationCode, String verificationType) {
        this.checkType = checkType;
        this.verificationCode = verificationCode;
        this.verificationType = verificationType;


    }

    public AccoutCalcePamars(String checkType, String credential) {
        this.checkType = checkType;
        this.credential = credential;


    }

    @SerializedName("checkType")
    public String checkType;
    @SerializedName("verificationCode")
    public String verificationCode;
    @SerializedName("verificationType")
    public String verificationType;
    @SerializedName("credential")
    public String credential;
}
