package com.pasc.business.cert.presenter;

import com.pasc.business.cert.ipresenter.IPresenter;
import com.pasc.business.cert.iview.ICardCertifyView;
import com.pasc.lib.net.resp.BaseRespThrowableObserver;
import com.pasc.lib.userbase.user.certification.net.CertifyBiz;
import com.pasc.lib.userbase.user.certification.net.resp.RealNameByBankResp;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class CardCertifyPresenter implements IPresenter {
    private ICardCertifyView cardCertifyView;
    private CompositeDisposable disposables = new CompositeDisposable();

    public CardCertifyPresenter(ICardCertifyView cardCertifyView) {
        this.cardCertifyView = cardCertifyView;
    }

    /**
     * 根据姓名和身份证号判断是否被其他账号认证过
     *  20191230 过期，使用函数{@checkIsCerted}替换
     * @param idNo     身份证号
     * @param certType 认证类型, 1:银行卡认证, 2:人脸认证
     */
    @Deprecated
    public void queryIsCerted(String idNo, String certType) {
        disposables.add(CertifyBiz.queryIsCerted(idNo, certType).subscribe(
                new Consumer<String>() {
                    @Override
                    public void accept(String data) {
                        if (cardCertifyView != null) {
                            cardCertifyView.queryIsCertedSucc(data);
                        }

                    }
                }, new BaseRespThrowableObserver() {
                    @Override
                    public void onV2Error(String code, String errorMsg) {
                        if (cardCertifyView != null) {
                            cardCertifyView.queryIsCertedFail(code, errorMsg);
                        }
                    }
                }));
    }
    /**
     * 根据姓名和身份证号判断是否被其他账号认证过
     *
     * @param idNo     身份证号
     * @param certType 认证类型, 1:银行卡认证, 2:人脸认证
     */
    public void checkIsCerted(String userName,String idNo, String certType) {
        disposables.add(CertifyBiz.checkIsCerted(userName,idNo, certType).subscribe(
                new Consumer<String>() {
                    @Override
                    public void accept(String data) {
                        if (cardCertifyView != null) {
                            cardCertifyView.queryIsCertedSucc(data);
                        }

                    }
                }, new BaseRespThrowableObserver() {
                    @Override
                    public void onV2Error(String code, String errorMsg) {
                        if (cardCertifyView != null) {
                            cardCertifyView.queryIsCertedFail(code, errorMsg);
                        }
                    }
                }));
    }

    /**
     * 更换绑定账号
     */
    public void realNameByBank(String idNo, String name, String bankCardNo, String mobileNo) {
        disposables.add(CertifyBiz.realNameByBank(idNo, name, bankCardNo, mobileNo).subscribe(
                new Consumer<RealNameByBankResp>() {
                    @Override
                    public void accept(RealNameByBankResp realNameByBankResp) {
                        if (cardCertifyView != null) {
                            cardCertifyView.realNameByBankSucc(realNameByBankResp);
                        }
                    }
                }, new BaseRespThrowableObserver() {
                    /***由于后台在错误数据里面添加了T(泛型), 所以这里的errorMsg实际为T(泛型)对应的jsonString*/
                    @Override
                    public void onV2Error(String code, String errorMsg) {
                        if (cardCertifyView != null) {
                            cardCertifyView.realNameByBankFail(code, errorMsg);
                        }
                    }
                }
        ));
    }

    @Override
    public void onDestroy() {
        if (!disposables.isDisposed()) {
            disposables.clear();
        }
        cardCertifyView = null;
    }
}
