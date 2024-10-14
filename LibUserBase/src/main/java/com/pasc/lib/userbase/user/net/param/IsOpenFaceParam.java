package com.pasc.lib.userbase.user.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ex-luyang001 on 2018/1/11.
 */

public class IsOpenFaceParam extends BaseTokenParam {

    @SerializedName("mobile") public String mobile;

    @SerializedName("hasOpenface") public String hasOpenface;

    public IsOpenFaceParam(String mobile) {
        this.mobile=mobile;
    }

}
