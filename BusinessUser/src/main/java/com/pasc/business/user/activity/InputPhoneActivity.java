package com.pasc.business.user.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.user.R;
import com.pasc.business.user.base.BaseMoreActivity;
import com.pasc.business.user.even.ChangePhoneFinish;
import com.pasc.business.user.iview.InputPhoneView;
import com.pasc.business.user.net.resp.ChangePhoneResp;
import com.pasc.business.user.net.resp.SMSNewResp;
import com.pasc.business.user.presenter.InputPhonePresenter;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.view.FormatEditText;
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
public class InputPhoneActivity extends BaseMoreActivity implements View.OnClickListener, InputPhoneView {
    TextView textView;
    FormatEditText editText;
    Button next;
    String result;
    InputPhonePresenter mPresenter;
    private String certId;


    public static void satrActivity(Context context, String certId) {
        Intent intent = new Intent(context, InputPhoneActivity.class);
        intent.putExtra("certId", certId);
        context.startActivity(intent);
    }

    @Override
    protected int layoutResId() {
        return R.layout.user_activity_account_input_phone;
    }

    @Override
    public void initData() {
        certId = getIntent().getStringExtra("certId");
        mPresenter = new InputPhonePresenter(this);
        next.setEnabled(false);
        next.setAlpha(0.3f);
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        textView = findViewById(R.id.user_et_user_phone_num_old);
        String phone = AppProxy.getInstance().getUserManager().getUserInfo().getMobileNo();
        if (!TextUtils.isEmpty(phone) && phone.length() >= 11){
            textView.setText(phone.substring(0, 3) + " **** **" + phone.substring(9, 11));
        }
        editText = findViewById(R.id.user_et_user_phone_num);
        editText.setFormatType(FormatEditText.TYPE_PHONE);
        next = findViewById(R.id.user_change_phone_next);
        next.setOnClickListener(this);
        editText.setEditTextChangeListener(new ClearEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                result = editText.getText().toString().trim();

                if (!TextUtils.isEmpty(result)) {
                    if (result.length() == 13) {
                        next.setEnabled(true);
                        next.setAlpha(1.0f);
                        result = result.replace(" ", "");
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
        if (view.getId() == R.id.user_change_phone_next) {
            if (AppProxy.getInstance().getUserManager().getUserInfo().getMobileNo().equals(result)) {
                showDialog(getResources().getString(R.string.user_change_phone_error_dialog_phone));
            } else {
                mPresenter.phoneLegilaty(result);
            }


        }
    }

    @Override
    public void isLegatily(ChangePhoneResp resp) {
        if (TextUtils.isEmpty(certId)) {
            SendSmsActivity.startActivity(InputPhoneActivity.this, resp.mobile, resp.preValidCode);
        } else {
            SendSmsActivity.startActivity(InputPhoneActivity.this, resp.mobile, "", certId, resp.preValidCode);
        }


//        if (("0").equals(resp.result)) {
//                SendSmsActivity.startActivity(InputPhoneActivity.this, resp.mobile);
//        } else if (("2").equals(resp.result)) {
//            showDialog(getResources().getString(R.string.user_change_phone_error_dialog_des));
//        } else if (("1").equals(resp.result)) {
//            showDialog(getResources().getString(R.string.user_change_phone_error_dialog_phone));
//        }
    }

    @Override
    public void mobileVerly(ChangePhoneResp resp) {
        SendSmsActivity.startActivity(InputPhoneActivity.this, result, resp.preValidCode);
    }

    @Override
    public void onPhoneError(String erroe) {
        ToastUtils.toastMsg(erroe);
    }

    @Override public void sendSms(SMSNewResp resp) {

    }

    @Override
    public void onError(String code, String error) {

//        ToastUtils.toastMsg(error);
        if ("201".equals(error) || "500".equals(error) || "404".equals(error)) {
            ToastUtils.toastMsg(error);
        } else {
            showDialog(error);
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

    private ConfirmDialogFragment himtDialog;

    private void showDialog(String msg) {
        if (isFinishing()) {//解决部分机型崩溃问题
            return;
        }
        if (himtDialog == null) {
            himtDialog = new ConfirmDialogFragment.Builder()
                    .setDesc(msg)
                    .setCloseText(getResources().getString(R.string.user_change_phone_error_dialog_btn))
                    .setCloseTextColor(getResources().getColor(R.color.pasc_primary))
                    .setHideConfirmButton(true)
                    .setOnCloseListener(new OnCloseListener<ConfirmDialogFragment>() {
                        @Override
                        public void onClose(ConfirmDialogFragment confirmDialogFragment) {
                            himtDialog.dismiss();
                            himtDialog.onDestroy();
                            himtDialog = null;
                        }
                    }).build();
        }
        himtDialog.show(getSupportFragmentManager(), "himtDialog");

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
