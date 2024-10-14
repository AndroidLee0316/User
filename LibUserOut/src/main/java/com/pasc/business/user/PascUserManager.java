package com.pasc.business.user;

import android.content.Context;
import android.util.Log;

import com.pasc.business.user.cert.CertConfig;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/22
 */
public class PascUserManager {

    protected PascUserManagerInter mUserManagerImpl;
    protected static volatile PascUserManager mUserManager;

    public static PascUserManager getInstance() {
        if (mUserManager == null) {
            synchronized (PascUserManager.class) {
                if (mUserManager == null) {
                    mUserManager = new PascUserManager();
                }
            }
        }
        return mUserManager;
    }

    protected PascUserManager(){
    }

    public PascUserManager init(Context context, PascUserManagerInter impl, PascUserConfig config) {
        mUserManagerImpl = impl;
        mUserManagerImpl.init(context, config);
        return this;
    }

    public PascUserManager setCertListener(PascUserCertListener callBack){
        mUserManagerImpl.setCertListener(callBack);
        return this;
    }

    public PascUserManager setLoginListener(PascUserLoginListener callBack){
        mUserManagerImpl.setLoginListener(callBack);
        return this;
    }
    public PascUserManager setFaceListener(PascUserFaceListener callBack){
        mUserManagerImpl.setFaceListener(callBack);
        return this;
    }
    public PascUserManager setUpdateUserInfoListener(PascUserUpdateListener callBack){
        mUserManagerImpl.setUserInfoUpdateListener(callBack);
        return this;
    }

    /**
     * 获取用户信息
     * @param key   用户信息key @{PascUserConfig.USER_INFO_KEY_XXX}
     * @return
     */
    public String getUserInfo(String key){
        return mUserManagerImpl.getUserInfo(key);
    }

    /**
     * 是否登陆
     * @return
     */
    public boolean isLogin(){
        return mUserManagerImpl.isLogin();
    }

    /**
     * 是否设置了密码
     * @return
     */
    public boolean hasPassword(){
        return mUserManagerImpl.hasPassword();
    }

    /**
     * 是否认证
     * @return
     */
    public boolean isCertification(){
        return mUserManagerImpl.isCertification();
    }

    /**
     * 退出登陆：直接删除本地用户数据
     * @return
     */
    public boolean loginOut(){
        return mUserManagerImpl.loginOut();
    }

    /**
     * 退出登陆：直接删除本地用户数据且调用后台退出登陆接口
     * @param callBack
     */
    public void loginOut(PascUserLoginOutListener callBack){
        mUserManagerImpl.loginOut(callBack);
    }

    /**
     * 跳转到账户中心
     */
    public void toAccount(){
        mUserManagerImpl.toAccount();
    }

    /**
     * 跳转到密码设置或者密码重置页面
     */
    public void toPasswordSetOrUpdate(){
        mUserManagerImpl.toPasswordSetOrUpdate();
    }

    /**
     * 跳转到登陆页面
     */
    public void toLogin(PascUserLoginListener listener){
        mUserManagerImpl.toLogin(listener);
    }

    /**
     * 跳转到认证页面
     * @param certificationType 认证方式 @{PascUserConfig.CERTIFICATION_TYPE_XXX}
     */
    public void toCertification(int certificationType, PascUserCertListener listener){
        mUserManagerImpl.toCertification(certificationType, listener);
    }

    /**
     * 跳转到人脸设置
     */
    public void toFaceSetting(PascUserFaceListener listener){
        mUserManagerImpl.toFaceSetting(listener);
    }

    /**
     * 获取用户最新信息
     * @param listener
     */
    public void updateUserInfo(PascUserUpdateListener listener){
        mUserManagerImpl.updateUserInfo(listener);
    }

    /**
     * 跳转到人脸核身
     * @param listener
     */
    public void toFaceCheck(String appid,PascUserFaceCheckListener listener){
        mUserManagerImpl.toFaceCheck(appid,listener);
    }

    public void toFaceCheck(String userName,String idCard,PascUserFaceCheckListener listener){
        mUserManagerImpl.toFaceCheck(userName,idCard,listener);
    }

    /**
     * 跳转到账号注销
     * @param listener
     */
    public void toCancelAccount(PascUserCancelAccountListener listener){
        mUserManagerImpl.toCancelAccount(listener);
    }


    /**
     * 跳转到更换手机号
     * @param listener
     */
    public void toChangePhoneNum(PascUserChangePhoneNumListener listener){
        mUserManagerImpl.toChangePhoneNum(listener);
    }

    /**
     * 添加一个自定义的认证
     * @param position
     * @param icon
     * @param name
     * @param desc
     * @param callBack
     */
    public void addCustomCert(int position, int icon, String name, String desc, int certType, CertConfig.CertClickCallBack callBack){
        mUserManagerImpl.addCustomCert(position,icon,name,desc,certType,callBack);
    };

    /**
     * 刷新自定义认证结果
     * @param customCertResult  值参考 CertConfig.CUSTOM_CERT_RESULT_XXX
     */
    public void updateCustomCertResult(int customCertResult){

        mUserManagerImpl.updateCustomCertResult(customCertResult);
    };

    /**
     * 清除所有的添加的自定义认证
     */
    public void clearAllCustomCert(){
        mUserManagerImpl.clearAllCustomCert();
    };


    /**
     * 注销
     */
    public void onDestroy(){
        mUserManagerImpl.onDestroy();
        mUserManagerImpl = null;
        mUserManager = null;
    }

}
