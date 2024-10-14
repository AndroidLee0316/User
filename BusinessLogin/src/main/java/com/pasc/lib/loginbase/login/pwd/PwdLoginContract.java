package com.pasc.lib.loginbase.login.pwd;

public interface PwdLoginContract {
    public interface View extends com.pasc.lib.loginbase.login.template.LoginContractTemplate.View {
        void onPhoneError(String msg);

        void onPwdError(String msg);
    }

    public interface Model extends com.pasc.lib.loginbase.login.template.LoginContractTemplate.Model<PwdLoginContract.Presenter> {
        void login(String username, String password);
    }

    public interface Presenter extends com.pasc.lib.loginbase.login.template.LoginContractTemplate.Presenter {
        boolean checkPwd(String password);

        void login(String username, String password);
    }
}
