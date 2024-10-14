package com.pasc.business.user;

/**
 * 功能：对外用户信息状态监听回调
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/23
 */
public interface PascUserUpdateListener {

    /**
     * 获取用户信息成功
     */
    void onSuccess();

    /**
     * 获取用户信息失败
     */
    void onFailed();

}
