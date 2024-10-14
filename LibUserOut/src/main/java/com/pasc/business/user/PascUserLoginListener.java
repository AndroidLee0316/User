package com.pasc.business.user;

/**
 * 功能：对外用户登陆状态监听回调
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/23
 */
public interface PascUserLoginListener {

    /**
     * 登陆成功
     */
    void onLoginSuccess();

    /**
     * 取消失败
     */
    void onLoginFailed();

    /**
     * 取消登陆
     */
    void onLoginCancled();


}
