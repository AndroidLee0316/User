package com.pasc.business.login.third;

import android.content.Context;
import android.util.Log;

import com.pasc.lib.userbase.user.third.IThridLoginService;
import com.pasc.lib.userbase.user.third.ThirdCallBack;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.user.ThirdLoginUser;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.HashMap;

public class ThirdLoginPresenter implements ThirdLoginContract.Presenter {
    ThirdLoginContract.View loginView;
    ThirdLoginHelper thirdLogin;

    public ThirdLoginPresenter(ThirdLoginContract.View view) {
        loginView = view;
        thirdLogin = new ThirdLoginHelper((RxAppCompatActivity) loginView);
    }

    @Override
    public void qqAuthorize(Context context) {
        IThridLoginService qqLoginService = BaseJumper.getService(RouterTable.Login_qq.PATH_LOGIN_ACTIVITY);
        if (qqLoginService != null){
            qqLoginService.auth(context, null, new ThirdCallBack.IAuthorizeCallBack() {
                @Override
                public void authorizeFailed(String errorCode, String message) {
                    loginView.qqAuthorizeFailed(errorCode,message);
                }

                @Override
                public void authorizeSuccess(String openID, String authCode) {
                    loginView.qqAuthorizeSuccess(openID, authCode);
                }
            });
        }
    }

    @Override
    public void qqLogin(Context context) {
        IThridLoginService qqLoginService = BaseJumper.getService(RouterTable.Login_qq.PATH_LOGIN_ACTIVITY);
        if (qqLoginService != null){
            qqLoginService.login(context, null, new ThirdCallBack.IAuthorizeCallBack() {
                @Override
                public void authorizeFailed(String errorCode, String message) {
                    loginView.qqAuthorizeFailed(errorCode, message);
                }

                @Override
                public void authorizeSuccess(String openID, String authCode) {
                    loginView.qqAuthorizeSuccess(openID, authCode);
                }
            }, new ThirdCallBack.ILoginCallBack() {
                @Override
                public void loginFailed(String errorCode, String message) {
                    loginView.qqLoginError(errorCode, message);
                }

                @Override
                public void loginSuccess(ThirdLoginUser user) {
                    loginView.qqLoginSuccess(user);
                }
            });
        }
    }

    @Override
    public void wxAuthorize(Context context) {
        IThridLoginService wxLoginService = BaseJumper.getService(RouterTable.Login_wx.PATH_LOGIN_ACTIVITY);
        if (wxLoginService != null){
            wxLoginService.auth(context, null, new ThirdCallBack.IAuthorizeCallBack() {
                @Override
                public void authorizeFailed(String errorCode, String message) {
                    loginView.wxAuthorizeFailed(errorCode, message);
                }

                @Override
                public void authorizeSuccess(String openID, String authCode) {
                    loginView.wxAuthorizeSuccess(authCode);
                }
            });
        }
    }

    @Override
    public void wxLogin(Context context) {
        IThridLoginService wxLoginService = BaseJumper.getService(RouterTable.Login_wx.PATH_LOGIN_ACTIVITY);
        if (wxLoginService != null){
            wxLoginService.login(context, null, new ThirdCallBack.IAuthorizeCallBack() {
                @Override
                public void authorizeFailed(String errorCode, String message) {
                    loginView.wxAuthorizeFailed(errorCode, message);
                }

                @Override
                public void authorizeSuccess(String openID, String authCode) {
                    loginView.wxAuthorizeSuccess(authCode);
                }

            }, new ThirdCallBack.ILoginCallBack() {
                @Override
                public void loginFailed(String errorCode, String message) {
                    loginView.wxLoginError(errorCode,message);
                }

                @Override
                public void loginSuccess(ThirdLoginUser user) {
                    loginView.wxLoginSuccess(user);
                }
            });
        }
    }

    @Override
    public void alipayAuthorize(Context context) {
        IThridLoginService thridLoginService = BaseJumper.getService(RouterTable.Login_alipay.PATH_LOGIN_ACTIVITY);
        if (thridLoginService != null){
            thridLoginService.auth(context, null, new ThirdCallBack.IAuthorizeCallBack() {
                @Override
                public void authorizeFailed(String errorCode, String message) {
                    loginView.alipayAuthorizeFailed(errorCode, message);
                }

                @Override
                public void authorizeSuccess(String openID, String authCode) {
                    loginView.alipayAuthorizeSuccess();
                }

            });
        }
    }

    @Override
    public void alipayLogin(Context context) {
        IThridLoginService thridLoginService = BaseJumper.getService(RouterTable.Login_alipay.PATH_LOGIN_ACTIVITY);
        if (thridLoginService != null){
            thridLoginService.login(context, null, new ThirdCallBack.IAuthorizeCallBack() {
                @Override
                public void authorizeFailed(String errorCode, String message) {
                    loginView.alipayAuthorizeFailed(errorCode, message);
                }

                @Override
                public void authorizeSuccess(String openID, String authCode) {
                    loginView.alipayAuthorizeSuccess();
                }

            }, new ThirdCallBack.ILoginCallBack() {
                @Override
                public void loginFailed(String errorCode, String message) {
                    loginView.alipayLoginError(errorCode,message);
                }

                @Override
                public void loginSuccess(ThirdLoginUser user) {
                    loginView.alipayLoginSuccess(user);
                }
            });
        }
    }

}
