package com.pasc.lib.userbase.user.certification.net.param;

import com.google.gson.annotations.SerializedName;

public class ConfirmByBankParam {
    /**
     * 证件号码
     */
    @SerializedName("idNo")
    public String idNo;
    /**
     * 【目前仅支持身份证】证件类型，0:身份证1:户口簿2:护照3:军官证4:士兵证5:港澳居民来往内地通行证6:台湾同胞来往内地通行证7:临时身份证8:外国人居留证9:警官证X:其他证件
     */
    @SerializedName("idType")
    public String idType;
    /**
     * 主体名称
     */
    @SerializedName("name")
    public String name;
    /**
     * 银行卡号
     */
    @SerializedName("bankCardno")
    public String bankCardNo;
    /**
     * 手机号码
     */
    @SerializedName("mobileNo")
    public String mobileNo;
    /**
     * 该身份证是否已经存在绑定【针对身份证已被其他账号绑定的情况】
     */
    @SerializedName("exists")
    public String exists;
    /**
     * 是否跟该账号的其他实名认证方式冲突【true：冲突；其他：不冲突】
     * 该信息来源于realNameAuthByBank.do接口的响应信息，请保持一致，否则无法通过
     */
    @SerializedName("conflict")
    public String conflict;
    /**
     * 验证码
     */
    @SerializedName("verificationCode")
    public String verificationCode;
    /**
     * 验证码类别(SMS_REGISTER-注册、SMS_MOBILE_LOGIN-手机号登录、SMS_UPDATE_PSD-忘记/修改密码、SMS_REAL_NAME_AUTH-实名认证)
     */
    @SerializedName("verificationType")
    public String verificationType;

    public ConfirmByBankParam(String idNo, String idType, String name, String bankCardNo,
                              String mobileNo, String exists, String conflict, String verifyCode, String verificationType) {

        this.idNo = idNo;
        this.idType = idType;
        this.name = name;
        this.bankCardNo = bankCardNo;
        this.mobileNo = mobileNo;
        this.exists = exists;
        this.conflict = conflict;
        this.verificationCode = verifyCode;
        this.verificationType = verificationType;

    }
}
