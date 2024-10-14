package com.pasc.business.cert.zm.resp;

import com.google.gson.annotations.SerializedName;

/**
 * 获取（支付宝SDK调用加密串+查询凭证标志）返回参数
 */
public class GetAliInitInfoResp {

    @SerializedName("certifyId") public String certifyId; //查询凭证标志
    @SerializedName("certifyUrl") public String certifyUrl;   //支付宝SDK调用加密串


    public String getCertifyId() {
        return certifyId;
    }

    public void setCertifyId(String certifyId) {
        this.certifyId = certifyId;
    }

    public String getCertifyUrl() {
        return certifyUrl;
    }

    public void setCertifyUrl(String certifyUrl) {
        this.certifyUrl = certifyUrl;
    }
}
