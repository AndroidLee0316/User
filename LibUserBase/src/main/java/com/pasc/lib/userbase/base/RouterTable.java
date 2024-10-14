package com.pasc.lib.userbase.base;

public interface RouterTable {
    interface Face {
        //人脸模块
        String PATH_FACE_LOGIN_ACT = "/face/login/act";
        String PATH_FACE_LOGIN_SWITCH_ACT = "/face/login_switch/act";
        //平安人脸认证
        String PATH_FACE_COMPARE_ACT = "/face/compare/act";
        //平安人脸认证成功
        String PATH_FACE_COMPARE_SUCC_ACT = "/face/compare_succ/act";
        //人脸核验提示
        String PATH_FACE_CHECK_PREPARE_ACT = "/face/check_prepare/act";
        //人脸核验(公积金、社保等功能需要，确认人脸是不是该账号持有人)
        String PATH_FACE_CHECK_ACT = "/face/check/act";
        //人脸核验失败
        String PATH_FACE_CHECK_FAILED_ACT = "/face/check_failed/act";
        //人脸核验(用户,身份证)
        String PATH_FACE_INFO_CHECK_ACT = "/face/info/check/act";

        String PATH_FACE_ACCOUNT_VERIFY_ACT = "/face/account/act";
        String PATH_FACE_INPUT_ACT = "/face/input/act";
        String PATH_FACE_RESET_ACT = "/face/reset/act";
    }
    interface Cert {
        //认证模块
        String PATH_CERT_AUTH_TYPE_ACT = "/cert/auth/act";
        String PATH_CERT_AUTH_TYPE_ACT_NEW = "/cert/auth/act_new";
        String PATH_CERT_AUTH_TYPE_FACE_CHOOSE = "/cert/auth/act_face_choose";
        String PATH_CERT_ACCOUNT_VERIFY_ACT = "/cert/account_verify/act";
        String PATH_CERT_AUTH_TYPE_ACT_BANK = "/cert/auth/act_bank";
        String PATH_CERT_SUCC_ACT = "/cert/succ/act";
        String PATH_CERT_FAIL_ACT = "/cert/fail/act";
    }

    interface Cert_ZM {
        //认证模块:芝麻认证|支付宝人脸认证
        String PATH_CERT_AUTH_TYPE_FACE_MAIN = "/cert_zm/zm/main";
        //认证模块:芝麻认证|支付宝人脸认证、首先进行身份证姓名验证
        String PATH_CERT_AUTH_TYPE_FACE_PRE = "/cert_zm/zm/pre";
    }
    interface Login {
        //登录模块
        String PATH_LOGIN_ACTIVITY = "/login/main/act";
        String PATH_LOGIN_FORGETPWD_ACTIVITY = "/login/forget_pwd/main";
        String PATH_LOGIN_RESETPWD_ACTIVITY = "/login/reset_pwd/main";
    }
    interface Login_alipay {
        //登录模块
        String PATH_LOGIN_ACTIVITY = "/login_alipay/main/act";
    }
    interface Login_qq {
        //登录模块
        String PATH_LOGIN_ACTIVITY = "/login_qq/main/act";
    }
    interface Login_wx {
        //登录模块
        String PATH_LOGIN_ACTIVITY = "/login_wx/main/act";
    }


    interface User {
        /*** LoginActivity path*****/
        String PATH_USER_ACCOUNT_SECURITY_ACT = "/user/account_security/act";
        String PATH_USER_ACCOUNT_CHANGE_PHONE_ACT = "/user/account_change_phone/act";
        String PATH_USER_ACCOUNT_CALCE_ACT = "/user/account_calce/act";
        String PATH_USER_ACCOUNT_CERTIFICATION_ACT = "/user/account/certification/act";

    }

    interface PlatformLogin{
        //登录模块
        String PATH_LOGIN_ACTIVITY = "/platformlogin/main/act";
    }

    interface PlatformAccount{
        //账户中心模块
        String PATH_ACCOUNT_ACTIVITY = "/platformaccount/main/act";
    }

    interface PlatformFace{
        //账户中心模块
        String PATH_FACE_LOGIN_ACTIVITY = "/platformface/login/act";
    }
}
