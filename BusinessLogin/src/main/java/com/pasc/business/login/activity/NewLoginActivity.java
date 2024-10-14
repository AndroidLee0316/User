package com.pasc.business.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jakewharton.rxbinding2.view.RxView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pasc.business.login.LoginContract;
import com.pasc.business.login.LoginPresenter;
import com.pasc.business.login.R;
import com.pasc.business.login.util.UserPrivacyHelper;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.keyboard.KeyboardBaseView;
import com.pasc.lib.keyboard.KeyboardPopWindow;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.user.LoginConstant;
import com.pasc.business.login.pwd.PWDLoginContract;
import com.pasc.business.login.pwd.PWDLoginPresenter;
import com.pasc.business.login.sms.SmsLoginModel;
import com.pasc.business.login.third.ThirdLoginContract;
import com.pasc.business.login.third.ThirdLoginPresenter;
import com.pasc.business.login.util.LoginSuccessActionUtils;
import com.pasc.business.login.util.NavigationBarUtils;
import com.pasc.business.login.util.ThirdPartUtils;
import com.pasc.lib.base.permission.PermissionUtils;
import com.pasc.lib.base.user.IUserInfo;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.hybrid.PascHybrid;
import com.pasc.lib.hybrid.nativeability.WebStrategy;
import com.pasc.lib.imageloader.PascImageLoader;
import com.pasc.lib.loginbase.login.sms.SmsLoginContract;
import com.pasc.lib.loginbase.login.sms.SmsLoginPresenter;
import com.pasc.lib.loginbase.login.template.LoginEventHandler;
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
import com.pasc.lib.userbase.base.view.FormatEditText;
import com.pasc.lib.userbase.user.net.resp.GetUserInfoResp;
import com.pasc.lib.userbase.user.urlconfig.LoginUrlManager;
import com.pasc.lib.userbase.user.urlconfig.UserUrlConfig;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.userbase.user.util.UserManagerImpl;
import com.pasc.lib.widget.dialog.OnCloseListener;
import android.support.design.widget.BottomSheetDialog;

import com.pasc.lib.widget.dialog.OnConfirmListener;
import com.pasc.lib.widget.dialog.common.ConfirmDialogFragment;
import com.pasc.lib.widget.tdialog.TDialog;
import com.pasc.lib.widget.tdialog.base.BindViewHolder;
import com.pasc.lib.widget.tdialog.listener.OnBindViewListener;
import com.pasc.lib.widget.tdialog.listener.OnViewClickListener;
import com.pasc.lib.widget.toolbar.ClearEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

import static com.pasc.lib.statistics.PageType.APP;
import static com.pasc.lib.userbase.base.RouterTable.User.PATH_USER_ACCOUNT_CERTIFICATION_ACT;

/**
 * 登录新界面
 * Created by ruanwei489 on 2018/5/11.
 */
@Route(path = RouterTable.Login.PATH_LOGIN_ACTIVITY)
public class NewLoginActivity extends BaseStatusBarActivity implements View.OnClickListener,
        SmsLoginContract.View, LoginEventHandler<User>, ThirdLoginContract.View, LoginContract.View, PWDLoginContract.View {

    private Context mContext;
    public static int SMS_CODE_LEN = 6; //验证码默认6位
    private RelativeLayout rlRoot;
    protected RelativeLayout llTopContainer;
    protected RoundedImageView rivHeadLogo;//图像
    protected RelativeLayout mMiddleContainer;
    private FormatEditText cetPhoneNum;//手机号输入框
    private FormatEditText etVerifyCode;//验证码输入框
    private TextView tvGetVerifyCode;//获取验证码按钮
    private Button tvLoginButton;//登录按钮

    private LinearLayout container;

    private LinearLayout llVerifyLogin;//显示或隐藏短信登录界面入口
    private LinearLayout llPwdLogin;//显示或隐藏密码登录入口
    private LinearLayout llThirdLogin;//显示三方登录入口

    private FormatEditText etPwdPhoneNum;//密码登录账号
    private ClearEditText etPassword;
    private KeyboardPopWindow etPasswordKPW;
    protected TextView tvPwdOrVerifyLogin; //密码登录
    protected View splitView;//分割线
    private LinearLayout tvFaceLogin; //人脸登录
    private LinearLayout llAgreement;//服务协议
    private TextView tvAgreement; //服务协议

    protected CommonTitleView viewTitle;
    private boolean verifyShown = true;//是否短信验证,第一次显示

    private String mType;  //附加属性，如账号被踢的跳转
    protected String phoneNumber;

    private SmsLoginPresenter smsLoginPresenter;
    private ThirdLoginContract.Presenter thirdLoginPresenter;
    private PWDLoginContract.Presenter pwdLoginPresenter;
    private LoginContract.Presenter mLoginPresenter;
    private View phoneDivider, codeDivider;
    private View pwdDivider1, pwdDivider2;
    private boolean mHasPassword = true;
//    private boolean mHasOpenface = false;
    private boolean mUserExist = true;
    protected UserManagerImpl mUserManager;

    ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    private BottomSheetDialog bottomSheetDialog;
    private ConfirmDialogFragment certificationDialog;
    /**
     * 用户隐私协议助手
     */
    private UserPrivacyHelper userPrivacyHelper;


    /**
     * 跳转到绑定账号页面的回调
     */
    private static final int REQUEST_CODE_BIND = 100;

    public static void start(Context context) {
        Intent intent = new Intent(context, NewLoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBarView();
        mContext = this;
        setContentView(R.layout.login_activity_new_login);
        EventBus.getDefault().register(this);
        NavigationBarUtils.assistActivity(findViewById(android.R.id.content), findViewById(R.id.user_login_top_container_ll));
        StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_START,null,APP,null);
        mUserManager = UserManagerImpl.getInstance();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mType = bundle.getString(LoginConstant.LOGIN_TYPE);
        }
        thirdLoginPresenter = new ThirdLoginPresenter(this);
        pwdLoginPresenter = new PWDLoginPresenter(this);
        mLoginPresenter = new LoginPresenter(this);
        initView();
        setupLoginByThird();
        if (container.getChildCount() <= 0) {
            llThirdLogin.setVisibility(View.GONE);
        }
        if (LoginConstant.LOGIN_TYPE_KICKED.equals(mType)) {
            showUserKickedDialog(getString(R.string.login_user_kicked));
            setupLoginType(true);
        }else if ("USER_TOKEN_INVALID".equals(mType)) {
            showUserKickedDialog(getString(R.string.login_user_invalid));
            setupLoginType(true);
        } else if (LoginConstant.LOGIN_TYPE_SWITCH_ACCOUNT.equals(mType)) {
            setupLoginType(verifyShown);
        } else if (LoginConstant.LOGIN_TYPE_SMS.equals(mType)) {
            setupLoginType(true);
        } else if (LoginConstant.LOGIN_TYPE_PWD.equals(mType)) {
            setupLoginType(false);
        } else {
            boolean hasOpenface = mUserManager.isOpenFaceVerify();
            if (hasOpenface) {
                startLoginSwitchActivity();
                overridePendingTransition(R.anim.login_in_from_bottom, R.anim.login_no_anim);
                return;
            }
            setupLoginType(verifyShown);
        }

        viewTitle.setUnderLineVisible(false);
        //监听输入框的内容实现自动搜索
        cetPhoneNum.setEditTextChangeListener(new ClearEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                if (!verifyShown) {
                    return;
                }
                s = s.replace(" ", "");
                setLeftBtnNextClickable();
                if (s.length() == 11) {
                    if(smsLoginPresenter!=null && !smsLoginPresenter.isCouting()){
                        tvGetVerifyCode.setAlpha(1.0f);
                        tvGetVerifyCode.setEnabled(true);
                    }
                    setUserInfoByMobile(s);
                } else {

                    if (s.length()==10){
                        PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_portrait, rivHeadLogo);
                    }

                    tvGetVerifyCode.setAlpha(0.4f);
                    tvGetVerifyCode.setEnabled(false);
                    tvFaceLogin.setVisibility(View.GONE);
                }
                showHint(cetPhoneNum, s.length() == 0);
            }
        });

        etVerifyCode.setEditTextChangeListener(new ClearEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                setLeftBtnNextClickable();
                showHint(etVerifyCode, s.length() == 0);
            }
        });

        etPwdPhoneNum.setEditTextChangeListener(new ClearEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                if (verifyShown) {
                    return;
                }
                s = s.replace(" ", "");
                setRightBtnNextClickable();
                if (s.length() == 11) {
                    if(smsLoginPresenter!=null && !smsLoginPresenter.isCouting()){
                        tvGetVerifyCode.setAlpha(1.0f);
                        tvGetVerifyCode.setEnabled(true);
                    }

                    setUserInfoByMobile(s);
                } else {
                    if (s.length()==10){
                        PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_portrait, rivHeadLogo);
                    }
                    tvGetVerifyCode.setAlpha(0.4f);
                    tvGetVerifyCode.setEnabled(false);
                    tvFaceLogin.setVisibility(View.GONE);
                }

                showHint(etPwdPhoneNum, s.length() == 0);
            }
        });
        etVerifyCode.setInnerFocusChangeListener(new ClearEditText.InnerFocusChangeListener() {
            @Override
            public void onInnerFocusChange(View view, boolean b) {
                codeDivider.setBackgroundColor(b ? getResources().getColor(R.color.pasc_primary)
                        : getResources().getColor(R.color.gray_dddddd));
            }
        });
        cetPhoneNum.setInnerFocusChangeListener(new ClearEditText.InnerFocusChangeListener() {
            @Override
            public void onInnerFocusChange(View view, boolean b) {
                phoneDivider.setBackgroundColor(b ? getResources().getColor(R.color.pasc_primary)
                        : getResources().getColor(R.color.gray_dddddd));
            }
        });

        etPwdPhoneNum.setInnerFocusChangeListener(new ClearEditText.InnerFocusChangeListener() {
            @Override
            public void onInnerFocusChange(View view, boolean b) {
                pwdDivider1.setBackgroundColor(b ? getResources().getColor(R.color.pasc_primary)
                        : getResources().getColor(R.color.gray_dddddd));
            }
        });

        etPassword.setEditTextChangeListener(new ClearEditText.EditTextChangeListener() {
            @Override
            public void afterChange(String s) {
                setRightBtnNextClickable();
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && etPwdPhoneNum.getOriginalText().length() == 11) {
                    if (!mUserExist) {
                        showNoPWDDialog(getString(R.string.login_not_register), getString(R.string.login_not_register_tip));
                        etPwdPhoneNum.requestFocus();
                        return;
                    } else if (!mHasPassword) {
                        showNoPWDDialog(getString(R.string.login_no_pwd), getString(R.string.login_no_pwd_tip));
                        etPwdPhoneNum.requestFocus();
                        return;
                    }
                }
                pwdDivider2.setBackgroundColor(hasFocus ? getResources().getColor(R.color.pasc_primary)
                        : getResources().getColor(R.color.gray_dddddd));
                etPasswordKPW.onEditFocesChange(v,hasFocus);
            }
        });

        cetPhoneNum.setIconDismissListener(new ClearEditText.IconDismissListener() {
            @Override
            public void onIconClick() {
                etVerifyCode.setText("");
            }
        });

        etPwdPhoneNum.setIconDismissListener(new ClearEditText.IconDismissListener() {
            @Override
            public void onIconClick() {
                etPassword.setText("");
            }
        });

        etPasswordKPW = KeyboardPopWindow.bindEdit(this, etPassword, KeyboardBaseView.KeyboardNormalTheme.TYPE_NORMAL);

        initAccount();
    }


    /**
     * 这个函数在BusinessUser_platform中复写，请谨慎修改
     */
    protected void initStatusBarView(){

    }

    //动态添加三方登录入口
    private void setupLoginByThird() {
        LayoutInflater inflater = LayoutInflater.from(this);
        if (ThirdPartUtils.isWxAppInstalledAndSupported(NewLoginActivity.this)) {
            getThirdLogin(inflater, R.drawable.login_ic_login_by_wx, "微信", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_TYPE, getString(R.string.login_wechat),APP,null);
                    thirdLoginPresenter.wxLogin(mContext);
                }
            });
        }

        if (ThirdPartUtils.isQQAppInstalledAndSupported(this)) {
            getThirdLogin(inflater, R.drawable.login_ic_login_by_qq, "QQ", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_TYPE, getString(R.string.login_qq),APP,null);
                    thirdLoginPresenter.qqLogin(mContext);
                }
            });
        }


        if (ThirdPartUtils.isAlipayAppInstalledAndSupported(this)) {
            getThirdLogin(inflater, R.drawable.login_ic_login_by_alipay, "支付宝", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_TYPE, getString(R.string.login_alipay),APP,null);
                    thirdLoginPresenter.alipayLogin(mContext);
                }
            });
        }
    }

    private View getThirdLogin(LayoutInflater inflater, int resId, String title,
                               View.OnClickListener listener) {
        View childView = inflater.inflate(R.layout.login_item_login_third, container, false);
        ImageView iconView = childView.findViewById(R.id.user_icon);
        TextView titleView = childView.findViewById(R.id.user_title);
        View view = childView.findViewById(R.id.user_clickView);
        iconView.setImageResource(resId);
        titleView.setText(title);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) childView.getLayoutParams();
        params.width = 0;
        params.weight = 1;
        childView.setLayoutParams(params);

        container.addView(childView);
        view.setOnClickListener(listener);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefresh(BaseEvent event) {

        if (EventTag.USER_LOGIN_SUCCEED.equals(event.getTag())) {
            LoginSuccessActionUtils.getInstance().onLoginSuccessAction();
            finish();
        }
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
            FastBindAccountActivity.actionStartForResult(this, REQUEST_CODE_BIND, user);
        } else {
            updateLoginSuccess(user,getString(R.string.login_qq));
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
            FastBindAccountActivity.actionStartForResult(this, REQUEST_CODE_BIND, user);
        } else {
            updateLoginSuccess(user,getString(R.string.login_wechat));
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
        CommonUtils.toastMsg(msg);
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
            updateLoginSuccess(user,getString(R.string.login_alipay));
        }
    }

    @Override
    public void alipayLoginError(String code, String msg) {
        dismissLoading();
        StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_FAIL, getString(R.string.login_alipay),APP,null);
        CommonUtils.toastMsg(msg);
    }

    /**
     * 设置右边的按钮状态
     */
    private void setRightBtnNextClickable() {
        boolean account_has = (etPwdPhoneNum.getOriginalText().length() == 11);
        boolean pwd_has = etPassword.getText().toString().trim().length() > 0;
        boolean privacyEnable = userPrivacyHelper.isSelectPrivacy();
        if (account_has && pwd_has && privacyEnable) {
            tvLoginButton.setEnabled(true);
            tvLoginButton.setAlpha(1.0f);
        } else {
            tvLoginButton.setEnabled(false);
            tvLoginButton.setAlpha(0.3f);
        }
    }

    /**
     * 跳转到例如切换页面
     */
    protected void startLoginSwitchActivity() {
        User user = mUserManager.getInnerUser();
        Bundle bundle = new Bundle();
        bundle.putString(LoginSwitchActivity.TAG_PHONE, user.mobileNo);
        actionStart(LoginSwitchActivity.class, bundle);
        finish();
    }

    /**
     * @param isSms true: 短信验证码登录, false: 密码登录
     */
    private void setupLoginType(boolean isSms) {
        this.verifyShown = isSms;
        if (verifyShown) {
            llVerifyLogin.setVisibility(View.VISIBLE);
            llPwdLogin.setVisibility(View.GONE);
            tvPwdOrVerifyLogin.setText(getString(R.string.user_pwd_login));
            String phone = etPwdPhoneNum.getText().toString();
            cetPhoneNum.setText(phone);
            cetPhoneNum.setSelection(phone.length());
            etVerifyCode.setText("");
            initSmsLoginPresenter();
        } else {
            llVerifyLogin.setVisibility(View.GONE);
            llPwdLogin.setVisibility(View.VISIBLE);
            tvPwdOrVerifyLogin.setText(getString(R.string.user_sms_login));
            String num = cetPhoneNum.getText().toString();
            etPwdPhoneNum.setText(num);
            etPwdPhoneNum.setSelection(num.length());
            etPassword.setText("");
        }
    }

    private void initSmsLoginPresenter() {
        if (smsLoginPresenter == null) {
            smsLoginPresenter = new SmsLoginPresenter(
                    new SmsLoginModel(this), this, this);
        }
    }

    private void setLeftBtnNextClickable() {
        boolean account_has = (cetPhoneNum.getOriginalText().length() == 11);
        int pwd_len = etVerifyCode.getOriginalText().length();
        boolean pwd_has = (pwd_len == SMS_CODE_LEN);
        boolean privacyEnable = userPrivacyHelper.isSelectPrivacy();
//        if (account_has && pwd_len == SMS_CODE_LEN && userPrivacyHelper.isSelectPrivacy()) {
//            PascLog.e("call login");
//            loginOrRegisterByVerify(llVerifyLogin.isShown());
//        }
        if (account_has && pwd_has && privacyEnable) {
            tvLoginButton.setEnabled(true);
            tvLoginButton.setAlpha(1.0f);
        } else {
            tvLoginButton.setEnabled(false);
            tvLoginButton.setAlpha(0.3f);
        }
    }

    /**
     * 首次登陆  后续进入该页面会自动保存到输入框
     */
    private void initAccount() {
        IUserInfo user = mUserManager.getUserInfo();
        if (user != null) {
            cetPhoneNum.setText(user.getMobileNo());
            etPwdPhoneNum.setText(user.getMobileNo());
            int len = user.getMobileNo().length();
            cetPhoneNum.setSelection(cetPhoneNum.length());
            etPwdPhoneNum.setSelection(etPwdPhoneNum.length());
            if (len > 0) {
                tvGetVerifyCode.setAlpha(1.0f);
                tvGetVerifyCode.setEnabled(true);
            }
            setUserInfoByMobile(user.getMobileNo());
        }
    }

    private void initView() {
        rlRoot = findViewById(R.id.rl_root);
        llTopContainer = findViewById(R.id.user_login_top_container_ll);
        rivHeadLogo = (RoundedImageView) findViewById(R.id.user_riv_head_logo);//图像
        mMiddleContainer = findViewById(R.id.user_rl_top);
        cetPhoneNum = findViewById(R.id.user_et_user_phone_num);//手机号输入框
        cetPhoneNum.setFormatType(FormatEditText.TYPE_PHONE);
        etVerifyCode = findViewById(R.id.user_et_code);//验证码输入框
        etVerifyCode.setFormatType(FormatEditText.TYPE_SMS_CODE);
        tvGetVerifyCode = (TextView) findViewById(R.id.user_tv_get_verify_code);//获取验证码按钮
        tvLoginButton = findViewById(R.id.user_rtv_login_button);//登录按钮
        container = (LinearLayout) findViewById(R.id.user_login_container);
        llVerifyLogin = (LinearLayout) findViewById(R.id.user_ll_verify_login);//显示或隐藏短信登录界面入口
        llPwdLogin = (LinearLayout) findViewById(R.id.user_ll_pwd_login);//显示或隐藏密码登录入口
        llThirdLogin = findViewById(R.id.user_ll_third_login);
        etPwdPhoneNum = findViewById(R.id.user_et_pwd_phone_num);//密码登录账号
        etPwdPhoneNum.setFormatType(FormatEditText.TYPE_PHONE);
        etPassword = (ClearEditText) findViewById(R.id.user_et_password);
        tvPwdOrVerifyLogin = findViewById(R.id.user_tv_psd_or_verify_login); //密码登录
        splitView = findViewById(R.id.user_view);
        tvFaceLogin = (LinearLayout) findViewById(R.id.user_ll_face_login); //人脸登录
        llAgreement = findViewById(R.id.ll_bottom);
        tvAgreement = findViewById(R.id.user_tv_sz_server);
        tvAgreement.setText(LoginUrlManager.getInstance().getAgreementText());
        viewTitle = (CommonTitleView) findViewById(R.id.user_view_title);
        phoneDivider = findViewById(R.id.user_tv_divider);
        codeDivider = findViewById(R.id.user_code_divider);
        pwdDivider1 = findViewById(R.id.user_tv_pwd_divider1);
        pwdDivider2 = findViewById(R.id.user_tv_pwd_divider2);

        globalLayoutListener = mLoginPresenter.addLayoutListener(rlRoot, getBottomView());

        findViewById(R.id.user_tv_forget_password).setOnClickListener(this);

        tvLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOrRegisterByVerify(llVerifyLogin.isShown());
            }
        });

        RxView.clicks(tvFaceLogin)
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        checkPermission();
                    }
                });
        initTopView();


        if (LoginUrlManager.getInstance().getAgreementLocation() == UserUrlConfig.LoginConfigBean.AGREEMENT_LOCATION_BEHIND_PRIVACY){
            llAgreement.setVisibility(View.INVISIBLE);
        }

        userPrivacyHelper = new UserPrivacyHelper(rlRoot, new UserPrivacyHelper.CallBack() {
            @Override
            public void onSelectClick(boolean isSelect) {
                if (verifyShown){
                   setLeftBtnNextClickable();
                }else {
                   setRightBtnNextClickable();
                }
            }
        });


    }


    /**
     * 这个函数在BusinessUser_platform中复写，请谨慎修改
     */
    protected void initTopView(){
        viewTitle.setBackDrawableLeft(R.drawable.login_ic_close);
        viewTitle.setRightText(R.string.login_code_unreceived);
        viewTitle.setRightTextColor(getResources().getColor(R.color.gray_666666));
        viewTitle.setOnRightClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showDialog();
            }
        });
        viewTitle.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBack();
            }
        });
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.user_tv_get_verify_code) {
            etVerifyCode.setText("");
            String phone = cetPhoneNum.getOriginalText();
            smsLoginPresenter.fetchSmsVerityCode(phone);
        } else if (i == R.id.user_tv_psd_or_verify_login) {
            verifyShown = !verifyShown;
            setupLoginType(verifyShown);
        } else if (i == R.id.user_tv_sz_server) {
            PascHybrid.getInstance()
                    .start(this, new WebStrategy().setUrl(LoginUrlManager.getInstance().getAgreementUrl()));
        } else if (i == R.id.user_tv_forget_password) {
            String account = etPwdPhoneNum.getOriginalText();
            if (account.length() != 11) {
                onPhoneError(account);
                return;
            }
            if (!mUserExist) {
                showNoPWDDialog(getString(R.string.login_not_register), getString(R.string.login_not_register_tip));
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt(LoginConstant.PWD_TYPE, LoginConstant.PWD_TYPE_FORGET);
            bundle.putString(LoginConstant.PHONE_NUMBER, account);

            Intent intent = new Intent(this, ForgetPWDActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * 通过短信注册或者登录  或者密码登录
     */
    private void loginOrRegisterByVerify(boolean isShown) {
        if (isShown) {
            //短信登录
            StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_TYPE, getString(R.string.user_sms_login),APP,null);
            String account = cetPhoneNum.getOriginalText();
            smsLoginPresenter.login(account, etVerifyCode.getOriginalText());
        } else {
            String account = etPwdPhoneNum.getOriginalText();
            //密码登录
            if (!TextUtils.isEmpty(account) && account.length() == 11) {
                if (!CommonUtils.isPasswordLegal(etPassword.getText().toString())) {
                    CommonUtils.toastMsg(getString(R.string.user_pwd_format_error));
                    etPassword.setText("");
                }else {
                    pwdLoginPresenter.pwdLogin(account, etPassword.getText().toString());
                    StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_TYPE, getString(R.string.user_pwd_login),APP,null);
                }
            } else {
                onPhoneError(account);
            }
        }
    }

    private void showNoPWDDialog(String title, String content) {

        if (isFinishing()) {//解决部分机型崩溃问题
            return;
        }
        new TDialog.Builder(getSupportFragmentManager()).setLayoutRes(R.layout.login_dialog_login_no_pwd)
                .setScreenWidthAspect(NewLoginActivity.this, 0.8f)
                .addOnClickListener(R.id.user_tv_cancel, R.id.user_tv_sms_login)
                .setCancelableOutside(false)
                .setGravity(Gravity.CENTER)
                .setOnBindViewListener(new OnBindViewListener() {
                    @Override
                    public void bindView(BindViewHolder viewHolder) {
                        viewHolder.setText(R.id.user_tv_title, title);
                        viewHolder.setText(R.id.user_tv_content, content);
                    }
                })
                .setOnViewClickListener(new OnViewClickListener() {
                    @Override
                    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                        int i = view.getId();
                        if (i == R.id.user_tv_cancel) {
                            tDialog.dismiss();
                        } else if (i == R.id.user_tv_sms_login) {
                            setupLoginType(true);
                            tDialog.dismiss();
                        }
                    }
                })
                .create()
                .show();
    }

    private void showPWDLoginErrorManyTimesDialog() {

        if (isFinishing()) {//解决部分机型崩溃问题
            return;
        }
        new TDialog.Builder(getSupportFragmentManager()).setLayoutRes(R.layout.login_dialog_login_error_many_times)
                .setScreenWidthAspect(NewLoginActivity.this, 0.8f)
                .addOnClickListener(R.id.user_tv_cancel, R.id.user_tv_sms_login)
                .setCancelableOutside(false)
                .setGravity(Gravity.CENTER)
                .setOnBindViewListener(new OnBindViewListener() {
                    @Override
                    public void bindView(BindViewHolder viewHolder) {
                        viewHolder.setText(R.id.user_tv_title, getString(R.string.login_pwd_error_many_times_title));
                        viewHolder.setText(R.id.user_tv_content, getString(R.string.login_pwd_error_many_times_content));
                    }
                })
                .setOnViewClickListener(new OnViewClickListener() {
                    @Override
                    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                        int i = view.getId();
                        if (i == R.id.user_tv_cancel) {
                            tDialog.dismiss();
                        } else if (i == R.id.user_tv_sms_login) {
                            setupLoginType(true);
                            tDialog.dismiss();
                        }
                    }
                })
                .create()
                .show();
    }

    private void showPWDLoginErrorDialog(final String title, final String msg) {
        if (isFinishing()) {//解决部分机型崩溃问题
            return;
        }
        new TDialog.Builder(getSupportFragmentManager()).setLayoutRes(R.layout.login_dialog_not_login)
                .setScreenWidthAspect(NewLoginActivity.this, 0.8f)
                .addOnClickListener(R.id.user_sure)
                .setCancelableOutside(false)
                .setGravity(Gravity.CENTER)
                .setOnBindViewListener(new OnBindViewListener() {
                    @Override
                    public void bindView(BindViewHolder viewHolder) {
                        viewHolder.setText(R.id.user_tv_title, title).setText(R.id.user_tv_content, msg);
                    }
                })
                .setOnViewClickListener(new OnViewClickListener() {
                    @Override
                    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                        int i = view.getId();
                        if (i == R.id.user_sure) {
                            tDialog.dismiss();
                        }
                    }
                })
                .create()
                .show();
    }

    /**
     * 获取避免被键盘遮挡的按钮
     */
    private View getBottomView() {
        if (tvFaceLogin.getVisibility() == View.VISIBLE) {
            return tvFaceLogin;
        }
        if (tvPwdOrVerifyLogin.getVisibility() == View.VISIBLE) {
            return tvPwdOrVerifyLogin;
        }
        return tvLoginButton;
    }
    private ConfirmDialogFragment loginDialog;

    private void showUserKickedDialog(final String msg) {
        if (isFinishing()) {//解决部分机型崩溃问题
            return;
        }
        if (loginDialog == null){
            loginDialog = new ConfirmDialogFragment.Builder()
                    .setTitle(getResources().getString(R.string.login_user_invalid_title))
                    .setDesc(msg)
                    .setCloseText(getResources().getString(R.string.login_user_invalid_close))
                    .setCloseTextColor(getResources().getColor(R.color.pasc_primary))
                    .setHideConfirmButton(true)
                    .setOnCloseListener(new OnCloseListener<ConfirmDialogFragment>() {
                        @Override
                        public void onClose(ConfirmDialogFragment confirmDialogFragment) {
                            loginDialog.dismiss();
                        }
                    }).build();
        }
        loginDialog.show(getSupportFragmentManager(), "loginDialog");

    }

    @Override
    public void onPWDLoginSuccess(User user) {
        dismissLoading();
        updateLoginSuccess(user,getString(R.string.user_pwd_login));
    }
//    USER_MIMA_EXCEPTION_MIMA_WRONG_TIMES_MAX
//    USER_MIMA_EXCEPTION_USER_NOT_EXISTS
    @Override
    public void onPWDLoginError(String code, String msg) {
        dismissLoading();
        StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_FAIL, getString(R.string.user_pwd_login),APP,null);
        if ("USER_PASSWORD_EXCEPTION_PASSWORD_WRONG_TIMES_MAX".equals(code) || "USER_MIMA_EXCEPTION_MIMA_WRONG_TIMES_MAX".equals(code)) {
            showPWDLoginErrorManyTimesDialog();
        } else  if ("USER_MIMA_EXCEPTION_LOGIN_NAME_FORMAT_ERROR".equals(code) || "USER_MIMA_EXCEPTION_USER_NOT_EXISTS".equals(code) ) {
            CommonUtils.toastMsg("用户名或密码错误");
        }else {
            CommonUtils.toastMsg(msg);
        }
    }


    /**
     * 弹出登陆成功toast
     * 通知presenter 登陆成功
     * @param user
     */
    private void updateLoginSuccess(User user, String typeName) {
        mLoginPresenter.onLoginSuccessAction(user);
        LoginSuccessActionUtils.getInstance().onLoginSuccessAction();
        CommonUtils.toastMsg(getString(R.string.user_login_success));
        StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_SUCCESS, typeName,APP,null);
        finish();
    }

    private void checkPermission() {
        //人脸识别开启相机权限
        PermissionUtils.requestWithDialog(this, PermissionUtils.Groups.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            isOpenFace();
                        }
                    }
                });
    }

    //人脸识别登录
    private void isOpenFace() {
        phoneNumber = verifyShown ? cetPhoneNum.getOriginalText()
                : etPwdPhoneNum.getOriginalText();
        if (TextUtils.isEmpty(phoneNumber)) {
            CommonUtils.toastMsg(getString(R.string.login_input_account_tip));
            return;
        }
        postFaceDetector();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注释by lcb ：为什么要在这里重新获取一次呢？
        phoneNumber = verifyShown ? cetPhoneNum.getOriginalText()
                : etPwdPhoneNum.getOriginalText();
        if (phoneNumber.length() == 11) {
            setUserInfoByMobile(phoneNumber);
        }
    }

    /**
     * 根据手机号获取用户信息
     *
     * @param mobile 电话号码
     */
    private void setUserInfoByMobile(String mobile) {

        mobile = mobile.replace(" ", "");
        userPrivacyHelper.updateSelectPrivacy(false);

        IUserInfo user = mUserManager.getUserInfo();
        if (user != null  && !TextUtils.isEmpty(user.getMobileNo()) && user.getMobileNo().equals(mobile)){
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

            if (User.HAS_OPEN_FACE.equals(user.getHasOpenFace())){
                tvFaceLogin.setVisibility(View.VISIBLE);
            }

        }else {
            PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_portrait, rivHeadLogo);
        }
//        mLoginPresenter.getUserInfoByMobile(mobile, new CallBack<GetUserInfoResp>() {
//            @Override
//            public void onSuccess(GetUserInfoResp data) {
//
//                if (data != null) {
//                    mUserExist = true;
//                    if (!TextUtils.isEmpty(data.getHeadImg())) {
//                        PascImageLoader.getInstance().loadImageUrl(data.getHeadImg(), rivHeadLogo,
//                                R.drawable.login_ic_head_portrait, PascImageLoader.SCALE_DEFAULT);
//                    } else {
//                        if (GetUserInfoResp.SEX_MALE.equals(data.getSex())) {
//                            PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_male, rivHeadLogo);
//                        } else if (GetUserInfoResp.SEX_FEMALE.equals(data.getSex())) {
//                            PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_female, rivHeadLogo);
//                        } else {
//                            PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_portrait, rivHeadLogo);
//                        }
//                    }
//                    mHasPassword = "1".equals(data.getHasPassword());
//                    if (Constant.USER_FACE_OPEND.equals(data.getHasOpenface())) {
//                        mHasOpenface = true;
//                        tvFaceLogin.setVisibility(View.VISIBLE);
//                    } else {
//                        mHasOpenface = false;
//                        tvFaceLogin.setVisibility(View.GONE);
//                    }
//
//                    if (GetUserInfoResp.SIGN_PRIVATE_AGREEMENT_ALREADY.equals(data.getSignPrivateAgreement())){
//                        //已经同意了用户隐私协议
//                        //userPrivacyHelper.updateIsAleadyAgreePrivacy(true);
//                        //如果配置的是强制一定要手动选择同意服务协议，则不需要调用获取用户信息
//                        if (LoginUrlManager.getInstance().getServiceSelectType() == UserUrlConfig.LoginConfigBean.SERVICE_SELECT_FORCE_UNSELECT){
//
//                            return;
//                        }else {
//                            userPrivacyHelper.updateSelectPrivacy(true);
//                        }
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFail(String code, String msg) {
//                if (LoginPresenter.USER_ACCOUNT_NOT_IN.equals(code)) { //账号不存在
//                    mHasPassword = false;
//                    mUserExist = false;
//                    mHasOpenface = false;
//                }
//                //userPrivacyHelper.updateIsAleadyAgreePrivacy(false);
//                userPrivacyHelper.updateSelectPrivacy(false);
//                PascImageLoader.getInstance().loadImageRes(R.drawable.login_ic_head_portrait, rivHeadLogo);
//            }
//        });
    }

    private void postFaceDetector() {
        mUserManager.getInnerUser().setMobileNo(phoneNumber);
//        if (mHasOpenface) {
            gotoFaceLogin();
//        } else {
//            new TDialog.Builder(getSupportFragmentManager()).setLayoutRes(
//                    R.layout.login_dialog_no_face_info)
//                    .setScreenWidthAspect(NewLoginActivity.this, 0.8f)
//                    .addOnClickListener(R.id.user_iv_cancel, R.id.user_sure)
//                    .setCancelableOutside(false)
//                    .setOnViewClickListener(new OnViewClickListener() {
//                        @Override
//                        public void onViewClick(BindViewHolder viewHolder,
//                                                View view, TDialog tDialog) {
//                            int i = view.getId();
//                            if (i == R.id.user_iv_cancel) {
//                                tDialog.dismiss();
//                            } else if (i == R.id.user_sure) {
//                                tDialog.dismiss();
//                            }
//                        }
//                    })
//                    .create()
//                    .show();
//        }
    }

    /**
     * 跳转到人脸登陆
     */
    protected void gotoFaceLogin(){
        Bundle bundle = new Bundle();
        bundle.putString(Constant.MOBILE_NUMBER, phoneNumber);
        BaseJumper.jumpBundleARouter(RouterTable.Face.PATH_FACE_LOGIN_ACT, bundle);
        overridePendingTransition(R.anim.login_in_from_right, R.anim.login_no_anim);
    }

    @Override
    public void showFetchVerifyCodeSuccessUI() {
        toastMsg(getString(R.string.user_code_sent));
    }

    @Override
    public void showTickingUI(long l) {
        tvGetVerifyCode.setText(getString(R.string.user_resend_code_login, l));
        tvGetVerifyCode.setAlpha(0.4f);
        tvGetVerifyCode.setEnabled(false);
    }

    @Override
    public void showTickFinishUI() {
        tvGetVerifyCode.setText(getString(R.string.user_get_code_again));
        tvGetVerifyCode.setAlpha(1.0f);
        tvGetVerifyCode.setEnabled(true);
    }

    @Override
    public void showFetchingVerifyCodeLoading() {
        showLoading("");
    }

    @Override
    public void showFetchVerifyCodeFailUI(int i, String s) {
        toastMsg(s);
    }

    @Override
    public void onPhoneError(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            toastMsg(getString(R.string.user_input_phone_number));
        } else {
            toastMsg(getString(R.string.user_error_phone));
        }
    }

    @Override
    public void onVerifyCodeError(String code) {
        if (TextUtils.isEmpty(code)) {
            toastMsg(getString(R.string.user_input_sms_code));
        } else {
            StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_LOGIN_FAIL, getString(R.string.user_sms_login),APP,null);
            toastMsg(getString(R.string.user_verify_code_wrong));
        }
    }

    @Override
    public void hideLoading() {
        dismissLoading();
    }

    @Override
    public void showLoginLoading() {
        showLoading("");
    }

    @Override
    public void toastMsg(String s) {
        CommonUtils.toastMsg(s);
    }

    @Override
    public void handleLoginFailEvent(int code, String msg) {
        if (null != etVerifyCode) {
            //      etVerifyCode.setText("");
        }
        switch (code) {
            case 11017://未设置密码用户点击登录时
                CommonUtils.toastMsg(getString(R.string.login_no_pwd) + "\n" + getString(R.string.login_no_pwd_tip));
                break;
            case 201://账号未注册点击登录时

                etPassword.setText("");
                showPWDLoginErrorDialog(getString(R.string.login_no_account), msg);
                break;
            default:

                etPassword.setText("");
                CommonUtils.toastMsg(msg);
                break;
        }
    }

    @Override
    public void handleLoginSuccessEvent(User resp) {
        updateLoginSuccess(resp,getString(R.string.user_sms_login));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.login_no_anim, R.anim.login_out_to_bottom);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (smsLoginPresenter != null) {
            smsLoginPresenter.release();
        }
        if (rlRoot != null) {
            rlRoot.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
        }
    }

    @Override
    public void onBackPressed() {
        keyBack();
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {

    }

    private void showHint(EditText editText, boolean ifHint) {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, ifHint ? 16 : 18);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果绑定成功
        if (requestCode == REQUEST_CODE_BIND && data != null && data.getBooleanExtra(FastBindAccountActivity.RESULT_PARAM_BIND_SUCCESS,false)){
            finish();
        }
    }

    protected void keyBack(){
        LoginInterceptor.notifyCallBack(false);
        LoginSuccessActionUtils.getInstance().clearCallback();
        EventBusOutUtils.postLoginCancle();
        finish();
    }

    private void showDialog() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(this);
            View view = LayoutInflater.from(this).inflate(R.layout.login_dialog_mobile_functional, null);
            bottomSheetDialog.setContentView(view);
            view.findViewById(R.id.tv_functional).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    PascHybrid.getInstance().start(NewLoginActivity.this,LoginUrlManager.getInstance().getMobileFunctionalUrl());
                }
            });
            view.findViewById(R.id.tv_not_functional).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    showCertificationDialog();
                }
            });
            view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });
        }
        if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
    }
    private void showCertificationDialog(){
        if (isFinishing()) {//解决部分机型崩溃问题
            return;
        }
        if (certificationDialog == null) {
            certificationDialog = new ConfirmDialogFragment.Builder()
                    .setDesc(getString(R.string.login_certification_hint))
                    .setTitle(getString(R.string.login_certification_title))
                    .setConfirmText(getString(R.string.login_certificated))
                    .setConfirmTextColor(getResources().getColor(R.color.pasc_primary))
                    .setOnCloseListener(new OnCloseListener<ConfirmDialogFragment>() {
                        @Override
                        public void onClose(ConfirmDialogFragment confirmDialogFragment) {
                            certificationDialog.dismiss();
                        }
                    })
                    .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
                        @Override public void onConfirm(ConfirmDialogFragment confirmDialogFragment) {
                            certificationDialog.dismiss();
                            BaseJumper.jumpARouter(PATH_USER_ACCOUNT_CERTIFICATION_ACT);
                        }
                    }).build();
        }
        certificationDialog.show(getSupportFragmentManager(), "certificationDialog");
    }
}
