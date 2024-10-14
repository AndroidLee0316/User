package com.pasc.business.user.presenter;

import android.os.CountDownTimer;
import android.text.TextUtils;
import com.pasc.business.user.iview.SendNewSmsView;
import com.pasc.business.user.iview.SendSmsView;
import com.pasc.business.user.net.PhoneBiz;
import com.pasc.business.user.net.pamars.ChangePhonePamars;
import com.pasc.business.user.net.pamars.SetPhoneParams;
import com.pasc.business.user.net.resp.AccoutCalceResp;
import com.pasc.business.user.net.resp.ChangePhoneResp;
import com.pasc.business.user.net.resp.SMSNewResp;
import com.pasc.business.user.net.resp.SMSResp;
import com.pasc.business.user.net.resp.SetPhoneResp;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.net.resp.BaseRespThrowableObserver;
import io.reactivex.functions.Consumer;
import java.util.Set;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public class SendNewSmsPresenter implements IPresenter {
  public SendNewSmsView view;

  public SendNewSmsPresenter(SendNewSmsView view) {
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

  public void sendSms(String mobile, String credential) {
    startCounting();
    view.showLoadings();
    disposables.add(PhoneBiz.sendNewSms(mobile, credential).subscribe(new Consumer<SMSNewResp>() {
      @Override
      public void accept(SMSNewResp resp) throws Exception {
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

  public void commit(String mobile, String certId, String code) {
    view.showLoadings();
    SetPhoneParams setPhoneParams = new SetPhoneParams(mobile, certId, code);
    disposables.add(PhoneBiz.setPhone(setPhoneParams).subscribe(new Consumer<SetPhoneResp>() {
      @Override
      public void accept(SetPhoneResp resp) throws Exception {
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

  /**
   * 是否正在倒计时
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
