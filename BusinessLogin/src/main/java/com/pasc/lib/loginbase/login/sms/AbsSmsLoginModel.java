package com.pasc.lib.loginbase.login.sms;

import android.os.CountDownTimer;
import com.pasc.lib.loginbase.login.sms.SmsLoginContract.Model;
import com.pasc.lib.loginbase.login.sms.SmsLoginContract.Presenter;

public abstract class AbsSmsLoginModel implements Model {
    protected Presenter presenter;
    private CountDownTimer counter;
    private final int countSeconds;
    private long currentDownSeconds;

    public AbsSmsLoginModel() {
        this(60);
    }

    public AbsSmsLoginModel(int countSeconds) {
        this.countSeconds = countSeconds;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void fetchSmsVerifyCode(String phone) {
        if (this.currentDownSeconds == 0L) {
            this.fetchSmsVerifyCodeFromRemote(phone);
            this.presenter.onFetchingVerifyCode();
        }
    }

    public void startCounting() {
        if (this.counter == null) {
            this.counter = new CountDownTimer((long)(this.countSeconds * 1000), 1000L) {
                public void onTick(long l) {
                    AbsSmsLoginModel.this.currentDownSeconds = l / 1000L;
                    AbsSmsLoginModel.this.presenter.onTick(AbsSmsLoginModel.this.currentDownSeconds);
                }

                public void onFinish() {
                    AbsSmsLoginModel.this.currentDownSeconds = 0L;
                    AbsSmsLoginModel.this.presenter.onTickFinish();
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

    /**
     * 是否正在倒计时
     * @return
     */
    @Override
    public boolean isCounting(){
        if (currentDownSeconds == 0L){
            return false;
        }
        return true;
    }

}
