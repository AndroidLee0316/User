package com.pasc.lib.userbase.user.login.net.resp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EnvValidateItemsBean {
    /**
     * result : false
     * msg : null
     * checkTypes : ["verifySelfRegister"]
     */
    @SerializedName("result")
    public boolean result;
    @SerializedName("msg")
    public Object msg;
    @SerializedName("checkTypes")
    public List<String> checkTypes;
}
