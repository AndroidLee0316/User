package com.pasc.lib.userbase.user.certification.net.param;

import com.google.gson.annotations.SerializedName;


/**
 * 查询该身份信息是否被其他用户认证过
 */
public class CheckIdCardParam {
    // 证件号码
    @SerializedName("idCard")
    public String idCard;
    // 认证类型,1,银行卡实名认证;2,人脸实名认证;3,支付宝实名认证
    @SerializedName("certType")
    public String certType;
    @SerializedName("userName")
    public String userName;

    public CheckIdCardParam( String userName,String idCard, String certType) {
        this.idCard = idCard;
        this.certType = certType;
        this.userName = userName;
    }
}
