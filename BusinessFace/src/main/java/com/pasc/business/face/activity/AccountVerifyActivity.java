package com.pasc.business.face.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.face.R;
import com.pasc.business.face.data.FaceConstant;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.base.view.FormatEditText;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.net.resp.BaseRespObserver;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.userbase.user.util.UserManagerImpl;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

/**
 * Created by luyang001 on 2018/1/12.
 * 人脸设置
 */

@Route(path = RouterTable.Face.PATH_FACE_ACCOUNT_VERIFY_ACT)
public class AccountVerifyActivity extends BaseStatusBarActivity implements View.OnClickListener {

    TextView phoneTv, tvGetVirifyCode;
    FormatEditText pwdEt;
    Button btnCommit;

    private User user;

    private int toNextType;
    private CommonTitleView titleBar;

    private boolean isTimerRunning = false;
    private MyCountDownTimer countDownTimer;
    private String phoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_activity_account_verify);
        EventBus.getDefault().register(this);
        phoneTv = findViewById(R.id.user_tv_phone);
        pwdEt = findViewById(R.id.user_et_pwd);
        pwdEt.setFormatType(FormatEditText.TYPE_SMS_CODE);
        btnCommit = findViewById(R.id.user_btn_commit);
        titleBar = findViewById(R.id.user_ctv_topbar);
        tvGetVirifyCode = findViewById(R.id.user_tv_get_verify_code);

        btnCommit.setOnClickListener(this);
        titleBar.setOnLeftClickListener(this);

        countDownTimer = new MyCountDownTimer(this, 1000 * 60, 1000);
        user = UserManagerImpl.getInstance().getInnerUser();
        if (user != null && !TextUtils.isEmpty(user.mobileNo)) {
            phoneNumber = user.mobileNo;
            phoneTv.setText(CommonUtils.hidePhoneNo(phoneNumber));
        }
        pwdEt.setEditTextChangeListener(new FormatEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                s = s.replace(" ", "");
                setBtnNextClickable();
                if (s.length() == 6) {
                    verifyAccount();
                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        toNextType = bundle.getInt(FaceConstant.VERIFY_TYPE, 0);
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {

    }

    /**
     * 设置下一步按钮是否可点击
     */
    private void setBtnNextClickable() {
        boolean bool1 = pwdEt.getOriginalText().length() > 0;

        if (bool1) {
            btnCommit.setAlpha(1.0f);
            btnCommit.setEnabled(true);
        } else {
            btnCommit.setAlpha(0.3f);
            btnCommit.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_title_left) {
            keyBack();
        } else if (i == R.id.user_btn_commit) {
            if (pwdEt.getOriginalText().isEmpty()) {
                CommonUtils.toastMsg("密码不能为空");
                return;
            }
            verifyAccount();
        } else if (i == R.id.user_tv_get_verify_code) {
            sendVerifyCode();
        }
    }

    private void sendVerifyCode() {
        showLoading(getString(R.string.user_code_sending));
        UserBiz.sendVerifyCode(phoneNumber, Constant.SMS_REAL_NAME_AUTH)
                .compose(this.<VoidObject>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseRespObserver<VoidObject>() {
                    @Override
                    public void onError(String msg) {
                        dismissLoading();
                        CommonUtils.toastMsg(msg);
                    }

                    @Override
                    public void onSuccess(VoidObject voidObject) {
                        super.onSuccess(voidObject);
                        if (!isTimerRunning) {
                            countDownTimer.start();
                        }
                        dismissLoading();
                        CommonUtils.toastMsg(getString(R.string.user_code_sent));
                    }
                });
    }

    private void toNextActivity(final int toNextType) {
        switch (toNextType) {
            case FaceConstant.VERIFY_TYPE_FACE_INPUT:
                Intent intent1 = new Intent(this, FaceDetectInputActivity.class);
                startActivity(intent1);
                break;
            case FaceConstant.VERIFY_TYPE_FACE_RESET:
                Intent intent2 = new Intent(this, FaceDetectResetActivity.class);
                startActivity(intent2);
                break;
        }
    }

    /**
     * 人脸设置成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccess(BaseEvent event) {
        if (EventTag.USER_REGISTER_FACE_SUCCESS.equals(event.getTag()) || EventTag.USER_RESET_FACE_SUCCESS.equals(event.getTag())) {
            finish();
        }
    }

    private void verifyAccount() {
        showLoading("");
        UserBiz.getVerifyOpenFace(user.mobileNo,
                Constant.SMS_REAL_NAME_AUTH,
                pwdEt.getOriginalText())
                .compose(this.<VoidObject>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseRespObserver<VoidObject>() {
                    @Override
                    public void onError(String msg) {
                        dismissLoading();
                        CommonUtils.toastMsg(msg);
                    }

                    @Override
                    public void onSuccess(VoidObject voidObject) {
                        dismissLoading();
                        toNextActivity(toNextType);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        countDownTimer.cancel();
    }

    private static class MyCountDownTimer extends CountDownTimer {

        private final WeakReference<AccountVerifyActivity> mWeakReference;
        private AccountVerifyActivity mActivity;

        public MyCountDownTimer(AccountVerifyActivity activity, long millisInFuture,
                                long countDownInterval) {
            super(millisInFuture, countDownInterval);
            mWeakReference = new WeakReference<>(activity);
            mActivity = mWeakReference.get();
        }

        @Override
        public void onTick(long l) {
            if (mActivity != null) {
                mActivity.tvGetVirifyCode.setText(
                        String.format(mActivity.getString(R.string.user_resend_code_login), l / 1000));
                mActivity.tvGetVirifyCode.setTextColor(
                        ContextCompat.getColor(mActivity, R.color.user_verify_code_disable_color));
                mActivity.isTimerRunning = true;
                mActivity.tvGetVirifyCode.setClickable(false);
            }
        }

        @Override
        public void onFinish() {
            if (mActivity != null) {
                mActivity.tvGetVirifyCode.setText(mActivity.getString(R.string.user_get_code_again));
                mActivity.tvGetVirifyCode.setTextColor(
                        ContextCompat.getColor(mActivity, R.color.pasc_primary));
                mActivity.isTimerRunning = false;
                mActivity.tvGetVirifyCode.setClickable(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        keyBack();
    }

    private void keyBack(){
        //人脸比对
        if (FaceConstant.VERIFY_TYPE_FACE_INPUT == toNextType){
            EventBusOutUtils.postRegisterFaceCancle();
        }else if (FaceConstant.VERIFY_TYPE_FACE_RESET == toNextType){
            EventBusOutUtils.postRestFaceCancle();
        }else {
            Log.e(AccountVerifyActivity.class.getName(),"toNextType unknow");
        }
        finish();
    }
}
