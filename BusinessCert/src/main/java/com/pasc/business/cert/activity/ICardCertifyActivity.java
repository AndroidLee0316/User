package com.pasc.business.cert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.pasc.business.cert.CertifyUtils;
import com.pasc.business.cert.R;
import com.pasc.business.cert.iview.ICardCertifyView;
import com.pasc.business.cert.presenter.CardCertifyPresenter;
import com.pasc.business.cert.view.CertSelectAgreementView;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.keyboard.KeyboardBaseView;
import com.pasc.lib.keyboard.KeyboardPopWindow;
import com.pasc.lib.statistics.StatisticsManager;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.view.FormatEditText;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.widget.ClearEditText;
import com.pasc.lib.widget.dialog.OnConfirmListener;
import com.pasc.lib.widget.dialog.common.ConfirmDialogFragment;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.user.certification.net.resp.ByBankErrorResp;
import com.pasc.lib.userbase.user.certification.net.resp.RealNameByBankResp;
import com.pasc.lib.userbase.user.util.UserManagerImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.pasc.lib.statistics.PageType.APP;

/**
 * 银行卡实名认证页面
 */

@Route(path = RouterTable.Cert.PATH_CERT_AUTH_TYPE_ACT_BANK)
public class ICardCertifyActivity extends BaseStatusBarActivity implements View.OnClickListener, ICardCertifyView {

    private LinearLayout llName;
    private LinearLayout llId;
    private View userView1;
    private View userView2;
    private TextView tvCertificationNext;
    private ClearEditText etRealName;
    private FormatEditText etIdNum;
    private FormatEditText etBankNum;
    private FormatEditText etMobileNum;
    private CommonTitleView ctvTitle;

    //认证协议
    protected CertSelectAgreementView certSelectAgreementView;

    private User user;
    private CardCertifyPresenter cardCertifyPresenter;
    private ConfirmDialogFragment queryIsCertedDialog;


    /**
     * 是否是从 IChooseCertificationActivity 跳转过来，如果是的话，按返回不能发送取消认证的通知
     */
    private boolean isFromIChooseCertificationActivity = false;

    public static final String EXTRA_FROM_ICHOOSE = "EXTRA_FROM_ICHOOSE";

    /**
     * 用户输入的身份证已经被绑定了，不能再绑定了（不能互踢的逻辑）
     */
    private static final String IDCARD_USERNAME_BIND_CHECK_ERROR = "IDCARD_USERNAME_BIND_CHECK_ERROR";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cert_activity_certification);
        EventBus.getDefault().register(this);
        cardCertifyPresenter = new CardCertifyPresenter(this);
        initView();
        initData();
        initEvent();
    }

    /***初始化控件*/
    private void initView() {
        llName = findViewById(R.id.ll_name);
        llId = findViewById(R.id.ll_id);
        userView1 = findViewById(R.id.user_view1);
        userView2 = findViewById(R.id.user_view2);
        tvCertificationNext = findViewById(R.id.user_tv_certification_next);
        ctvTitle = findViewById(R.id.user_ctv_title);
        etRealName = findViewById(R.id.user_activity_bankCerti_etRealName);
        etIdNum = findViewById(R.id.user_activity_bankCerti_etIdNum);
        etIdNum.setFormatType(FormatEditText.TYPE_ID_CARD);
        etBankNum = findViewById(R.id.user_activity_bankCerti_etBankNum);
        etBankNum.setFormatType(FormatEditText.TYPE_BANK_NUMBER);
        etMobileNum = findViewById(R.id.user_activity_bankCerti_etMobileNum);
        etMobileNum.setFormatType(FormatEditText.TYPE_PHONE);
        certSelectAgreementView = findViewById(R.id.user_activity_bankCerti_csav);
    }

    /***初始化数据*/
    private void initData() {

        isFromIChooseCertificationActivity = getIntent().hasExtra(EXTRA_FROM_ICHOOSE);

        user = UserManagerImpl.getInstance().getInnerUser();
        if (!TextUtils.isEmpty(user.userName) && !TextUtils.isEmpty(user.idCard)) {
            etIdNum.setFormatType(FormatEditText.TYPE_NONE);
            etRealName.setText(CertifyUtils.encrypted(user.userName));
            etRealName.setEnabled(false);
            etRealName.setFocusable(false);
            etIdNum.setText(CommonUtils.hideIDNum(user.idCard));
            etIdNum.setEnabled(false);
            etIdNum.setFocusable(false);
            llName.setBackgroundColor(getResources().getColor(R.color.default_bg));
            llId.setBackgroundColor(getResources().getColor(R.color.default_bg));
            userView1.setVisibility(View.GONE);
            userView2.setVisibility(View.GONE);
        }
    }

    /***初始化点击事件*/
    private void initEvent() {
        ctvTitle.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBack();
            }
        });
        tvCertificationNext.setOnClickListener(this);
        etRealName.setLimited(true);
        etRealName.setEditTextChangeListener(new ClearEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                setBtnNextClickable();
            }
        });

        etIdNum.setEditTextChangeListener(new FormatEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                setBtnNextClickable();
            }
        });
        etBankNum.setEditTextChangeListener(new FormatEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                setBtnNextClickable();
            }
        });
        etMobileNum.setEditTextChangeListener(new FormatEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                setBtnNextClickable();
            }
        });

        KeyboardPopWindow idKPW = KeyboardPopWindow.bindEdit(this, etIdNum, KeyboardBaseView.KeyboardNumberTheme.TYPE_ID_CARD);
        etIdNum.setOnFocusChangeOutListener(new FormatEditText.OnFocusChangeOutListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                idKPW.onEditFocesChange(v, hasFocus);
            }
        });
        KeyboardPopWindow bankKPW = KeyboardPopWindow.bindEdit(this, etBankNum, KeyboardBaseView.KeyboardNumberTheme.TYPE_NUMBER);
        etBankNum.setOnFocusChangeOutListener(new FormatEditText.OnFocusChangeOutListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                bankKPW.onEditFocesChange(v, hasFocus);
            }
        });

        KeyboardPopWindow mobileKPW = KeyboardPopWindow.bindEdit(this, etMobileNum, KeyboardBaseView.KeyboardNumberTheme.TYPE_NUMBER);
        etMobileNum.setOnFocusChangeOutListener(new FormatEditText.OnFocusChangeOutListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mobileKPW.onEditFocesChange(v, hasFocus);
            }
        });

        certSelectAgreementView.setSelectCallback( CertSelectAgreementView.CERT_TYPE_BANK, new CertSelectAgreementView.SelectCallback() {
            @Override
            public void onSelectClick(boolean select) {
                setBtnNextClickable();
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

    private void setBtnNextClickable() {
        boolean b1 = etRealName.getText().toString().length() >= 2;
        boolean b2 = etIdNum.getOriginalText().length() >= 18;
        boolean b3 = etBankNum.getOriginalText().length() >= 16;
        boolean b4 = etMobileNum.getOriginalText().length() >= 11;
        boolean b5 = certSelectAgreementView.isSelect();
        if (b1 && b2 && b3 && b4 && b5) {
            tvCertificationNext.setEnabled(true);
            tvCertificationNext.setAlpha(1f);
        } else {
            tvCertificationNext.setEnabled(false);
            tvCertificationNext.setAlpha(0.3f);
        }
    }

    /**
     * 接收到实名认证成功后关闭掉自身
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCertification(BaseEvent event) {
        if (EventTag.USER_CERTIFICATE_SUCCEED.equals(event.getTag()) || EventTag.USER_CERTIFICATE_FAILED.equals(event.getTag())) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        cardCertifyPresenter.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.user_tv_certification_next) {
            certSelectAgreementView.showCertDialog(getSupportFragmentManager(), CertSelectAgreementView.CERT_TYPE_BANK, new OnConfirmListener() {
                @Override
                public void onConfirm(DialogFragment dialogFragment) {
                    checkIsCerted();
                }
            });
        }
    }

    @Override
    public void queryIsCertedSucc(String data) {
        //1.该身份信息未被认证过 2.该身份信息已经被认证过
        dismissLoading();
        if ("2".equals(data)) {
            showQueryIsCertedDialog();
        } else {
            realNameByBank();
        }
    }

    @Override
    public void queryIsCertedFail(String code, String errorMsg) {
        dismissLoading();
        if (code != null && IDCARD_USERNAME_BIND_CHECK_ERROR.equals(code)){
            showWarningDialog(errorMsg);
        }else {
            CommonUtils.toastMsg(errorMsg);
        }
    }


    private void showQueryIsCertedDialog() {
        if (queryIsCertedDialog == null) {
            queryIsCertedDialog = new ConfirmDialogFragment.Builder()
                    .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
                        @Override
                        public void onConfirm(ConfirmDialogFragment confirmDialogFragment) {
                            confirmDialogFragment.dismiss();
                            realNameByBank();
                        }
                    })
                    .setDesc(getResources().getString(R.string.user_idcard_used_warning))
                    .setDescColor(getResources().getColor(R.color.gray_333333))
                    .setConfirmText(getString(R.string.user_confirm))
                    .setCancelable(false)
                    .setCloseTextColor(getResources().getColor(R.color.user_dialog_cancel_textColor))
                    .setConfirmTextColor(getResources().getColor(R.color.user_dialog_confirm_textColor))
                    .build();
        }
        if (queryIsCertedDialog.getDialog() == null || !queryIsCertedDialog.getDialog().isShowing()) {
            queryIsCertedDialog.show(getSupportFragmentManager(), "queryIsCerted");
        } else {
            queryIsCertedDialog.dismiss();
        }
    }


    /**
     * 显示警告的弹框
     * @param msg
     */
    private void showWarningDialog(String msg) {
        if (queryIsCertedDialog == null) {
            queryIsCertedDialog = new ConfirmDialogFragment.Builder()
                    .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
                        @Override
                        public void onConfirm(ConfirmDialogFragment confirmDialogFragment) {
                            queryIsCertedDialog.dismiss();
                            queryIsCertedDialog = null;
                        }
                    })
                    .setDesc(msg)
                    .setDescColor(getResources().getColor(R.color.gray_333333))
                    .setCancelable(false)
                    .setConfirmText(getString(R.string.user_iknow))
                    .setHideCloseButton(true)
                    .setConfirmTextColor(getResources().getColor(R.color.user_dialog_confirm_textColor))
                    .build();
        }
        if (queryIsCertedDialog.getDialog() == null || !queryIsCertedDialog.getDialog().isShowing()) {
            queryIsCertedDialog.show(getSupportFragmentManager(), "queryIsCerted");
        } else {
            queryIsCertedDialog.dismiss();
        }
    }



    @Override
    public void realNameByBankSucc(RealNameByBankResp realNameByBankResp) {
        dismissLoading();
        StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_CERT_SUCCESS, getString(R.string.user_bank_cert), APP, null);
        intentToCertificationCompletely(realNameByBankResp);
    }

    /**
     * 跳转到输入验证码页面
     */
    protected void intentToCertificationCompletely(RealNameByBankResp realNameByBankResp) {
        Intent intent = new Intent(this, CertificationCompletelyActivity.class);
        intent.putExtra(CertificationCompletelyActivity.REAL_NAME_BY_BANK_RESP, realNameByBankResp);
        startActivity(intent);
    }

    @Override
    public void realNameByBankFail(String code, String errorMsg) {
        StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_CERT_FAIL, getString(R.string.user_bank_cert), APP, null);
        dismissLoading();
        switch (code) {
            case "-1":
                CommonUtils.toastMsg(errorMsg);
                break;
            default:
                try {
                    ByBankErrorResp byBankErrorResp = new Gson().fromJson(errorMsg, ByBankErrorResp.class);
                    Intent intent = new Intent(this, CertifyFailActivity.class);
                    intent.putExtra(Constant.CERT_TYPE, Constant.CERT_TYPE_BANK);
                    intent.putExtra(Constant.CERT_FAIL_REMAIN_COUNT, Integer.valueOf(byBankErrorResp.data.allowedAuthCount));
                    intent.putExtra(Constant.CERT_FAIL_MSG, byBankErrorResp.msg);
                    startActivity(intent);
                } catch (Exception e) {
                    CommonUtils.toastMsg(errorMsg);
                }
                break;
        }
    }

//    @Override
//    public void checkIsCertedSucc(String data) {
//        dismissLoading();
//        realNameByBank();
//    }
//
//    @Override
//    public void checkIsCertedFail(String code, String errorMsg) {
//        dismissLoading();
//        if (queryIsCertedDialog == null) {
//            queryIsCertedDialog = new ConfirmDialogFragment.Builder()
//                    .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
//                        @Override
//                        public void onConfirm(ConfirmDialogFragment confirmDialogFragment) {
//                            queryIsCertedDialog.dismiss();
//                            queryIsCertedDialog.onDestroy();
//                            queryIsCertedDialog = null;
////                            realNameByBank();
//                        }
//                    })
//                    .setDesc(errorMsg)
//                    .setDescColor(getResources().getColor(R.color.gray_333333))
//                    .setConfirmText(getString(R.string.user_confirm))
//                    .setCancelable(false)
//                    .setHideCloseButton(true)
//                    .setCloseTextColor(getResources().getColor(R.color.user_dialog_cancel_textColor))
//                    .setConfirmTextColor(getResources().getColor(R.color.user_dialog_confirm_textColor))
//                    .build();
//        }
//        if (queryIsCertedDialog.getDialog() == null || !queryIsCertedDialog.getDialog().isShowing()) {
//            queryIsCertedDialog.show(getSupportFragmentManager(), "queryIsCerted");
//        } else {
//            queryIsCertedDialog.dismiss();
//        }
//    }

    /**
     * 查询该身份信息是否被其他用户认证过
     * 20191230 过期，使用函数{@checkIsCerted}替换
     */
    @Deprecated
    private void queryIsCerted() {
        showLoading("");
        String idNum = etIdNum.getOriginalText();
        if (idNum.contains("*")) {
            idNum = user.idCard;
        }
        cardCertifyPresenter.queryIsCerted(idNum, "1");
    }

    /**
     * 查询该身份信息是否被其他用户认证过
     */
    private void checkIsCerted() {
        String idNum = etIdNum.getOriginalText();
        if (idNum.contains("*")) {
            idNum = user.idCard;
        }

        if (!CommonUtils.checkIdcardValid(idNum)) {
            CommonUtils.toastMsg("身份证格式有误，请重试");
            return;
        }
        showLoading("");
        cardCertifyPresenter.checkIsCerted(etRealName.getText().toString().trim(), idNum, "1");
    }

    /**
     * 银行卡实名认证一（认证接口）
     */
    private void realNameByBank() {
        String name = CertifyUtils.getTvContent(etRealName);
        String idCardNo = etIdNum.getOriginalText();
        String bankNum = etBankNum.getOriginalText();
        String bankPhoneNum = etMobileNum.getOriginalText();
        if (idCardNo.contains("*") || name.contains("*")) {
            idCardNo = user.getIdCard();
            name = user.getUserName();
        }
        //埋点
        if (!CommonUtils.isUsernameLegal(name)) {
            CommonUtils.toastMsg("姓名格式有误");
            return;
        }
        /**
         * modify by lcb : 删除!"2".equals(user.getCertiType())，这个有什么意义？
         */
        if (!CommonUtils.checkIdcardValid(idCardNo)) {
            //    if (!CommonUtils.checkIdcardValid(idCardNo) && !"2".equals(user.getCertiType())) {
            CommonUtils.toastMsg("身份证格式有误，请重试");
            return;
        }
        if (!CommonUtils.isBankCardLegal(bankNum)) {
            CommonUtils.toastMsg("银行卡录入有误，请重试");
            return;
        }
        if (bankPhoneNum.length() != 11) {
            CommonUtils.toastMsg("手机格式有误");
            return;
        }
        showLoading("");
        cardCertifyPresenter.realNameByBank(idCardNo, name, bankNum, bankPhoneNum);
    }


    @Override
    public void onBackPressed() {
        keyBack();
    }

    protected void keyBack() {
        if (isFromIChooseCertificationActivity) {
            finish();
        } else {
            EventBusOutUtils.postCertificationCancle(Constant.CERT_TYPE_BANK);
            finish();
        }
    }
}
