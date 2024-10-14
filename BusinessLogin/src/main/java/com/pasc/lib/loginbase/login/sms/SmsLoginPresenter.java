package com.pasc.lib.loginbase.login.sms;

import android.text.TextUtils;

import com.pasc.lib.loginbase.login.sms.SmsLoginContract.Model;
import com.pasc.lib.loginbase.login.sms.SmsLoginContract.Presenter;
import com.pasc.lib.loginbase.login.sms.SmsLoginContract.View;
import com.pasc.lib.loginbase.login.template.LoginEventHandler;
import com.pasc.lib.loginbase.login.template.LoginPresenterTemplate;

public class SmsLoginPresenter extends LoginPresenterTemplate implements Presenter {
    private final Model model;
    private final View view;

    public SmsLoginPresenter(Model model, View view, LoginEventHandler handler) {
        super(view, model, handler);
        this.model = model;
        this.view = view;
        model.setPresenter(this);
    }

    public void fetchSmsVerityCode(String phone) {
        if (this.checkPhoneNum(phone)) {
            this.model.fetchSmsVerifyCode(phone);
        } else {
            this.view.onPhoneError(phone);
        }

    }

    public void onFetchVerifyCodeSuccess() {
        this.view.showFetchVerifyCodeSuccessUI();
        this.view.hideLoading();
        this.model.startCounting();
    }

    public void onFetchVerifyCodeFail(int code, String msg) {
        this.view.showFetchVerifyCodeFailUI(code, msg);
        this.view.hideLoading();
    }

    public void onTick(long seconds) {
        this.view.showTickingUI(seconds);
    }

    public void onTickFinish() {
        this.view.showTickFinishUI();
    }

    public void login(String phone, String verifyCode) {
        if (!this.checkPhoneNum(phone)) {
            this.view.onPhoneError(phone);
        } else if (!this.checkVerifyCode(verifyCode)) {
            this.view.onVerifyCodeError(verifyCode);
        } else {
            this.model.login(phone, verifyCode);
            this.view.showLoginLoading();
        }
    }

    public boolean checkVerifyCode(String verifyCode) {
        return !TextUtils.isEmpty(verifyCode) && verifyCode.matches("^[0-9]{6}$");
    }

    public void onFetchingVerifyCode() {
        this.view.showFetchingVerifyCodeLoading();
    }

    /**
     * 是否正在倒计时
     * @return
     */
    public boolean isCouting(){
        return this.model.isCounting();
    }
}
