package com.pasc.lib.loginbase.login.sms;

public interface SmsLoginContract {
    public interface View extends com.pasc.lib.loginbase.login.template.LoginContractTemplate.View {
        void showFetchVerifyCodeSuccessUI();

        void showTickingUI(long var1);

        void showTickFinishUI();

        void showFetchingVerifyCodeLoading();

        void showFetchVerifyCodeFailUI(int var1, String var2);

        void onPhoneError(String var1);

        void onVerifyCodeError(String var1);
    }

    public interface Model extends com.pasc.lib.loginbase.login.template.LoginContractTemplate.Model<SmsLoginContract.Presenter> {
        void fetchSmsVerifyCode(String var1);

        void fetchSmsVerifyCodeFromRemote(String var1);

        void startCounting();

        boolean isCounting();

        void login(String var1, String var2);
    }

    public interface Presenter extends com.pasc.lib.loginbase.login.template.LoginContractTemplate.Presenter {
        void fetchSmsVerityCode(String var1);

        void onFetchVerifyCodeSuccess();

        void onFetchVerifyCodeFail(int var1, String var2);

        void onTick(long var1);

        void onTickFinish();

        void login(String var1, String var2);

        boolean checkVerifyCode(String var1);

        void onFetchingVerifyCode();
    }
}