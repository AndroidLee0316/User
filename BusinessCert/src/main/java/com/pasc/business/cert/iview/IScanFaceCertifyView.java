package com.pasc.business.cert.iview;

/**
 * 根据用户真实姓名和身份证号校验是否已被其他用户认证
 */
public interface IScanFaceCertifyView {
    void queryIsCertedSucc(String queryIsCerted);

    void queryIsCertedFail(String code, String errorMsg);
}
