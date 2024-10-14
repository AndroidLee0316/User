package com.pasc.lib.userbase.base.data.user;

import com.google.gson.annotations.SerializedName;
import com.pasc.lib.userbase.base.data.SmtDb;
import com.raizlabs.android.dbflow.annotation.Table;

import java.io.Serializable;

/**
 * Created by duyuan797 on 17/10/25.
 */
@Table(database = SmtDb.class)
public class ThirdLoginUser extends User implements Serializable {
    @SerializedName("accessToken")
    public String accessToken;
    @SerializedName("openid")
    public String openid;
    @SerializedName("loginType")
    public String loginType;
    @SerializedName("osType")
    public String osType;
    @SerializedName("code")
    public String code;
}
