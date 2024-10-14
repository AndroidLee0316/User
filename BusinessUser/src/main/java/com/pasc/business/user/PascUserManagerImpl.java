package com.pasc.business.user;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.pasc.business.cert.activity.IChooseCertificationActivity;
import com.pasc.business.cert.config.CertOutConfigManager;
import com.pasc.business.face.FaceCheckManager;
import com.pasc.business.face.net.resp.FaceInitResp;
import com.pasc.business.user.cert.CertConfig;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.base.widget.LoadingDialog;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.user.LoginConstant;
import com.pasc.lib.userbase.user.UserProxy;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.net.resp.BaseRespV2Observer;
import com.pasc.lib.userbase.user.urlconfig.CertiUrlManager;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.userbase.user.util.UserManagerImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/22
 */
public class PascUserManagerImpl implements PascUserManagerInter {

    private static final String USER_INFO_NIKE_NAME = "nickName";

    private static final String USER_INFO_SEX = "sex";

    protected Context mContext;

    protected UserManagerImpl mUserManagerImpl;

    /**
     * 认证监听
     */
    protected PascUserCertListener mPascUserCertListener;
    /**
     * 登陆监听
     */
    protected PascUserLoginListener mPascUserLoginListener;
    /**
     * 人脸监听
     */
    protected PascUserFaceListener mPascUserFaceListener;
    /**
     * 用户信息更新监听
     */
    protected PascUserUpdateListener mPascUserUpdateListener;

    /**
     * 认证监听(只会存在一次，当接收到回调只会就会置为空)
     */
    protected PascUserCertListener mTmpCertListener;
    /**
     * 登陆监听(只会存在一次，当接收到回调只会就会置为空)
     */
    protected PascUserLoginListener mTmpLoginListener;
    /**
     * 人脸监听(只会存在一次，当接收到回调只会就会置为空)
     */
    protected PascUserFaceListener mTmpFaceListener;
    /**
     * 用户信息更新监听(只会存在一次，当接收到回调只会就会置为空)
     */
    protected PascUserUpdateListener mTmpUpdateListener;
    /**
     * 人脸二次核验(只会存在一次，当接收到回调只会就会置为空)
     */
    protected PascUserFaceCheckListener mTmpFaceCheckListener;

    /**
     * 账号注销(只会存在一次，当接收到回调只会就会置为空)
     */
    protected PascUserCancelAccountListener mTmpCancelAccountListener;

    /**
     * 手机号更换(只会存在一次，当接收到回调只会就会置为空)
     */
    protected PascUserChangePhoneNumListener mTmpChangePhoneNumListener;

    public PascUserManagerImpl() {

    }

    @Override
    public void init(Context context, PascUserConfig config) {
        mContext = context;
        mUserManagerImpl = (UserManagerImpl) UserManagerImpl.getInstance().init(context, null);
        mUserManagerImpl.initUserConfig(R.raw.pasc_user_config);
        AppProxy.getInstance().setUserManager(mUserManagerImpl);
        initEventBus();
    }


    /**
     * 设置监听回调
     *
     * @param callBack
     */
    @Override
    public void setCertListener(PascUserCertListener callBack) {
        mPascUserCertListener = callBack;
    }

    @Override
    public void setLoginListener(PascUserLoginListener callBack) {
        mPascUserLoginListener = callBack;
    }


    /**
     * 设置监听回调
     *
     * @param callBack
     */
    @Override
    public void setFaceListener(PascUserFaceListener callBack) {
        mPascUserFaceListener = callBack;
    }

    @Override
    public void setUserInfoUpdateListener(PascUserUpdateListener callBack) {
        mPascUserUpdateListener = callBack;
    }

    /**
     * 获取用户信息
     *
     * @param key 用户信息@{PascUserConfig.USER_INFO_KEY}
     * @return
     */
    @Override
    public String getUserInfo(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        switch (key) {
            case PascUserConfig.USER_INFO_KEY_USER_ID:
                return mUserManagerImpl.getUserId();
            case PascUserConfig.USER_INFO_KEY_USER_NAME:
                return mUserManagerImpl.getUserName();
            case PascUserConfig.USER_INFO_KEY_TOKEN:
                return mUserManagerImpl.getToken();
            case PascUserConfig.USER_INFO_KEY_PHONE:
                return mUserManagerImpl.getMobile();
            case PascUserConfig.USER_INFO_KEY_BIRTHDAY:
                return mUserManagerImpl.getBirthday();
            case PascUserConfig.USER_INFO_KEY_ADDRESS:
                return mUserManagerImpl.getAddress();
            case PascUserConfig.USER_INFO_KEY_HEAD_IMG:
                return mUserManagerImpl.getHeadImg();
            case PascUserConfig.USER_INFO_KEY_IS_LOGIN:
                return String.valueOf(mUserManagerImpl.isLogin());
            case PascUserConfig.USER_INFO_KEY_CERTIFICATION_TYPE:
                return mUserManagerImpl.getCertifyType();
            case PascUserConfig.USER_INFO_KEY_PAY_ACCOUNT_ID:
                return mUserManagerImpl.getExtraInfo(PascUserConfig.USER_INFO_KEY_PAY_ACCOUNT_ID);
            case USER_INFO_NIKE_NAME:
                return mUserManagerImpl.getNickName();
            case USER_INFO_SEX:
                return mUserManagerImpl.getUserInfo().getSex();
            default:
                return null;
        }
    }

    @Override
    public boolean isLogin() {

        return mUserManagerImpl.isLogin();
    }

    @Override
    public boolean isCertification() {
        return mUserManagerImpl.isCertified();
    }

    @Override
    public boolean hasPassword() {
        User user = mUserManagerImpl.getInnerUser();
        if (user != null && User.HAS_PASSWORD.equals(user.hasPassword)){
            return true;
        }
        return false;
    }

    @Override
    public boolean loginOut() {
        return mUserManagerImpl.exitUser(mContext);
    }

    @Override
    public void loginOut(PascUserLoginOutListener callBack) {

        boolean success = mUserManagerImpl.exitUser(mContext);
        if (callBack != null) {
            if (success) {
                callBack.onLoginOutSuccess();
            } else {
                callBack.onLoginOutFailed();
            }
        }
    }

    /**
     * 跳转到账户中心
     */
    @Override
    public void toAccount() {
        if (UserManagerImpl.getInstance().isLogin()){
            BaseJumper.jumpARouter(RouterTable.User.PATH_USER_ACCOUNT_SECURITY_ACT);
        }else {
            Log.e(PascUserManagerImpl.class.getSimpleName(),"toAccount failed, no login");
        }
    }


    /**
     * 认证
     *
     * @param certificationType 认证类型
     */
    @Override
    public void toCertification(int certificationType, PascUserCertListener callBack) {
        mTmpCertListener = callBack;
        if (UserManagerImpl.getInstance().isLogin()){
            switch (certificationType) {
                case PascUserConfig.CERTIFICATION_TYPE_ALL:
                    if (CertiUrlManager.getInstance().isCertFaceNewWay()) {
                        BaseJumper.jumpARouter(RouterTable.Cert.PATH_CERT_AUTH_TYPE_ACT_NEW);
                    } else {
                        BaseJumper.jumpARouter(RouterTable.Cert.PATH_CERT_AUTH_TYPE_ACT);
                    }
                    break;
                case PascUserConfig.CERTIFICATION_TYPE_ALL_AND_FINISH_WHEN_SUCCESS:
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(IChooseCertificationActivity.EXTRA_PARAM_FINISH_WHEN_CERT_SUCCESS, true);
                    if (CertiUrlManager.getInstance().isCertFaceNewWay()) {
                        BaseJumper.jumpBundleARouter(RouterTable.Cert.PATH_CERT_AUTH_TYPE_ACT_NEW, bundle);
                    } else {
                        BaseJumper.jumpBundleARouter(RouterTable.Cert.PATH_CERT_AUTH_TYPE_ACT, bundle);
                    }
                    break;
                case PascUserConfig.CERTIFICATION_TYPE_BANK:
                    BaseJumper.jumpARouter(RouterTable.Cert.PATH_CERT_AUTH_TYPE_ACT_BANK);
                    break;
                case PascUserConfig.CERTIFICATION_TYPE_FACE_PA:
                    if (UserManagerImpl.getInstance().getUserInfo() != null && !TextUtils.isEmpty(UserManagerImpl.getInstance().getUserInfo().getIdCard()) && !TextUtils.isEmpty(UserManagerImpl.getInstance().getUserInfo().getUserName())) {
                        Bundle paFaceBundle = new Bundle();
                        paFaceBundle.putString("IDcard", UserManagerImpl.getInstance().getUserInfo().getIdCard());
                        paFaceBundle.putString("name", UserManagerImpl.getInstance().getUserInfo().getUserName());
                        BaseJumper.jumpBundleARouter(RouterTable.Face.PATH_FACE_COMPARE_ACT, paFaceBundle);
                    } else {
                        BaseJumper.jumpARouter(RouterTable.Cert.PATH_CERT_ACCOUNT_VERIFY_ACT);
                    }
                    break;
            }
        }else {
            Log.e(PascUserManagerImpl.class.getSimpleName(),"toCertification failed, no login");
            EventBusOutUtils.postCertificationFailed(certificationType);
        }

    }

    @Override
    public void toFaceCheck(String appId, PascUserFaceCheckListener callBack) {
        mTmpFaceCheckListener = callBack;

        if (UserManagerImpl.getInstance().isCertified()) {//已认证
            LoadingDialog loadingDialog = null;
            if (mContext instanceof Activity){
                loadingDialog = new LoadingDialog(mContext);
                loadingDialog.show();
            }
            LoadingDialog finalLoadingDialog = loadingDialog;
            FaceCheckManager.getInstance().faceCheck(appId, new FaceCheckManager.FaceInitLinster() {
                @Override
                public void error(String s, String s1) {
                    if (finalLoadingDialog != null){
                        finalLoadingDialog.dismiss();
                    }
//                    EventBusOutUtils.postFaceCheckFailed(s, s1);
                    //逻辑修改为跟IOS端一致，错误去人脸核验Pre界面弹
                    Bundle paFaceBundle = new Bundle();
                    paFaceBundle.putString("name", UserManagerImpl.getInstance().getUserInfo().getUserName());
                    paFaceBundle.putString("appId", appId);
                    paFaceBundle.putString("errorCode", s);
                    paFaceBundle.putString("errorMsg", s1);
                    BaseJumper.jumpBundleARouter(RouterTable.Face.PATH_FACE_CHECK_PREPARE_ACT, paFaceBundle);
                }

                @Override
                public void isValidity(FaceInitResp resp) {
                    if (finalLoadingDialog != null){
                        finalLoadingDialog.dismiss();
                    }
                    if (resp.isValidate) {
                        EventBusOutUtils.postFaceCheckSuccess(resp.credential, "true");
                    } else {
                        Bundle paFaceBundle = new Bundle();
                        paFaceBundle.putString("name", UserManagerImpl.getInstance().getUserInfo().getUserName());
                        paFaceBundle.putString("appId", appId);
                        BaseJumper.jumpBundleARouter(RouterTable.Face.PATH_FACE_CHECK_PREPARE_ACT, paFaceBundle);
                    }

                }
            });

        } else {
            Log.e(getClass().getSimpleName(), "face check failed, please certification first");
            EventBusOutUtils.postFaceCheckFailed("user_facecheck_uncert",mContext.getResources().getString(R.string.user_face_check_failed_not_cert));
        }
    }

    @Override
    public void toFaceCheck(String userName, String idCard, PascUserFaceCheckListener callBack) {
        mTmpFaceCheckListener = callBack;

        Bundle paFaceBundle = new Bundle();
        paFaceBundle.putString("userName", userName);
        paFaceBundle.putString("idCard", idCard);
        BaseJumper.jumpBundleARouter(RouterTable.Face.PATH_FACE_INFO_CHECK_ACT, paFaceBundle);
    }

    @Override
    public void toFaceSetting(PascUserFaceListener callBack) {
        mTmpFaceListener = callBack;
        if (UserManagerImpl.getInstance().isLogin()){
            BaseJumper.jumpARouter(RouterTable.Face.PATH_FACE_LOGIN_SWITCH_ACT);
        }else {
            Log.e(PascUserManagerImpl.class.getSimpleName(),"toFaceSetting failed, no login");
            EventBusOutUtils.postRegisterFaceFailed();
        }
    }

    @Override
    public void updateUserInfo(PascUserUpdateListener callBack) {
        mTmpUpdateListener = callBack;
        if (UserManagerImpl.getInstance().isLogin()){

            //更新用户信息，调用
            UserBiz.getCurrentUserInfo()
                    .subscribe(new BaseRespV2Observer<User>() {
                        @Override
                        public void onSuccess(User user) {
                            super.onSuccess(user);
                            if (user != null && UserManagerImpl.getInstance() != null) {
                                user.setToken(UserManagerImpl.getInstance().getToken());
                            }
                            UserManagerImpl.getInstance().updateUser(user);
                            EventBusOutUtils.postUpdateUserInfo();
                        }

                        @Override
                        public void onError(String code, String msg) {
                            super.onError(msg);
                            EventBusOutUtils.postGetUpdateUserInfoFailed();
                        }
                    });

        }else {
            Log.e(PascUserManagerImpl.class.getSimpleName(),"updateUserInfo failed, no login");
        }

    }

    @Override
    public void toChangePhoneNum(PascUserChangePhoneNumListener callBack) {
        mTmpChangePhoneNumListener = callBack;
        if (UserManagerImpl.getInstance().isLogin()){
            BaseJumper.jumpARouter(RouterTable.User.PATH_USER_ACCOUNT_CHANGE_PHONE_ACT);
        }else {
            Log.e(PascUserManagerImpl.class.getSimpleName(),"toChangePhoneNum failed, no login");
            EventBusOutUtils.postChangePhoneNumFailed();
        }
    }

    @Override
    public void toCancelAccount(PascUserCancelAccountListener callBack) {
        mTmpCancelAccountListener = callBack;
        if (UserManagerImpl.getInstance().isLogin()){
            BaseJumper.jumpARouter(RouterTable.User.PATH_USER_ACCOUNT_CALCE_ACT);
        }else {
            Log.e(PascUserManagerImpl.class.getSimpleName(),"toCancelAccount failed, no login");
            EventBusOutUtils.postCancelAccountFailed();
        }
    }

    @Override
    public void toPasswordSetOrUpdate() {
        if (UserManagerImpl.getInstance().isLogin()){
            User user = UserManagerImpl.getInstance().getInnerUser();
            if (user != null) {
                Bundle bundle = new Bundle();
                if (Constant.USER_HAS_PWD.equals(user.hasPassword)) {
                    bundle.putInt(LoginConstant.PWD_TYPE, LoginConstant.PWD_TYPE_UPDATE);
                } else {
                    bundle.putInt(LoginConstant.PWD_TYPE, LoginConstant.PWD_TYPE_RESET);
                }
                bundle.putString(LoginConstant.PHONE_NUMBER, user.getMobileNo());
                BaseJumper.jumpBundleARouter(RouterTable.Login.PATH_LOGIN_FORGETPWD_ACTIVITY, bundle);
            }else {
                Log.e(getClass().getSimpleName(),"toSetPassword need login first and user not null");
            }
        }else {
            Log.e(getClass().getSimpleName(),"toSetPassword need login first");
        }
    }

    @Override
    public void addCustomCert(int position, int icon, String name, String desc, int certType, CertConfig.CertClickCallBack outCallBack) {

        CertOutConfigManager.CustomCertClickCallBack inClickCallBack = new CertOutConfigManager.CustomCertClickCallBack(){

            @Override
            public void onClick(Activity activity) {
                if (outCallBack != null){
                    outCallBack.onClick(activity);
                }
            }
        };
        CertOutConfigManager.getInstance().addCert(position,icon,name,desc,certType,inClickCallBack);

    }

    @Override
    public void updateCustomCertResult(int customCertResult) {
        CertOutConfigManager.CustomCertResultListener listener = CertOutConfigManager.getInstance().getCustomCertResultListener();

        if (listener != null){
            switch (customCertResult){
                case CertConfig.CUSTOM_CERT_RESULT_CANCEL:
                    listener.onCancel();
                    break;
                case CertConfig.CUSTOM_CERT_RESULT_FAILED:
                    listener.onFailed();
                    break;
                case CertConfig.CUSTOM_CERT_RESULT_SUCCESS:
                    listener.onSuccess();
                    break;
                    default:

            }
        }
    }

    @Override
    public void clearAllCustomCert() {
        CertOutConfigManager.getInstance().clearAllCustomCert();
    }


    /**
     * 登陆
     */
    @Override
    public void toLogin(PascUserLoginListener callBack) {
        mTmpLoginListener = callBack;
        BaseJumper.jumpARouter(RouterTable.Login.PATH_LOGIN_ACTIVITY);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onListener(BaseEvent event) {

        Log.e(getClass().getName(), "receive event : "+event.getTag());
        if (event.getTag().startsWith("user_")) {
            //---------认证监听回调开始

            if (mPascUserCertListener != null) {
                if (EventTag.USER_CERTIFICATE_SUCCEED.equals(event.getTag())) {
                    mPascUserCertListener.onCertificationSuccess();
                } else if (EventTag.USER_CERTIFICATE_FAILED.equals(event.getTag())) {
                    mPascUserCertListener.onCertificationFailed();
                } else if (EventTag.USER_CERTIFICATE_CANCLED.equals(event.getTag())) {
                    mPascUserCertListener.onCertificationCancled();
                }
            }

            if (mTmpCertListener != null) {
                if (EventTag.USER_CERTIFICATE_SUCCEED.equals(event.getTag())) {
                    mTmpCertListener.onCertificationSuccess();
                    mTmpCertListener = null;
                } else if (EventTag.USER_CERTIFICATE_FAILED.equals(event.getTag())) {
                    mTmpCertListener.onCertificationFailed();
                    mTmpCertListener = null;
                } else if (EventTag.USER_CERTIFICATE_CANCLED.equals(event.getTag())) {
                    mTmpCertListener.onCertificationCancled();
                    mTmpCertListener = null;
                }
            }

            //---------登陆监听回调开始

            if (mPascUserLoginListener != null) {
                if (EventTag.USER_LOGIN_SUCCEED.equals(event.getTag())) {
                    mPascUserLoginListener.onLoginSuccess();
                } else if (EventTag.USER_LOGIN_CANCLE.equals(event.getTag())) {
                    mPascUserLoginListener.onLoginCancled();
                } else if (EventTag.USER_LOGIN_FAILED.equals(event.getTag())) {
                    mPascUserLoginListener.onLoginFailed();
                }
            }

            if (mTmpLoginListener != null) {
                if (EventTag.USER_LOGIN_SUCCEED.equals(event.getTag())) {
                    mTmpLoginListener.onLoginSuccess();
                    mTmpLoginListener = null;
                } else if (EventTag.USER_LOGIN_CANCLE.equals(event.getTag())) {
                    mTmpLoginListener.onLoginCancled();
                    mTmpLoginListener = null;
                } else if (EventTag.USER_LOGIN_FAILED.equals(event.getTag())) {
                    mTmpLoginListener.onLoginFailed();
                    mTmpLoginListener = null;
                }
            }

            //---------人脸监听回调开始

            if (mPascUserFaceListener != null) {
                if (EventTag.USER_REGISTER_FACE_SUCCESS.equals(event.getTag())) {
                    mPascUserFaceListener.onRegisterSuccess();
                } else if (EventTag.USER_REGISTER_FACE_CANCLED.equals(event.getTag())) {
                    mPascUserFaceListener.onRegisterCancled();
                } else if (EventTag.USER_RESET_FACE_SUCCESS.equals(event.getTag())) {
                    mPascUserFaceListener.onResetSuccess();
                } else if (EventTag.USER_RESET_FACE_CANCLED.equals(event.getTag())) {
                    mPascUserFaceListener.onResetCancled();
                } else if (EventTag.USER_SET_FACE_RESULT.equals(event.getTag())) {
                    mPascUserFaceListener.onSetFaceResult(UserManagerImpl.getInstance().isOpenFaceVerify());
                }
            }

            if (mTmpFaceListener != null) {
                if (EventTag.USER_REGISTER_FACE_SUCCESS.equals(event.getTag())) {
                    mTmpFaceListener.onRegisterSuccess();
                    mTmpFaceListener = null;
                } else if (EventTag.USER_REGISTER_FACE_CANCLED.equals(event.getTag())) {
                    mTmpFaceListener.onRegisterCancled();
                    mTmpFaceListener = null;
                } else if (EventTag.USER_RESET_FACE_SUCCESS.equals(event.getTag())) {
                    mTmpFaceListener.onResetSuccess();
                    mTmpFaceListener = null;
                } else if (EventTag.USER_RESET_FACE_CANCLED.equals(event.getTag())) {
                    mTmpFaceListener.onResetCancled();
                    mTmpFaceListener = null;
                } else if (EventTag.USER_SET_FACE_RESULT.equals(event.getTag())) {
                    mTmpFaceListener.onSetFaceResult(UserManagerImpl.getInstance().isOpenFaceVerify());
                    mTmpFaceListener = null;
                }else if (EventTag.USER_REGISTER_OR_RESET_FACE_FAILED.equals(event.getTag())){
                    mTmpFaceListener = null;
                }
            }

            //---------获取用户最新信息回调开始

            if (mPascUserUpdateListener != null) {
                if (EventTag.USER_UPDATE_MSG_SUCCESS.equals(event.getTag())) {
                    mPascUserUpdateListener.onSuccess();
                } else if (EventTag.USER_GET_UPDATE_MSG_FAILED.equals(event.getTag())) {
                    mPascUserUpdateListener.onFailed();
                }
            }

            if (mTmpUpdateListener != null) {
                if (EventTag.USER_UPDATE_MSG_SUCCESS.equals(event.getTag())) {
                    mTmpUpdateListener.onSuccess();
                    mTmpUpdateListener = null;
                } else if (EventTag.USER_GET_UPDATE_MSG_FAILED.equals(event.getTag())) {
                    mTmpUpdateListener.onFailed();
                    mTmpUpdateListener = null;
                }
            }

            //---------人脸核身

            if (mTmpFaceCheckListener != null) {
                if (EventTag.USER_FACE_CHECK_SUCCEED.equals(event.getTag())) {
                    if (event.getParams() != null) {
                        mTmpFaceCheckListener.onSuccess(event.getParams());
                        mTmpFaceCheckListener = null;
                    } else {
                        mTmpFaceCheckListener.onSuccess(event.getParams());
                        mTmpFaceCheckListener = null;
                    }

                } else if (EventTag.USER_FACE_CHECK_FAILED.equals(event.getTag())) {
                    String errorCode = event.getParams().get(EventTag.USER_ERROR_CODE);
                    String errorMsg = event.getParams().get(EventTag.USER_ERROR_MSG);
                    if (mTmpFaceCheckListener instanceof PascUserFaceCheckNewListener){
                        ((PascUserFaceCheckNewListener)mTmpFaceCheckListener).onFailed(errorCode,errorMsg);
                    }else {
                        mTmpFaceCheckListener.onFailed();
                    }
                    mTmpFaceCheckListener = null;
                } else if (EventTag.USER_FACE_CHECK_CANCLED.equals(event.getTag())) {
                    mTmpFaceCheckListener.onCancled();
                    mTmpFaceCheckListener = null;
                }
            }


            //--------- 账号注销

            if (mTmpCancelAccountListener != null) {
                if (EventTag.USER_CANCEL_ACCOUNT_SUCCEED.equals(event.getTag())) {
                    mTmpCancelAccountListener.onSuccess();
                    mTmpCancelAccountListener = null;
                } else if (EventTag.USER_CANCEL_ACCOUNT_FAILED.equals(event.getTag())) {
                    mTmpCancelAccountListener.onFailed();
                    mTmpCancelAccountListener = null;
                } else if (EventTag.USER_CANCEL_ACCOUNT_CANCELD.equals(event.getTag())) {
                    mTmpCancelAccountListener.onCanceld();
                    mTmpCancelAccountListener = null;
                }
            }


            //--------- 手机号设置

            if (mTmpChangePhoneNumListener != null) {
                if (EventTag.USER_CHANGE_PHONE_NUM_SUCCEED.equals(event.getTag())) {
                    mTmpChangePhoneNumListener.onSuccess();
                    mTmpChangePhoneNumListener = null;
                } else if (EventTag.USER_CHANGE_PHONE_NUM_FAILED.equals(event.getTag())) {
                    mTmpChangePhoneNumListener.onFailed();
                    mTmpChangePhoneNumListener = null;
                } else if (EventTag.USER_CHANGE_PHONE_NUM_CANCELD.equals(event.getTag())) {
                    mTmpChangePhoneNumListener.onCanceld();
                    mTmpChangePhoneNumListener = null;
                }
            }

        }

    }

    private void initEventBus() {
        EventBus.getDefault().register(this);
    }

}
