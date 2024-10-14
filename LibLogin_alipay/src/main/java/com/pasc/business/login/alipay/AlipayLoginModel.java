package com.pasc.business.login.alipay;

import com.pasc.business.login.alipay.main.CallBack;
import com.pasc.business.login.alipay.param.GetAilAuthRequestInfoParam;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.userbase.base.data.user.ThirdLoginUser;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.user.LoginConstant;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.net.param.BindThirdPartParam;
import com.pasc.lib.userbase.user.net.param.ThirdLoginParam;
import com.pasc.lib.userbase.user.net.param.UnbindThirdParam;
import com.pasc.lib.userbase.user.net.resp.BaseRespObserver;
import com.pasc.lib.userbase.user.net.resp.BaseRespV2Observer;
import com.pasc.lib.userbase.user.third.ThirdCallBack;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AlipayLoginModel  {

    public void getAilAuthRequestInfo(final CallBack.GetAuthInfoCallBack callBack) {
        GetAilAuthRequestInfoParam param = new GetAilAuthRequestInfoParam();
        AlipayLoginBiz.getAilAuthRequestInfoParam(param)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseRespObserver<String>() {
                    @Override
                    public void onSuccess(String authInfo) {
                        super.onSuccess(authInfo);
                        callBack.onSuccess(authInfo);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        super.onError(code, msg);
                        callBack.onFailed(String.valueOf(code), msg);
                    }
                });
    }

    public void alipayLogin(String openID, String authCode, final CallBack.LoginCallBack callBack) {
        ThirdLoginParam param = new ThirdLoginParam("", openID, LoginConstant.LOGIN_TYPE_ALIPAY, authCode);
        UserBiz.thirdLogin(param)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseRespObserver<ThirdLoginUser>() {
                    @Override
                    public void onSuccess(ThirdLoginUser user) {
                        super.onSuccess(user);
                        callBack.onSuccess(user);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        super.onError(code, msg);
                        callBack.onFailed(String.valueOf(code), msg);
                    }
                });
    }


    /**
     * 绑定
     * @param param
     * @param callBack
     */
    public void bindThird(BindThirdPartParam param, final ThirdCallBack.IBindThirdCallBack callBack) {
        UserBiz.bindThird(param)
                .subscribe(new BaseRespObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        callBack.onSuccess(user);
                    }

                    @Override
                    public void onError(String msg) {
                        callBack.onError("", msg);
                    }
                });
    }

    /**
     * 解绑
     */
    public void unbindThird(String loginType, final ThirdCallBack.IUnBindThirdCallBack callBack) {
        UnbindThirdParam param = new UnbindThirdParam(loginType);
        UserBiz.unbindThird(param)
                .subscribe(new BaseRespV2Observer<VoidObject>() {
                    @Override
                    public void onSuccess(VoidObject voidObject) {
                        super.onSuccess(voidObject);
                        callBack.onSuccess();
                    }

                    @Override
                    public void onError(String code, String msg) {
                        super.onError(msg);
                        callBack.onError(code, msg);
                    }
                });
    }


}
