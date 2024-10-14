package com.pasc.business.user.cert;

import android.app.Activity;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/12/20
 */
public class CertConfig {

    /**
     * 刷新第三方认证结果：认证成功
     */
    public static final int CUSTOM_CERT_RESULT_SUCCESS = 1;

    /**
     * 刷新第三方认证结果：认证失败
     */
    public static final int CUSTOM_CERT_RESULT_FAILED = 0;

    /**
     * 刷新第三方认证结果：取消认证
     */
    public static final int CUSTOM_CERT_RESULT_CANCEL = -1;

    /**
     * 认证item的点击事件
     */
    public static interface CertClickCallBack{
        public void onClick(Activity activity);
    }

}
