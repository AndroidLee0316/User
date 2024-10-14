package com.pasc.lib.userbase.user.util;

import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.userbase.base.event.EventKey;
import com.pasc.lib.userbase.base.event.EventTag;

import org.greenrobot.eventbus.EventBus;

/**
 * 功能：对外通知用户系统状态结果eventbus工具类
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/12
 */
public class EventBusOutUtils {


    /**
     * 取消登陆
     */
    public static void postLoginCancle(){
        EventBus.getDefault().postSticky(new BaseEvent(EventTag.USER_LOGIN_CANCLE));
    }

    /**
     * 登陆失败
     */
    public static void postLoginFailed(){
        EventBus.getDefault().postSticky(new BaseEvent(EventTag.USER_LOGIN_FAILED));
    }

    /**
     * 登陆成功
     */
    public static void postLoginSuccess(){
        EventBus.getDefault().postSticky(new BaseEvent(EventTag.USER_LOGIN_SUCCEED));
    }

    /**
     * 取消认证
     * @param type  认证方式
     */
    public static void postCertificationCancle(int type){
        BaseEvent baseEvent = new BaseEvent(EventTag.USER_CERTIFICATE_CANCLED);
        baseEvent.put(EventKey.USER_CERTIFICATE_TYPE, String.valueOf(type));
        EventBus.getDefault().post(baseEvent);
    }

    /**
     * 认证失败
     * @param type  认证方式
     */
    public static void postCertificationFailed(int type){

        BaseEvent baseEvent = new BaseEvent(EventTag.USER_CERTIFICATE_FAILED);
        baseEvent.put(EventKey.USER_CERTIFICATE_TYPE, String.valueOf(type));
        EventBus.getDefault().post(baseEvent);
    }

    /**
     * 认证成功
     * @param type  认证方式
     */
    public static void postCertificationSuccess(int type){
        BaseEvent baseEvent = new BaseEvent(EventTag.USER_CERTIFICATE_SUCCEED);
        baseEvent.put(EventKey.USER_CERTIFICATE_TYPE, String.valueOf(type));
        EventBus.getDefault().post(baseEvent);

    }

    /**
     * 取消人脸检测/确认
     */
    public static void postFaceCheckCancled(){
        BaseEvent baseEvent = new BaseEvent(EventTag.USER_FACE_CHECK_CANCLED);
        EventBus.getDefault().post(baseEvent);
    }

    /**
     * 人脸检测/确认失败
     */
    public static void postFaceCheckFailed(String errorCode, String errorMsg){
        BaseEvent baseEvent = new BaseEvent(EventTag.USER_FACE_CHECK_FAILED);
        baseEvent.put(EventTag.USER_ERROR_CODE, errorCode);
        baseEvent.put(EventTag.USER_ERROR_MSG, errorMsg);
        EventBus.getDefault().post(baseEvent);
    }

    /**
     * 人脸检测/确认成功
     */
    public static void postFaceCheckSuccess(String certId, String  isValidity){
        BaseEvent baseEvent = new BaseEvent(EventTag.USER_FACE_CHECK_SUCCEED);
        baseEvent.put("certId",certId);
        baseEvent.put("isValidity",isValidity);
        EventBus.getDefault().post(baseEvent);
    }

    /**
     * 人脸重置成功（人脸比对也是调用的这个？）
     */
    public static void postRestFaceSuccess(){
        EventBus.getDefault().post(new BaseEvent(EventTag.USER_RESET_FACE_SUCCESS));
    }

    /**
     * 取消人脸重置（人脸比对也是调用的这个？）
     */
    public static void postRestFaceCancle(){
        EventBus.getDefault().post(new BaseEvent(EventTag.USER_RESET_FACE_CANCLED));
    }


    /**
     * 人脸注册成功
     */
    public static void postRegisterFaceSuccess(){
        EventBus.getDefault().post(new BaseEvent(EventTag.USER_REGISTER_FACE_SUCCESS));

        //这个为什么要加呢？因为注册与重置人脸用的同一个EventTag.USER_RESET_FACE_SUCCESS,为了兼容以前的开放平台等等，暂时加这里
        EventBus.getDefault().post(new BaseEvent(EventTag.USER_RESET_FACE_SUCCESS));
    }

    /**
     * 取消人脸注册
     */
    public static void postRegisterFaceCancle(){
        EventBus.getDefault().post(new BaseEvent(EventTag.USER_REGISTER_FACE_CANCLED));
    }

    /**
     * 人脸注册失败
     */
    public static void postRegisterFaceFailed(){
        EventBus.getDefault().post(new BaseEvent(EventTag.USER_REGISTER_OR_RESET_FACE_FAILED));
    }

    /**
     * 取消人脸注册
     */
    public static void postSetFaceResult(){
        EventBus.getDefault().post(new BaseEvent(EventTag.USER_SET_FACE_RESULT));
    }

    /**
     * 更新用户信息
     */
    public static void postUpdateUserInfo(){
        EventBus.getDefault().post(new BaseEvent(EventTag.USER_UPDATE_MSG_SUCCESS));
    }

    /**
     * 获取最新用户信息失败了
     */
    public static void postGetUpdateUserInfoFailed(){
        EventBus.getDefault().post(new BaseEvent(EventTag.USER_GET_UPDATE_MSG_FAILED));
    }



    /**
     * 取消账号注销
     */
    public static void postCancelAccountCancled(){
        BaseEvent baseEvent = new BaseEvent(EventTag.USER_CANCEL_ACCOUNT_CANCELD);
        EventBus.getDefault().post(baseEvent);
    }

    /**
     * 账号注销失败
     */
    public static void postCancelAccountFailed(){
        BaseEvent baseEvent = new BaseEvent(EventTag.USER_CANCEL_ACCOUNT_FAILED);
        EventBus.getDefault().post(baseEvent);
    }

    /**
     * 账号注销成功
     */
    public static void postCancelAccountSuccess(){
        BaseEvent baseEvent = new BaseEvent(EventTag.USER_CANCEL_ACCOUNT_SUCCEED);
        EventBus.getDefault().post(baseEvent);
    }



    /**
     * 取消手机号设置
     */
    public static void postChangePhoneNumCancled(){
        BaseEvent baseEvent = new BaseEvent(EventTag.USER_CHANGE_PHONE_NUM_CANCELD);
        EventBus.getDefault().post(baseEvent);
    }

    /**
     * 手机号设置失败
     */
    public static void postChangePhoneNumFailed(){
        BaseEvent baseEvent = new BaseEvent(EventTag.USER_CHANGE_PHONE_NUM_FAILED);
        EventBus.getDefault().post(baseEvent);
    }

    /**
     * 手机号设置成功
     */
    public static void postChangePhoneNumSuccess(){
        BaseEvent baseEvent = new BaseEvent(EventTag.USER_CHANGE_PHONE_NUM_SUCCEED);
        EventBus.getDefault().post(baseEvent);
    }


}
