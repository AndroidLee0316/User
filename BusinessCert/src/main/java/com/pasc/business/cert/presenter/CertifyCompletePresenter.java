package com.pasc.business.cert.presenter;


import com.pasc.business.cert.ipresenter.IPresenter;
import com.pasc.lib.net.resp.BaseRespThrowableObserver;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.userbase.user.certification.net.CertifyBiz;
import com.pasc.lib.userbase.user.certification.net.resp.CertifyCompleteResp;
import com.pasc.lib.userbase.user.certification.net.resp.RealNameByBankResp;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class CertifyCompletePresenter implements IPresenter {
    private com.pasc.business.cert.iview.ICertifyCompleteView ICertifyCompleteView;
    private CompositeDisposable disposables = new CompositeDisposable();

    public CertifyCompletePresenter(com.pasc.business.cert.iview.ICertifyCompleteView ICertifyCompleteView) {
        this.ICertifyCompleteView = ICertifyCompleteView;
    }

    /**
     * 获取验证码
     */
    public void sendCertifyCode(String mobile) {
        disposables.add(CertifyBiz.sendVerifyCode(mobile).subscribe(
                new Consumer<VoidObject>() {
                    @Override
                    public void accept(VoidObject voidObject) {
                        if (ICertifyCompleteView != null) {
                            ICertifyCompleteView.sendCertificationCodeSucc();
                        }
                    }
                }, new BaseRespThrowableObserver() {
                    @Override
                    public void onV2Error(String code, String errorMsg) {
                        if (ICertifyCompleteView != null) {
                            ICertifyCompleteView.sendCertificationCodeFail(code, errorMsg);
                        }
                    }
                }
        ));
    }

    /**
     * 银行卡认证
     */
    public void confirmByBank(RealNameByBankResp resp, String verifyCode) {
        disposables.add(CertifyBiz.confirmByBank(resp, verifyCode).subscribe(
                new Consumer<CertifyCompleteResp>() {
                    @Override
                    public void accept(CertifyCompleteResp certifyCompleteResp) {
                        if (ICertifyCompleteView != null) {
                            ICertifyCompleteView.confirmByBankSucc(certifyCompleteResp);
                        }
                    }
                }, new BaseRespThrowableObserver() {
                    @Override
                    public void onV2Error(String code, String errorMsg) {
                        if (ICertifyCompleteView != null) {
                            ICertifyCompleteView.confirmByBankFail(code, errorMsg);
                        }
                    }
                }));
    }

    @Override
    public void onDestroy() {
        if (!disposables.isDisposed()) {
            disposables.clear();
        }
        ICertifyCompleteView = null;
    }
}
