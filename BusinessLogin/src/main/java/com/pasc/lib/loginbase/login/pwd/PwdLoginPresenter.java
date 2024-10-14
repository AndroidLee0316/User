package com.pasc.lib.loginbase.login.pwd;

import android.text.TextUtils;

import com.pasc.lib.loginbase.login.template.LoginEventHandler;
import com.pasc.lib.loginbase.login.template.LoginPresenterTemplate;

public class PwdLoginPresenter extends LoginPresenterTemplate implements PwdLoginContract.Presenter {
    private final PwdLoginContract.View view;
    private final PwdLoginContract.Model model;

    public PwdLoginPresenter(PwdLoginContract.View view, PwdLoginContract.Model model, LoginEventHandler handler) {
        super(view, model, handler);
        this.view = view;
        this.model = model;
        model.setPresenter(this);
    }

    public boolean checkPwd(String pwd) {
        return !TextUtils.isEmpty(pwd);
    }

    public void login(String phone, String pwd) {
        if (!this.checkPhoneNum(phone)) {
            this.view.onPhoneError(phone);
        } else if (!this.checkPwd(pwd)) {
            this.view.onPwdError(pwd);
        } else {
            this.model.login(phone, pwd);
            this.view.showLoginLoading();
        }
    }
}