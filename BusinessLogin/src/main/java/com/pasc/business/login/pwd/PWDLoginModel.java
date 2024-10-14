package com.pasc.business.login.pwd;

import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.net.resp.BaseRespV2Observer;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class PWDLoginModel implements PWDLoginContract.Model {
    PWDLoginContract.View loginView;
    LifecycleProvider<ActivityEvent> provider;

    public PWDLoginModel(PWDLoginContract.View view) {
        loginView = view;
        provider = (LifecycleProvider<ActivityEvent>) view;
    }

    @Override
    public void pwdLogin(String phoneNumber, String password) {
        UserBiz.pwdLogin(phoneNumber, password)
                .compose(provider.<User>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseRespV2Observer<User>() {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        loginView.onPWDLoginSuccess(user);
                    }

                    @Override
                    public void onError(String code, String msg) {
                        super.onError(code, msg);
                        loginView.onPWDLoginError(code, msg);
                    }
                });
    }
}
