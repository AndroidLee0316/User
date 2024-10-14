package com.pasc.business.cert.zm;

import com.pasc.business.cert.zm.resp.GetAliInitInfoResp;
import com.pasc.lib.userbase.base.data.user.ThirdLoginUser;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/9
 */
public interface CallBack {

    /**
     * 初始化获取传递给支付宝认证的数据串
     */
    static interface GetAliInitInfoCallBack {
        void onSuccess(GetAliInitInfoResp resp);
        void onFailed(String errorCode, String errorMsg);
    }

    /**
     * 获取支付宝认证状态
     */
    static interface GetAliCertResultCallBack{
        void onFailed(String errorCode, String errorMsg);
        void onSuccess(boolean success);
    }

}
