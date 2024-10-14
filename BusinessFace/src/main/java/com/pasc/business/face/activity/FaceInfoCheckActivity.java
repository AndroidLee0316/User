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
import com.pasc.business.face.R;
import com.pasc.business.face.iview.IFaceCheckView;
import com.pasc.business.face.net.resp.FaceCheckResp;
import com.pasc.business.face.presenter.FaceCheckPresenter;
import com.pasc.business.face.view.CameraSurfaceView;
import com.pasc.lib.base.util.BitmapUtils;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.widget.FaceCircleProcessView;
import com.pasc.lib.widget.dialog.OnCloseListener;
import com.pasc.lib.widget.dialog.OnConfirmListener;
import com.pasc.lib.widget.dialog.common.ConfirmDialogFragment;

/**
 * 人脸二次核验，接口跟实名认证不同需要区分
 */
@Route(path = RouterTable.Face.PATH_FACE_INFO_CHECK_ACT)
public class FaceInfoCheckActivity extends BaseFaceDetectActivity
    implements CameraSurfaceView.PreviewCallback, View.OnClickListener, IFaceCheckView {
  private String userName;
  private String idCard;
  private TextView tvFaceHint;
  private TextView timeTv;
  private FrameLayout frameLayout;
  FaceCircleProcessView cpvFace;
  ImageView mCoverView;
  ProgressBar mPbProgress;
  private int count = 5;

  private FaceCheckPresenter faceCheckPresenter;
  Handler mHandler = new Handler();

  //退出页面提示框
  private ConfirmDialogFragment exitDialog;
  //错误提示/超时提示
  private ConfirmDialogFragment errorSingleDialog;
  //错误提示/超时提示
  private ConfirmDialogFragment errorMultiDialog;

  @Override
  public void onBackPressed() {
    EventBusOutUtils.postFaceCheckCancled();
    finish();
  }

  public void onFailedAndFinish(String msg, int account) {
    FaceCheckFailActivity.start(FaceInfoCheckActivity.this, userName,idCard,
        FaceCheckFailActivity.TYPE_INFO_CHECK_COMPARE, msg, account);
  }

  @Override
  protected int layoutResId() {
    return R.layout.face_activity_face_info_identify;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.face_actiivty_face_identify);
    userName = getIntent().getStringExtra("userName");
    idCard = getIntent().getStringExtra("idCard");
    faceCheckPresenter = new FaceCheckPresenter(this);
    tvFaceHint = findViewById(R.id.user_tv_face_hint);
    timeTv = findViewById(R.id.user_tv_time);
    frameLayout = findViewById(R.id.user_frameLayout);
    cpvFace = findViewById(R.id.cpv_face);
    mCoverView = findViewById(R.id.user_imageview);
    mPbProgress = findViewById(R.id.pb_progress);
    CommonTitleView title = findViewById(R.id.user_title_bar);
    title.setOnLeftClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });

    cpvFace.post(new Runnable() {
      @Override
      public void run() {
        ConstraintLayout.LayoutParams params =
            (ConstraintLayout.LayoutParams) cpvFace.getLayoutParams();
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
    if (exitDialog == null) {
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
    showErrorSingleDialog(getString(R.string.face_cert_timeout_title),
        getString(R.string.face_chech_timeout_content), getString(R.string.user_retry),
        new OnConfirmListener() {
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
   * @param msg 内容
   */
  protected void showErrorSingleDialog(final String title, final String msg, String confirmText,
      OnConfirmListener listener) {
    if (isFinishing() || isShowDialog()) {//解决部分机型崩溃问题
      return;
    }

    reset();
    if (errorSingleDialog == null) {
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
    if (exitDialog != null && exitDialog.getDialog() != null && exitDialog.getDialog()
        .isShowing()) {
      return true;
    }
    if (errorSingleDialog != null
        && errorSingleDialog.getDialog() != null
        && errorSingleDialog.getDialog().isShowing()) {
      return true;
    }
    if (errorMultiDialog != null
        && errorMultiDialog.getDialog() != null
        && errorMultiDialog.getDialog().isShowing()) {
      return true;
    }
    return false;
  }

  //人脸检测回调消息
  @Override
  public void toNextActivity(Bitmap bitmap, String imageBase64, String imageDigest) {
    faceAndIdComparision(bitmap, imageBase64, imageDigest);
  }

  @Override
  public void faceCompare(FaceCheckResp resp) {
    if (resp.passed) {
      count = resp.remainErrorTimes;
      //            gotoSuccess();
      EventBusOutUtils.postFaceCheckSuccess(resp.credential, "false");
      //            finish();
    } else {
      onFailedAndFinish("请重新识别完成认证", resp.remainErrorTimes);
    }
    finish();
  }

  @Override
  public void onError(String code, String error) {
//    if ("-1".equals(code)) {
//      ToastUtils.toastMsg(error);
//    }
//    onFailedAndFinish(error, count);
    ToastUtils.toastMsg(error);
    finish();
  }

  @Override
  public void showLoadings() {
    //        showLoading();
  }

  @Override
  public void dismissLoadings() {
    dismissLoading();
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
    //        String initCode, String appid, String userId, String plat, String version, String model, String type,  byte[] bytes
    faceCheckPresenter.faceInfoCompare(userName, idCard, "1", version, phoneModel, "jpg", bytes, imageBase64, imageDigest);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    faceCheckPresenter.onDestroy();
    if (mHandler != null) {
      mHandler.removeCallbacksAndMessages(null);
    }
  }

  @Override
  protected void onInit(@Nullable Bundle bundle) {
    setTipColor("#FF4C4C");
  }

  private void gotoSuccess() {
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
  protected boolean checkAlive() {
    return true;
  }
}
