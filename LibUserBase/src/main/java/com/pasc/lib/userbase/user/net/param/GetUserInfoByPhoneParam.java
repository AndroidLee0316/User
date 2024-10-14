package com.pasc.lib.userbase.user.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kuangxiangkui192 on 2018/12/3.
 * 根据手机号获取用户信息，目前主要获取登录前的头像信息
 */
public class GetUserInfoByPhoneParam {
    @SerializedName("mobile")
    public String mobile;

    public GetUserInfoByPhoneParam(String mobile) {
        this.mobile=mobile;
    }
}
