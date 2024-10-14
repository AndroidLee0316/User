package com.pasc.business.cert.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.cert.CertifyUtils;
import com.pasc.business.cert.R;
import com.pasc.business.cert.iview.IScanFaceCertifyView;
import com.pasc.business.cert.presenter.ScanFaceCertifyPresenter;

import com.pasc.business.cert.view.CertSelectAgreementView;
import com.pasc.lib.base.ICallBack;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.base.permission.PermissionUtils;
import com.pasc.lib.hybrid.PascHybrid;
import com.pasc.lib.keyboard.KeyboardBaseView;
import com.pasc.lib.keyboard.KeyboardPopWindow;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.view.FormatEditText;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.widget.ClearEditText;
import com.pasc.lib.widget.dialog.OnConfirmListener;
import com.pasc.lib.widget.dialog.common.ConfirmDialogFragment;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.UserConstant;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.base.utils.CommonUtils;

import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.functions.Consumer;

/**
 * Created by luyang834 on 2018/4/18.
 * 扫脸认证
 */
@Route(path = RouterTable.Cert.PATH_CERT_ACCOUNT_VERIFY_ACT)
public class ScanFaceCertificationActivity extends BaseStatusBarActivity implements View.OnClickListener, IScanFaceCertifyView {

    protected ClearEditText etRealName;

    protected FormatEditText etIdNumber;

    private Button btnScanFace;

    //认证协议
    protected CertSelectAgreementView certSelectAgreementView;

    private ScanFaceCertifyPresenter scanFaceCertifyPresenter;

    private ConfirmDialogFragment isCertifiedDialog;

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
        scanFaceCertifyPresenter = new ScanFaceCertifyPresenter(this);
        setContentView(R.layout.cert_activity_scan_face_certification);

        etRealName = findViewById(R.id.user_activity_faceCertify_etRealname);

        etIdNumber = findViewById(R.id.user_et_id_number);
        etIdNumber.setFormatType(FormatEditText.TYPE_ID_CARD);
        btnScanFace = findViewById(R.id.user_btn_scan_face);
        certSelectAgreementView = findViewById(R.id.user_btn_scan_face_agreement);
        CommonTitleView titleBar = findViewById(R.id.user_ctv_title);

        btnScanFace.setOnClickListener(this);
        titleBar.setOnLeftClickListener(this);

        EventBus.getDefault().register(this);

        etRealName.setLimited(true);
        etRealName.setEditTextChangeListener(new ClearEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                setBtnNextClickable();
            }
        });
        etIdNumber.setEditTextChangeListener(new FormatEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                setBtnNextClickable();
            }
        });

        KeyboardPopWindow idKPW = KeyboardPopWindow.bindEdit(this, etIdNumber, KeyboardBaseView.KeyboardNumberTheme.TYPE_ID_CARD);
        etIdNumber.setOnFocusChangeOutListener(new FormatEditText.OnFocusChangeOutListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                idKPW.onEditFocesChange(v, hasFocus);
            }
        });

        certSelectAgreementView.setSelectCallback( getCertSelectAgreementViewType(), new CertSelectAgreementView.SelectCallback() {
            @Override
            public void onSelectClick(boolean select) {
                setBtnNextClickable();
            }
        });

        isFromIChooseCertificationActivity = getIntent().hasExtra(EXTRA_FROM_ICHOOSE);

    }

    /**
     * 设置下一步按钮是否可点击
     */
    private void setBtnNextClickable() {
        boolean b1 = etRealName.length() >= 2;
        boolean b2 = etIdNumber.getOriginalText().length() >= 18;
        boolean b3 = certSelectAgreementView.isSelect();
        if (b1 && b2 && b3) {
            btnScanFace.setAlpha(1.0f);
            btnScanFace.setEnabled(true);
        } else {
            btnScanFace.setAlpha(0.3f);
            btnScanFace.setEnabled(false);
        }
    }

    public int getCertSelectAgreementViewType(){
        return CertSelectAgreementView.CERT_TYPE_FACE_PA;
    }

    protected void checkPermission(final ICallBack callBack) {
        //高德地图需要的权限获取
        PermissionUtils.requestWithDialog(this, PermissionUtils.Groups.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) {
                        if (granted) {
                            callBack.callBack();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_title_left) {
            keyBack();
        } else if (i == R.id.user_btn_scan_face) {
            scanFaceClick();
        }
    }

    /**
     * 点击去人脸认证
     */
    protected void scanFaceClick() {
        checkPermission(new ICallBack() {
            @Override
            public void callBack() {
                if (check()) {
                    queryIsCerted();
                }
            }
        });
    }

    /**
     * @return 姓名和身份证格式校验, true: 校验通过, false: 校验不通过
     */
    protected boolean check() {
        String msg = "";
        if (!CommonUtils.isUsernameLegal(CertifyUtils.getTvContent(etRealName))) {
            msg = "姓名格式有误";
        } else if (!CommonUtils.checkIdcardValid(etIdNumber.getOriginalText())) {
            msg = "身份证格式有误，请重试";
        }
        boolean isEmpty = TextUtils.isEmpty(msg);
        if (!isEmpty) {
            CommonUtils.toastMsg(msg);
        }
        return isEmpty;
    }

    /**
     * 查询该身份信息是否被其他用户认证过
     */
    protected void queryIsCerted() {

        certSelectAgreementView.showCertDialog(getSupportFragmentManager(), getCertSelectAgreementViewType(), new OnConfirmListener() {

            @Override
            public void onConfirm(DialogFragment dialogFragment) {
                showLoading("");
                scanFaceCertifyPresenter.checkIsCerted(etRealName.getText().toString(), etIdNumber.getOriginalText(), "2");
            }
        });

    }

    /**
     * 接收到实名认证成功后关闭掉自身
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCertification(BaseEvent event) {
        if (EventTag.USER_CERTIFICATE_SUCCEED.equals(event.getTag()) || EventTag.USER_CERTIFICATE_FAILED.equals(event.getTag())) {
            PascHybrid.getInstance().triggerCallbackFunction(UserConstant.WEB_BEHAVIOR_NAME_NATIVE_ROUTE, null);
            //延时100秒是防止支付宝认证完成后，这个 activity 先被finish出现闪一下黑色背景的问题
            btnScanFace.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 100);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanFaceCertifyPresenter.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {

    }

    @Override
    public void queryIsCertedSucc(String queryIsCerted) {
        dismissLoading();
        if ("2".equals(queryIsCerted)) {//旧接口，即可以互踢的逻辑如果没有认证返回的 queryIsCerted 是 2
            showQueryIsCertedDialog();
        } else {//新接口，即不能互踢的逻辑如果没有认证返回的 queryIsCerted 是空，
            intentToFaceCertificationActivity();
        }
    }

    /**
     * 跳转到人脸认证页面
     */
    protected void intentToFaceCertificationActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("IDcard", etIdNumber.getOriginalText());
        bundle.putString("name", CertifyUtils.getTvContent(etRealName));
        BaseJumper.jumpBundleARouter(RouterTable.Face.PATH_FACE_COMPARE_ACT, bundle);
    }

    private void showQueryIsCertedDialog() {
        if (isCertifiedDialog == null) {
            isCertifiedDialog = new ConfirmDialogFragment.Builder()
                    .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
                        @Override
                        public void onConfirm(ConfirmDialogFragment confirmDialogFragment) {
                            confirmDialogFragment.dismiss();
                            intentToFaceCertificationActivity();
                        }
                    })
                    .setDesc(getResources().getString(R.string.user_idcard_used_warning))
                    .setDescColor(getResources().getColor(R.color.gray_333333))
                    .setCancelable(false)
                    .setConfirmText(getString(R.string.user_confirm))
                    .setCloseTextColor(getResources().getColor(R.color.user_dialog_cancel_textColor))
                    .setConfirmTextColor(getResources().getColor(R.color.user_dialog_confirm_textColor))
                    .build();
        }
        if (isCertifiedDialog.getDialog() == null || !isCertifiedDialog.getDialog().isShowing()) {
            isCertifiedDialog.show(getSupportFragmentManager(), "queryIsCerted");
        } else {
            isCertifiedDialog.dismiss();
        }
    }

    /**
     * 显示警告的弹框
     * @param msg
     */
    private void showWarningDialog(String msg) {
        if (isCertifiedDialog == null) {
            isCertifiedDialog = new ConfirmDialogFragment.Builder()
                    .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
                        @Override
                        public void onConfirm(ConfirmDialogFragment confirmDialogFragment) {
                            isCertifiedDialog.dismiss();
                            isCertifiedDialog = null;
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
        if (isCertifiedDialog.getDialog() == null || !isCertifiedDialog.getDialog().isShowing()) {
            isCertifiedDialog.show(getSupportFragmentManager(), "queryIsCerted");
        } else {
            isCertifiedDialog.dismiss();
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


    @Override
    public void onBackPressed() {
        keyBack();
    }

    protected void keyBack() {
        if (isFromIChooseCertificationActivity) {
            finish();
        } else {
            EventBusOutUtils.postCertificationCancle(Constant.CERT_TYPE_FACE);
            finish();
        }
    }

}
