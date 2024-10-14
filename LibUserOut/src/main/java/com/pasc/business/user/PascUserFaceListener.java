package com.pasc.business.user;

/**
 * 功能：对外用户人脸状态监听回调
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/23
 */
public interface PascUserFaceListener {

    void onRegisterSuccess();

    void onRegisterCancled();

    void onResetSuccess();

    void onResetCancled();

    void onSetFaceResult(boolean isFaceOpen);


}
