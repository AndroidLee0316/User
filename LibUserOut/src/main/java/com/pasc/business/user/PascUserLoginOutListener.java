package com.pasc.business.user;

/**
 * 功能：对外用户退出登陆状态监听回调
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/23
 */
public interface PascUserLoginOutListener {

    /**
     * 退出登陆成功
     */
    void onLoginOutSuccess();

    /**
     * 退出登陆失败
     */
    void onLoginOutFailed();

    /**
     * 取消退出登陆
     */
    void onLoginCancled();


}
