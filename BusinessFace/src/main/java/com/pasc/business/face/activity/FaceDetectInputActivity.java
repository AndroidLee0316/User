package com.pasc.business.face.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.face.R;
import com.pasc.business.face.view.CameraSurfaceView;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.base.util.BitmapUtils;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.widget.FaceCircleProcessView;
import com.pasc.lib.widget.dialog.OnConfirmListener;
import com.pasc.lib.widget.dialog.common.ConfirmDialogFragment;
import com.pasc.lib.widget.tdialog.TDialog;
import com.pasc.lib.widget.tdialog.base.BindViewHolder;
import com.pasc.lib.widget.tdialog.listener.OnBindViewListener;
import com.pasc.lib.widget.tdialog.listener.OnViewClickListener;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.net.resp.BaseRespObserver;
import com.pasc.lib.userbase.user.util.UserManagerImpl;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ex-luyang001 on 2018/1/12.
 */

@Route(path = RouterTable.Face.PATH_FACE_INPUT_ACT)
public class FaceDetectInputActivity extends BaseFaceDetectActivity
        implements CameraSurfaceView.PreviewCallback, View.OnClickListener {

    TextView tvFaceHint;
    TextView timeTv;
    FrameLayout frameLayout;
    FaceCircleProcessView cpvFace;
    ImageView mCoverView;
    CommonTitleView titleView;
    Handler mHandler = new Handler();

    ConfirmDialogFragment loginErrorDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_actiivty_face_detect);
        tvFaceHint = findViewById(R.id.user_tv_face_hint);
        timeTv = findViewById(R.id.user_tv_time);
        frameLayout = findViewById(R.id.user_frameLayout);
        cpvFace = findViewById(R.id.cpv_face);
        mCoverView = findViewById(R.id.user_imageview);
        titleView = findViewById(R.id.user_title_bar);

        titleView.setOnLeftClickListener(this);

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
    protected boolean checkAlive() {
        return false;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_title_left) {
            finish();
        }
    }

    @Override
    protected TextView getTimeTextView() {
        return timeTv;
    }

    @Override
    protected void toFailedActivity(String errorMsg) {
        showFaceLoginErrorDialog(getString(R.string.user_login_fail_face_title_timeout), getString(R.string.face_cert_timeout_content));
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
        if (loginErrorDialog != null && loginErrorDialog.getDialog() != null && loginErrorDialog.getDialog().isShowing()){
            return true;
        }
        return false;
    }

    @Override
    public void toNextActivity(Bitmap bitmap, String imageBase64, String imageDigest) {
        String mobileType = android.os.Build.MODEL;  //获取手机型号
        String system = android.os.Build.VERSION.RELEASE;//获取版本号

        byte[] bytes = BitmapUtils.Bitmap2Bytes(bitmap);

        UserBiz.registerFace(bytes,
                AppProxy.getInstance().getUserManager().getMobile(),
                "jpg",
                mobileType,
                system,
                imageBase64,
                imageDigest)
                .compose(this.<VoidObject>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseRespObserver<VoidObject>() {
                    @Override
                    public void onError(String msg) {
                        Log.i("FaceDetectInputActivity", "eee=" + msg);
                        showFaceLoginErrorDialog(getString(R.string.user_register_fail), msg);
                    }

                    @Override
                    public void onSuccess(VoidObject voidObject) {
                        super.onSuccess(voidObject);
                        cpvFace.setCenterColor("#80000000");
                        cpvFace.setProgress(60);
                        tvFaceHint.setTextColor(getResources().getColor(R.color.black_333333));
                        tvFaceHint.setText("认证中");

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cpvFace.setProgress(100);
                                User user = UserManagerImpl.getInstance().getInnerUser();
                                user.setHasOpenFace(Constant.USER_FACE_OPEND);
                                AppProxy.getInstance().getUserManager().updateUser(user);
                                actionStart(FaceDetectInputSuccessActivity.class);
                                finish();
                            }
                        }, 100);
                    }
                });
    }

    /**
     * 人脸识别超时对话框
     *
     * @param title 标题
     * @param msg   内容
     */
    private void showFaceLoginErrorDialog(final String title, final String msg) {
        if (isFinishing()) {//解决部分机型崩溃问题
            return;
        }

        //防止重复弹出
        if (loginErrorDialog != null && loginErrorDialog.getDialog() != null && loginErrorDialog.getDialog().isShowing()){
            return;
        }

        reset();
        if (loginErrorDialog == null){
            loginErrorDialog = new ConfirmDialogFragment.Builder()
                    .setTitle(title)
                    .setDesc(msg)
                    .setConfirmText(getString(R.string.user_i_know))
                    .setConfirmTextColor(getResources().getColor(R.color.pasc_primary))
                    .setHideCloseButton(true)
                    .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
                        @Override
                        public void onConfirm(ConfirmDialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            resumeFaceDetect();
                        }
                    }).build();
        }
        loginErrorDialog.show(getSupportFragmentManager(), "replaceDialog");

    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {
        setTipColor("#FF4C4C");
    }
}
