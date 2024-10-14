package com.pasc.business.cert.zm.param;

import com.google.gson.annotations.SerializedName;

/**
 * 查询支付宝认证状态请求参数
 */
public class GetAliCertResultParam {

    @SerializedName("userName") public String userName; //用户姓名
    @SerializedName("idCard") public String idCard;   //用户身份证
    @SerializedName("certifyId") public String certifyId; //支付宝认证凭证标志

    public GetAliCertResultParam(String userName, String idCard, String certifyId) {
        this.userName = userName;
        this.idCard = idCard;
        this.certifyId = certifyId;
    }
}
