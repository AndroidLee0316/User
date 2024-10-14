package com.pasc.lib.userbase.user.certification.net.resp;

import com.google.gson.annotations.SerializedName;

/**
 * 认证完成
 */
public class CertifyCompleteResp {
    @SerializedName("authResult")
    public String authResult; //认证结果： 1【成功】；其他【失败】
    @SerializedName("authMsg")
    public String authMsg;// 认证结果描述
}
