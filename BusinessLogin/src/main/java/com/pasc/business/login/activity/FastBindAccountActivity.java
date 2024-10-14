package com.pasc.business.login.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.pasc.business.login.LoginContract;
import com.pasc.business.login.LoginPresenter;
import com.pasc.business.login.R;
import com.pasc.business.login.base.LoginHelper;
import com.pasc.business.login.third.ThirdLoginHelper;
import com.pasc.business.login.util.UserPrivacyHelper;
import com.pasc.lib.base.activity.BaseActivity;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.base.user.IUserInfo;
import com.pasc.lib.base.util.StatusBarUtils;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.hybrid.PascHybrid;
import com.pasc.lib.hybrid.nativeability.WebStrategy;
import com.pasc.lib.imageloader.PascImageLoader;
import com.pasc.lib.userbase.user.net.resp.GetUserInfoResp;
import com.pasc.lib.userbase.user.third.ThirdCallBack;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.userbase.base.CallBack;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.data.user.ThirdLoginUser;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.event.EventKey;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.base.view.FormatEditText;
import com.pasc.lib.userbase.user.net.param.BindThirdPartParam;
import com.pasc.lib.userbase.user.urlconfig.LoginUrlManager;
import com.pasc.lib.userbase.user.urlconfig.UserUrlConfig;
import com.pasc.lib.userbase.user.util.UserManagerImpl;
import com.pasc.lib.widget.toolbar.ClearEditText;

import org.greenrobot.eventbus.EventBus;

/**
 * 快速绑定手机号
 */
public class FastBindAccountActivity extends BaseActivity implements View.OnClickListener, LoginContract.View {
    private static final String EXTRA_BIND_DATA = "Bind_Third_Info";


    RoundedImageView rivHeadLogo;
    TextView tvWelcome;
    FormatEditText etCode;
    TextView tvCode;

    private ThirdLoginUser thirdUserInfo;
    private ThirdLoginHelper thirdLogin;
    private RelativeLayout rlRoot;
    private CommonTitleView titleBar;
    private FormatEditText etUserPhoneNum;
    private View divider1, divider2;
    private Button btnBind;
    private TextView tvAgreement;
    private LinearLayout serverLL;
    private TextView tvSzServer;
    private LoginContract.Presenter mLoginPresenter;
    private LoginHelper mLoginHelper;

    private String mPhoneNumber = "";


    /**
     * 用户隐私协议助手
     */
    private UserPrivacyHelper userPrivacyHelper;


    /**
     * 绑定成功后给调用activity的result回调字段   */
    public static final String RESULT_PARAM_BIND_SUCCESS = "bindSuccess";

    public static void actionStartForResult(Activity context, int requestCode, ThirdLoginUser user) {
        Intent intent = new Intent(context, FastBindAccountActivity.class);
        intent.putExtra(EXTRA_BIND_DATA, user);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_fast_bind_acount);

        initView();
        initData();

        Intent intent = getIntent();
        if (intent != null) {
            thirdUserInfo = (ThirdLoginUser) intent.getSerializableExtra(EXTRA_BIND_DATA);
            if (thirdUserInfo != null) {
                PascImageLoader.getInstance().loadImageUrl(thirdUserInfo.headImg, rivHeadLogo,
                        R.drawable.login_ic_head_portrait, PascImageLoader.SCALE_DEFAULT);
                if (TextUtils.isEmpty(thirdUserInfo.nickName)){
                    tvWelcome.setText(R.string.user_fast_bind_no_name);
                }else {
                    tvWelcome.setText("Hi," + thirdUserInfo.nickName);
                }

            }
        }
        etUserPhoneNum.setEditTextChangeListener(new ClearEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                s = s.replace(" ", "");
                mPhoneNumber = s;
                //手机号11位，验证码6位数
                if (mPhoneNumber.length() == 11) {
                    setUserInfoByMobile(s);
                }else if (mPhoneNumber.length()==10){
                    PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_portrait, rivHeadLogo);
                }
                setBtnNextClickable();

            }
        });
        etCode.setEditTextChangeListener(new ClearEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                setBtnNextClickable();
            }
        });

        etUserPhoneNum.setInnerFocusChangeListener(new ClearEditText.InnerFocusChangeListener() {
            @Override
            public void onInnerFocusChange(View view, boolean b) {
                divider1.setBackgroundColor(b ? getResources().getColor(R.color.pasc_primary)
                        : getResources().getColor(R.color.gray_dddddd));
            }
        });
        etCode.setInnerFocusChangeListener(new ClearEditText.InnerFocusChangeListener() {
            @Override
            public void onInnerFocusChange(View view, boolean b) {
                divider2.setBackgroundColor(b ? getResources().getColor(R.color.pasc_primary)
                        : getResources().getColor(R.color.gray_dddddd));
            }
        });
    }

    private void initView() {
        rlRoot = findViewById(R.id.rl_root);
        titleBar = findViewById(R.id.user_title_bar);
        rivHeadLogo = findViewById(R.id.user_riv_head_logo);
        tvWelcome = findViewById(R.id.user_tv_welcome);
        etUserPhoneNum = findViewById(R.id.user_et_user_phone_num);
        etUserPhoneNum.setFormatType(FormatEditText.TYPE_PHONE);
        etCode = findViewById(R.id.user_et_code);
        etCode.setFormatType(FormatEditText.TYPE_SMS_CODE);
        divider1 = findViewById(R.id.user_v_divider);
        divider2 = findViewById(R.id.user_v_divider2);
        tvCode = findViewById(R.id.user_tv_get_verify_code);
        btnBind = findViewById(R.id.user_btn_bind);
        tvAgreement = findViewById(R.id.tv_agreement);
        serverLL = findViewById(R.id.login_activity_fast_bind_acount_server_ll);
        tvSzServer = findViewById(R.id.user_tv_server);
        tvSzServer.setText(LoginUrlManager.getInstance().getAgreementText());
        StatusBarUtils.setStatusBarBgColor(this, getResources().getColor(R.color.white_ffffff));
        StatusBarUtils.setStatusBarColor(this, true);


        if (LoginUrlManager.getInstance().getAgreementLocation() == UserUrlConfig.LoginConfigBean.AGREEMENT_LOCATION_BEHIND_PRIVACY){
            serverLL.setVisibility(View.INVISIBLE);
        }
        userPrivacyHelper = new UserPrivacyHelper(rlRoot, new UserPrivacyHelper.CallBack() {
            @Override
            public void onSelectClick(boolean isSelect) {
                setBtnNextClickable();
            }
        });
    }

    private void initData() {
        tvCode.setOnClickListener(this);
        tvSzServer.setOnClickListener(this);
        btnBind.setOnClickListener(this);
        titleBar.setOnLeftClickListener(this);
        mLoginPresenter = new LoginPresenter(this);
        thirdLogin = new ThirdLoginHelper(this);
        mLoginHelper = new LoginHelper(this);

    }

    private void setBtnNextClickable() {
        boolean bool1 = mPhoneNumber.length() == 11;
        boolean bool2 = etCode.getOriginalText().length() == 6;
        boolean bool3 = userPrivacyHelper.isSelectPrivacy();
//        if (bool1 && bool2 && userPrivacyHelper.isSelectPrivacy()){
//            bindAccount();
//        }
        if (bool1 && bool2 && bool3) {
            btnBind.setEnabled(true);
            btnBind.setAlpha(1f);
        } else {
            btnBind.setEnabled(false);
            btnBind.setAlpha(0.3f);
        }

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_title_left) {
            finish();
        } else if (i == R.id.user_tv_server) {
            PascHybrid.getInstance().start(this,
                    new WebStrategy().setUrl(LoginUrlManager.getInstance().getAgreementUrl()));
        } else if (i == R.id.user_tv_get_verify_code) {
            if (TextUtils.isEmpty(mPhoneNumber)) {
                CommonUtils.toastMsg(getString(R.string.user_input_phone_number));
                return;
            }
            if (mPhoneNumber.length() != 11) {
                CommonUtils.toastMsg(getString(R.string.user_error_phone));
                return;
            }
            etCode.setText("");//清除验证码
            getVerificationCode();
        } else if (i == R.id.user_btn_bind) {
            if (TextUtils.isEmpty(mPhoneNumber)) {
                CommonUtils.toastMsg(getString(R.string.user_input_phone_number));
                return;
            }

            if (mPhoneNumber.length() != 11) {
                CommonUtils.toastMsg(getString(R.string.user_error_phone));
                return;
            }

            String verifyCode = etCode.getOriginalText();
            if (TextUtils.isEmpty(verifyCode)) {
                CommonUtils.toastMsg(getString(R.string.user_input_sms_code));
                return;
            }

            if (!CommonUtils.isVerifyCodeLegal(verifyCode)) {
                CommonUtils.toastMsg(getString(R.string.user_verify_code_wrong));
                return;
            }
            bindAccount();
        }
    }



    /**
     * 根据手机号获取用户信息
     *
     * @param mobile 电话号码
     */
    private void setUserInfoByMobile(String mobile) {
        //初始化设置为false or 切换账号的时候初始化为false
        //userPrivacyHelper.updateIsAleadyAgreePrivacy(false);
        userPrivacyHelper.updateSelectPrivacy(false);
        //如果配置的是强制一定要手动选择同意服务协议，则不需要调用获取用户信息
        if (LoginUrlManager.getInstance().getServiceSelectType() == UserUrlConfig.LoginConfigBean.SERVICE_SELECT_FORCE_UNSELECT){
            return;
        }
        mobile = mobile.replace(" ", "");


        IUserInfo user = UserManagerImpl.getInstance().getUserInfo();
        if (user != null && !TextUtils.isEmpty(user.getMobileNo()) && user.getMobileNo().equals(mobile)){
            if (!TextUtils.isEmpty(user.getHeadImg())) {
                PascImageLoader.getInstance().loadImageUrl(user.getHeadImg(), rivHeadLogo,
                        R.drawable.login_ic_head_portrait, PascImageLoader.SCALE_DEFAULT);
            } else {
                if (GetUserInfoResp.SEX_MALE.equals(user.getSex())) {
                    PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_male, rivHeadLogo);
                } else if (GetUserInfoResp.SEX_FEMALE.equals(user.getSex())) {
                    PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_female, rivHeadLogo);
                } else {
                    PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_portrait, rivHeadLogo);
                }
            }

        }else {
            PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_portrait, rivHeadLogo);
        }


//        mLoginPresenter.getUserInfoByMobile(mobile, new CallBack<GetUserInfoResp>() {
//            @Override
//            public void onSuccess(GetUserInfoResp data) {
//                if (data != null) {
//                    if (GetUserInfoResp.SIGN_PRIVATE_AGREEMENT_ALREADY.equals(data.getSignPrivateAgreement())){
//                        //已经同意了用户隐私协议
////                        userPrivacyHelper.updateIsAleadyAgreePrivacy(true);
//                        userPrivacyHelper.updateSelectPrivacy(true);
//                    }else {
//                        //未同意用户隐私协议
////                        userPrivacyHelper.updateIsAleadyAgreePrivacy(false);
//                        userPrivacyHelper.updateSelectPrivacy(false);
//                    }
//                }else {
////                    userPrivacyHelper.updateIsAleadyAgreePrivacy(false);
//                    userPrivacyHelper.updateSelectPrivacy(false);
//                }
//            }
//
//            @Override
//            public void onFail(String code, String msg) {
////                userPrivacyHelper.updateIsAleadyAgreePrivacy(false);
//                userPrivacyHelper.updateSelectPrivacy(false);
//                //已经获取到了第三方的头像，不要覆盖
//                //PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_portrait, rivHeadLogo);
//            }
//        });
    }


    private void getVerificationCode() {
        mLoginHelper.sendVerifyCode(mPhoneNumber, Constant.SMS_BIND_MOBILE, new CallBack<VoidObject>() {
            @Override
            public void onSuccess(VoidObject data) {
                CommonUtils.toastMsg(getString(R.string.user_code_sent));
                mLoginHelper.showCountDown(60, tvCode, getResources());
            }

            @Override
            public void onFail(String code, String msg) {
                CommonUtils.toastMsg(msg);
                if (tvCode != null) {
                    tvCode.setClickable(true);
                }
            }
        });
    }

    private void bindAccount() {
        showLoading(getString(R.string.pasc_user_bind_account_ing),false);
        BindThirdPartParam param = new BindThirdPartParam(mPhoneNumber, thirdUserInfo.accessToken,
                thirdUserInfo.openid, thirdUserInfo.loginType, "", etCode.getOriginalText(), "0");
        thirdLogin.bindThird(param, new ThirdCallBack.IBindThirdCallBack() {
            @Override
            public void onSuccess(User user) {
                ToastUtils.toastLongMsg(getString(R.string.pasc_user_bind_phone_success));
                dismissLoading();
                btnBind.setEnabled(false);
                btnBind.setAlpha(0.3f);
                btnBind.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.cancel();
                        mLoginPresenter.onLoginSuccessAction(user);
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_PARAM_BIND_SUCCESS, true);
                        setResult(1,intent);
                        finish();
                    }
                }, 3000);

            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                dismissLoading();
                CommonUtils.toastMsg(errorMsg);
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

}
