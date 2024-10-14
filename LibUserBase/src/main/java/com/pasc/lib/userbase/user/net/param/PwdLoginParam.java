package com.pasc.lib.userbase.user.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ruanwei489 on 2017/11/29.
 */

public class PwdLoginParam {
    @SerializedName("loginName")
    public String loginName;
    @SerializedName("password")
    public String password;

    public PwdLoginParam(String loginName, String password) {
        this.loginName = loginName;
        this.password = password;
    }
}
