package com.pasc.business.user.net.pamars;

import com.google.gson.annotations.SerializedName;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public class SMSPamars {

    @SerializedName("mobile")
    public String mobile;
    @SerializedName("verificationType")
    public String verificationType;
    @SerializedName("preValidCode")
    public String preValidCode;

    public SMSPamars() {
    }

    public SMSPamars (String mobile , String verificationType){
        this.mobile = mobile;
        this.verificationType = verificationType;
    }

    public SMSPamars(String mobile, String verificationType, String preValidCode) {
        this.mobile = mobile;
        this.verificationType = verificationType;
        this.preValidCode = preValidCode;
    }
}
