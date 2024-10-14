package com.pasc.business.cert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.pasc.business.cert.CertifyUtils;
import com.pasc.business.cert.R;
import com.pasc.business.cert.iview.ICertifyCompleteView;
import com.pasc.business.cert.presenter.CertifyCompletePresenter;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.activity.BaseActivity;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.view.FormatEditText;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.event.EventKey;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.user.certification.net.resp.CertifyCompleteResp;
import com.pasc.lib.userbase.user.certification.net.resp.RealNameByBankResp;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;


/**
 * 实名认证完成页面
 */

public class CertificationCompletelyActivity extends BaseStatusBarActivity implements View.OnClickListener, ICertifyCompleteView {

    private TextView getCodeTv;
    private FormatEditText clearEditText;
    private TextView tvNext;
    private boolean isTimerRunning = false;
    private MyCountDownTimer mCountDownTimer;
    private CertifyCompletePresenter certifyCompletePresenter;
    public static final String REAL_NAME_BY_BANK_RESP = "realNameByBankResp";
    private RealNameByBankResp realNameByBankResp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        certifyCompletePresenter = new CertifyCompletePresenter(this);
        setContentView(R.layout.cert_activity_certification_completely);
        CommonTitleView ctvTitle = findViewById(R.id.user_ctv_title);
        getCodeTv = findViewById(R.id.user_tv_get_code);
        clearEditText = findViewById(R.id.user_et_verification_code);
        clearEditText.setFormatType(FormatEditText.TYPE_SMS_CODE);
        tvNext = findViewById(R.id.user_tv_next);
        TextView tvMobileNo = findViewById(R.id.user_activity_bankCertified_tvMobileNo);
        getCodeTv.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        ctvTitle.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            realNameByBankResp = (RealNameByBankResp) bundle.getSerializable(REAL_NAME_BY_BANK_RESP);
            if (realNameByBankResp != null) {
                tvMobileNo.setText(CertifyUtils.encryptedAndSpace(3, 7, realNameByBankResp.mobileNo));
            }
        }
        clearEditText.setEditTextChangeListener(new FormatEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                s = s.replace(" ", "");
                setBtnNextClickable();
                if (s.length() == 6) {
                    confirmByBank();
                }
            }
        });
        initData();
    }

    private void initData() {
        mCountDownTimer = new MyCountDownTimer(this, 1000 * 60, 1000);
    }

    private void setBtnNextClickable() {
        boolean b = clearEditText.getOriginalText().length() > 0;
        if (b) {
            tvNext.setEnabled(true);
            tvNext.setAlpha(1f);
        } else {
            tvNext.setEnabled(false);
            tvNext.setAlpha(0.3f);
        }
    }

    /**
     * 发送验证码
     */
    private void sendCertifyCode() {
        certifyCompletePresenter.sendCertifyCode(realNameByBankResp.mobileNo);
    }

    /**
     * 银行卡认证
     */
    private void confirmByBank() {
        String code = clearEditText.getOriginalText();
        if (!CommonUtils.isVerifyCodeLegal(code)) {
            CommonUtils.toastMsg("验证码格式有误");
            return;
        }
        showLoading();
        certifyCompletePresenter.confirmByBank(realNameByBankResp, code);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.user_tv_get_code) {
            clearEditText.setText("");
            sendCertifyCode();
        } else if (i == R.id.user_tv_next) {
            confirmByBank();
        }
    }

    @Override
    public void sendCertificationCodeSucc() {
        dismissLoading();
        CommonUtils.toastMsg("发送成功");
        if (!isTimerRunning) {
            mCountDownTimer.start();
        }
    }

    @Override
    public void sendCertificationCodeFail(String code, String errorMsg) {
        dismissLoading();
        CommonUtils.toastMsg(errorMsg);
    }

    @Override
    public void confirmByBankSucc(CertifyCompleteResp certifyCompleteResp) {
        dismissLoading();
        User user = (User) AppProxy.getInstance().getUserManager().getUserInfo();
        user.userName = realNameByBankResp.name;
        user.idCard = realNameByBankResp.idNo;

        //删除，使用 addCertType 替换
        //user.setCertiType(User.CERTIFY_BANK);
        user.addCertType(User.CERTIFY_BANK);

        user.sex = CommonUtils.checkSex(realNameByBankResp.idNo);
        AppProxy.getInstance().getUserManager().updateUser(user);

        intentToSuccessActivity();
    }

    /**
     * 跳转到成功页面
     */
    protected void intentToSuccessActivity(){

        Intent intent = new Intent(this, CertifySuccActivity.class);
        intent.putExtra(Constant.CERT_TYPE, Constant.CERT_TYPE_BANK);
        startActivity(intent);
        finish();
    }

    @Override
    public void confirmByBankFail(String code, String errorMsg) {
        dismissLoading();
        CommonUtils.toastMsg(errorMsg);
    }

    private static class MyCountDownTimer extends CountDownTimer {
        private final WeakReference<CertificationCompletelyActivity> mWeakReference;
        private CertificationCompletelyActivity mActivity;

        public MyCountDownTimer(CertificationCompletelyActivity activity, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            mWeakReference = new WeakReference<>(activity);
            mActivity = mWeakReference.get();
        }

        @Override
        public void onTick(long l) {
            if (mActivity != null) {
                mActivity.getCodeTv.setText(String.format(mActivity.getString(R.string.user_resend_code_login), l / 1000));
                mActivity.getCodeTv.setTextColor(ContextCompat.getColor(mActivity, R.color.blue_aaddff));
                mActivity.isTimerRunning = true;
                mActivity.getCodeTv.setClickable(false);
            }
        }

        @Override
        public void onFinish() {
            if (mActivity != null) {
                mActivity.getCodeTv.setText(mActivity.getString(R.string.user_get_code_again));
                mActivity.getCodeTv.setTextColor(ContextCompat.getColor(mActivity, R.color.blue_27a5f9));
                mActivity.isTimerRunning = false;
                mActivity.getCodeTv.setClickable(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
        certifyCompletePresenter.onDestroy();
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {

    }
}
