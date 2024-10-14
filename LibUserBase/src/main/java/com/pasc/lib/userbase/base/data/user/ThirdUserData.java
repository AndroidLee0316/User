package com.pasc.lib.userbase.base.data.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 第三方账户信息
 */
public class ThirdUserData implements Serializable {
    @SerializedName("nickName") public String nickName; //用户名
    @SerializedName("headImgUrl") public String headImgUrl; //头像URL
    @SerializedName("unionId") public String unionId;
    @SerializedName("sex") public String sex; //性别

    @Override public String toString() {
        return "ThirdUserData{"
                + "nickName='"
                + nickName
                + '\''
                + ", headImgUrl='"
                + headImgUrl
                + '\''
                + ", unionId='"
                + unionId
                + '\''
                + ", sex='"
                + sex
                + '\''
                + '}';
    }
}
