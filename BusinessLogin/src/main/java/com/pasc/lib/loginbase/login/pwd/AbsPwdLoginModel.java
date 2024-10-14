package com.pasc.lib.loginbase.login.pwd;

import com.pasc.lib.loginbase.login.pwd.PwdLoginContract.Model;
import com.pasc.lib.loginbase.login.pwd.PwdLoginContract.Presenter;

public abstract class AbsPwdLoginModel implements Model {
    protected Presenter presenter;

    public AbsPwdLoginModel() {
    }

    public void release() {
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}