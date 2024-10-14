package com.pasc.business.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.pasc.business.login.LoginContract;
import com.pasc.business.login.LoginPresenter;
import com.pasc.business.login.R;
import com.pasc.business.login.util.UserPrivacyHelper;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.base.user.IUserInfo;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.user.LoginConstant;
import com.pasc.business.login.third.ThirdLoginContract;
import com.pasc.business.login.third.ThirdLoginPresenter;
import com.pasc.business.login.util.LoginSuccessActionUtils;
import com.pasc.business.login.util.ThirdPartUtils;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.hybrid.PascHybrid;
import com.pasc.lib.hybrid.nativeability.WebStrategy;
import com.pasc.lib.imageloader.PascImageLoader;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.router.interceptor.LoginInterceptor;
import com.pasc.lib.statistics.StatisticsManager;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.base.CallBack;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.data.user.ThirdLoginUser;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.user.net.resp.GetUserInfoResp;
import com.pasc.lib.userbase.user.urlconfig.LoginUrlManager;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.userbase.user.util.UserManagerImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.pasc.lib.statistics.PageType.APP;

public class LoginSwitchActivity extends BaseStatusBarActivity implements ThirdLoginContract.View, LoginContract.View {
    public static final String TAG_PHONE = "TAG_PHONE";

    private Context mContext;
    private RelativeLayout rootRL;
    private RoundedImageView rivHead;
    protected TextView mTvMobile;
    protected String mMobile;
    private BottomSheetDialog bottomSheetDialog;
    private CommonTitleView commonTitleView;
    private ThirdLoginContract.Presenter thirdLoginPresenter;
    private LoginContract.Presenter mLoginPresenter;

    /**
     * 用户隐私协议助手
     */
    private UserPrivacyHelper userPrivacyHelper;

    /**
     * 跳转到绑定账号页面的回调
     */
    private static final int REQUEST_CODE_BIND = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.login_activity_login_switch);
        EventBus.getDefault().register(this);
        thirdLoginPresenter = new ThirdLoginPresenter(this);
        mLoginPresenter = new LoginPresenter(this);
        initView();
        setUserInfoByMobile(mMobile);
    }

    private void initView() {
        rootRL = findViewById(R.id.login_activity_login_switch_root);
        rivHead = findViewById(R.id.user_riv_head);
        mTvMobile = findViewById(R.id.user_tv_mobile);
        mMobile = getIntent().getExtras().getString(TAG_PHONE);
        commonTitleView = findViewById(R.id.user_view_title);
        mTvMobile.setText(mMobile);
        commonTitleView.setUnderLineVisible(false);
        commonTitleView.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBack();
            }
        });

        userPrivacyHelper = new UserPrivacyHelper(rootRL, null);
        userPrivacyHelper.switchNoSelectType();
    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.user_rtv_face_login) {
            gotoFaceLogin();
        } else if (i == R.id.user_tv_login_method_switch) {
            showDialog();
        } else if (i == R.id.user_tv_account_switch) {
            switchAccout();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.login_no_anim, R.anim.login_out_to_bottom);
    }

    @Override
    public void onBackPressed() {
        keyBack();
    }

    /**
     * 根据手机号获取用户信息
     *
     * @param mobile 电话号码
     */
    private void setUserInfoByMobile(String mobile) {

        IUserInfo user = UserManagerImpl.getInstance().getUserInfo();
        if (user != null && !TextUtils.isEmpty(user.getMobileNo()) && user.getMobileNo().equals(mobile)){
            if (!TextUtils.isEmpty(user.getHeadImg())) {
                PascImageLoader.getInstance().loadImageUrl(user.getHeadImg(), rivHead,
                        R.drawable.login_ic_head_portrait, PascImageLoader.SCALE_DEFAULT);
            } else {
                if (GetUserInfoResp.SEX_MALE.equals(user.getSex())) {
                    PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_male, rivHead);
                } else if (GetUserInfoResp.SEX_FEMALE.equals(user.getSex())) {
                    PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_female, rivHead);
                } else {
                    PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_portrait, rivHead);
                }
            }

        }else {
            PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_portrait, rivHead);
        }

//        mLoginPresenter.getUserInfoByMobile(mobile, new CallBack<GetUserInfoResp>() {
//            @Override
//            public void onSuccess(GetUserInfoResp data) {
//                if (data != null && !TextUtils.isEmpty(data.getHeadImg())) {
//                    PascImageLoader.getInstance().loadImageUrl(data.getHeadImg(), rivHead,
//                            R.mipmap.ic_launcher, PascImageLoader.SCALE_DEFAULT);
//                } else {
//                    if (GetUserInfoResp.SEX_MALE.equals(data.getSex())) {
//                        PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_male, rivHead);
//                    } else if (GetUserInfoResp.SEX_FEMALE.equals(data.getSex())) {
//                        PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_female, rivHead);
//                    } else {
//                        PascImageLoader.getInstance().loadImageRes(R.mipmap.ic_launcher, rivHead);
//                    }
//                }
//            }
//
//            @Override
//            public void onFail(String code, String msg) {
//                PascImageLoader.getInstance().loadImageRes(R.mipmap.ic_launcher, rivHead);
//            }
//        });
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {
    }

    @Override
    public void qqAuthorizeFailed(String errorCode, String msg) {
        CommonUtils.toastMsg(msg);
    }

    @Override
    public void qqAuthorizeSuccess(String openID, String accessToken) {
        showLoading(getString(R.string.user_logining));
    }

    @Override
    public void qqLoginSuccess(ThirdLoginUser user) {
        dismissLoading();
        if (TextUtils.isEmpty(user.mobileNo)) {
            FastBindAccountActivity.actionStartForResult(this,REQUEST_CODE_BIND, user);
        } else {
            StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_SUCCESS, getString(R.string.login_qq),APP,null);
            updateLoginSuccess(user);
        }
    }

    @Override
    public void qqLoginError(String code, String msg) {
        dismissLoading();
        StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_FAIL, getString(R.string.login_qq),APP,null);
        CommonUtils.toastMsg(msg);
    }

    @Override
    public void wxLoginSuccess(ThirdLoginUser user) {
        dismissLoading();
        if (TextUtils.isEmpty(user.mobileNo)) {
            FastBindAccountActivity.actionStartForResult(this,REQUEST_CODE_BIND, user);
        } else {
            updateLoginSuccess(user);
            StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_SUCCESS, getString(R.string.login_wechat),APP,null);
        }
    }

    @Override
    public void wxAuthorizeFailed(String errorCode, String msg) {
        CommonUtils.toastMsg(msg);
    }

    @Override
    public void wxAuthorizeSuccess(String code) {
        showLoading(getString(R.string.user_logining));
    }

    @Override
    public void wxLoginError(String code, String msg) {
        dismissLoading();
        StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_FAIL, getString(R.string.login_wechat),APP,null);
        CommonUtils.toastMsg(msg);
    }

    @Override
    public void alipayAuthorizeFailed(String code, String msg) {
        ToastUtils.toastMsg(msg);
    }

    @Override
    public void alipayAuthorizeSuccess() {
        showLoading(getString(R.string.user_logining));
    }

    @Override
    public void alipayLoginSuccess(ThirdLoginUser user) {
        dismissLoading();
        if (TextUtils.isEmpty(user.mobileNo)) {
            FastBindAccountActivity.actionStartForResult(this, REQUEST_CODE_BIND, user);
        } else {
            StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_SUCCESS, getString(R.string.login_alipay),APP,null);
            updateLoginSuccess(user);
        }
    }

    @Override
    public void alipayLoginError(String code, String msg) {
        dismissLoading();
        StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_FAIL, getString(R.string.login_alipay),APP,null);
        CommonUtils.toastMsg(msg);
    }

    private void showDialog() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(this);
            View view = LayoutInflater.from(this).inflate(R.layout.login_dialog_switch_login, null);
            bottomSheetDialog.setContentView(view);
            view.findViewById(R.id.tv_sms).setOnClickListener(
                    v -> {
                        bottomSheetDialog.dismiss();
                        gotoSMSLogin();
                    });
            View pwdView = view.findViewById(R.id.tv_pwd);
            pwdView.setOnClickListener(
                    v -> {
                        bottomSheetDialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString(LoginConstant.LOGIN_TYPE, LoginConstant.LOGIN_TYPE_PWD);

                        Intent intent = new Intent(this, NewLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    });

            View wxView = view.findViewById(R.id.tv_wx);
            if (ThirdPartUtils.isWxAppInstalledAndSupported(this)) {
                wxView.setOnClickListener(
                        v -> {
                            bottomSheetDialog.dismiss();
                            thirdLoginPresenter.wxLogin(mContext);
                        });
            } else {
                wxView.setVisibility(View.GONE);
            }

            View qqView = view.findViewById(R.id.tv_qq);
            if (ThirdPartUtils.isQQAppInstalledAndSupported(this)) {
                qqView.setOnClickListener(
                        v -> {
                            bottomSheetDialog.dismiss();
                            thirdLoginPresenter.qqLogin(mContext);
                        });
            } else {
                qqView.setVisibility(View.GONE);
            }


            View alipayView = view.findViewById(R.id.tv_alipay);
            if (ThirdPartUtils.isAlipayAppInstalledAndSupported(this)) {
                alipayView.setOnClickListener(
                        v -> {
                            bottomSheetDialog.dismiss();
                            thirdLoginPresenter.alipayLogin(mContext);
                        });
            } else {
                alipayView.setVisibility(View.GONE);
            }


            view.findViewById(R.id.tv_cancel).setOnClickListener(
                    v -> {
                        bottomSheetDialog.dismiss();
                    });
        }
        if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefresh(BaseEvent event) {
        if (EventTag.USER_LOGIN_SUCCEED.equals(event.getTag())) {
            LoginSuccessActionUtils.getInstance().onLoginSuccessAction();
            finish();
        }
    }
    private void updateLoginSuccess(User user) {
        mLoginPresenter.onLoginSuccessAction(user);
        CommonUtils.toastMsg(getString(R.string.user_login_success));
        finish();
    }

    /**
     * 切换账号
     */
    protected void switchAccout(){
        Bundle bundle = new Bundle();
        bundle.putString(LoginConstant.LOGIN_TYPE, LoginConstant.LOGIN_TYPE_SWITCH_ACCOUNT);

        Intent intent = new Intent(this, NewLoginActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转到人脸登陆
     */
    protected void gotoFaceLogin(){
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MOBILE_NUMBER, mMobile);
        BaseJumper.jumpBundleARouter(RouterTable.Face.PATH_FACE_LOGIN_ACT, bundle);
        overridePendingTransition(R.anim.login_in_from_right, R.anim.login_no_anim);
    }


    /**
     * 跳转到短信登陆
     */
    protected void gotoSMSLogin(){

        Bundle bundle = new Bundle();
        bundle.putString(LoginConstant.LOGIN_TYPE, LoginConstant.LOGIN_TYPE_SMS);

        Intent intent = new Intent(this, NewLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果绑定成功
        if (requestCode == REQUEST_CODE_BIND && data != null && data.getBooleanExtra(FastBindAccountActivity.RESULT_PARAM_BIND_SUCCESS,false)){
            finish();
        }
    }

    private void keyBack(){
        LoginInterceptor.notifyCallBack(false);
        LoginSuccessActionUtils.getInstance().clearCallback();
        EventBusOutUtils.postLoginCancle();
        finish();
    }

}