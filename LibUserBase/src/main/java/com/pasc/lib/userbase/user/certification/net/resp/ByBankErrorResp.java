package com.pasc.lib.userbase.user.certification.net.resp;

import com.google.gson.annotations.SerializedName;

public class ByBankErrorResp {
    @SerializedName("code")
    public String code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public RealNameByBankResp data;
}
