package com.pasc.business.login.base;

import android.content.res.Resources;
import android.widget.TextView;

import com.pasc.business.login.R;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.userbase.base.CallBack;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.net.resp.BaseRespObserver;
import com.pasc.lib.userbase.user.net.resp.CheckVerifyCodeResp;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public class LoginHelper {
    private RxAppCompatActivity mActivity;
    CountDownHandler mHandler;

    public LoginHelper(RxAppCompatActivity activity) {
        mActivity = activity;
    }

    public void sendVerifyCode(String phoneNumber, String type, CallBack<VoidObject> callBack) {
        UserBiz.sendVerifyCode(phoneNumber, type)
                .compose(mActivity.<VoidObject>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseRespObserver<VoidObject>() {
                    @Override
                    public void onSuccess(VoidObject voidObject) {
                        super.onSuccess(voidObject);
                        callBack.onSuccess(voidObject);
                    }

                    @Override
                    public void onError(String msg) {
                        callBack.onFail("", msg);
                    }
                });
    }

    public void checkVerifycode(String phoneNum, String type, String verifyCode, CallBack<CheckVerifyCodeResp> callBack) {
        UserBiz.checkVerifyCode(phoneNum, type, verifyCode)
                .compose(mActivity.<CheckVerifyCodeResp>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseRespObserver<CheckVerifyCodeResp>() {
                    @Override
                    public void onSuccess(CheckVerifyCodeResp verifyCodeResp) {
                        callBack.onSuccess(verifyCodeResp);
                    }

                    @Override
                    public void onError(String msg) {
                        callBack.onFail("", msg);
                    }
                });
    }

    public void updatePWD(String mobileNo, String pwd,String validateCode, CallBack<VoidObject> callBack) {
        UserBiz.pwdUpdate(mobileNo, pwd,validateCode)
                .compose(mActivity.<VoidObject>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseRespObserver<VoidObject>() {
                    @Override
                    public void onSuccess(VoidObject voidObject) {
                        super.onSuccess(voidObject);
                        callBack.onSuccess(voidObject);
                    }

                    @Override
                    public void onError(String msg) {
                        callBack.onFail("", msg);
                    }
                });
    }

    public void resetPWD(String mobileNo, String pwd,String validateCode, CallBack<VoidObject> callBack) {
        UserBiz.pwdReset(mobileNo, pwd, validateCode)
                .compose(mActivity.<VoidObject>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseRespObserver<VoidObject>() {
                    @Override
                    public void onSuccess(VoidObject voidObject) {
                        super.onSuccess(voidObject);
                        callBack.onSuccess(voidObject);
                    }

                    @Override
                    public void onError(String msg) {
                        callBack.onFail("", msg);
                    }
                });
    }

    public void forgetPWD(String phoneNum, String pwd, String validateCode, CallBack<VoidObject> callBack) {

        UserBiz.pwdForget(phoneNum, pwd, validateCode)
                .compose(mActivity.<VoidObject>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseRespObserver<VoidObject>() {
                    @Override
                    public void onSuccess(VoidObject voidObject) {
                        super.onSuccess(voidObject);
                        callBack.onSuccess(voidObject);
                    }

                    @Override
                    public void onError(String msg) {
                        callBack.onFail("", msg);
                    }
                });
    }

    /**
     * 验证码重发倒计时
     */
    public void showCountDown(int countDownTime, TextView textView, Resources resources) {
        textView.setClickable(false);
        mHandler = new CountDownHandler(textView, resources);
        mHandler.setCountDownTime(countDownTime);
        textView.setTextColor(resources.getColor(R.color.user_verify_code_disable_color));
        mHandler.sendEmptyMessage(0);
    }
}
