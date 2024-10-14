package com.pasc.business.login;

import com.pasc.lib.userbase.base.CallBack;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.net.resp.BaseRespV2Observer;
import com.pasc.lib.userbase.user.net.resp.GetUserInfoResp;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public class LoginModel implements LoginContract.Model {
    LoginContract.View loginView;
    private final RxAppCompatActivity context;

    public LoginModel(LoginContract.View view) {
        loginView = view;
        context = (RxAppCompatActivity) view;
    }

//    @Override
//    public void getUserInfoByMobile(String mobile, CallBack<GetUserInfoResp> callBack) {
//        UserBiz.getUserInfoByMobile(mobile)
//                .compose(context.bindToLifecycle())
//                .subscribe(new BaseRespV2Observer<GetUserInfoResp>() {
//                    @Override
//                    public void onSuccess(GetUserInfoResp getUserInfoResp) {
//                        super.onSuccess(getUserInfoResp);
//                        callBack.onSuccess(getUserInfoResp);
//                    }
//
//                    @Override
//                    public void onError(String code, String msg) {
//                        super.onError(msg);
//                        callBack.onFail(code, msg);
//                    }
//                });
//    }
}
