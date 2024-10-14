package com.pasc.lib.userbase.user.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by duyuan797 on 17/10/25.
 */

public class SendVerifyCodeParam{
    @SerializedName("mobile") public String mobile;
    @SerializedName("verificationType") public String type;
}
