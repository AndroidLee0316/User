package com.pasc.business.user;

import android.content.Context;

import com.pasc.business.user.cert.CertConfig;

/**
 * 功能：统一对外接口
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/23
 */
public interface PascUserManagerInter {

    /**
     * 初始化
     * @param context
     * @param config
     */
    void init(Context context, PascUserConfig config);

    /**
     * 设置全局认证监听
     * @param callBack
     */
    void setCertListener(PascUserCertListener callBack);

    /**
     * 设置全局登陆监听
     * @param callBack
     */
    void setLoginListener(PascUserLoginListener callBack);

    /**
     * 设置全局人脸监听
     * @param callBack
     */
    void setFaceListener(PascUserFaceListener callBack);

    /**
     * 设置全局用户数据更新监听
     * @param callBack
     */
    void setUserInfoUpdateListener(PascUserUpdateListener callBack);

    /**
     * 获取用户信息
     * @param key   用户信息
     * @return
     */
    String getUserInfo(String key);

    /**
     * 用户是否登陆
     * @return
     */
    boolean isLogin();

    /**
     * 用户是否认证
     * @return
     */
    boolean isCertification();

    /**
     * 是否设置了密码
     * @return
     */
    boolean hasPassword();
    /**
     * 退出登陆
     * @return
     */
    boolean loginOut();

    /**
     * 退出登陆
     * @param callBack  退出登陆回调
     */
    void loginOut(PascUserLoginOutListener callBack);

    /**
     * 跳转到账户中心
     */
    void toAccount();

    /**
     * 跳转到登陆
     * @param callBack  登陆结果监听
     */
    void toLogin(PascUserLoginListener callBack);

    /**
     * 跳转到认证页面
     * @param certificationType 认证类型
     * @param callBack  认证结果监听
     */
    void toCertification(int certificationType, PascUserCertListener callBack);

    /**
     * 跳转到人脸核验
     * @param appid     第三方服务ID
     * @param callBack  人脸核验回调
     */
    void toFaceCheck(String appid,PascUserFaceCheckListener callBack);

    void toFaceCheck(String name,String idNum,PascUserFaceCheckListener callBack);

    /**
     * 跳转到人脸设置
     * @param callBack  人脸设置结果监听
     */
    void toFaceSetting(PascUserFaceListener callBack);

    /**
     * 刷新用户信息
     * @param callBack  刷新用户信息结果监听
     */
    void updateUserInfo(PascUserUpdateListener callBack);

    /**
     * 跳转到更换手机号
     * @param callBack
     */
    void toChangePhoneNum(PascUserChangePhoneNumListener callBack);

    /**
     * 跳转到账号注销
     * @param callBack
     */
    void toCancelAccount(PascUserCancelAccountListener callBack);

    /**
     * 跳转到密码设置 or 密码更新页面,
     */
    void toPasswordSetOrUpdate();

    /**
     * 添加一个自定义的认证
     * @param position      自定义认证所在的listview中的位置
     * @param icon
     * @param name
     * @param desc
     * @param certType 认证类型
     * @param callBack
     */
    void addCustomCert(int position, int icon, String name, String desc, int certType, CertConfig.CertClickCallBack callBack);

    /**
     * 刷新自定义认证结果
     * @param customCertResult  值参考 CertConfig.CUSTOM_CERT_RESULT_XXX
     */
    void updateCustomCertResult(int customCertResult);

    /**
     * 清除所有的添加的自定义认证
     */
    void clearAllCustomCert();

    /**
     * 释放资源
     */
    void onDestroy();
}
