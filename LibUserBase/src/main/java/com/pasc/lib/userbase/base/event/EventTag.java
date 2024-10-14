package com.pasc.lib.userbase.base.event;


/**
 * EventBus发送的tag
 */
public class EventTag {

    /**
     * 失败的错误码
     */
    public static final String USER_ERROR_CODE = "user_error_code";

    /**
     * 失败的错误信息
     */
    public static final String USER_ERROR_MSG = "user_error_msg";

    /**
     * 用户注册成功
     */
    public static final String USER_REGISTER_SUCCEED = "user_register_succeed";
    /**
     * 用户登陆取消（失败）
     */
    public static final String USER_LOGIN_CANCLE = "user_login_cancle";
    /**
     * 用户登陆成功
     */
    public static final String USER_LOGIN_SUCCEED = "user_login_succeed";
    /**
     * 用户登陆失败
     */
    public static final String USER_LOGIN_FAILED = "user_login_failed";
    /**
     * 用户重置密码成功
     */
    public static final String USER_RESET_PASS_SUCCEED = "user_reset_password_succeed";
    /**
     * 用户重置人脸成功
     */
    public static final String USER_RESET_FACE_SUCCESS = "user_reset_face_success";
    /**
     * 用户取消重置人脸
     */
    public static final String USER_RESET_FACE_CANCLED = "user_reset_face_cancled";
    /**
     * 人脸设置结果
     */
    public static final String USER_SET_FACE_RESULT = "user_set_face_result";
    /**
     * 用户认证成功
     */
    public static final String USER_CERTIFICATE_SUCCEED = "user_certificate_succeed";
    /**
     * 用户认证失败
     */
    public static final String USER_CERTIFICATE_FAILED = "user_certificate_failed";
    /**
     * 用户认证取消
     */
    public static final String USER_CERTIFICATE_CANCLED = "user_certificate_cancled";

    /**
     * 用户人脸检测/确认成功
     */
    public static final String USER_FACE_CHECK_SUCCEED = "user_face_check_succeed";
    /**
     * 用户人脸检测/确认失败
     */
    public static final String USER_FACE_CHECK_FAILED = "user_face_check_failed";
    /**
     * 用户人脸检测/确认取消
     */
    public static final String USER_FACE_CHECK_CANCLED = "user_face_check_cancled";
    /**
     * 用户取消注册
     */
    public static final String USER_REGISTER_FACE_CANCLED = "user_register_face_cancled";
    /**
     * 用户注册人脸失败
     */
    public static final String USER_REGISTER_OR_RESET_FACE_FAILED = "user_register_or_reset_face_failed";
    /**
     * 用户注册成功
     */
    public static final String USER_REGISTER_FACE_SUCCESS = "user_register_face_success";
    /**
     * 第三方登陆成功
     */
    public static final String USER_THIRD_LOGIN = "user_third_login";
    /**
     * 通知用户信息更新了、刷新用户信息
     */
    public static final String USER_UPDATE_MSG_SUCCESS = "user_update_msg_success";
    /**
     * 通知获取用户信息更新失败了
     */
    public static final String USER_GET_UPDATE_MSG_FAILED = "user_get_update_msg_failed";


    /**
     * 账号注销成功
     */
    public static final String USER_CANCEL_ACCOUNT_SUCCEED = "user_cancel_account_succeed";
    /**
     *账号注销失败
     */
    public static final String USER_CANCEL_ACCOUNT_FAILED = "user_cancel_account_failed";
    /**
     * 取消账号注销
     */
    public static final String USER_CANCEL_ACCOUNT_CANCELD = "user_cancel_account_canceld";


    /**
     * 手机号设置成功
     */
    public static final String USER_CHANGE_PHONE_NUM_SUCCEED = "user_change_phone_num_succeed";
    /**
     * 手机号设置失败
     */
    public static final String USER_CHANGE_PHONE_NUM_FAILED = "user_change_phone_num_failed";
    /**
     * 手机号设置注销
     */
    public static final String USER_CHANGE_PHONE_NUM_CANCELD = "user_change_phone_num_canceld";

}
