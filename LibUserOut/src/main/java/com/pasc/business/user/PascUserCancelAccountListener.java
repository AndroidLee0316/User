package com.pasc.business.user;

/**
 * 功能：对外用户账号注销状态监听回调
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/23
 */
public interface PascUserCancelAccountListener {

    /**
     * 账号注销成功
     */
    void onSuccess();

    /**
     * 账号注销失败
     */
    void onFailed();

    /**
     * 取消账号注销
     */
    void onCanceld();


}
