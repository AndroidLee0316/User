package com.pasc.business.login.pwd;

import com.pasc.lib.userbase.base.data.user.User;

public class PWDLoginContract {
    public interface View {
        void onPWDLoginSuccess(User user);
        void onPWDLoginError(String code, String msg);
    }

    public interface Model {
        void pwdLogin(String phoneNumber, String password);
    }

    public interface Presenter {
        void pwdLogin(String phoneNumber, String password);
    }
}
