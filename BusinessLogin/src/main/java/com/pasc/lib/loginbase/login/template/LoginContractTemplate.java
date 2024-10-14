package com.pasc.lib.loginbase.login.template;

import com.pasc.lib.loginbase.BaseModel;
import com.pasc.lib.loginbase.BaseView;

public interface LoginContractTemplate {
    public interface Presenter {
        boolean checkPhoneNum(String phoneNum);

        <T> void onLoginSuccess(T obj);

        void onLoginFail(int errorCode, String errorMsg);

        void release();
    }

    public interface Model<T> extends BaseModel<T> {
        void release();
    }

    public interface View extends BaseView {
        void hideLoading();

        void showLoginLoading();
    }
}
