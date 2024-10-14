package com.pasc.business.face.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.face.R;
import com.pasc.business.face.data.FaceConstant;
import com.pasc.lib.base.user.IUserInfo;
import com.pasc.lib.net.resp.BaseRespThrowableObserver;
import com.pasc.lib.net.resp.VoidObject;;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.userbase.user.util.UserManagerImpl;
import com.pasc.lib.widget.dialog.OnCloseListener;
import com.pasc.lib.widget.dialog.OnConfirmListener;
import com.pasc.lib.widget.dialog.common.ConfirmDialogFragment;

import io.reactivex.functions.Consumer;

/**
 * 功能：人脸登录开关界面
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2018/8/4
 */
@Route(path = RouterTable.Face.PATH_FACE_LOGIN_SWITCH_ACT)
public class LoginByFaceSwitchActivity extends BaseStatusBarActivity {

    private Context mContext;
    /**
     * 开关
     */
    TextView mStatusTV;
    ImageView mSwitchIV;
    TextView mTvResetFaceHint;
    TextView mTvResetFace;
    ImageView ivLeft;
    protected boolean mHasOpenFace;

    /**
     * 启动本activity ，其他activity调用本activity 建议调用该函数
     *
     * @param context
     */
    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginByFaceSwitchActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.face_activity_face_login_switch);
        initView();
        setListener();
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {

    }

    private void setListener() {

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.activity_account_login_by_face_switch_iv) {
                    if (mHasOpenFace) {
                        showWarningDialog(getString(R.string.user_login_face_off_tip));
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FaceConstant.VERIFY_TYPE, FaceConstant.VERIFY_TYPE_FACE_INPUT);
                        actionStart(AccountVerifyActivity.class, bundle);
                    }
                } else if (v.getId() == R.id.user_tv_reset_face) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FaceConstant.VERIFY_TYPE, FaceConstant.VERIFY_TYPE_FACE_RESET);
                    actionStart(AccountVerifyActivity.class, bundle);
                } else if (v.getId() == R.id.iv_title_left) {
                    onBackPressed();
                }
            }
        };

        ivLeft.setOnClickListener(onClickListener);
        mSwitchIV.setOnClickListener(onClickListener);
        mTvResetFace.setOnClickListener(onClickListener);
    }

    private void initView() {
        mStatusTV = findViewById(R.id.activity_account_login_by_face_switch_status_tv);
        ivLeft = findViewById(R.id.iv_title_left);
        mSwitchIV = findViewById(R.id.activity_account_login_by_face_switch_iv);
        mTvResetFaceHint = findViewById(R.id.user_tv_reset_face_hint);
        mTvResetFace = findViewById(R.id.user_tv_reset_face);


    }


    protected void updateSwitchView() {
        if (mHasOpenFace) {
            mStatusTV.setText(R.string.user_face_opened);
            mSwitchIV.setImageResource(R.mipmap.face_ic_switch_on);
            mTvResetFaceHint.setVisibility(View.VISIBLE);
            mTvResetFace.setVisibility(View.VISIBLE);
        } else {
            mStatusTV.setText(R.string.user_face_unopen);
            mSwitchIV.setImageResource(R.mipmap.face_ic_switch_off);
            mTvResetFaceHint.setVisibility(View.GONE);
            mTvResetFace.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHasOpenFace = UserManagerImpl.getInstance().isOpenFaceVerify();
        updateSwitchView();
    }

    /**
     * 弹出关闭警告（提示）确认框
     */
    private void showWarningDialog(final String msg) {
        if (isFinishing()) {//解决部分机型崩溃问题
            return;
        }

        ConfirmDialogFragment replaceDialog = new ConfirmDialogFragment.Builder()
                .setDesc(msg)
                .setConfirmText("暂不关闭")
                .setCloseText("关闭")
                .setCloseTextColor(getResources().getColor(R.color.black_666666))
                .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
                    @Override
                    public void onConfirm(ConfirmDialogFragment dialogFragment) {
                        dialogFragment.dismiss();

                    }
                }).setOnCloseListener(new OnCloseListener<ConfirmDialogFragment>() {
                    @Override
                    public void onClose(ConfirmDialogFragment confirmDialogFragment) {
                        confirmDialogFragment.dismiss();
                        showLoading();
                        UserBiz.updateOpenFace(UserManagerImpl.getInstance().getInnerUser().getToken(),
                                Constant.USER_FACE_UN_OPEN)
                                .subscribe(new Consumer<VoidObject>() {
                                    @Override
                                    public void accept(VoidObject voidObject) throws Exception {
                                        dismissLoading();
                                        IUserInfo user = UserManagerImpl.getInstance().getInnerUser();
                                        if (user != null) {
                                            user.setHasOpenFace(Constant.USER_FACE_UN_OPEN);
                                            UserManagerImpl.getInstance().updateUser(user);
                                        }
                                        mHasOpenFace = false;
                                        updateSwitchView();
                                    }
                                }, new BaseRespThrowableObserver() {
                                    @Override
                                    public void onV2Error(String errorCode, String errorMsg) {
                                        dismissLoading();
                                        CommonUtils.toastMsg(errorMsg);
                                    }
                                });
                    }
                }).build();

        replaceDialog.show(getSupportFragmentManager(), "replaceDialog");


    }

    @Override
    public void onBackPressed() {
        keyBack();
    }

    private void keyBack(){
        EventBusOutUtils.postSetFaceResult();
        finish();
    }

}
