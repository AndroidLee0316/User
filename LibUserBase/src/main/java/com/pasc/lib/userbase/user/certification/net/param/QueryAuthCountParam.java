package com.pasc.lib.userbase.user.certification.net.param;

import com.google.gson.annotations.SerializedName;


/**
 * 查询认证次数是否超过最大允许次数
 */
public class QueryAuthCountParam {
    // 认证类型, 1:银行卡认证, 2:人脸认证
    @SerializedName("certType")
    public String certType;

    public QueryAuthCountParam(String certType) {
        this.certType = certType;
    }
}
