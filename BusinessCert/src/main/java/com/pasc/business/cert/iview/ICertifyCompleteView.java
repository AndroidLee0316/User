package com.pasc.business.cert.iview;


import com.pasc.lib.userbase.user.certification.net.resp.CertifyCompleteResp;

/**
 * 认证完成
 */
public interface ICertifyCompleteView {
    void sendCertificationCodeSucc();

    void sendCertificationCodeFail(String code, String errorMsg);

    void confirmByBankSucc(CertifyCompleteResp resp);

    void confirmByBankFail(String code, String errorMsg);
}
