package com.pasc.lib.userbase.user.net.param;


import com.google.gson.annotations.SerializedName;
import com.pasc.lib.userbase.user.util.UserManagerImpl;
import com.pasc.lib.userbase.user.util.UserManagerImpl;


/**
 * 请求需要token，需要继承该类
 * Created by duyuan797 on 17/10/24.
 */

public class BaseTokenParam {
    @SerializedName("token")
    public String token = UserManagerImpl.getInstance().getToken();
}
