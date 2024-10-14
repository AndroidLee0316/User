package com.pasc.lib.userbase.user.certification.net.resp;

import com.google.gson.annotations.SerializedName;

public class FaceAndIdComparisonErrorResp {
    @SerializedName("code")
    public String code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public String data;//成功失败均返回剩余可认证次数
}