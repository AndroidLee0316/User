package com.pasc.lib.userbase.base.data.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ThirdUserInfo implements Serializable{
    @SerializedName("weixin") public String weixin;//微信昵称", //1：和微信建立了绑定，2：和qq建立了绑定
    @SerializedName("QQ") public String QQ;//        "2":"qq昵称"

    @Override public String toString() {
        return "ThirdUserInfo{" + "weixin='" + weixin + '\'' + ", QQ='" + QQ + '\'' + '}';
    }
}
