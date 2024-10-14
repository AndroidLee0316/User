package com.pasc.lib.userbase.user.urlconfig;

import com.pasc.lib.base.AppProxy;

/**
 * 登录相关URL统一管理
 */
public class LoginUrlManager extends BaseUrlManager{
    private static volatile LoginUrlManager instance;


    private String sendVerifyCodeUrl = "/api/platform/user/sendVerifyCode";
    private String mobileLoginUrl = "/api/platform/user/mobileLogin";
    private String pwdLoginUrl = "/api/platform/user/doPwdLogin";
    private String pwdResetUrl = "/api/platform/user/resetPassword";
    private String pwdUpdateUrl = "/api/platform/user/updatePassword";
    private String pwdForgetUrl = "/api/platform/user/forgetPassword";
    private String thirdBindUrl = "/api/platform/thirdUser/bindUserInfo";
    private String thirdUnBindUrl = "/api/platform/thirdUser/unbindUserInfo";
    private String thirdLoginUrl = "/api/platform/thirdUser/bindAccount";
//    private String userInfoByMobileUrl = "/api/auth/user/getUserInfoByMobile";
    private String getCurrentUserInfo = "/api/auth/user/getCurrentUserInfo";
    private String unregisterUrl="/api/zhuhai-app/user/userCancellation";
    private String logOutUrl = "/api/platform/user/logout";

    private UserUrlConfig.LoginConfigBean loginConfigBean;

    private LoginUrlManager() {

    }

    public static LoginUrlManager getInstance() {
        if (instance == null) {
            synchronized (LoginUrlManager.class) {
                if (instance == null) {
                    instance = new LoginUrlManager();
                }
            }
        }
        return instance;
    }

    public void setLoginConfigBean(UserUrlConfig.LoginConfigBean loginConfigBean) {
        this.loginConfigBean = loginConfigBean;
    }

    /**
     * 获取短信验证码的url
     */
    public String getSendVerifyCodeUrl() {
        return addPrefixHost(sendVerifyCodeUrl);
    }

    /**
     * 验证码登录的url
     */
    public String getMobileLoginUrl() {
        return addPrefixHost(mobileLoginUrl);
    }

    /**
     * 密码登录的url
     */
    public String getPwdLoginUrl() {
        return addPrefixHost(pwdLoginUrl);
    }

    /**
     * 重置密码的url
     */
    public String getPwdResetUrl() {
        return addPrefixHost(pwdResetUrl);
    }

    /**
     * 修改密码的url
     */
    public String getPwdUpdateUrl() {
        return addPrefixHost(pwdUpdateUrl);
    }

    /**
     * 忘记密码的url
     */
    public String getPwdForgetUrl() {
        return addPrefixHost(pwdForgetUrl);
    }

    /**
     * 三方账号绑定
     */
    public String getThirdBindUrl() {
        return addPrefixHost(thirdBindUrl);
    }

    /**
     * 三方账号解绑
     */
    public String getThirdUnBindUrl() {
        return addPrefixHost(thirdUnBindUrl);
    }

    /**
     * 三方登录url
     */
    public String getThirdLoginUrl() {
        return addPrefixHost(thirdLoginUrl);
    }

//    /**
//     * 根据手机号获取用户信息的url
//     */
//    public String getUserInfoUrl() {
//        return addPrefixHost(userInfoByMobileUrl);
//    }

    /**
     * 根据手机号获取用户信息的url
     */
    public String getCurrentUserInfo() {
        return addPrefixHost(getCurrentUserInfo);
    }

    /**
     * 用户协议提示文字
     */
    public String getAgreementText() {
        return loginConfigBean.agreementText;
    }

    /**
     * 用户协议跳转url
     */
    public String getAgreementUrl() {
        return addPrefixH5Host(loginConfigBean.agreementUrl);
    }

    /**
     * 服务协议位置
     * @return
     */
    public int getAgreementLocation(){
        return loginConfigBean.agreementLocation;
    }

    /**
     * 服务协议显示类型
     * @return
     */
    public int getServiceSelectType(){
        return loginConfigBean.serviceSelectType;
    }

    /**
     * 用户协议提示文字
     */
    public String getPrivacyText() {
        return loginConfigBean.privacyText;
    }

    /**
     * 用户隐私协议跳转url
     */
    public String getPrivacyUrl() {
        return addPrefixH5Host(loginConfigBean.privacyUrl);
    }

    public boolean getSupportWeChat() {
        return loginConfigBean.supportWeChat;
    }

    public boolean getSupportQQ() {
        return loginConfigBean.supportQQ;
    }

    public boolean getSupportAlipay() {
        return loginConfigBean.supportAlipay;
    }

    //销户 url
    public String getUnregisterUrl() {
        return addPrefixHost(unregisterUrl);
    }

    public String getLogOutUrl() {
        return addPrefixHost(logOutUrl);
    }

    public String getMobileFunctionalUrl(){return addPrefixH5Host(OtherConfigManager.getInstance().getOtherConfigBean().mobileFunctionalUrl);}


}
