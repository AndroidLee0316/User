package com.pasc.business.user.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.user.R;
import com.pasc.business.user.base.BaseMoreActivity;
import com.pasc.business.user.data.UserConstants;
import com.pasc.business.user.even.AccoutCalceFinish;
import com.pasc.business.user.even.ChangePhoneFinish;
import com.pasc.business.user.iview.InputPhoneView;
import com.pasc.business.user.iview.SendSmsView;
import com.pasc.business.user.net.resp.AccoutCalceResp;
import com.pasc.business.user.net.resp.ChangePhoneResp;
import com.pasc.business.user.net.resp.SMSResp;
import com.pasc.business.user.presenter.InputPhonePresenter;
import com.pasc.business.user.presenter.SendSmsPresenter;
import com.pasc.business.user.utils.EvenUtil;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.user.IUserInfo;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.view.FormatEditText;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.widget.dialog.OnCloseListener;
import com.pasc.lib.widget.dialog.common.ConfirmDialogFragment;
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
public class SendSmsActivity extends BaseMoreActivity implements View.OnClickListener, SendSmsView {

    TextView hintWarningTV;
    TextView textView;
    FormatEditText etVerifyCode;
    TextView tvGetVerifyCode;
    Button next;
    String code;
    SendSmsPresenter mPresenter;
    String phoneNo;
    String inputType;
    String sendSmsType;
    /**
     * 校验码，由验证手机号接口返回
     */
    String preValidCode;
    String certId;

    public static void startActivity(Context context, String phone, String preValidCode) {
        Intent intent = new Intent(context, SendSmsActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("preValidCode", preValidCode);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String phone, String inputType, String certId, String preValidCode) {
        Intent intent = new Intent(context, SendSmsActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("inputType", inputType);
        intent.putExtra("certId", certId);
        intent.putExtra("preValidCode", preValidCode);
        context.startActivity(intent);
    }

    @Override
    protected int layoutResId() {
        return R.layout.user_activity_account_send_sms;
    }

    @Override
    public void initData() {
        mPresenter = new SendSmsPresenter(this);
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        hintWarningTV = findViewById(R.id.user_activity_account_send_sms_hint_warning);
        textView = findViewById(R.id.tv_phone);
        phoneNo = getIntent().getStringExtra("phone");
        inputType = getIntent().getStringExtra("inputType");
        certId = getIntent().getStringExtra("certId");
        preValidCode = getIntent().getStringExtra("preValidCode");
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

        if (UserConstants.TYPE_SEND_SMS_ACCOUT_CALCE.equals(inputType)) {
            sendSmsType = SendSmsPresenter.CODE_SMS_ACCOUT_CALCE;
            hintWarningTV.setText(R.string.user_account_calce_send_sms_hint);
            next.setText(R.string.user_account_calce_sure);
            titleBar.setTitleText(R.string.user_account_calce);
        } else {
            sendSmsType = SendSmsPresenter.CODE_SMS_CHANGE;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.user_commit) {
            if (UserConstants.TYPE_SEND_SMS_ACCOUT_CALCE.equals(inputType)) {
                mPresenter.accoutCalce(code, sendSmsType);
            } else {
                mPresenter.commit(phoneNo, code, certId);
            }
        } else if (view.getId() == R.id.user_tv_get_verify_code) {
            if (!mPresenter.isCounting()) {
                mPresenter.sendSms(phoneNo, sendSmsType, preValidCode);
            }
        }
    }

    @Override
    public void onError(String code, String error) {
        if (UserConstants.TYPE_SEND_SMS_ACCOUT_CALCE.equals(inputType)) {
            AccoutCalceErrorActivity.startActivity(this, getString(R.string.user_account_vcode_error));
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
    public void sendSms(SMSResp resp) {
        if (resp != null) {
            ToastUtils.toastMsg(R.string.user_account_vcode_send_success);
        }
    }

    @Override
    public void commit(ChangePhoneResp resp) {
        ToastUtils.toastMsg(R.string.user_account_exchange_success);
        IUserInfo userInfo = AppProxy.getInstance().getUserManager().getUserInfo();
        userInfo.setMobileNo(phoneNo);
        AppProxy.getInstance().getUserManager().updateUser(userInfo);
        EventBusOutUtils.postUpdateUserInfo();
        EventBus.getDefault().post(new ChangePhoneFinish());
        EventBusOutUtils.postChangePhoneNumSuccess();
        finish();

    }

    @Override
    public void accoutCalce(AccoutCalceResp resp) {
        AccoutCalcePaySuccessActivity.startActivity(this);
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
