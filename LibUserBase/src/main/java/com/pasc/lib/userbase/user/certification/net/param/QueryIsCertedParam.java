package com.pasc.lib.userbase.user.certification.net.param;

import com.google.gson.annotations.SerializedName;


/**
 * 查询该身份信息是否被其他用户认证过
 */
public class QueryIsCertedParam {
    // 证件号码
    @SerializedName("idNo")
    public String idNo;
    // 认证类型, 1:银行卡认证, 2:人脸认证
    @SerializedName("certType")
    public String certType;

    public QueryIsCertedParam(String idNo, String certType) {
        this.idNo = idNo;
        this.certType = certType;
    }
}
