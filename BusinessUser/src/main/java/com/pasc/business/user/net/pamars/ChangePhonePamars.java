package com.pasc.business.user.net.pamars;

import com.google.gson.annotations.SerializedName;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */

public class ChangePhonePamars {
    public ChangePhonePamars() {

    }

    public ChangePhonePamars(String newMobile) {
        this.newMobile = newMobile;
    }

    public ChangePhonePamars(String newMobile, String verificationCode, String verificationType, String oldMobile) {
        this.verificationCode = verificationCode;
        this.verificationType = verificationType;
        this.newMobile = newMobile;
        this.oldMobile = oldMobile;
    }
    public ChangePhonePamars(String newMobile, String verificationCode, String verificationType, String oldMobile, String credential) {
        this.verificationCode = verificationCode;
        this.verificationType = verificationType;
        this.newMobile = newMobile;
        this.oldMobile = oldMobile;
        this.credential = credential;
    }

    @SerializedName("newMobile")
    public String newMobile;
    @SerializedName("verificationCode")
    public String verificationCode;
    @SerializedName("verificationType")
    public String verificationType;
    @SerializedName("oldMobile")
    public String oldMobile;
    @SerializedName("credential")
    public String credential;
}
