package com.pasc.business.login.wechat;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.pasc.lib.userbase.user.LoginConstant;
import com.pasc.lib.userbase.user.net.param.BindThirdPartParam;
import com.pasc.lib.userbase.user.third.ThirdCallBack;
import com.pasc.lib.share.ShareManager;

public class WechatLoginPresenter {

    /**
     * 微信授权
     * 注意：需要在微信方案中的 WXEntryActivity 调用 WechatAuthOutListener 通知我们结果
     * @param context
     * @param authorizeCallBack 授权回调
     */
    public void auth(Context context, final ThirdCallBack.IAuthorizeCallBack authorizeCallBack){
        //监听授权回调
        WechatAuthOutListener.getInstance().setCallback(new WechatAuthOutListener.CallBack() {
            @Override
            public void authFailed(String errorCode, String errorMsg) {
                authorizeCallBack.authorizeFailed(errorCode, errorMsg);
            }

            @Override
            public void authSuccess(String code) {
                authorizeCallBack.authorizeSuccess(null, code);
            }
        });
        ShareManager.getInstance().authorizeForWechat((Activity) context);
    }

    /**
     * 微信登陆
     * @param context
     * @param authorizeCallBack 授权回调
     * @param loginCallBack 登陆回调
     */
    public void login(Context context, final ThirdCallBack.IAuthorizeCallBack authorizeCallBack, final ThirdCallBack.ILoginCallBack loginCallBack) {
        //登陆前需要先授权
        auth(context, new ThirdCallBack.IAuthorizeCallBack() {
            @Override
            public void authorizeFailed(String errorCode, String message) {
                authorizeCallBack.authorizeFailed(errorCode, message);
            }

            @Override
            public void authorizeSuccess(String openID, String accessToken) {
                authorizeCallBack.authorizeSuccess(openID, accessToken);
                //调用服务器第三方登陆接口
                WechatLoginModel wechatLoginModel = new WechatLoginModel();
                wechatLoginModel.wxLogin(accessToken,loginCallBack);
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

                requestParam.code = authCode;
                requestParam.loginType = LoginConstant.LOGIN_TYPE_WX;

                WechatLoginModel model = new WechatLoginModel();
                model.bindThird(requestParam, bindThirdCallBack);
            }

        });
    }

    public void unBind( ThirdCallBack.IUnBindThirdCallBack callBack){
        WechatLoginModel model = new WechatLoginModel();
        model.unbindThird(LoginConstant.LOGIN_TYPE_WX,callBack);
    }


}
