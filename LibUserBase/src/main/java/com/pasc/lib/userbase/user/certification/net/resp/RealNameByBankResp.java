package com.pasc.lib.userbase.user.certification.net.resp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RealNameByBankResp implements Serializable {
    @SerializedName("token")
    public String token;
    @SerializedName("authResult")
    public String authResult; //0【否】；1【是】；2【用户未覆盖】
    @SerializedName("authMsg")
    public String authMsg;// 认证结果描述
    @SerializedName("idNo")
    public String idNo;// 证件号码
    @SerializedName("idType")
    public String idType;// 证件类型，0:身份证1:户口簿2:护照3:军官证4:士兵证5:港澳居民来往内地通行证6:台湾同胞来往内地通行证7:临时身份证8:外国人居留证9:警官证X:其他证件
    @SerializedName("name")
    public String name;// 主体名称
    @SerializedName("bankCardno")
    public String bankCardno;// 银行卡号
    @SerializedName("mobileNo")
    public String mobileNo;// 手机号码
    @SerializedName("exists")
    public String exists;// 该身份证是否已经存在绑定【针对身份证已被其他账号绑定的情况】【true：存在绑定；其他：不存在绑定】
    @SerializedName("conflict")
    public String conflict;// 是否跟该账号的其他实名认证方式冲突【true：冲突；其他：不冲突】
    @SerializedName("allowedAuthCount")
    public int allowedAuthCount;
}
