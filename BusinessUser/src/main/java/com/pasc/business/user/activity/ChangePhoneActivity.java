package com.pasc.business.user.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.pasc.business.user.R;
import com.pasc.business.user.base.BaseMoreActivity;
import com.pasc.business.user.even.ChangePhoneFinish;
import com.pasc.business.user.iview.InputPhoneView;
import com.pasc.business.user.iview.SendSmsView;
import com.pasc.business.user.net.resp.AccoutCalceResp;
import com.pasc.business.user.net.resp.ChangePhoneResp;
import com.pasc.business.user.net.resp.SMSNewResp;
import com.pasc.business.user.net.resp.SMSResp;
import com.pasc.business.user.presenter.InputPhonePresenter;
import com.pasc.business.user.presenter.SendSmsPresenter;
import com.pasc.business.user.view.VerificationCodeEditText;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.userbase.base.event.EventTag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public class ChangePhoneActivity extends BaseMoreActivity implements InputPhoneView,View.OnClickListener{
    TextView tvBtn;
    String oldPhone;
    TextView tvFirst;
    TextView tvEnd;
    InputPhonePresenter mPresenter;
    VerificationCodeEditText editText;
    public static void startActivity(Context context){
        Intent intent = new Intent(context,ChangePhoneActivity.class);
        context.startActivity(intent);
    }
    @Override
    public void initData() {
        if (!TextUtils.isEmpty(oldPhone) && oldPhone.length() >= 11){
            tvFirst.setText(oldPhone.substring(0,3));
            tvEnd.setText(oldPhone.substring(7,11));
        }
        tvBtn.setAlpha(0.3f);
        tvBtn.setEnabled(false);

    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mPresenter = new InputPhonePresenter(this);
        tvFirst = findViewById(R.id.tv_phone_first);
        tvEnd = findViewById(R.id.tv_phone_end);
        oldPhone = AppProxy.getInstance().getUserManager().getMobile();
        tvBtn = findViewById(R.id.user_commit);
        tvBtn.setOnClickListener(this);
        editText  = findViewById(R.id.edit_VerificationCodeEditText);
        editText.setOnInputListener(new VerificationCodeEditText.OnInputListener() {
            @Override
            public void OnEdittextChange(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence)){
                    if (charSequence.length() == 4){
                        tvBtn.setAlpha(1.0f);
                        tvBtn.setEnabled(true);
                        return;
                    }

                }
                tvBtn.setAlpha(0.3f);
                tvBtn.setEnabled(false);
            }

            @Override
            public void OnInputOk(String codeNum) {

            }
        });
    }

    @Override
    protected int layoutResId() {
        return R.layout.user_activity_account_change_phone;
    }



    @Override
    public void isLegatily(ChangePhoneResp resp) {

    }

    @Override
    public void mobileVerly(ChangePhoneResp resp) {
        InputPhoneActivity.satrActivity(ChangePhoneActivity.this,"");
    }

    @Override
    public void onPhoneError(String msg) {

    }

    @Override
    public void sendSms(SMSNewResp resp) {

    }

    @Override
    public void onError(String code, String error) {
        ToastUtils.toastMsg(error);
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
    public void onClick(View view) {
        String mobile = AppProxy.getInstance().getUserManager().getMobile();
       mPresenter.moblieVerly(mobile.substring(0,3)+editText.getCode()+mobile.substring(7,11));
    }
    /**
     *
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinish(ChangePhoneFinish event) {
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
