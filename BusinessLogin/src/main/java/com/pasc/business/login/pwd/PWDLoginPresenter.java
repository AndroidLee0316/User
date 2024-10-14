package com.pasc.business.login.pwd;

public class PWDLoginPresenter implements PWDLoginContract.Presenter {
    PWDLoginContract.View loginView;
    PWDLoginContract.Model loginModel;

    public PWDLoginPresenter(PWDLoginContract.View view) {
        loginView = view;
        loginModel = new PWDLoginModel(loginView);
    }

    @Override
    public void pwdLogin(String phoneNumber, String password) {
        loginModel.pwdLogin(phoneNumber, password);
    }
}
