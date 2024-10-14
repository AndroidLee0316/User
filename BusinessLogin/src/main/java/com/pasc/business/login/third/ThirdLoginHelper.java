package com.pasc.business.login.third;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.pasc.business.login.R;
import com.pasc.business.login.util.ThirdPartUtils;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.userbase.base.data.bean.QQLoginBean;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.net.param.BindThirdPartParam;
import com.pasc.lib.userbase.user.net.param.UnbindThirdParam;
import com.pasc.lib.userbase.user.net.resp.BaseRespObserver;
import com.pasc.lib.userbase.user.net.resp.BaseRespV2Observer;
import com.pasc.lib.userbase.user.third.ThirdCallBack;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by kuangxiangkui192 on 2018/12/21.
 */
public class ThirdLoginHelper {

    private RxAppCompatActivity mActivity;

    public ThirdLoginHelper(RxAppCompatActivity activity) {
        mActivity = activity;
    }

    public void bindThird(BindThirdPartParam param, ThirdCallBack.IBindThirdCallBack callBack) {
        UserBiz.bindThird(param)
                .compose(mActivity.<User>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseRespObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        callBack.onSuccess(user);
                    }

                    @Override
                    public void onError(String msg) {
                        callBack.onError("",msg);
                    }
                });
    }

}
