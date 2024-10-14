package com.pasc.business.cert.config;

public interface CertifyErrorCodeConfig {
    /***银行卡实名认证_校验账号是否绑定过错误码*/
    String USER_CERT_PINGAN_FACE_HAS_BEEN_CERTIFIED_ERROR = "USER_CERT_PINGAN_FACE_HAS_BEEN_CERTIFIED_ERROR";
    /***查询认证次数是否超过最大允许次数*/
    String USER_CERT_BY_BANK_MAX_TIME = "USER_CERT_BY_BANK_MAX_TIME";
    /***查询认证次数是否超过最大允许次数*/
    String USER_CERT_BY_FACE_MAX_TIME = "USER_CERT_BY_FACE_MAX_TIME";
}
