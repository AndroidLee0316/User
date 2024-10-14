package com.pasc.lib.loginbase.login.template;

import android.text.TextUtils;

import com.pasc.lib.loginbase.login.template.LoginContractTemplate.Model;
import com.pasc.lib.loginbase.login.template.LoginContractTemplate.Presenter;
import com.pasc.lib.loginbase.login.template.LoginContractTemplate.View;

public abstract class LoginPresenterTemplate implements Presenter {
    private final LoginEventHandler handler;
    private View viewTemplate;
    private Model modelTemplate;

    public LoginPresenterTemplate(View viewTemplate, Model modelTemplate, LoginEventHandler handler) {
        this.viewTemplate = viewTemplate;
        this.modelTemplate = modelTemplate;
        this.handler = handler;
    }

    public boolean checkPhoneNum(String phone) {
        return !TextUtils.isEmpty(phone) && phone.length() == 11;
    }

    public <T> void onLoginSuccess(T t) {
        this.viewTemplate.hideLoading();
        this.handler.handleLoginSuccessEvent(t);
    }

    public void onLoginFail(int code, String msg) {
        this.viewTemplate.hideLoading();
        this.handler.handleLoginFailEvent(code, msg);
    }

    public void release() {
        this.modelTemplate.release();
    }

}
