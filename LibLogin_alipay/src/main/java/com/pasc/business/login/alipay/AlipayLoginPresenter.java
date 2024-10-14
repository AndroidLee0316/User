package com.pasc.business.login.alipay;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.pasc.business.login.alipay.main.AlipayLoginUtils;
import com.pasc.business.login.alipay.main.CallBack;
import com.pasc.lib.userbase.base.data.user.ThirdLoginUser;
import com.pasc.lib.userbase.user.LoginConstant;
import com.pasc.lib.userbase.user.net.param.BindThirdPartParam;
import com.pasc.lib.userbase.user.third.ThirdCallBack;

public class AlipayLoginPresenter {

    public void auth(final Context context, final ThirdCallBack.IAuthorizeCallBack authorizeCallBack){
        AlipayLoginModel model = new AlipayLoginModel();
        model.getAilAuthRequestInfo(new CallBack.GetAuthInfoCallBack() {
            @Override
            public void onSuccess(String authInfo) {
                AlipayLoginUtils.auth((Activity) context, authInfo, new CallBack.AuthorizeCallBack() {
                    @Override
                    public void callBack(String openID, String authCode) {
                        if (TextUtils.isEmpty(authCode)){//授权失败
                            authorizeCallBack.authorizeFailed("","取消授权");
                        }else {
                            authorizeCallBack.authorizeSuccess(openID, authCode);
                        }
                    }
                });
            }

            @Override
            public void onFailed(String errorCode, String errorMsg) {
                authorizeCallBack.authorizeFailed(errorCode,errorMsg);
            }
        });

    }

    public void login(Context context, final ThirdCallBack.IAuthorizeCallBack authorizeCallBack, final ThirdCallBack.ILoginCallBack loginCallBack) {
        auth(context, new ThirdCallBack.IAuthorizeCallBack() {
            @Override
            public void authorizeFailed(String errorCode, String message) {
                authorizeCallBack.authorizeFailed(errorCode,message);
            }

            @Override
            public void authorizeSuccess(String openID, String authCode) {
                authorizeCallBack.authorizeSuccess(openID, authCode);
                AlipayLoginModel model = new AlipayLoginModel();
                model.alipayLogin(openID, authCode, new CallBack.LoginCallBack() {
                    @Override
                    public void onFailed(String errorCode, String errorMsg) {
                        loginCallBack.loginFailed(errorCode,errorMsg);
                    }

                    @Override
                    public void onSuccess(ThirdLoginUser user) {
                        loginCallBack.loginSuccess(user);
                    }
                });
            }

        });
    }

    public void bind(Context context, final BindThirdPartParam requestParam, final ThirdCallBack.IAuthorizeCallBack authorizeCallBack, final ThirdCallBack.IBindThirdCallBack bindThirdCallBack) {

        auth(context, new ThirdCallBack.IAuthorizeCallBack() {
            @Override
            public void authorizeFailed(String errorCode, String message) {
                authorizeCallBack.authorizeFailed(errorCode,message);
            }

            @Override
            public void authorizeSuccess(String openID, String authCode) {
                authorizeCallBack.authorizeSuccess(openID, authCode);
                AlipayLoginModel model = new AlipayLoginModel();
                requestParam.code = authCode;
                requestParam.accessToken = authCode;
                requestParam.openid = openID;
                requestParam.loginType = LoginConstant.LOGIN_TYPE_ALIPAY;
                model.bindThird(requestParam, bindThirdCallBack);
            }

        });
    }

    public void unBind(ThirdCallBack.IUnBindThirdCallBack callBack){
        AlipayLoginModel model = new AlipayLoginModel();
        model.unbindThird(LoginConstant.LOGIN_TYPE_ALIPAY,callBack);
    }
}
