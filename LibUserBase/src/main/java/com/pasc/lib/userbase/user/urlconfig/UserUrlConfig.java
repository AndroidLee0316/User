package com.pasc.lib.userbase.user.urlconfig;

import com.google.gson.annotations.SerializedName;

public class UserUrlConfig {
    /***认证*/
    @SerializedName("certificationConfig")
    public CertiConfigBean certiConfigBean;
    /***登录*/
    @SerializedName("loginConfig")
    public LoginConfigBean loginConfigBean;
    /***人脸*/
    @SerializedName("faceConfig")
    public FaceConfigBean faceConfigBean;
    /***其它配置项*/
    @SerializedName("otherConfig")
    public OtherConfigBean otherConfig;

    public static class CertiConfigBean {

        /**
         * 无需提示
         */
        public static final int CERT_WARNINNG_TYPE_NOTHING = -1;
        /**
         * 只显示提示信息
         */
        public static final int CERT_WARNINNG_TYPE_JUST_WARNING = 0;
        /**
         * 只是弹框
         */
        public static final int CERT_WARNINNG_TYPE_JUST_DIALOG = 1;
        /**
         * 显示提示信息 + 弹框
         */
        public static final int CERT_WARNINNG_TYPE_WARNING_AND_DIALOG = 2;
        /**
         * 显示勾选按钮
         */
        public static final int CERT_WARNINNG_TYPE_CHOOSE_WARNING = 3;
        /**
         * 显示勾选按钮 + 弹框提示
         */
        public static final int CERT_WARNINNG_TYPE_CHOOSE_WARNING_AND_DIALOG = 4;


        /**
         * 认证列表是否显示推荐：任何认证都不显示推荐，默认也是这个，值为-1
         */
        public static final int CERT_RECOMMEND_NONE = -1;

        @SerializedName("needBankCert")
        public boolean needBankCert;
        @SerializedName("needPAFaceCert")
        public boolean needPAFaceCert;
        @SerializedName("needAlipayFaceCert")
        public boolean needAlipayFaceCert;
        //适配盐城（人脸相关认证有两种路径：平安人脸与支付宝共存  or  平安人脸与支付宝认证二选一）
        @SerializedName("certFaceNewWay")
        public boolean certFaceNewWay;
        //认证提示类型：0表示只是有提示信息，1表示有勾选但是没有弹框，2表示有勾选也有弹框，默认是 CERT_WARNINNG_TYPE_CHOOSE_WARNING_AND_DIALOG
        @SerializedName("certWarningType")
        public int certWarningType = CERT_WARNINNG_TYPE_NOTHING;

        /**
         * 哪个认证显示 "推荐" 字样，默认都不显示，即-1，仅支持显示一个推荐，从 0 开始
         */
        @SerializedName("certRecommend")
        public int certRecommend = CERT_RECOMMEND_NONE;
        /**
         * 支付宝认证后deeplink跳转
         */
        @SerializedName("alipayReturnJumpUrl")
        public String alipayReturnJumpUrl;
    }

    public static class LoginConfigBean {

        /**
         * 服务协议位于登陆页底部
         */
        public static final int AGREEMENT_LOCATION_IN_BOTTOM = 0;
        /**
         * 服务协议位于隐私协议后面
         */
        public static final int AGREEMENT_LOCATION_BEHIND_PRIVACY = 1;
        /**
         * 强制设置服务协议未勾选
         */
        public static final int SERVICE_SELECT_FORCE_NORMAL  = 0;
        /**
         * 强制设置服务协议未勾选
         */
        public static final int SERVICE_SELECT_FORCE_UNSELECT  = 1;

        @SerializedName("agreementUrl")
        public String agreementUrl;
        @SerializedName("agreementText")
        public String agreementText;
        /**
         * 服务协议位置，默认是底部
         */
        @SerializedName("agreementLocation")
        public int agreementLocation = AGREEMENT_LOCATION_IN_BOTTOM;
        @SerializedName("privacyUrl")
        public String privacyUrl;
        @SerializedName("privacyText")
        public String privacyText;
        @SerializedName("supportWeChat")
        public boolean supportWeChat;
        @SerializedName("supportQQ")
        public boolean supportQQ;
        @SerializedName("supportAlipay")
        public boolean supportAlipay;
        @SerializedName("serviceSelectType")
        public int serviceSelectType = SERVICE_SELECT_FORCE_NORMAL;

    }

    public static class FaceConfigBean {
        @SerializedName("needAlipayFaceCheck")
        public boolean needAlipayFaceCheck = true;
    }


    public static class OtherConfigBean {
        @SerializedName("needCertMenu")
        public boolean needCertMenu = true;
        @SerializedName("needFaceSetting")
        public boolean needFaceSetting = true;
        @SerializedName("needPasswordSetting")
        public boolean needPasswordSetting = true;
        @SerializedName("needChangePhoneNum")
        public boolean needChangePhoneNum = true;
        @SerializedName("needAccountCancel")
        public boolean needAccountCancel = true;
        @SerializedName("accoutCancelHintUrl")
        public String accoutCancelHintUrl;
        @SerializedName("accoutCancelPayUrl")
        public String accoutCancelPayUrl;
        @SerializedName("mobileFunctionalUrl")
        public String mobileFunctionalUrl;
        //默认不需要
        @SerializedName("needFingerprint")
        public boolean needFingerprint = false;

        public String getAccoutCancelHintUrl(){
            return BaseUrlManager.addPrefixH5Host(accoutCancelHintUrl);
        }

        public String getAccoutCancelPayUrl() {
            return BaseUrlManager.addPrefixH5Host(accoutCancelPayUrl);
        }
    }

}
