package com.pasc.business.user;

import java.util.Map;

/**
 * 功能：对外用户人脸核验监听回调
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/23
 */
public interface PascUserFaceCheckNewListener extends PascUserFaceCheckListener{

    /**
     * 认证失败
     */
    void onFailed(String code, String msg);

}
