package com.pasc.business.cert.iview;


/**
 * 认证选择
 */
public interface IChooseCertifyView {

    void queryAuthCountSucc();

    void queryAuthCountFail(String code, String errorMsg);
}
