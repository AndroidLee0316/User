package com.pasc.business.login.alipay.main;

import com.pasc.lib.userbase.base.data.user.ThirdLoginUser;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/9
 */
public interface CallBack {

    static interface GetAuthInfoCallBack {
        void onSuccess(String authInfo);
        void onFailed(String errorCode, String errorMsg);
    }

    static interface AuthorizeCallBack {
        void callBack(String openID, String authCode);
    }

    static interface LoginCallBack{
        void onFailed(String errorCode, String errorMsg);
        void onSuccess(ThirdLoginUser user);
    }

}
