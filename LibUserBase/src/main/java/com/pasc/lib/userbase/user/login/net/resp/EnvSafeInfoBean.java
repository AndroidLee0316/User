package com.pasc.lib.userbase.user.login.net.resp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EnvSafeInfoBean {
    @SerializedName("safe")
    public boolean safe;
    @SerializedName("userLastLoginTime")
    public long userLastLoginTime;
    @SerializedName("h5Link")
    public String h5Link;
    @SerializedName("validateCode")
    public String validateCode;
    @SerializedName("envValidateItems")
    public List<EnvValidateItemsBean> envValidateItems;
}
