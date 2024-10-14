package com.pasc.business.face.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.pasc.business.face.BuildConfig;
import com.pasc.business.face.R;
import com.pasc.business.face.data.Tips;
import com.pasc.business.face.util.BitmapUtil;
import com.pasc.business.face.view.CameraSurfaceView;
import com.pasc.lib.base.ICallBack;
import com.pasc.lib.base.permission.PermissionUtils;
import com.pasc.lib.base.util.CameraUtils;
import com.pasc.lib.base.widget.IPermissionDialog;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pingan.reai.face.common.RePaFaceConstants;
import com.pingan.reai.face.entity.RePaFaceDetectFrame;
import com.pingan.reai.face.manager.RePAFaceDetectorManager;
import com.pingan.reai.face.manager.impl.ReOnPAFaceDetectorListener;
import java.util.ArrayList;
import java.util.Collections;

abstract class BaseFaceDetectActivity extends BaseStatusBarActivity
    implements CameraSurfaceView.PreviewCallback {
    protected CameraSurfaceView cameraSurfaceView;
    protected RePAFaceDetectorManager detector;
    CountDownTimer timer;
    private static final int COUNTDOWN_TIME = 13000; //倒计时13秒
    private int blinkTime;
    private String mTipColor;
    private boolean mIsOpenTime = true;
    private int previewCount;
    private volatile boolean inited;
    private IPermissionDialog permissionSettingDialog;

    //人脸识别配置
    String mAppKey = null;
    String mAppId = null;
    String mAuthUrl = null;

    static {
        System.loadLibrary("face_detect_rename");
        System.loadLibrary("nllvm1625666481");
    }

    private volatile boolean initing;
    private String[] permissions = new String[]{
        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    };

    protected void checkPermissionAndGps(final ICallBack callBack) {
        PermissionUtils.request(this, permissions)
            .subscribe(granted -> {
                if (isFinishing()) {
                    return;
                }
                if (granted) {
                    callBack.callBack();
                } else {
                    showPermissionDialog();
                }
            });
    }

    private void showPermissionDialog() {
        if (permissionSettingDialog != null && permissionSettingDialog.isShowing()) {
            return;
        }
        permissionSettingDialog = PermissionUtils.showPermissionDialog(this, permissions);
        permissionSettingDialog.show();
    }

    //初始化相机预览界面
    private void initSurfaceView() {
        cameraSurfaceView.initPreview(this, getCameraViewContainer(),
            Camera.CameraInfo.CAMERA_FACING_FRONT);
        cameraSurfaceView.setPreviewCallback(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initSurfaceView();
    }

    //初始化人脸检测sdk
    private void initDetector() {
        detector = RePAFaceDetectorManager.getInstance();
        detector.setLoggerEnable(BuildConfig.DEBUG);

        ArrayList<Integer> integers = new ArrayList<>();
        if (checkAlive()) {
            if (openShakeHead()) {
                integers.add(RePaFaceConstants.MotionType.SHAKE_HEAD);
            }
            if (openBlinkEye()) {
                integers.add(RePaFaceConstants.MotionType.BLINK_EYE);
            }
            if (openOpenMouth()) {
                integers.add(RePaFaceConstants.MotionType.OPEN_MOUTH);
            }
            //if (openShakeHeadLeft()) {
            //    integers.add(ReRePaFaceConstants.MotionType.SHAKE_LEFT_HEAD);
            //}
            //if (openShakeHeadRight()) {
            //    integers.add(ReRePaFaceConstants.MotionType.SHAKE_RIGHT_HEAD);
            //}
            if (openNodHead()) {
                integers.add(RePaFaceConstants.MotionType.NOD_HEAD);
            }

            Collections.shuffle(integers);
        }
        detector.initFaceDetector(BaseFaceDetectActivity.this);

        detector.setMotions(integers.subList(0, Math.min(2, integers.size())));

        detector.setOnFaceDetectorListener(new ReOnPAFaceDetectorListener() {
            @Override
            public void onDetectTips(int userTip) {
                showTip(userTip);
            }

            @Override
            public void onDetectMotionTips(int userTip) {
                showTip(userTip);
            }

            @Override
            public void onDetectFaceInfo(int i, RePaFaceDetectFrame frame) {
            }

            @Override
            public void onDetectComplete(int tip, RePaFaceDetectFrame[] frame) {
                cancelTask();
                detector.stopFaceDetect();
                if (tip == RePaFaceConstants.DetectComplete.ALL_DONE) {//活体，需要再进行一次后台网络请求确认活体
                    //Bitmap bitmap =
                    //        BitmapUtils.getBitmap(frame.frmaeWidth, frame.frameHeight, frame.frame,
                    //                frame.frmaeOri);
                    //如果需要验证人脸图片，请调用以下方法，原来PaFaceDetectFrame对象内部的原始yuv流属性 frame[] 已经废弃。
                    Bitmap bitmap = BitmapUtil.bytes2Bitmap(frame[0].imageFrame);
                    toNextActivity(bitmap, frame[0].imageBase64, frame[0].imageDigest);
                } else {
                    String errorMsg = Tips.getDescriptionV2(tip);
                    toFailedActivity(errorMsg);
                }
            }

            @Override
            public void onDetectMotionDone(int motionType) {
                startTime();
            }
        });
        if (permissionSettingDialog != null) {
            permissionSettingDialog.dismiss();
        }
        cameraSurfaceView.openCamera();
        cameraSurfaceView.startPreview();
        //try {
        detector.startFaceDetect();
        //} catch (Exception e) {
        //  e.printStackTrace();
        //}
        inited = true;
        initing = false;
        if (mIsOpenTime) {
            startTime();
        }
    }

    public void setTipColor(String color) {
        mTipColor = color;
    }

    private void showTip(int userTip) {
        String descriptionV2 = Tips.getDescriptionV2(userTip);
        if (!TextUtils.isEmpty(descriptionV2)) {
            getTipTextView().setText(descriptionV2);
            if (mTipColor != null && !Tips.isNormalTip(userTip)) {
                getTipTextView().setTextColor(Color.parseColor(mTipColor));
            } else {
                getTipTextView().setTextColor(getResources().getColor(R.color.gray_333333));
            }
        }
    }

    private boolean openShakeHeadRight() {
        return true;
    }

    private boolean openShakeHeadLeft() {
        return true;
    }

    private boolean openOpenMouth() {
        return true;
    }

    private boolean openNodHead() {
        return true;
    }

    private boolean openBlinkEye() {
        return true;
    }

    private boolean openShakeHead() {
        return true;
    }

    protected boolean checkAlive() {
        return true;
    }

    protected void cancelTask() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraSurfaceView = new CameraSurfaceView();
        detector = RePAFaceDetectorManager.getInstance();
    }

    private void init() {
        if (!inited && !initing) {
            initing = true;
            initSurfaceView();
            initDetector();
        }
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        //如果正在显示dialog
        if (isShowDialog()){
            return;
        }
        resumeFaceDetect();
    }

    public void resumeFaceDetect() {
        reset();
        if (!inited) {
            if (permissionSettingDialog != null && permissionSettingDialog.isShowing()) {
                return;
            }
            checkPermissionAndGps(new ICallBack() {
                @Override
                public void callBack() {
                    if (CameraUtils.isCameraCanUse()) {
                        init();
                        if (permissionSettingDialog != null && permissionSettingDialog.isShowing()) {
                            permissionSettingDialog.dismiss();
                        }
                    } else if (cameraSurfaceView == null || !cameraSurfaceView.available()) {
                        showPermissionDialog();
                    }
                }
            });
        }
    }

    @Override
    @CallSuper
    protected void onPause() {
        super.onPause();
        reset();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (permissionSettingDialog != null) {
            permissionSettingDialog.dismiss();
        }
    }

    protected synchronized void reset() {
        if (inited) {
            cancelTask();
            cameraSurfaceView.stopPreview();//停止预览
            cameraSurfaceView.release();
            detector.stopFaceDetect();
            //FaceLibUtils.release(detector);
            detector.release();
            if (timer != null) {
                timer = null;
            }
            inited = false;
        }
    }

    @Override
    public synchronized void onPreviewFrame(byte[] frameData) {
        previewCount++;
        if (previewCount > 15 && inited) {//等待相机准备好，一般相机1秒输出20帧数据左右，15帧后的相机画面较为稳定
            detector.detectPreviewFrame(previewCount, frameData, cameraSurfaceView.getCameraMode()
                , cameraSurfaceView.getCameraOri(), cameraSurfaceView.getCameraWidth(),
                cameraSurfaceView.getCameraHeight());
        }
    }

    private void startTime() {
        //计时器-------------------------------------------------------------------------
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(COUNTDOWN_TIME, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                getTimeTextView().setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                Log.v("UserTest", "onFinish");
                getTimeTextView().setText("0");
                toFailedActivity("检测超时");
                //detector.collectLog();
            }
        };
        timer.start();
    }

    protected abstract TextView getTimeTextView();

    protected abstract void toNextActivity(Bitmap bitmap, String imageBase64, String imageDigest);

    protected abstract void toFailedActivity(String s);

    protected abstract TextView getTipTextView();

    protected abstract FrameLayout getCameraViewContainer();

    protected abstract boolean isShowDialog();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reset();
    }
}