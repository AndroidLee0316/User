package com.pasc.business.user.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.face.data.FaceConstant;
import com.pasc.business.user.PascUserCancelAccountListener;
import com.pasc.business.user.PascUserChangePhoneNumListener;
import com.pasc.business.user.PascUserFaceListener;
import com.pasc.business.user.PascUserManager;
import com.pasc.business.user.PascUserManagerImpl;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.userbase.user.third.IThridLoginService;
import com.pasc.lib.userbase.user.LoginConstant;
import com.pasc.business.login.util.ThirdPartUtils;
import com.pasc.business.user.R;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.userbase.user.third.ThirdCallBack;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.statistics.StatisticsManager;
import com.pasc.lib.userbase.user.urlconfig.CertiUrlManager;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.widget.tdialog.TDialog;
import com.pasc.lib.widget.tdialog.base.BindViewHolder;
import com.pasc.lib.widget.tdialog.listener.OnBindViewListener;
import com.pasc.lib.widget.tdialog.listener.OnViewClickListener;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.net.param.IsOpenFaceParam;
import com.pasc.lib.userbase.user.net.resp.BaseRespObserver;
import com.pasc.lib.userbase.user.urlconfig.OtherConfigManager;
import com.pasc.lib.userbase.user.util.UserManagerImpl;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import static com.pasc.lib.statistics.PageType.APP;

/**
 * 账户安全
 * Created by ex-luyang001 on 2018/1/9.
 */
@Route(path = RouterTable.User.PATH_USER_ACCOUNT_SECURITY_ACT)
public class AccountSecurityActivity extends BaseStatusBarActivity implements View.OnClickListener {

    CommonTitleView titleBar;
    RelativeLayout rlCerti;
    RelativeLayout rl_alter_pwd;
    RelativeLayout faceLoginRl;

    View user_rl_certi_split;
    View user_rl_alter_pwd_split;
    View user_rl_face_setting_split;
    View user_rl_wx_item_split;
    View user_rl_qq_item_split;

    TextView tvThirdBindTitle;
    RelativeLayout rlWeChat;
    RelativeLayout rlQQItem;
    RelativeLayout rlAlipayItem;
    RelativeLayout changePhoneRl;
    RelativeLayout rlAcountCalce;
    RelativeLayout rlFingerprint;

    TextView tvPhone;
    TextView tv_isOpen;
    ImageView image;
    TextView tvCertiOpen;
    TextView tvSetupPwd;
    ImageView ivQQ;
    ImageView ivWeChat;
    ImageView ivAlipay;

    TextView wxNameView;
    TextView qqNameView;
    TextView alipayNameView;

    private Bundle bundle;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_account_security);
        rlCerti = findViewById(R.id.user_rl_certi);
        if (!OtherConfigManager.getInstance().getOtherConfigBean().needCertMenu) {
            rlCerti.setVisibility(View.GONE);
        }
        rl_alter_pwd = findViewById(R.id.user_rl_alter_pwd);
        faceLoginRl = findViewById(R.id.rl_user_face_register);
        user_rl_certi_split = findViewById(R.id.user_rl_certi_split);
        user_rl_alter_pwd_split = findViewById(R.id.user_rl_alter_pwd_split);
        user_rl_face_setting_split = findViewById(R.id.user_rl_alter_face_setting_split);
        user_rl_wx_item_split = findViewById(R.id.user_rl_wx_item_split);
        user_rl_qq_item_split = findViewById(R.id.user_rl_qq_item_split);
        changePhoneRl = findViewById(R.id.rl_user_phone_register);
        tvPhone = findViewById(R.id.user_tv_phone);
        rlAcountCalce = findViewById(R.id.rl_user_accout_calce);
        rlFingerprint = findViewById(R.id.rl_user_fingerprint_set);
        tvCertiOpen = findViewById(R.id.user_tv_certi_open);
        tv_isOpen = findViewById(R.id.user_tv_isOpen);
        image = findViewById(R.id.user_image);
        tvThirdBindTitle = findViewById(R.id.tv_third_bind_title);
        rlWeChat = findViewById(R.id.user_rl_wx_item);
        rlQQItem = findViewById(R.id.user_rl_qq_item);
        rlAlipayItem = findViewById(R.id.user_rl_alipay_item);
        tvSetupPwd = findViewById(R.id.user_tv_setup_pwd);
        ivQQ = findViewById(R.id.user_iv_qq);
        ivWeChat = findViewById(R.id.user_iv_wx);
        ivAlipay = findViewById(R.id.user_iv_alipay);
        titleBar = findViewById(R.id.user_ctv_title);
        wxNameView = findViewById(R.id.user_tv_wx_name);
        qqNameView = findViewById(R.id.user_tv_qq_name);
        alipayNameView = findViewById(R.id.user_tv_alipay_name);

        rlWeChat.setOnClickListener(this);
        rlQQItem.setOnClickListener(this);
        rlAlipayItem.setOnClickListener(this);
        titleBar.setOnLeftClickListener(this);
        rlCerti.setOnClickListener(this);
        rl_alter_pwd.setOnClickListener(this);
        faceLoginRl.setOnClickListener(this);
        rlAcountCalce.setOnClickListener(this);
        changePhoneRl.setOnClickListener(this);
        rlFingerprint.setOnClickListener(this);

        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    private void initView() {
        boolean bool1 = ThirdPartUtils.isWxAppInstalledAndSupported(this.getApplicationContext());
        boolean bool2 = ThirdPartUtils.isQQAppInstalledAndSupported(this);
        boolean bool3 = ThirdPartUtils.isAlipayAppInstalledAndSupported(this);
        rlWeChat.setVisibility(bool1 ? View.VISIBLE : View.GONE);
        user_rl_wx_item_split.setVisibility(bool1 ? View.VISIBLE : View.GONE);
        rlQQItem.setVisibility(bool2 ? View.VISIBLE : View.GONE);
        user_rl_qq_item_split.setVisibility(bool2 ? View.VISIBLE : View.GONE);
        rlAlipayItem.setVisibility(bool3 ? View.VISIBLE : View.GONE);
        if (!bool1 && !bool2 && !bool3) {
            tvThirdBindTitle.setVisibility(View.GONE);
        }

        if (OtherConfigManager.getInstance().getOtherConfigBean().needPasswordSetting) {
            rl_alter_pwd.setVisibility(View.VISIBLE);
        } else {
            rl_alter_pwd.setVisibility(View.GONE);
        }

        if (OtherConfigManager.getInstance().getOtherConfigBean().needFaceSetting) {
            faceLoginRl.setVisibility(View.VISIBLE);
        } else {
            faceLoginRl.setVisibility(View.GONE);
        }

        if (OtherConfigManager.getInstance().getOtherConfigBean().needChangePhoneNum) {
            changePhoneRl.setVisibility(View.VISIBLE);
        } else {
            changePhoneRl.setVisibility(View.GONE);
            user_rl_face_setting_split.setVisibility(View.GONE);
        }

        if (OtherConfigManager.getInstance().getOtherConfigBean().needAccountCancel) {
            rlAcountCalce.setVisibility(View.VISIBLE);
        } else {
            rlAcountCalce.setVisibility(View.GONE);
        }

        if (OtherConfigManager.getInstance().getOtherConfigBean().needFingerprint) {
            rlFingerprint.setVisibility(View.VISIBLE);
        } else {
            rlFingerprint.setVisibility(View.GONE);
        }



        //如果人脸设置或者密码设置隐藏，需要跟认证合并中间无空
        if (!OtherConfigManager.getInstance().getOtherConfigBean().needFaceSetting || !OtherConfigManager.getInstance().getOtherConfigBean().needPasswordSetting) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlCerti.getLayoutParams();
            params.bottomMargin = 0;
            rlCerti.setLayoutParams(params);
            if (!OtherConfigManager.getInstance().getOtherConfigBean().needFaceSetting
                    && !OtherConfigManager.getInstance().getOtherConfigBean().needPasswordSetting
                    && !OtherConfigManager.getInstance().getOtherConfigBean().needChangePhoneNum){
                //如果只留下认证，则隐藏分割线
                user_rl_certi_split.setVisibility(View.GONE);
            } else {
                user_rl_certi_split.setVisibility(View.VISIBLE);
            }

        }

    }

    private void initData() {
        mUser = UserManagerImpl.getInstance().getInnerUser();
        if (mUser != null) {
            if (mUser.mobileNo != null && mUser.mobileNo.length() == 11) {
                tvPhone.setText(mUser.mobileNo.substring(0,3) + "******" +mUser.mobileNo.substring(mUser.mobileNo.length()-2));
            }

            if (PascUserManager.getInstance().hasPassword()) {
                tvSetupPwd.setText("重置/找回");
                tvSetupPwd.setTextColor(getResources().getColor(R.color.black_2e332f));
            } else {
                tvSetupPwd.setText("未设置");
                tvSetupPwd.setTextColor(getResources().getColor(R.color.gray_c7c7c7));
            }

            ivWeChat.setImageDrawable(getResources().getDrawable(
                    isWXBind() ? R.drawable.user_ic_security_wechat : R.drawable.user_ic_security_wechat_gray));

            ivQQ.setImageDrawable(getResources().getDrawable(
                    isQQBind() ? R.drawable.user_ic_security_qq : R.drawable.user_ic_security_qq_gray));

            ivAlipay.setImageDrawable(getResources().getDrawable(
                    isAlipayBind() ? R.drawable.user_ic_security_alipay : R.drawable.user_ic_security_alipay_gray));

            wxNameView.setText(mUser.wxName);
            qqNameView.setText(mUser.qqName);
            alipayNameView.setText(mUser.alipayName);

            updateFaceStatus();

        } else {
            changePhoneRl.setVisibility(View.GONE);
            rlAcountCalce.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(BaseEvent event) {
        if (EventTag.USER_UPDATE_MSG_SUCCESS.equals(event.getTag())) {
            initData();
        } else if ("user_login_status".equals(event.getTag())) {

            if (event.getParams() != null) {
                String tag = event.getParams().get("status");
                if ("user_login_status_out_value".equals(tag)) {
                    finish();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hasCerti();
    }


    @Override
    public void onClick(View view) {
        bundle = new Bundle();
        int i = view.getId();
        if (i == R.id.iv_title_left) {
            finish();
        } else if (i == R.id.user_rl_certi) {
            if (CertiUrlManager.getInstance().isCertFaceNewWay()) {
                BaseJumper.jumpARouter(RouterTable.Cert.PATH_CERT_AUTH_TYPE_ACT_NEW);
            } else {
                BaseJumper.jumpARouter(RouterTable.Cert.PATH_CERT_AUTH_TYPE_ACT);
            }
        } else if (i == R.id.user_rl_alter_pwd) {
            PascUserManager.getInstance().toPasswordSetOrUpdate();
        } else if (i == R.id.rl_user_face_register) {
            gotoFaceRegister();
        } else if (i == R.id.user_rl_wx_item) {
            if (isWXBind()) {//微信解绑
                showUnbindDialog("确定要解绑微信？", "解绑微信后将无法继续使用它登录" + CommonUtils.getAppName(this),
                        LoginConstant.LOGIN_TYPE_WX);
            } else {//微信绑定

                Map<String, String> map = new HashMap();
                map.put("account_name", "微信");
                StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_SECURITY, "点击绑定", APP, map);
                authorizeThird(Constant.LOGIN_TYPE_WX);
            }
        } else if (i == R.id.user_rl_qq_item) {
            if (isQQBind()) {//QQ解绑
                showUnbindDialog("确定要解绑QQ？", "解绑QQ后将无法继续使用它登录" + CommonUtils.getAppName(this),
                        LoginConstant.LOGIN_TYPE_QQ);
            } else {//QQ绑定
                Map<String, String> map = new HashMap();
                map.put("account_name", "QQ");
                StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_SECURITY, "点击绑定", APP, map);
                authorizeThird(Constant.LOGIN_TYPE_QQ);
            }
        } else if (i == R.id.user_rl_alipay_item) {
            if (isAlipayBind()) {//支付宝解绑
                showUnbindDialog("确定要解绑支付宝？", "解绑支付宝后将无法继续使用它登录" + CommonUtils.getAppName(this),
                        LoginConstant.LOGIN_TYPE_ALIPAY);
            } else {//支付宝绑定
                Map<String, String> map = new HashMap();
                map.put("account_name", "alipay");
                StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_SECURITY, "点击绑定", APP, map);
                authorizeThird(Constant.LOGIN_TYPE_ALIPAY);
            }

        } else if (i == R.id.rl_user_accout_calce) {
            PascUserManager.getInstance().toCancelAccount(new PascUserCancelAccountListener() {
                @Override
                public void onSuccess() {

                    BaseEvent event = new BaseEvent("user_login_status");
                    event.put("status", "user_login_status_out_value");
                    EventBus.getDefault().post(event);

                    BaseJumper.jumpARouter("/app/main/home");
                    finish();
                }

                @Override
                public void onFailed() {

                }

                @Override
                public void onCanceld() {
                }
            });
        } else if (i == R.id.rl_user_phone_register) {
            PascUserManager.getInstance().toChangePhoneNum(new PascUserChangePhoneNumListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailed() {

                }

                @Override
                public void onCanceld() {

                }
            });
        } else if (i == R.id.rl_user_fingerprint_set) {
            //跳转到手势设置
            BaseJumper.jumpARouter("/fingerprint/setting");
        }
    }


    /**
     * 跳转到人脸注册页面
     */
    protected void gotoFaceRegister() {
        PascUserManager.getInstance().toFaceSetting(new PascUserFaceListener() {
            @Override
            public void onRegisterSuccess() {
                updateFaceStatus();
            }

            @Override
            public void onRegisterCancled() {
                updateFaceStatus();
            }

            @Override
            public void onResetSuccess() {
                updateFaceStatus();
            }

            @Override
            public void onResetCancled() {
                updateFaceStatus();
            }

            @Override
            public void onSetFaceResult(boolean isFaceOpen) {
                updateFaceStatus();
            }
        });
    }

    /**
     * 刷新人脸状态
     */
    private void updateFaceStatus(){
        boolean hasOpenFace = UserManagerImpl.getInstance().isOpenFaceVerify();
        if (hasOpenFace) {//开通
            tv_isOpen.setText("重置/关闭");
            tv_isOpen.setTextColor(
                    getResources().getColor(R.color.black_2e332f));
        }else  {
            tv_isOpen.setText("未开启");
            tv_isOpen.setTextColor(
                    getResources().getColor(R.color.gray_c7c7c7));
        }
    }

    private void showUnbindDialog(final String title, final String msg, final String loginType) {
        if (isFinishing()) {//解决部分机型崩溃问题
            return;
        }
        //取消toast弹框
        ToastUtils.cancel();
        new TDialog.Builder(getSupportFragmentManager()).setLayoutRes(R.layout.user_dialog_unbind)
                .setScreenWidthAspect(AccountSecurityActivity.this, 0.8f)
                .addOnClickListener(R.id.user_tv_cancel, R.id.user_tv_sure)
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
                        if (i == R.id.user_tv_sure) {
                            unbindThird(loginType);
                            tDialog.dismiss();
                        } else if (i == R.id.user_tv_cancel) {
                            tDialog.dismiss();
                        }
                    }
                })
                .create()
                .show();
    }

    /**
     * 绑定
     */
    private void authorizeThird(String loginType) {
        if (LoginConstant.LOGIN_TYPE_WX.equals(loginType)) {//微信

            bindThird(LoginConstant.LOGIN_TYPE_WX);

        } else if (LoginConstant.LOGIN_TYPE_QQ.equals(loginType)) {//QQ

            bindThird(LoginConstant.LOGIN_TYPE_QQ);
        } else if (LoginConstant.LOGIN_TYPE_ALIPAY.equals(loginType)) {
            bindThird(LoginConstant.LOGIN_TYPE_ALIPAY);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefresh(BaseEvent event) {
//        String status = event.getParams().get(EventKey.USER_THIRD_STATUS);
//        String code = event.getParams().get(EventKey.USER_WX_CODE);
//        if (status == EventKey.WX_Authorize_Success) {//绑定微信
//            bindThird("", "", code, LoginConstant.LOGIN_TYPE_WX);
//        }
    }


    /**
     * 用户设置密码成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCertification(BaseEvent event) {
        if (EventTag.USER_RESET_PASS_SUCCEED.equals(event.getTag())) {
            UserManagerImpl.getInstance().getInnerUser().hasPassword = User.HAS_PASSWORD;
            UserManagerImpl.getInstance().getInnerUser().update();
            tvSetupPwd.setText("重置/找回");
            tvSetupPwd.setTextColor(getResources().getColor(R.color.black_2e332f));
        }
    }

    /**
     * 绑定账号
     */
    private void bindThird(String loginType) {
        //取消toast弹框
        ToastUtils.cancel();
        showLoading();
        HashMap<String, String> param = new HashMap<>();
        param.put("mobileNo", mUser.getMobileNo());
        param.put("isLogin", "1");
        switch (loginType) {
            case LoginConstant.LOGIN_TYPE_WX:

                IThridLoginService wxLoginService = BaseJumper.getService(RouterTable.Login_wx.PATH_LOGIN_ACTIVITY);
                if (wxLoginService != null) {
                    wxLoginService.bind(this, param, new ThirdCallBack.IAuthorizeCallBack() {
                        @Override
                        public void authorizeFailed(String errorCode, String message) {
                            dismissLoading();
                            ToastUtils.toastMsg(message);
                        }

                        @Override
                        public void authorizeSuccess(String openID, String authCode) {

                        }
                    }, new ThirdCallBack.IBindThirdCallBack() {
                        @Override
                        public void onSuccess(User user) {
                            bindSuccess(user);
                        }

                        @Override
                        public void onError(String errorCode, String errorMsg) {
                            dismissLoading();
                            ToastUtils.toastMsg(errorMsg);
                        }
                    });
                }

                break;
            case LoginConstant.LOGIN_TYPE_QQ:

                IThridLoginService qqLoginService = BaseJumper.getService(RouterTable.Login_qq.PATH_LOGIN_ACTIVITY);
                if (qqLoginService != null) {
                    qqLoginService.bind(this, param, new ThirdCallBack.IAuthorizeCallBack() {
                        @Override
                        public void authorizeFailed(String errorCode, String message) {
                            dismissLoading();
                            ToastUtils.toastMsg(message);
                        }

                        @Override
                        public void authorizeSuccess(String openID, String authCode) {

                        }
                    }, new ThirdCallBack.IBindThirdCallBack() {
                        @Override
                        public void onSuccess(User user) {

                            bindSuccess(user);
                        }

                        @Override
                        public void onError(String errorCode, String errorMsg) {
                            dismissLoading();
                            ToastUtils.toastMsg(errorMsg);
                        }
                    });
                }


                break;
            case LoginConstant.LOGIN_TYPE_ALIPAY:

                IThridLoginService alipayLoginService = BaseJumper.getService(RouterTable.Login_alipay.PATH_LOGIN_ACTIVITY);
                if (alipayLoginService != null) {
                    alipayLoginService.bind(this, param, new ThirdCallBack.IAuthorizeCallBack() {
                        @Override
                        public void authorizeFailed(String errorCode, String message) {
                            dismissLoading();
                            ToastUtils.toastMsg(message);
                        }

                        @Override
                        public void authorizeSuccess(String openID, String authCode) {

                        }
                    }, new ThirdCallBack.IBindThirdCallBack() {
                        @Override
                        public void onSuccess(User user) {
                            bindSuccess(user);
                        }

                        @Override
                        public void onError(String errorCode, String errorMsg) {
                            dismissLoading();
                            ToastUtils.toastMsg(errorMsg);
                        }
                    });
                }
                break;
        }

    }

    private void bindSuccess(User user) {
        dismissLoading();
        ToastUtils.toastMsg(R.string.user_bind_success);
        mUser = user;
        UserManagerImpl.getInstance().updateUser(mUser);
        EventBusOutUtils.postUpdateUserInfo();
    }

    /**
     * 解绑
     */
    public void unbindThird(String loginType) {

        showLoading();
        switch (loginType) {
            case LoginConstant.LOGIN_TYPE_WX:

                IThridLoginService wxLoginService = BaseJumper.getService(RouterTable.Login_wx.PATH_LOGIN_ACTIVITY);
                if (wxLoginService != null) {
                    wxLoginService.unBind(this, null, new ThirdCallBack.IUnBindThirdCallBack() {

                        @Override
                        public void onSuccess() {
                            dismissLoading();
                            ToastUtils.toastMsg(R.string.user_unbind_success);
                            Map<String, String> map = new HashMap();
                            map.put("account_name", "微信");
                            StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_SECURITY, "解绑成功", APP, map);

                            //删除更新用户类中的绑定信息
                            if (mUser.getBindThirds() != null && mUser.getBindThirds().size() > 0) {
                                for (int i = 0; i < mUser.getBindThirds().size(); i++) {
                                    if (User.THIRD_LOGIN_WX.equals(mUser.getBindThirds().get(i))) {
                                        mUser.getBindThirds().remove(i);
                                        break;
                                    }
                                }
                            }
                            mUser.wxName = "";

                            UserManagerImpl.getInstance().updateUser(mUser);
                            EventBusOutUtils.postUpdateUserInfo();
                        }

                        @Override
                        public void onError(String errorCode, String errorMsg) {
                            dismissLoading();
                            ToastUtils.toastMsg(errorMsg);
                        }
                    });
                }

                break;
            case LoginConstant.LOGIN_TYPE_QQ:

                IThridLoginService qqLoginService = BaseJumper.getService(RouterTable.Login_qq.PATH_LOGIN_ACTIVITY);
                if (qqLoginService != null) {
                    qqLoginService.unBind(this, null, new ThirdCallBack.IUnBindThirdCallBack() {

                        @Override
                        public void onSuccess() {
                            dismissLoading();
                            ToastUtils.toastMsg(R.string.user_unbind_success);
                            Map<String, String> map = new HashMap();
                            map.put("account_name", "QQ");
                            StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_SECURITY, "解绑成功", APP, map);

                            //删除更新用户类中的绑定信息
                            if (mUser.getBindThirds() != null && mUser.getBindThirds().size() > 0) {
                                for (int i = 0; i < mUser.getBindThirds().size(); i++) {
                                    if (User.THIRD_LOGIN_QQ.equals(mUser.getBindThirds().get(i))) {
                                        mUser.getBindThirds().remove(i);
                                        break;
                                    }
                                }
                            }
                            mUser.qqName = "";


                            UserManagerImpl.getInstance().updateUser(mUser);
                            EventBusOutUtils.postUpdateUserInfo();
                        }

                        @Override
                        public void onError(String errorCode, String errorMsg) {
                            dismissLoading();
                            ToastUtils.toastMsg(errorMsg);
                        }
                    });
                }


                break;
            case LoginConstant.LOGIN_TYPE_ALIPAY:

                IThridLoginService alipayLoginService = BaseJumper.getService(RouterTable.Login_alipay.PATH_LOGIN_ACTIVITY);
                if (alipayLoginService != null) {
                    alipayLoginService.unBind(this, null, new ThirdCallBack.IUnBindThirdCallBack() {

                        @Override
                        public void onSuccess() {
                            dismissLoading();
                            ToastUtils.toastMsg(R.string.user_unbind_success);
                            Map<String, String> map = new HashMap();
                            map.put("account_name", "支付宝");
                            StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_SECURITY, "解绑成功", APP, map);

                            //删除更新用户类中的绑定信息
                            if (mUser.getBindThirds() != null && mUser.getBindThirds().size() > 0) {
                                for (int i = 0; i < mUser.getBindThirds().size(); i++) {
                                    if (User.THIRD_LOGIN_ALIPAY.equals(mUser.getBindThirds().get(i))) {
                                        mUser.getBindThirds().remove(i);
                                        break;
                                    }
                                }
                            }
                            mUser.alipayName = "";

                            UserManagerImpl.getInstance().updateUser(mUser);
                            EventBusOutUtils.postUpdateUserInfo();
                        }

                        @Override
                        public void onError(String errorCode, String errorMsg) {
                            dismissLoading();
                            ToastUtils.toastMsg(errorMsg);
                        }
                    });
                }
                break;
        }

    }

    private void hasCerti() {
        User user = UserManagerImpl.getInstance().getInnerUser();
        if (user == null) {
            return;
        }
        if (TextUtils.isEmpty(user.getCertiType())) {
            tvCertiOpen.setText("未认证");
            tvCertiOpen.setTextColor(getResources().getColor(R.color.gray_c7c7c7));
        } else {
            tvCertiOpen.setText("已实名");
            tvCertiOpen.setTextColor(getResources().getColor(R.color.black_2e332f));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {

    }

    /**
     * 是否绑定了微信
     *
     * @return
     */
    private boolean isWXBind() {
        if (mUser.getBindThirds() != null && mUser.getBindThirds().size() > 0) {
            for (String bindThird : mUser.getBindThirds()) {
                if (User.THIRD_LOGIN_WX.equals(bindThird)) {
                    return true;
                }
            }
        }
        return false;
//        return Constant.THIRD_BIND_WX_QQ.equals(mUser.isBindThird)
//                || Constant.THIRD_BIND_WX.equals(mUser.isBindThird);
    }

    /**
     * 是否绑定了QQ
     *
     * @return
     */
    private boolean isQQBind() {
        if (mUser.getBindThirds() != null && mUser.getBindThirds().size() > 0) {
            for (String bindThird : mUser.getBindThirds()) {
                if (User.THIRD_LOGIN_QQ.equals(bindThird)) {
                    return true;
                }
            }
        }
        return false;
//        return Constant.THIRD_BIND_WX_QQ.equals(mUser.isBindThird)
//                || Constant.THIRD_BIND_QQ.equals(mUser.isBindThird);
    }

    private boolean isAlipayBind() {
        if (mUser.getBindThirds() != null && mUser.getBindThirds().size() > 0) {
            for (String bindThird : mUser.getBindThirds()) {
                if (User.THIRD_LOGIN_ALIPAY.equals(bindThird)) {
                    return true;
                }
            }
        }
        return false;
//        return Constant.THIRD_BIND_Alipay.equals(mUser.isBindThird);
    }


}
