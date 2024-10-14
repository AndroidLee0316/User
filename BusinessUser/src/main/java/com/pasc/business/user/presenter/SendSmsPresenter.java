package com.pasc.business.user.presenter;

import android.os.CountDownTimer;
import android.text.TextUtils;

import com.pasc.business.user.iview.InputPhoneView;
import com.pasc.business.user.iview.SendSmsView;
import com.pasc.business.user.net.PhoneBiz;
import com.pasc.business.user.net.pamars.ChangePhonePamars;
import com.pasc.business.user.net.pamars.SMSPamars;
import com.pasc.business.user.net.resp.AccoutCalceResp;
import com.pasc.business.user.net.resp.ChangePhoneResp;
import com.pasc.business.user.net.resp.SMSResp;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.loginbase.login.sms.AbsSmsLoginModel;
import com.pasc.lib.net.resp.BaseRespThrowableObserver;

import io.reactivex.functions.Consumer;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public class SendSmsPresenter implements IPresenter {
    public static final String CODE_SMS_CHANGE = "SMS_REBIND_MOBILE";
    public static final String CODE_SMS_ACCOUT_CALCE = "SMS_ACCOUNT_CANCEL";
    public SendSmsView view;

    public SendSmsPresenter(SendSmsView view) {
        this.view = view;
    }

    private CountDownTimer counter;
    private final int countSeconds = 60;
    private long currentDownSeconds;

    @Override
    public void onDestroy() {
        if (!disposables.isDisposed()) {
            disposables.clear();
        }
        view = null;
    }

    public void sendSms(String mobile, String sendSmsType, String preValidCode) {
        startCounting();
        view.showLoadings();
        disposables.add(PhoneBiz.sendSms(mobile, sendSmsType, preValidCode).subscribe(new Consumer<SMSResp>() {
            @Override
            public void accept(SMSResp resp) throws Exception {
                view.dismissLoadings();
                if (view != null) {
                    view.sendSms(resp);
                }

            }
        }, new BaseRespThrowableObserver() {
            @Override
            public void onV2Error(String code, String error) {
                super.onV2Error(code, error);
                view.dismissLoadings();
                view.onError(code, error);
            }
        }));
//        ToastUtils.toastMsg("验证码已发送");
    }

    public void commit(String mobile, String code, String certId) {
        view.showLoadings();
        ChangePhonePamars phonePamars = null;
        String old = AppProxy.getInstance().getUserManager().getMobile();
        if (TextUtils.isEmpty(certId)) {
            phonePamars = new ChangePhonePamars(mobile, code, "SMS_REBIND_MOBILE", old);
        } else {
            phonePamars = new ChangePhonePamars(mobile, code, "SMS_REBIND_MOBILE", old, certId);

        }
        disposables.add(PhoneBiz.changePhone(phonePamars).subscribe(new Consumer<ChangePhoneResp>() {
            @Override
            public void accept(ChangePhoneResp resp) throws Exception {
                view.dismissLoadings();
                if (view != null) {
                    view.commit(resp);
                }

            }
        }, new BaseRespThrowableObserver() {
            @Override
            public void onV2Error(String code, String error) {
                super.onV2Error(code, error);
                view.dismissLoadings();
                view.onError(code, error);
            }
        }));
    }


    public void startCounting() {
        if (this.counter == null) {
            this.counter = new CountDownTimer((long) (this.countSeconds * 1000), 1000L) {
                @Override
                public void onTick(long l) {
                    currentDownSeconds = l / 1000L;
                    if (view != null) {
                        view.onTick(currentDownSeconds);
                    }
                }

                @Override
                public void onFinish() {
                    currentDownSeconds = 0L;
                    if (view != null) {
                        view.onTickFinish();
                    }
                }
            };
        }

        this.counter.start();
    }

    public void release() {
        if (this.counter != null) {
            this.counter.cancel();
        }

    }

    public void accoutCalce(String verificationCode, String verificationType) {

        disposables.add(PhoneBiz.commitCalce("1", verificationCode, verificationType).subscribe(new Consumer<AccoutCalceResp>() {
            @Override
            public void accept(AccoutCalceResp resp) throws Exception {
                view.dismissLoadings();
                if (view != null) {
                    view.accoutCalce(resp);
                }

            }
        }, new BaseRespThrowableObserver() {
            @Override
            public void onV2Error(String code, String error) {
                super.onV2Error(code, error);
                view.dismissLoadings();
                view.onError(code, error);
            }
        }));
    }

    public void accoutCalce(String credential) {

        disposables.add(PhoneBiz.commitCalce("2", credential).subscribe(new Consumer<AccoutCalceResp>() {
            @Override
            public void accept(AccoutCalceResp resp) throws Exception {
                view.dismissLoadings();
                if (view != null) {
                    view.accoutCalce(resp);
                }

            }
        }, new BaseRespThrowableObserver() {
            @Override
            public void onV2Error(String code, String error) {
                super.onV2Error(code, error);
                view.dismissLoadings();
                view.onError(code, error);
            }
        }));
    }

    /**
     * 是否正在倒计时
     *
     * @return
     */
    public boolean isCounting() {
        if (currentDownSeconds == 0L) {
            return false;
        }
        return true;
    }

    public boolean checkVerifyCode(String verifyCode) {
        return !TextUtils.isEmpty(verifyCode) && verifyCode.matches("^[0-9]{6}$");
    }

}
