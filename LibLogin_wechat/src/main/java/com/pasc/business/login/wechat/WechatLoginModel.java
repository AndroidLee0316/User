package com.pasc.business.login.wechat;

import android.util.Log;

import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.user.LoginConstant;
import com.pasc.lib.userbase.user.net.param.BindThirdPartParam;
import com.pasc.lib.userbase.user.net.param.UnbindThirdParam;
import com.pasc.lib.userbase.user.net.resp.BaseRespV2Observer;
import com.pasc.lib.userbase.user.third.ThirdCallBack;
import com.pasc.lib.userbase.base.data.user.ThirdLoginUser;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.net.param.ThirdLoginParam;
import com.pasc.lib.userbase.user.net.resp.BaseRespObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class WechatLoginModel {

    /**
     * 微信登陆
     * @param code  微信授权返回的code
     * @param loginCallBack 登陆回调
     */
    public void wxLogin(String code, final ThirdCallBack.ILoginCallBack loginCallBack) {
        ThirdLoginParam param = new ThirdLoginParam("", "", LoginConstant.LOGIN_TYPE_WX, code);
        UserBiz.thirdLogin(param)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseRespObserver<ThirdLoginUser>() {
                    @Override
                    public void onSuccess(ThirdLoginUser user) {
                        super.onSuccess(user);
                        loginCallBack.loginSuccess(user);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        super.onError(code, msg);
                        loginCallBack.loginFailed(String.valueOf(code), msg);
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
