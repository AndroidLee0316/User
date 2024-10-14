package com.pasc.lib.userbase.user.third;

import com.pasc.lib.userbase.base.data.user.ThirdLoginUser;
import com.pasc.lib.userbase.base.data.user.User;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/9
 */
public interface ThirdCallBack {


    /**
     * 授权回调
     */
    interface IAuthorizeCallBack {
        void authorizeFailed(String errorCode, String message);
        void authorizeSuccess(String openID, String authCode);
    }

    /**
     * 登陆回调
     */
    interface ILoginCallBack {
        void loginFailed(String errorCode, String message);
        void loginSuccess(ThirdLoginUser user);
    }


    /**
     * 第三方绑定回调
     */
    interface IBindThirdCallBack {
        void onSuccess(User user);

        void onError(String errorCode, String errorMsg);
    }



    /**
     * 第三方解绑回调
     */
    interface IUnBindThirdCallBack {
        void onSuccess();

        void onError(String errorCode, String errorMsg);
    }

}
