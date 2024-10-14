package com.pasc.business.user;

/**
 * 功能：对外用户认证状态监听回调
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/23
 */
public interface PascUserCertListener {

    /**
     * 认证成功
     */
    void onCertificationSuccess();

    /**
     * 认证失败
     */
    void onCertificationFailed();

    /**
     * 取消认证
     */
    void onCertificationCancled();

}
