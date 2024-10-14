package com.pasc.business.login;

import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.gson.Gson;
import com.pasc.lib.userbase.base.CallBack;
import com.pasc.business.login.util.LoginSuccessActionUtils;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.base.util.SPUtils;
import com.pasc.lib.hybrid.PascHybrid;
import com.pasc.lib.router.interceptor.LoginInterceptor;
import com.pasc.lib.userbase.base.UserConstant;
import com.pasc.lib.userbase.base.data.bean.WebUserBean;
import com.pasc.lib.userbase.base.data.user.ThirdLoginUser;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.user.UserProxy;
import com.pasc.lib.userbase.user.net.resp.GetUserInfoResp;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.userbase.user.util.UserManagerImpl;

import org.greenrobot.eventbus.EventBus;

public class LoginPresenter implements LoginContract.Presenter {

    /**
     * 用户不存在
     */
    public static final String USER_ACCOUNT_NOT_IN = "11003";
    LoginContract.View loginView;
    LoginContract.Model loginModel;

    public LoginPresenter(LoginContract.View view) {
        loginView = view;
        loginModel = new LoginModel(loginView);
    }

//    @Override
//    public void getUserInfoByMobile(String mobile, CallBack<GetUserInfoResp> callBack) {
//        loginModel.getUserInfoByMobile(mobile, callBack);
//    }

    @Override
    public void onLoginSuccessAction(User tUser) {
        UserManagerImpl mUserManager = UserManagerImpl.getInstance();
        User user = tUser;
        if (tUser instanceof ThirdLoginUser) {
            user = new Gson().fromJson(new Gson().toJson(tUser), User.class);
        }
        // ARouter拦截器的回调
        UserProxy.getInstance().getDataBaseContext().switchUserDb(user.getMobileNo());

        mUserManager.updateUser(user);
        mUserManager.updateLoginSuccess();
        EventBusOutUtils.postLoginSuccess();
        LoginInterceptor.notifyCallBack(true);
    }

    /**
     * 监测如果键盘覆盖了登录按钮，则将视图上移
     *
     * @param rootView  需要移动的view
     * @param coverView 需要避免被遮挡的view
     * @return global listener
     */
    @Override
    public ViewTreeObserver.OnGlobalLayoutListener addLayoutListener(View rootView, final View coverView) {
        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);
                int[] location = new int[2];
                coverView.getLocationInWindow(location);
                int scrollHeight = (location[1] + coverView.getHeight()) - rect.bottom;
                if (scrollHeight > 0) {
                    rootView.scrollBy(0, (int) (scrollHeight + coverView.getHeight() * 0.4));
                } else if (scrollHeight < -400) {
                    rootView.scrollTo(0, 0);
                }
            }
        };
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        return globalLayoutListener;
    }
}
