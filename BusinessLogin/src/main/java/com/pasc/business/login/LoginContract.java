package com.pasc.business.login;

import android.view.ViewTreeObserver;

import com.pasc.lib.userbase.base.CallBack;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.user.net.resp.GetUserInfoResp;

public class LoginContract {
    public interface View {

    }

    public interface Model {
//        void getUserInfoByMobile(String mobile, CallBack<GetUserInfoResp> callBack);
    }

    public interface Presenter {
        ViewTreeObserver.OnGlobalLayoutListener addLayoutListener(final android.view.View rootView, final android.view.View coverView);

        void onLoginSuccessAction(User user);

//        void getUserInfoByMobile(String mobile, CallBack<GetUserInfoResp> callBack);
    }
}
