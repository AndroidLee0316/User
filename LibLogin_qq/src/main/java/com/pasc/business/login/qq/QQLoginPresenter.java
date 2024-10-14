package com.pasc.business.login.qq;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.pasc.lib.share.ShareManager;
import com.pasc.lib.share.callback.AuthActionCallBack;
import com.pasc.lib.userbase.base.data.bean.QQLoginBean;
import com.pasc.lib.userbase.user.LoginConstant;
import com.pasc.lib.userbase.user.net.param.BindThirdPartParam;
import com.pasc.lib.userbase.user.third.ThirdCallBack;

public class QQLoginPresenter {

    /**
     * QQ授权
     * @param context
     * @param authorizeCallBack
     */
    public void auth(Context context, final ThirdCallBack.IAuthorizeCallBack authorizeCallBack){
        ShareManager.getInstance().authorizeForQQ((Activity) context,
                new AuthActionCallBack() {
                    @Override
                    public void onComplete(int i, String s) {
                        try {
                            Gson gson = new Gson();
                            QQLoginBean bean = gson.fromJson(s, QQLoginBean.class);
                            authorizeCallBack.authorizeSuccess(bean.getOpenid(), bean.getAccess_token());
                        } catch (Exception e) {
                            e.printStackTrace();
                            authorizeCallBack.authorizeFailed(String.valueOf(i),"授权失败");
                        }

                    }

                    @Override
                    public void onCancel(int i) {
                        authorizeCallBack.authorizeFailed(String.valueOf(i),"取消授权");
                    }

                    @Override
                    public void onError(int i, Throwable throwable) {
                        authorizeCallBack.authorizeFailed(String.valueOf(i),throwable.getMessage());
                    }
                });
    }

    /**
     * QQ登陆
     * @param context
     * @param authorizeCallBack
     * @param loginCallBack
     */
    public void login(Context context, final ThirdCallBack.IAuthorizeCallBack authorizeCallBack, final ThirdCallBack.ILoginCallBack loginCallBack) {
        //QQ登陆前需要先授权
        auth(context, new ThirdCallBack.IAuthorizeCallBack() {
            @Override
            public void authorizeFailed(String errorCode, String message) {
                authorizeCallBack.authorizeFailed(errorCode, message);
            }

            @Override
            public void authorizeSuccess(String openID, String accessToken) {
                authorizeCallBack.authorizeSuccess(openID, accessToken);
                //授权完后执行调用服务器QQ登陆
                QQLoginModel qqLoginModel = new QQLoginModel();
                qqLoginModel.qqLogin(openID, accessToken,loginCallBack);
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

                requestParam.accessToken = authCode;
                requestParam.openid = openID;
                requestParam.loginType = LoginConstant.LOGIN_TYPE_QQ;

                QQLoginModel model = new QQLoginModel();
                model.bindThird(requestParam, bindThirdCallBack);
            }

        });
    }

    public void unBind(ThirdCallBack.IUnBindThirdCallBack callBack){
        QQLoginModel model = new QQLoginModel();
        model.unbindThird(LoginConstant.LOGIN_TYPE_QQ,callBack);
    }

}
