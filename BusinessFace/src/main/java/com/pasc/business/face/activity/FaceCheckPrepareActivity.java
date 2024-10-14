package com.pasc.business.face.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.pasc.business.face.R;
import com.pasc.business.face.data.FaceConstant;
import com.pasc.business.face.iview.IFaceInitView;
import com.pasc.business.face.net.resp.FaceInitResp;
import com.pasc.business.face.presenter.FaceInitPresenter;
import com.pasc.lib.base.activity.BaseActivity;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.router.interceptor.CertificationInterceptor;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.user.certification.net.resp.FaceAndIdComparisonErrorResp;
import com.pasc.lib.userbase.user.certification.net.resp.FaceCompareDataResp;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.widget.dialog.OnCloseListener;
import com.pasc.lib.widget.dialog.OnConfirmListener;
import com.pasc.lib.widget.dialog.common.ConfirmDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 提示用户人脸核验
 */
@Route(path = RouterTable.Face.PATH_FACE_CHECK_PREPARE_ACT)
public class FaceCheckPrepareActivity extends BaseStatusBarActivity implements IFaceInitView {

    private Context mContext;
    private CommonTitleView mTitleView;
    private TextView mHintTV;
    private Button mBeginBtn;
    private FaceInitPresenter presenter;
    private String appid;
    private int type;

    @Override
    protected int layoutResId() {
        return R.layout.face_activity_face_check_prepare;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {
        mContext = this;
        presenter = new FaceInitPresenter(this);
        EventBus.getDefault().register(this);
        initView();
        initData();

    }

    private void initView() {
        mTitleView = findViewById(R.id.user_title_bar);
        mHintTV = findViewById(R.id.face_activity_face_check_prepare_hint_tv);
        mBeginBtn = findViewById(R.id.face_activity_face_check_prepare_begin_btn);

        mBeginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInitCode();
            }
        });

        mTitleView.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initData() {

        String name = getIntent().getStringExtra("name");
        name = maskRealName(name);
        String hint = String.format(getString(R.string.face_check_hint), name);
        appid = getIntent().getStringExtra("appId");
        SpannableString hintSS = new SpannableString(hint);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.pasc_primary));
        hintSS.setSpan(colorSpan, hintSS.length() - 4 - name.length(), hintSS.length() - 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        mHintTV.setText(hintSS);

        String errorCode = getIntent().getStringExtra("errorCode");
        String errorMsg = getIntent().getStringExtra("errorMsg");

        //如果传递了错误过来，需要先展示错误，
        if (!TextUtils.isEmpty(errorCode)){
            onError(errorCode, errorMsg);
        }
    }


    private String maskRealName(String username) {
        String maskName = "";
        if (!TextUtils.isEmpty(username)) {
            maskName =
                    username.length() > 2 ? username.substring(0, 1) + "*" + username.substring(
                            username.length() - 1, username.length())
                            : "*" + username.substring(username.length() - 1, username.length());
        }
        return maskName;
    }


    /**
     * 接收到人脸核验成功后关闭掉自身
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCertification(BaseEvent event) {
        if (EventTag.USER_FACE_CHECK_SUCCEED.equals(event.getTag()) || EventTag.USER_FACE_CHECK_FAILED.equals(event.getTag()) || EventTag.USER_FACE_CHECK_CANCLED.equals(event.getTag())) {
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        EventBusOutUtils.postFaceCheckCancled();
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void faceInitview(FaceInitResp resp) {

        if (resp.isValidate) {
            EventBusOutUtils.postFaceCheckSuccess(resp.credential, "true");
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("initCode", resp.initcode);
            bundle.putString("appId", appid);
            bundle.putString("compare", resp.validType);
            BaseJumper.jumpBundleARouter(RouterTable.Face.PATH_FACE_CHECK_ACT, bundle);
//            finish();

        }

    }

    @Override
    public void onError(String code, String error) {

        if ("40001".equals(code)) {
            showUserKickedDialog("");
        } else {
            ToastUtils.toastMsg(error);
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

    private void showUserKickedDialog(final String msg) {
        if (isFinishing()) {//解决部分机型崩溃问题
            return;
        }
        if (himtDialog == null) {
            himtDialog = new ConfirmDialogFragment.Builder()
                    .setDesc(getResources().getString(R.string.face_check_dialog_hint))
                    .setCloseText(getResources().getString(R.string.face_check_dialog_btn))
                    .setCloseTextColor(getResources().getColor(R.color.pasc_primary))
                    .setHideConfirmButton(true)
                    .setOnCloseListener(new OnCloseListener<ConfirmDialogFragment>() {
                        @Override
                        public void onClose(ConfirmDialogFragment confirmDialogFragment) {
                            himtDialog.dismiss();
                        }
                    }).build();
        }
        himtDialog.show(getSupportFragmentManager(), "himtDialog");

    }

    private void getInitCode() {
        if (!TextUtils.isEmpty(appid)) {
            presenter.faceInit(appid, 1, FaceConstant.URL_APLI);
        } else {
            ToastUtils.toastMsg("服务appId不能为空");
        }

    }

}
