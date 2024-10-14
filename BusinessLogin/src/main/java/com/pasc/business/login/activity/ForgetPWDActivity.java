package com.pasc.business.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.login.R;
import com.pasc.lib.userbase.user.LoginConstant;
import com.pasc.business.login.base.LoginHelper;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.statistics.StatisticsManager;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.base.CallBack;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.base.view.FormatEditText;
import com.pasc.lib.userbase.user.net.resp.CheckVerifyCodeResp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.pasc.lib.statistics.PageType.APP;

/**
 * Created by ex-luyang001 on 2017/11/29.
 */
@Route(path = RouterTable.Login.PATH_LOGIN_FORGETPWD_ACTIVITY)
public class ForgetPWDActivity extends BaseStatusBarActivity implements View.OnClickListener {

    protected TextView tv_phone;

    protected TextView tv_get_code;

    protected Button btn_next;

    protected String phoneNum;
    protected FormatEditText etCode;
    protected CommonTitleView topBar;
    protected String mValidateCode;
    protected LoginHelper mLoginHelper;

    protected String sendType = Constant.SMS_UPDATE_PSD;
    protected String eventTag = "找回密码页面";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_forget_pwd);
        StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_FIND_PASSWORD, eventTag, APP, null);
        EventBus.getDefault().register(this);
        topBar = findViewById(R.id.user_top_bar);
        tv_phone = findViewById(R.id.user_tv_phone);
        etCode = findViewById(R.id.user_et_code);
        etCode.setFormatType(FormatEditText.TYPE_SMS_CODE);
        tv_get_code = findViewById(R.id.user_tv_get_code);

        btn_next = findViewById(R.id.user_btn_next);
        btn_next.setOnClickListener(this);
        topBar.setOnLeftClickListener(this);
        tv_get_code.setOnClickListener(this);

        mLoginHelper = new LoginHelper(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phoneNum = bundle.getString(LoginConstant.PHONE_NUMBER);
        }
        if (!TextUtils.isEmpty(phoneNum)) {
            String hidePhoneNo = CommonUtils.hidePhoneNo(phoneNum);
            tv_phone.setText(hidePhoneNo);
        }
        initETCodeListener();
    }

    protected void initETCodeListener(){

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setBtnNextClickable();
                if (editable.length() == 7) {
                    if (check()) {
                        checkVerifycode();
                    }
                }
            }
        });
    }

    /**
     * 设置下一步按钮是否可点击
     */
    protected void setBtnNextClickable() {
        boolean bool2 = etCode.getText().length() > 0;
        if (bool2) {
            btn_next.setAlpha(1.0f);
            btn_next.setEnabled(true);
        } else {
            btn_next.setAlpha(0.3f);
            btn_next.setEnabled(false);
        }
    }

    /**
     * 户设置密码成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCertification(BaseEvent event) {
        if (EventTag.USER_RESET_PASS_SUCCEED.equals(event.getTag())) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_title_left) {
            onBackPressed();
        } else if (i == R.id.user_tv_get_code) {
            etCode.setText("");
            sendVerifyCode();
        } else if (i == R.id.user_btn_next) {
            if (check()) {
                checkVerifycode();
            }
        }
    }

    protected void toResetPWD() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundle.putString(LoginConstant.VALIDATE_CODE, mValidateCode);
        }

        Intent intent = new Intent(this, ResetPWDActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private boolean check() {
        String msg = null;
        if (phoneNum.length() != 11) {//手机号码需要是11位
            msg = getString(R.string.user_error_phone);
        } else if (CommonUtils.isEmpty(etCode.getOriginalText())) {
            msg = getString(R.string.user_verify_code_wrong);
        }
        if (msg != null) {
            CommonUtils.toastMsg(msg);
        }
        return msg == null;
    }

    /**
     * 检测验证码
     */
    public void checkVerifycode() {
        btn_next.setEnabled(false);
        mLoginHelper.checkVerifycode(phoneNum, sendType, etCode.getOriginalText(), new CallBack<CheckVerifyCodeResp>() {
            @Override
            public void onSuccess(CheckVerifyCodeResp data) {
                mValidateCode = data.validateCode;
                btn_next.setEnabled(true);
                toResetPWD();
            }

            @Override
            public void onFail(String code, String msg) {
                CommonUtils.toastMsg(msg);
                btn_next.setEnabled(true);
            }
        });
    }

    /**
     * 发送验证码
     */
    public void sendVerifyCode() {
        showLoading(getString(R.string.user_code_sending));
        mLoginHelper.sendVerifyCode(phoneNum, sendType, new CallBack<VoidObject>() {
            @Override
            public void onSuccess(VoidObject data) {
                dismissLoading();
                CommonUtils.toastMsg(getString(R.string.user_code_sent));
                mLoginHelper.showCountDown(60, tv_get_code, getResources());
            }

            @Override
            public void onFail(String code, String msg) {
                dismissLoading();
                CommonUtils.toastMsg(msg);
            }
        });
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {

    }
}
