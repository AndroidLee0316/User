package com.pasc.business.user.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.pasc.business.user.R;
import com.pasc.business.user.base.BaseMoreActivity;
import com.pasc.business.user.data.UserConstants;
import com.pasc.business.user.even.AccoutCalceFinish;
import com.pasc.business.user.iview.SendNewSmsView;
import com.pasc.business.user.net.resp.SMSNewResp;
import com.pasc.business.user.net.resp.SetPhoneResp;
import com.pasc.business.user.presenter.SendNewSmsPresenter;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.userbase.base.view.FormatEditText;
import com.pasc.lib.widget.toolbar.ClearEditText;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public class SendNewSmsActivity extends BaseMoreActivity implements View.OnClickListener,
    SendNewSmsView {

  TextView textView;
  FormatEditText etVerifyCode;
  TextView tvGetVerifyCode;
  Button next;
  String code;
  SendNewSmsPresenter mPresenter;
  String phoneNo;
  String inputType;
  String sendSmsType;
  String certId;
  String setPhoneCertId;

  public static void startActivity(Context context, String phone) {
    Intent intent = new Intent(context, SendNewSmsActivity.class);
    intent.putExtra("phone", phone);
    context.startActivity(intent);
  }

  public static void startActivity(Context context, String phone, String inputType, String certId) {
    Intent intent = new Intent(context, SendNewSmsActivity.class);
    intent.putExtra("phone", phone);
    intent.putExtra("inputType", inputType);
    intent.putExtra("certId", certId);
    context.startActivity(intent);
  }

  @Override
  protected int layoutResId() {
    return R.layout.user_activity_account_send_new_sms;
  }

  @Override
  public void initData() {
    mPresenter = new SendNewSmsPresenter(this);
    mPresenter.startCounting();
  }

  @Override
  public void initView() {
    EventBus.getDefault().register(this);
    textView = findViewById(R.id.tv_phone);
    phoneNo = getIntent().getStringExtra("phone");
    inputType = getIntent().getStringExtra("inputType");
    certId = getIntent().getStringExtra("certId");
    setPhoneCertId = certId;
    textView.setText(phoneNo.substring(0, 3) + " **** **" + phoneNo.substring(9, 11));
    etVerifyCode = findViewById(R.id.user_et_code);
    tvGetVerifyCode = findViewById(R.id.user_tv_get_verify_code);
    next = findViewById(R.id.user_commit);
    next.setEnabled(false);
    next.setAlpha(0.3f);
    next.setOnClickListener(this);
    tvGetVerifyCode.setOnClickListener(this);
    etVerifyCode.setFormatType(FormatEditText.TYPE_PHONE);
    etVerifyCode.setEditTextChangeListener(new ClearEditText.EditTextChangeListener() {
      @Override
      public void afterChange(String s) {
        code = etVerifyCode.getText().toString().trim();

        if (!TextUtils.isEmpty(code)) {
          if (code.length() == 7) {
            next.setEnabled(true);
            next.setAlpha(1.0f);
            code = code.replace(" ", "");
          } else {
            next.setEnabled(false);
            next.setAlpha(0.3f);
          }
        } else {
          next.setEnabled(false);
          next.setAlpha(0.3f);
        }
      }
    });
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.user_commit) {
      mPresenter.commit(phoneNo, setPhoneCertId, code);
    } else if (view.getId() == R.id.user_tv_get_verify_code) {
      if (!mPresenter.isCounting()) {
        mPresenter.sendSms(phoneNo, certId);
      }
    }
  }

  @Override
  public void onError(String code, String error) {
    if (UserConstants.TYPE_SEND_SMS_ACCOUT_CALCE.equals(inputType)) {

      AccoutCalceErrorActivity.startActivity(this, "短信验证码错误，请重试");
    } else {
      ToastUtils.toastMsg(error);
    }
  }

  @Override
  public void showLoadings() {
    showLoading();
  }

  @Override
  public void dismissLoadings() {
    dismissLoading();
  }

  @Override
  public void sendSms(SMSNewResp resp) {
    if (resp != null) {
      ToastUtils.toastMsg("验证码发送成功");
      setPhoneCertId = resp.credential;
    }
  }

  @Override
  public void commit(SetPhoneResp resp) {
    if (!resp.isSuccess) {
      ToastUtils.toastMsg("新手机绑定失败");
      return;
    }
    startActivity(new Intent(this, SetPhoneSuccessActivity.class));
    finish();
  }

  @Override
  public void onTick(long l) {
    tvGetVerifyCode.setText(getString(com.pasc.business.login.R.string.user_resend_code_login, l));
    tvGetVerifyCode.setAlpha(0.4f);
    tvGetVerifyCode.setEnabled(false);
  }

  @Override
  public void onTickFinish() {
    tvGetVerifyCode.setText(getString(com.pasc.business.login.R.string.user_get_code_again));
    tvGetVerifyCode.setAlpha(1.0f);
    tvGetVerifyCode.setEnabled(true);
  }

  @Override
  public void onPhoneError(String msg) {
    ToastUtils.toastMsg(msg);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mPresenter.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onFinish(AccoutCalceFinish event) {
    finish();
  }
}
