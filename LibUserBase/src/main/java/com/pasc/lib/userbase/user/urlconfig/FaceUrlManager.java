package com.pasc.lib.userbase.user.urlconfig;

/**
 * 人脸相关URL统一管理
 */
public class FaceUrlManager extends BaseUrlManager{

    private String faceComparisonUrl = "/api/platform/pinganFace/faceComparisonV2";
    private String registerFaceUrl = "/api/platform/pinganFace/registerFaceV2";
    private String getOpenfaceStateByMobileUrl = "/api/platform/pinganFace/getOpenfaceStateByMobile";
    private String resetFaceUrl = "/api/platform/pinganFace/resetFaceV2";
    private String verifyOpenFaceUrl = "/api/platform/user/verifyOpenFace";
    private String updateOpenFaceUrl = "/api/platform/pinganFace/updateOpenFace";
    private String faceAndIdComparisonUrl = "/api/platform/face/faceAndIdComparisonV2";
    private String upgradeToFaceAndIdComparisonUrl = "/api/platform/face/upgradeToFaceAndIdComparison";
    private String openFaceCertificationLoginUrl = "/api/platform/pinganFace/openFaceCertificationLogin";

    private UserUrlConfig.FaceConfigBean faceConfigBean;

    private static volatile FaceUrlManager instance;
    private FaceUrlManager() {

    }

    public static FaceUrlManager getInstance() {
        if (instance == null) {
            synchronized (FaceUrlManager.class) {
                if (instance == null) {
                    instance = new FaceUrlManager();
                }
            }
        }
        return instance;
    }

    public void setFaceConfigBean(UserUrlConfig.FaceConfigBean faceConfigBean) {
        this.faceConfigBean = faceConfigBean;
    }

    /**
     * 人脸比对
     */
    public String getFaceComparisonUrl() {
        return addPrefixHost(faceComparisonUrl);
    }

    /**
     * 注册人脸
     */
    public String getRegisterFaceUrl() {
        return addPrefixHost(registerFaceUrl);
    }

    /**
     * 根据手机号获取人脸
     */
    public String getOpenfaceStateByMobileUrl() {
        return addPrefixHost(getOpenfaceStateByMobileUrl);
    }

    /**
     * 重置人脸
     */
    public String getResetFaceUrl() {
        return addPrefixHost(resetFaceUrl);
    }

    /**
     * 人脸开通验证码校验
     */
    public String getVerifyOpenFaceUrl() {
        return addPrefixHost(verifyOpenFaceUrl);
    }

    /**
     * 人脸开关
     */
    public String getUpdateOpenFaceUrl() {
        return addPrefixHost(updateOpenFaceUrl);
    }

    public String getFaceAndIdComparisonUrl() {
        return addPrefixHost(faceAndIdComparisonUrl);
    }

    public String getUpgradeToFaceAndIdComparisonUrl() {
        return addPrefixHost(upgradeToFaceAndIdComparisonUrl);
    }

    public String getOpenFaceCertificationLoginUrl() {
        return addPrefixHost(openFaceCertificationLoginUrl);
    }

    public boolean needAlipayFaceCheck(){
        if (faceConfigBean != null){
            return faceConfigBean.needAlipayFaceCheck;
        }
        return true;
    }

}
