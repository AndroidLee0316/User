package com.pasc.business.face.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.pasc.business.face.R;
import com.pasc.business.face.iview.IFaceCompareView;
import com.pasc.business.face.presenter.FaceComparePresenter;
import com.pasc.business.face.view.CameraSurfaceView;
import com.pasc.lib.base.util.BitmapUtils;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.statistics.StatisticsManager;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.user.certification.net.resp.FaceAndIdComparisonErrorResp;
import com.pasc.lib.userbase.user.certification.net.resp.FaceCompareDataResp;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.userbase.user.util.UserManagerImpl;
import com.pasc.lib.widget.FaceCircleProcessView;
import com.pasc.lib.widget.dialog.OnCloseListener;
import com.pasc.lib.widget.dialog.OnConfirmListener;
import com.pasc.lib.widget.dialog.common.ConfirmDialogFragment;

import static com.pasc.lib.statistics.PageType.APP;

/**
 * Created by luyang834 on 2018/4/19.
 * 人脸认证对比
 */
@Route(path = RouterTable.Face.PATH_FACE_COMPARE_ACT)
public class FaceCompareActivity extends BaseFaceDetectActivity
        implements CameraSurfaceView.PreviewCallback, View.OnClickListener, IFaceCompareView {
    private TextView tvFaceHint;
    private TextView timeTv;
    private FrameLayout frameLayout;
    FaceCircleProcessView cpvFace;
    ImageView mCoverView;
    ProgressBar mPbProgress;

    private String name;
    private String iDcard;

    private FaceComparePresenter faceComparePresenter;
    Handler mHandler = new Handler();

    //退出页面提示框
    private ConfirmDialogFragment exitDialog;
    //错误提示/超时提示
    private ConfirmDialogFragment errorSingleDialog;
    //错误提示/超时提示
    private ConfirmDialogFragment errorMultiDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        faceComparePresenter = new FaceComparePresenter(this);
        setContentView(R.layout.face_actiivty_face_identify);
        tvFaceHint = findViewById(R.id.user_tv_face_hint);
        timeTv = findViewById(R.id.user_tv_time);
        frameLayout = findViewById(R.id.user_frameLayout);
        cpvFace = findViewById(R.id.cpv_face);
        mCoverView = findViewById(R.id.user_imageview);
        mPbProgress = findViewById(R.id.pb_progress);
        CommonTitleView title = findViewById(R.id.user_title_bar);
        title.setOnLeftClickListener(this);
        iDcard = getIntent().getStringExtra("IDcard");
        name = getIntent().getStringExtra("name");

        cpvFace.post(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) cpvFace.getLayoutParams();
                params.width = mCoverView.getWidth();
                params.height = mCoverView.getWidth();
                cpvFace.setLayoutParams(params);
            }
        });
    }


    @Override
    protected TextView getTimeTextView() {
        return timeTv;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_title_left) {
            onBackPressed();
        }
    }

    private void showChooseDialog() {
        //停止视频
        reset();
        if (exitDialog == null){
            exitDialog = new ConfirmDialogFragment.Builder()
                    .setDesc(getString(R.string.face_cert_exit_tips))
                    .setCloseTextColor(getResources().getColor(R.color.pasc_primary))
                    .setConfirmTextColor(getResources().getColor(R.color.black_666666))
                    .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
                        @Override
                        public void onConfirm(ConfirmDialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            finish();
                        }
                    }).setOnCloseListener(new OnCloseListener<ConfirmDialogFragment>() {
                        @Override
                        public void onClose(ConfirmDialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            resumeFaceDetect();
                        }
                    })
                    .build();
        }
        exitDialog.show(getSupportFragmentManager(), "exitDialog");

    }

    @Override
    public void toFailedActivity(String errorMsg) {
        showErrorSingleDialog(getString(R.string.face_cert_timeout_title), getString(R.string.face_cert_timeout_content), getString(R.string.user_retry), new OnConfirmListener() {
            @Override
            public void onConfirm(DialogFragment dialogFragment) {
                dialogFragment.dismiss();
                resumeFaceDetect();
            }
        });
    }

    /**
     * 人脸识别错误对话框
     *
     * @param title 标题
     * @param msg   内容
     */
    protected void showErrorSingleDialog(final String title, final String msg, String confirmText, OnConfirmListener listener) {
        if (isFinishing() || isShowDialog()) {//解决部分机型崩溃问题
            return;
        }

        reset();
        if (errorSingleDialog == null){
            errorSingleDialog = new ConfirmDialogFragment.Builder()
                    .setTitle(title)
                    .setDesc(msg)
                    .setConfirmText(confirmText)
                    .setConfirmTextColor(getResources().getColor(R.color.pasc_primary))
                    .setHideCloseButton(true)
                    .setOnConfirmListener(listener)
                    .build();
        }
        errorSingleDialog.show(getSupportFragmentManager(), "errorSingleDialog");

    }

    /**
     * 人脸识别错误对话框
     *
     * @param title 标题
     * @param msg   内容
     */
    protected void showErrorMultiDialog(final String title, final String msg, String closeText, OnCloseListener closeListener, String confirmText, OnConfirmListener confirmListener) {
        if (isFinishing() || isShowDialog()) {//解决部分机型崩溃问题
            return;
        }
        reset();
        if (errorMultiDialog == null){
            errorMultiDialog = new ConfirmDialogFragment.Builder()
                    .setTitle(title)
                    .setDesc(msg)
                    .setCloseText(closeText)
                    .setCloseTextColor(getResources().getColor(R.color.black_666666))
                    .setConfirmText(confirmText)
                    .setConfirmTextColor(getResources().getColor(R.color.pasc_primary))
                    .setOnCloseListener(closeListener)
                    .setOnConfirmListener(confirmListener)
                    .build();
        }
        errorMultiDialog.show(getSupportFragmentManager(), "errorMultiDialog");

    }


    @Override
    protected TextView getTipTextView() {
        return tvFaceHint;
    }

    @Override
    protected FrameLayout getCameraViewContainer() {
        return frameLayout;
    }

    @Override
    protected boolean isShowDialog() {
        if (exitDialog != null && exitDialog.getDialog() != null && exitDialog.getDialog().isShowing()){
            return true;
        }
        if (errorSingleDialog != null && errorSingleDialog.getDialog() != null && errorSingleDialog.getDialog().isShowing()){
            return true;
        }
        if (errorMultiDialog != null && errorMultiDialog.getDialog() != null && errorMultiDialog.getDialog().isShowing()){
            return true;
        }
        return false;
    }

    //人脸检测回调消息
    @Override
    public void toNextActivity(Bitmap bitmap, String imageBase64, String imageDigest) {
        faceAndIdComparision(bitmap, imageBase64, imageDigest);
    }

    private void faceAndIdComparision(Bitmap bitmap, String imageBase64, String imageDigest) {
        byte[] bytes = BitmapUtils.Bitmap2Bytes(bitmap);
        String version = null;
        PackageManager mPKM = this.getPackageManager();
        try {
            PackageInfo mInfo = mPKM.getPackageInfo(this.getPackageName(), 0);
            version = mInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String phoneModel = android.os.Build.BRAND;
        showLoading("", false);
        faceComparePresenter.faceAndIdComparison(bytes, iDcard, name, "1", version, phoneModel, "jpeg", imageBase64, imageDigest);
    }

    @Override
    public void faceAndIdComparisonSucc(byte[] bytes) {
        dismissLoading();
        if (timer != null) {
            timer.cancel();
        }
        User user = UserManagerImpl.getInstance().getInnerUser();
        user.userName = name;
        user.idCard = iDcard;

        //删除，使用 addCertType 替换
//        if (User.CERTIFY_BANK.equals(user.getCertiType()) || User.CERTIFY_BOTH.equals(user.getCertiType())) {
//            user.setCertiType("3");
//        } else {
//            user.setCertiType("2");
//        }
        user.addCertType(User.CERTIFY_FACE);
        if (CommonUtils.checkIdcardValid(iDcard)) {
            user.sex = CommonUtils.checkSex(iDcard);
        }
        UserManagerImpl.getInstance().updateUser(user);

        showRealNameSuccessOpenFaceDialog(bytes);
    }

    /**
     * 实名认证成功之后的打开或者覆盖人脸弹窗
     */
    protected void showRealNameSuccessOpenFaceDialog(final byte[] bytes) {
        if (isFinishing()) {//解决部分机型崩溃问题
            EventBusOutUtils.postCertificationSuccess(Constant.CERT_TYPE_FACE);
            return;
        }
        StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_CERT_SUCCESS, getString(R.string.face_cert_title),APP,null);
        String content = null;
        String ok = null;
        if (UserManagerImpl.getInstance().isOpenFaceVerify()) {
            content = getString(R.string.face_login_replace_tips);
            ok = getString(R.string.face_login_replace);
        } else {
            content = getString(R.string.face_login_open_tips);
            ok = getString(R.string.face_login_open);
        }

        ConfirmDialogFragment replaceDialog = new ConfirmDialogFragment.Builder()
                    .setDesc(content)
                    .setConfirmText(ok)
                    .setConfirmTextColor(getResources().getColor(R.color.pasc_primary))
                    .setCloseText(getString(R.string.cancel))
                    .setCloseTextColor(getResources().getColor(R.color.black_666666))
                    .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
                        @Override
                        public void onConfirm(ConfirmDialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            openFaceCertificationLogin(bytes);
                        }
                    }).setOnCloseListener(new OnCloseListener<ConfirmDialogFragment>() {
                    @Override
                    public void onClose(ConfirmDialogFragment confirmDialogFragment) {
                        confirmDialogFragment.dismiss();
                        gotoSuccess();
                    }
                }).build();

        replaceDialog.show(getSupportFragmentManager(), "replaceDialog");

        StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_CERT_SUCCESS, getString(R.string.face_cert_title),APP,null);

    }

    private void openFaceCertificationLogin(byte[] bytes) {
        showLoading("");
        faceComparePresenter.openFaceCertificationLogin(bytes);
    }

    @Override
    public void faceAndIdComparisonFail(String code, String errorMsg) {
        dismissLoading();
        StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_CERT_FAIL, getString(R.string.face_cert_title),APP,null);
        switch (code) {
            case "-1":
                CommonUtils.toastMsg(errorMsg);
                finish();
                break;
            default:
                try {
                    FaceAndIdComparisonErrorResp resp = new Gson().fromJson(errorMsg, FaceAndIdComparisonErrorResp.class);
                    FaceCompareDataResp data = new Gson().fromJson(resp.data, FaceCompareDataResp.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.CERT_TYPE, Constant.CERT_TYPE_FACE);
                    bundle.putString(Constant.CERT_FAIL_MSG, resp.msg);
                    bundle.putInt(Constant.CERT_FAIL_REMAIN_COUNT, Integer.valueOf(data.allowedAuthCount));
                    BaseJumper.jumpBundleARouter(RouterTable.Cert.PATH_CERT_FAIL_ACT, bundle);
                    finish();
                } catch (Exception e) {
                    CommonUtils.toastMsg(errorMsg);
                    finish();
                }
                break;
        }
    }

    @Override
    public void openFaceCertificationLoginSucc(int type) {
        dismissLoading();
        dismissDialogs();
        User user = UserManagerImpl.getInstance().getInnerUser();
        user.setHasOpenFace(Constant.USER_FACE_OPEND);
        UserManagerImpl.getInstance().updateUser(user);
        gotoSuccess();
    }

    @Override
    public void openFaceCertificationLoginFail(int type, String code, String errorMsg) {
        dismissLoading();
        dismissDialogs();
        ToastUtils.toastMsg(errorMsg);
        gotoSuccess();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        faceComparePresenter.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {
        setTipColor("#FF4C4C");
    }

    private void gotoSuccess(){
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.CERT_TYPE, Constant.CERT_TYPE_FACE);

        mPbProgress.setVisibility(View.VISIBLE);
        cpvFace.setCenterColor("#80000000");
        cpvFace.setProgress(60);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cpvFace.setProgress(100);
                BaseJumper.jumpBundleARouter(RouterTable.Face.PATH_FACE_COMPARE_SUCC_ACT, bundle);
                finish();
            }
        }, 100);
    }

    @Override
    public void onBackPressed() {

        showChooseDialog();
    }
}
