package com.pasc.business.cert.iview;

import com.pasc.lib.userbase.user.certification.net.resp.RealNameByBankResp;

public interface ICardCertifyView {
    void queryIsCertedSucc(String data);

    void queryIsCertedFail(String code, String errorMsg);

    void realNameByBankSucc(RealNameByBankResp byBankErrorResp);

    void realNameByBankFail(String code, String errorMsg);

}
