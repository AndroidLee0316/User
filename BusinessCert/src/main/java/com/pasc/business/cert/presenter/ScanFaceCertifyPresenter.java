package com.pasc.business.cert.presenter;

import com.pasc.business.cert.ipresenter.IPresenter;
import com.pasc.business.cert.iview.IScanFaceCertifyView;
import com.pasc.lib.net.resp.BaseRespThrowableObserver;
import com.pasc.lib.userbase.user.certification.net.CertifyBiz;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class ScanFaceCertifyPresenter implements IPresenter {
    private IScanFaceCertifyView scanFaceCertifyView;
    private CompositeDisposable disposables = new CompositeDisposable();

    public ScanFaceCertifyPresenter(IScanFaceCertifyView scanFaceCertifyView) {
        this.scanFaceCertifyView = scanFaceCertifyView;
    }

    /**
     * 根据姓名和身份证号判断是否被其他账号认证过
     * 20191230 过期，使用函数{@checkIsCerted}替换
     * @param idNo     身份证号
     * @param certType 1.银行卡认证，2 人脸认证
     */
    @Deprecated
    public void queryIsCerted(String idNo, String certType) {
        disposables.add(CertifyBiz.queryIsCerted(idNo, certType).subscribe(new Consumer<String>() {
            @Override
            public void accept(String queryIsCerted) {
                if (scanFaceCertifyView != null) {
                    scanFaceCertifyView.queryIsCertedSucc(queryIsCerted);
                }
            }
        }, new BaseRespThrowableObserver() {
            @Override
            public void onV2Error(String code, String errorMsg) {
                if (scanFaceCertifyView != null) {
                    scanFaceCertifyView.queryIsCertedFail(code, errorMsg);
                }
            }
        }));
    }
    /**
     * 根据姓名和身份证号判断是否被其他账号认证过
     *
     * @param idNo     身份证号
     * @param certType 1.银行卡认证，2 人脸认证
     */
    public void checkIsCerted(String userName,String idNo, String certType) {
        disposables.add(CertifyBiz.checkIsCerted(userName,idNo, certType).subscribe(new Consumer<String>() {
            @Override
            public void accept(String queryIsCerted) {
                if (scanFaceCertifyView != null) {
                    scanFaceCertifyView.queryIsCertedSucc(queryIsCerted);
                }
            }
        }, new BaseRespThrowableObserver() {
            @Override
            public void onV2Error(String code, String errorMsg) {
                if (scanFaceCertifyView != null) {
                    scanFaceCertifyView.queryIsCertedFail(code, errorMsg);
                }
            }
        }));
    }

    @Override
    public void onDestroy() {
        if (disposables != null) {
            disposables.clear();
        }
        scanFaceCertifyView = null;
    }
}
