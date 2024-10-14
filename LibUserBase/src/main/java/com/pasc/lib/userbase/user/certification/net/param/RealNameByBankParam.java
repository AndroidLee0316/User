package com.pasc.lib.userbase.user.certification.net.param;

import com.google.gson.annotations.SerializedName;


/**
 * 银行卡实名认证参数集合
 */
public class RealNameByBankParam {
    // 证件号码
    @SerializedName("idNo")
    public String idNo;
    // 【目前仅支持身份证】证件类型，0:身份证1:户口簿2:护照3:军官证4:士兵证5:港澳居民来往内地通行证6:台湾同胞来往内地通行证7:临时身份证8:外国人居留证9:警官证X:其他证件
    @SerializedName("idType")
    public String idType;
    //主体名称
    @SerializedName("name")
    public String name;
    //银行卡号
    @SerializedName("bankCardno")
    public String bankCardNo;
    // 手机号码
    @SerializedName("mobileNo")
    public String mobileNo;

    public RealNameByBankParam(String idNo, String idType, String name, String bankCardNo, String mobileNo) {
        this.idNo = idNo;
        this.idType = idType;
        this.name = name;
        this.bankCardNo = bankCardNo;
        this.mobileNo = mobileNo;
    }
}
