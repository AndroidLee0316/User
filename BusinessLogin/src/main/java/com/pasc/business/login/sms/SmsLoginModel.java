package com.pasc.business.login.sms;

import com.pasc.lib.loginbase.login.sms.AbsSmsLoginModel;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.net.resp.BaseRespObserver;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SmsLoginModel extends AbsSmsLoginModel {

    private final RxAppCompatActivity context;

    public SmsLoginModel(RxAppCompatActivity context) {
        this.context = context;
    }

    @Override
    public void fetchSmsVerifyCodeFromRemote(String phoneNumber) {
        UserBiz.sendVerifyCode(phoneNumber, Constant.SMS_MOBILE_LOGIN)
                .compose(context.bindToLifecycle())
                .subscribe(new BaseRespObserver<VoidObject>() {

                    @Override
                    public void onSuccess(VoidObject voidObject) {
                        super.onSuccess(voidObject);
                        presenter.onFetchVerifyCodeSuccess();
                    }

                    @Override
                    public void onError(int i, String msg) {
                        presenter.onFetchVerifyCodeFail(i, msg);
                    }
                });
    }

    @Override
    public void login(String phoneNumber, String verifyCode) {
        UserBiz.verifyAndLogin(verifyCode, Constant.SMS_MOBILE_LOGIN, phoneNumber)
                .compose(context.bindToLifecycle())
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseRespObserver<User>() {
                    @Override
                    public void onError(int code, String msg) {
                        presenter.onLoginFail(code, msg);
                    }

                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        presenter.onLoginSuccess(user);
                    }
                });
    }
}
