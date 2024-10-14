package com.pasc.business.user.net.resp;

import com.google.gson.annotations.SerializedName;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public class ChangePhoneResp {
    @SerializedName("mobile")
    public  String mobile;
    @SerializedName("result")
    public  String result;
    @SerializedName("preValidCode")
    public  String preValidCode;
}
