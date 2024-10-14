package com.pasc.lib.userbase.user.urlconfig;

import com.pasc.lib.base.AppProxy;

/**
 * 认证相关url统一管理
 */
public class CertiUrlManager extends BaseUrlManager{

    private static volatile CertiUrlManager instance;

    private String byBankUrl = "/api/platform/byBank";
    private String sendVerifyCodeUrl = "/api/platform/user/sendVerifyCode";
    private String confirmByBankUrl = "/api/platform/confirmByBank";
    private String queryAuthCountUrl = "/api/platform/cert/queryAuthCount";
    /**
     * 查询用户输入的姓名+身份证是否认证，旧的基线接口，可以互踢 by lcb at 20191230
     */
    public String queryIsCertedUrl = "/api/platform/cert/queryIsCerted";
    /**
     * 查询用户输入的姓名+身份证是否认证，新的基线接口，来自盐城项目，不可以互踢，不向前兼容 by lcb at 20191230
     * 这里改成 public static ,是为了防止如果以前的项目更新到新版本后，还是想要互踢，可以使用以前的旧接口，即 {@queryIsCertedUrl}
     * 但是技术改了旧接口，也使用不了，因为两个接口中的身份证字段命名不一样，一个是idNo,一个是idCard，需要后台兼容
     */
    public static String checkIdCardUser = "/api/platform/face/checkIdcardUserame";

    private UserUrlConfig.CertiConfigBean certiConfigBean;

    private CertiUrlManager() {

    }


    public static CertiUrlManager getInstance() {
        if (instance == null) {
            synchronized (CertiUrlManager.class) {
                if (instance == null) {
                    instance = new CertiUrlManager();
                }
            }
        }
        return instance;
    }

    public void setCertiConfigBean(UserUrlConfig.CertiConfigBean certiConfigBean) {
        this.certiConfigBean = certiConfigBean;
    }

    /**
     * @return 实名认证URL
     */
    public String getByBankUrl() {
        return addPrefixHost(byBankUrl);
    }

    /**
     * @return 银行卡认证发送手机验证码
     */
    public String getSendVerifyCodeUrl() {
        return addPrefixHost(sendVerifyCodeUrl);
    }

    /**
     * @return 银行卡确认实名认证接口
     */
    public String getConfirmByBankUrl() {
        return addPrefixHost(confirmByBankUrl);
    }

    /**
     * @return 查询认证次数是否超过最大允许次数
     */
    public String getQueryAuthCountUrl() {
        return addPrefixHost(queryAuthCountUrl);
    }

    /**
     * @return 查询该身份信息是否被其他用户认证过
     */
    @Deprecated
    public String getQueryIsCertedUrl() {
        return addPrefixHost(queryIsCertedUrl);
    }

    /**
     * @return 查询该身份信息是否被其他用户认证过
     */
    public String getCheckIdCardUser() {
        return addPrefixHost(checkIdCardUser);
    }

    /**
     * 是否开启银行卡认证
     */
    public boolean getNeedBankCert() {
        return certiConfigBean.needBankCert;
    }

    /**
     * 是否开启平安人脸认证
     */
    public boolean getNeedPAFaceCert() {
        return certiConfigBean.needPAFaceCert;
    }

    /**
     * 是否开启支付宝人脸认证
     */
    public boolean getNeedAlipayFaceCert() {
        return certiConfigBean.needAlipayFaceCert;
    }

    /**
     * 是否使用第二种方式的人脸认证流程
     */
    public boolean isCertFaceNewWay() {
        return certiConfigBean.certFaceNewWay;
    }

    /**
     * 获取认证警示类型，类型包括的种类参考 UserUrlConfig.CertiConfigBean.CERT_WARNINNG_TYPE_XXX
     * @return
     */
    public int getCertWarningType(){
        return certiConfigBean.certWarningType;
    }

    /**
     * 哪个认证显示 "推荐" 字样，默认都不显示，仅支持显示一个推荐
     * @return
     */
    public int getCertRecommend(){
        return certiConfigBean.certRecommend;
    }

    /**
     * 支付宝认证后deeplink跳转
     * @return
     */
    public String getAlipayReturnJumpUrl(){
        return certiConfigBean.alipayReturnJumpUrl;
    }


}
