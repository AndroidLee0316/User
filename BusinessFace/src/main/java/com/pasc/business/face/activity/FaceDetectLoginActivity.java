package com.pasc.business.face.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pasc.business.face.R;
import com.pasc.business.face.view.CameraSurfaceView;
import com.pasc.lib.base.util.BitmapUtils;
import com.pasc.lib.net.resp.BaseV2Resp;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.router.interceptor.LoginInterceptor;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.user.UserProxy;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.net.resp.BaseRespObserver;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.userbase.user.util.UserManagerImpl;
import com.pasc.lib.widget.FaceCircleProcessView;
import com.pasc.lib.widget.dialog.OnCloseListener;
import com.pasc.lib.widget.dialog.OnConfirmListener;
import com.pasc.lib.widget.dialog.common.ConfirmDialogFragment;
import com.trello.rxlifecycle2.android.ActivityEvent;
import java.lang.reflect.Type;

/**
 * Created by linhaiyang807 on 17/11/9.
 */
@Route(path = RouterTable.Face.PATH_FACE_LOGIN_ACT)
public class FaceDetectLoginActivity extends BaseFaceDetectActivity
        implements CameraSurfaceView.PreviewCallback {

    TextView mTimeTv;
    FrameLayout frameLayout;
    FaceCircleProcessView cpvFace;
    TextView faceHintTv;
    ImageView mCoverView;
    ProgressBar mPbProgress;
    CommonTitleView titleBar;
    Handler mHandler = new Handler();

    private String phoneNumber = "";

    ConfirmDialogFragment errorNormalDialog;

    ConfirmDialogFragment errorMoreTimesDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_actiivty_face_detect);
        mTimeTv = findViewById(R.id.user_tv_time);
        frameLayout = findViewById(R.id.user_frameLayout);
        cpvFace = findViewById(R.id.cpv_face);
        mCoverView = findViewById(R.id.user_imageview);
        mPbProgress = findViewById(R.id.pb_progress);
        faceHintTv = findViewById(R.id.user_tv_face_hint);
        titleBar = findViewById(R.id.user_title_bar);

        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginInterceptor.notifyCallBack(false);
                finish();
            }
        });

        cpvFace.post(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) cpvFace.getLayoutParams();
                params.width = mCoverView.getWidth();
                params.height = mCoverView.getWidth();
                cpvFace.setLayoutParams(params);
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phoneNumber = bundle.getString(Constant.MOBILE_NUMBER);
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

    @Override
    protected TextView getTimeTextView() {
        return mTimeTv;
    }


    @Override
    protected void toFailedActivity(String errorMsg) {
        showFaceLoginErrorDialog(getString(R.string.user_login_fail_face_title_timeout), getString(R.string.user_login_fail_face_timeout));
    }

    @Override
    protected TextView getTipTextView() {
        return faceHintTv;
    }

    @Override
    protected FrameLayout getCameraViewContainer() {
        return frameLayout;
    }

    @Override
    protected boolean isShowDialog() {
        if (errorNormalDialog != null && errorNormalDialog.getDialog() != null && errorNormalDialog.getDialog().isShowing()){
            return true;
        }
        if (errorMoreTimesDialog != null && errorMoreTimesDialog.getDialog() != null && errorMoreTimesDialog.getDialog().isShowing()){
            return true;
        }
        return false;
    }

    @Override
    protected void toNextActivity(Bitmap bitmap, String imageBase64, String imageDigest) {

        showLoading();
        String mobileType = android.os.Build.MODEL;  //获取手机型号
        String system = android.os.Build.VERSION.RELEASE;//获取版本号

        byte[] bytes = BitmapUtils.Bitmap2Bytes(bitmap);

        UserBiz.loginFace(bytes, phoneNumber, "2", "jpg", mobileType, system, imageBase64, imageDigest)
                .compose(this.<User>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseRespObserver<User>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (isFinishing()) {
                            return;
                        }
                        dismissLoading();
                        try {
                            Type type = new TypeToken<BaseV2Resp<User>>() {
                            }.getType();
                            BaseV2Resp<User> resp = new Gson().fromJson(msg, type);
                            User user = resp.data;
                            if (user.faceComparasionLoginCount > 0) {
                                showFaceLoginErrorDialog(getString(R.string.user_login_fail_face_title), resp.msg);
                            } else {
                                showFaceLoginErrorManyTimesDialog(getString(R.string.user_login_fail_face_title), getString(R.string.user_login_fail_face_many_times));
                            }
                        } catch (Exception e) {
                            Log.d("FaceDetectLoginActivity", "json msg: " + msg);
                            CommonUtils.toastMsg(msg);
                            resumeFaceDetect();
                        }
                    }

                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        dismissLoading();
                        mPbProgress.setVisibility(View.VISIBLE);
                        cpvFace.setCenterColor("#80000000");
                        cpvFace.setProgress(60);
                        UserProxy.getInstance().getDataBaseContext().switchUserDb(user.getMobileNo());
                        UserManagerImpl.getInstance().updateUser(user);
                        Log.i("FaceDetectLoginActivity", "user=" + user);

                        UserManagerImpl.getInstance().updateUser(user);
                        UserManagerImpl.getInstance().updateLoginSuccess();

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cpvFace.setProgress(100);
                                CommonUtils.toastMsg("人脸登录成功");

                                EventBusOutUtils.postLoginSuccess();
                                LoginInterceptor.notifyCallBack(true);
                                finish();
                            }
                        }, 100);
                    }
                });
    }

    private void showFaceLoginErrorDialog(final String title, final String msg) {
        if (isFinishing()) {//解决部分机型崩溃问题
            return;
        }
        if (errorMoreTimesDialog != null && errorMoreTimesDialog.getDialog() != null && errorMoreTimesDialog.getDialog().isShowing()){
            return;
        }

        reset();
        if (errorNormalDialog == null){
            errorNormalDialog = new ConfirmDialogFragment.Builder()
                    .setTitle(title)
                    .setDesc(msg)
                    .setConfirmText(getString(R.string.user_retry))
                    .setConfirmTextColor(getResources().getColor(R.color.pasc_primary))
                    .setHideCloseButton(true)
                    .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
                        @Override
                        public void onConfirm(ConfirmDialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            errorNormalDialog = null;
                            resumeFaceDetect();
                        }
                    }).build();
        }
        errorNormalDialog.show(getSupportFragmentManager(), "errorNormalDialog");

    }

    private void showFaceLoginErrorManyTimesDialog(final String title, final String msg) {
        if (isFinishing()) {//解决部分机型崩溃问题
            return;
        }

        if (errorNormalDialog != null && errorNormalDialog.getDialog() != null && errorNormalDialog.getDialog().isShowing()){
            return;
        }

        reset();
        if (errorMoreTimesDialog == null){

            errorMoreTimesDialog = new ConfirmDialogFragment.Builder()
                    .setTitle(title)
                    .setDesc(msg)
                    .setConfirmText(getString(R.string.user_sms_login))
                    .setConfirmTextColor(getResources().getColor(R.color.pasc_primary))
                    .setCloseText(getString(R.string.user_retry))
                    .setCloseTextColor(getResources().getColor(R.color.black_666666))
                    .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
                        @Override
                        public void onConfirm(ConfirmDialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            errorMoreTimesDialog = null;
                            gotoSMSLogin();
                        }
                    }).setOnCloseListener(new OnCloseListener<ConfirmDialogFragment>() {
                        @Override
                        public void onClose(ConfirmDialogFragment confirmDialogFragment) {
                            confirmDialogFragment.dismiss();
                            resumeFaceDetect();
                        }
                    }).build();

        }
        errorMoreTimesDialog.show(getSupportFragmentManager(), "errorMoreTimesDialog");

    }

    @Override
    protected boolean checkAlive() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LoginInterceptor.notifyCallBack(false);
    }

    /**
     * 跳转到短信登陆
     */
    protected void gotoSMSLogin(){
        Bundle bundle = new Bundle();
        bundle.putString(Constant.LOGIN_TYPE, Constant.LOGIN_TYPE_SWITCH_ACCOUNT);
        BaseJumper.jumpBundleARouter(RouterTable.Login.PATH_LOGIN_ACTIVITY, bundle);
        finish();
    }
}
