package com.pasc.business.login.third;

import android.content.Context;

import com.pasc.lib.userbase.base.data.user.ThirdLoginUser;

public class ThirdLoginContract {
    public interface View {

        void qqAuthorizeFailed(String errorCode, String msg);

        void qqAuthorizeSuccess(String openID, String accessToken);

        void qqLoginSuccess(ThirdLoginUser user);

        void qqLoginError(String code, String msg);

        void wxLoginSuccess(ThirdLoginUser user);

        void wxAuthorizeFailed(String errorCode, String msg);

        void wxAuthorizeSuccess(String code);

        void wxLoginError(String code, String msg);

        void alipayAuthorizeFailed(String code, String msg);

        void alipayAuthorizeSuccess();

        void alipayLoginSuccess(ThirdLoginUser user);

        void alipayLoginError(String code, String msg);


    }

    public interface Presenter {
        void qqAuthorize(Context context);

        void qqLogin(Context context);

        void wxAuthorize(Context context);

        void wxLogin(Context context);

        void alipayAuthorize(Context context);

        void alipayLogin(Context context);

    }
}