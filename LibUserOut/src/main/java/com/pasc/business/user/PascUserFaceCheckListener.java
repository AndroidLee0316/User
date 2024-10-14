package com.pasc.business.user;

import org.json.JSONObject;

import java.util.Map;

/**
 * 功能：对外用户人脸核验监听回调
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/23
 */
public interface PascUserFaceCheckListener {

    /**
     * 认证成功
     */
    void onSuccess(Map<String, String> data);

    /**
     * 认证失败
     */
    void onFailed();

    /**
     * 取消认证
     */
    void onCancled();

}
