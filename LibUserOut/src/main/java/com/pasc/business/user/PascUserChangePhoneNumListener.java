package com.pasc.business.user;

/**
 * 功能：对外用户更换手机号状态监听回调
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/23
 */
public interface PascUserChangePhoneNumListener {

    /**
     * 更换手机号成功
     */
    void onSuccess();

    /**
     * 更换手机号失败
     */
    void onFailed();

    /**
     * 取消更换手机号
     */
    void onCanceld();


}
