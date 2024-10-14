package com.pasc.business.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.login.R;
import com.pasc.lib.keyboard.KeyboardBaseView;
import com.pasc.lib.keyboard.KeyboardPopWindow;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.base.view.ClearEditText2;
import com.pasc.lib.userbase.user.LoginConstant;
import com.pasc.business.login.base.LoginHelper;
import com.pasc.lib.base.activity.BaseActivity;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.base.util.InputMethodUtils;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.statistics.StatisticsManager;
import com.pasc.lib.userbase.base.CallBack;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.widget.toolbar.ClearEditText;

import org.greenrobot.eventbus.EventBus;

import static com.pasc.lib.statistics.PageType.APP;

/**
 * Created by kuangxiangkui192 on 2018/12/20.
 */
@Route(path = RouterTable.Login.PATH_LOGIN_RESETPWD_ACTIVITY)
public class ResetPWDActivity extends BaseStatusBarActivity implements View.OnClickListener {

    private CommonTitleView titleBar;
    private ClearEditText2 et_pwd;
    private ClearEditText2 et_confirm_pwd;
    private KeyboardPopWindow et_pwdKPW;
    private KeyboardPopWindow et_confirm_pwdKPW;
    private Button btn_commit;

    private String mPhoneNum;
    private String mValidateCode;
    private int mType = 0;

    private LoginHelper mLoginHelper;

    InputFilter emojiFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (!TextUtils.isEmpty(source) && !CommonUtils.isPwdCharacterLegal(source.toString())) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_reset_password);
        et_pwd = findViewById(R.id.user_et_pwd);
        et_confirm_pwd = findViewById(R.id.user_et_confirm_pwd);
        btn_commit = findViewById(R.id.user_btn_commit);
        titleBar = findViewById(R.id.user_top_bar);
        titleBar.setOnLeftClickListener(this);

        btn_commit.setOnClickListener(this);
        mLoginHelper = new LoginHelper(this);
        getData();

        checkEdit();
    }

    private void checkEdit() {

        et_pwd.setFilters(new InputFilter[]{emojiFilter});
        et_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length()>=32){
                    int selectionEnd = et_pwd.getSelectionEnd();
                    editable.delete(32, selectionEnd);
                }else {
                    et_pwd.setEnabled(true);
                }

                if (editable.toString().length()<=1){
                    et_pwd.updateClearIconVisiable(true);
                }
                setBtnNextClickable();
            }
        });

        et_confirm_pwd.setFilters(new InputFilter[]{emojiFilter});
        et_confirm_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length()>=32){
                    int selectionEnd = et_confirm_pwd.getSelectionEnd();
                    editable.delete(32, selectionEnd);
                }else {
                    et_confirm_pwd.setEnabled(true);
                }

                if (editable.toString().length()<=1){
                    et_confirm_pwd.updateClearIconVisiable(true);
                }
                setBtnNextClickable();
            }
        });

        et_pwdKPW = KeyboardPopWindow.bindEdit(this, et_pwd, KeyboardBaseView.KeyboardNormalTheme.TYPE_NORMAL);
        et_confirm_pwdKPW = KeyboardPopWindow.bindEdit(this, et_confirm_pwd, KeyboardBaseView.KeyboardNormalTheme.TYPE_NORMAL);
        et_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                et_pwdKPW.onEditFocesChange(view,b);
                et_pwd.updateClearIconVisiable(b);
            }
        });
        et_confirm_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                et_confirm_pwdKPW.onEditFocesChange(view,b);
                et_confirm_pwd.updateClearIconVisiable(b);
            }
        });
    }

    /**
     * 设置下一步按钮是否可点击
     */
    private void setBtnNextClickable() {
        String pwd = et_pwd.getText().toString();
        String pwdConfirm = et_confirm_pwd.getText().toString();
        boolean enabled = !TextUtils.isEmpty(pwd)
                && !TextUtils.isEmpty(pwdConfirm)
                && pwd.length() >= 8
                && pwd.length() == pwdConfirm.length();
        btn_commit.setEnabled(enabled);
        btn_commit.setAlpha(enabled ? 1.0f : 0.3f);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        Intent intent = getIntent();
        if (intent != null) {
            mType = bundle.getInt(LoginConstant.PWD_TYPE, LoginConstant.PWD_TYPE_RESET);
            mPhoneNum = bundle.getString(LoginConstant.PHONE_NUMBER);
            mValidateCode = bundle.getString(LoginConstant.VALIDATE_CODE);
        }
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_title_left) {
            onBackPressed();
        } else if (i == R.id.user_btn_commit) {
            if (check()) {
                btn_commit.setEnabled(false);
                btn_commit.setAlpha(0.3f);
                InputMethodUtils.hideInputMethod(this);
                if (mType == LoginConstant.PWD_TYPE_RESET) {
                    resetPWD();
                } else if (mType == LoginConstant.PWD_TYPE_FORGET) {
                    forgetPWD();
                } else if (mType == LoginConstant.PWD_TYPE_UPDATE) {
                    updatePWD();
                }
            }
        }
    }

    private void updatePWD() {
        mLoginHelper.updatePWD(mPhoneNum, et_confirm_pwd.getText().toString(),mValidateCode, new CallBack<VoidObject>() {
            @Override
            public void onSuccess(VoidObject data) {
                modifyPwdSuccess();
            }

            @Override
            public void onFail(String code, String msg) {
                modifyPwdFail(msg);
            }
        });
    }

    private void resetPWD() {
        mLoginHelper.resetPWD(mPhoneNum, et_confirm_pwd.getText().toString(),mValidateCode, new CallBack<VoidObject>() {
            @Override
            public void onSuccess(VoidObject data) {
                modifyPwdSuccess();
            }

            @Override
            public void onFail(String code, String msg) {
                modifyPwdFail(msg);
            }
        });
    }

    private void forgetPWD() {
        mLoginHelper.forgetPWD(mPhoneNum, et_confirm_pwd.getText().toString(), mValidateCode, new CallBack<VoidObject>() {
            @Override
            public void onSuccess(VoidObject data) {
                modifyPwdSuccess();
            }

            @Override
            public void onFail(String code, String msg) {
                modifyPwdFail(msg);
            }
        });
    }

    private void modifyPwdSuccess() {
        if (mType != LoginConstant.PWD_TYPE_RESET) {
            StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_FIND_PASSWORD, "找回密码成功",APP,null);
        }
        actionStart(ResetPWDSuccessActivity.class);
        BaseEvent event = new BaseEvent(EventTag.USER_RESET_PASS_SUCCEED);
        EventBus.getDefault().post(event);
        finish();
    }

    private void modifyPwdFail(String msg) {
        CommonUtils.toastMsg(msg);
        btn_commit.setEnabled(true);
        btn_commit.setAlpha(1.0f);
    }

    private boolean check() {
        String msg = null;
        if (CommonUtils.isEmpty(et_pwd.getText())) {
            msg = "新密码不能为空";
        } else if (CommonUtils.isEmpty(et_confirm_pwd.getText())) {
            msg = "确认密码不能为空";
        } else if (!CommonUtils.isPasswordLegal(et_pwd.getText().toString())) {
            msg = getString(R.string.user_pwd_format_error);
            et_pwd.setText("");
            et_confirm_pwd.setText("");
            et_pwd.requestFocus();
        } else if (!et_pwd.getText().toString().equals(et_confirm_pwd.getText().toString())) {
            msg = "两次密码不一致";
            et_pwd.setText("");
            et_confirm_pwd.setText("");
            et_pwd.requestFocus();
        }
        if (msg != null) {
            CommonUtils.toastMsg(msg);
        }
        return msg == null;
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {

    }
}
