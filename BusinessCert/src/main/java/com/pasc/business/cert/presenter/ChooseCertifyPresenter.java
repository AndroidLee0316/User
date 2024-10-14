package com.pasc.business.cert.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.pasc.business.cert.ipresenter.IPresenter;
import com.pasc.business.cert.iview.IChooseCertifyView;
import com.pasc.lib.net.resp.BaseRespThrowableObserver;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.user.certification.net.CertifyBiz;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.net.resp.BaseRespV2Observer;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.userbase.user.util.UserManagerImpl;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class ChooseCertifyPresenter implements IPresenter {
    private IChooseCertifyView chooseCertifyView;
    private CompositeDisposable disposables = new CompositeDisposable();

    public ChooseCertifyPresenter(IChooseCertifyView chooseCertifyView) {
        this.chooseCertifyView = chooseCertifyView;
    }

    /**
     * 查询认证次数是否超过最大允许次数
     *
     * @param certType 1 银行卡认证 2 人脸认证
     */
    public void queryAuthCount(String certType) {
        disposables.add(CertifyBiz.queryAuthCount(certType).subscribe(new Consumer<VoidObject>() {
            @Override
            public void accept(VoidObject voidObject) {
                if (chooseCertifyView != null) {
                    chooseCertifyView.queryAuthCountSucc();
                }
            }
        }, new BaseRespThrowableObserver() {
            @Override
            public void onV2Error(String code, String errorMsg) {
                if (chooseCertifyView != null) {
                    chooseCertifyView.queryAuthCountFail(code, errorMsg);
                }
            }
        }));
    }

    public void queryUserInfo(int certType, QueryUserInfoCallback callback) {

        //更新用户信息，调用
        UserBiz.getCurrentUserInfo()
                .subscribe(new BaseRespV2Observer<User>() {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);

                        //保存认证信息，这里特别要注意，不能直接用 user，因为获取用户信息返回的User其实是没有token的，特别注意
                        User innerUser = UserManagerImpl.getInstance().getInnerUser();
                        innerUser.userName = user.userName;
                        innerUser.idCard = user.idCard;
                        innerUser.addCertType(String.valueOf(certType));
                        if (!TextUtils.isEmpty(user.idCard) && CommonUtils.checkIdcardValid(user.idCard)) {
                            innerUser.sex = CommonUtils.checkSex(user.idCard);
                        }
                        UserManagerImpl.getInstance().updateUser(innerUser);

                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }

                    @Override
                    public void onError(String code, String msg) {
                        super.onError(msg);
                        if (callback != null) {
                            callback.onFailed(code,msg);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        if (chooseCertifyView != null) {
            chooseCertifyView = null;
        }
        if (!disposables.isDisposed()) {
            disposables.clear();
        }
    }

    public static interface QueryUserInfoCallback{

        void onSuccess();

        void onFailed(String code, String errorMsg);

    }

}
