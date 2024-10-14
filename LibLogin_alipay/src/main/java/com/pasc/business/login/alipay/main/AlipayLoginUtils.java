package com.pasc.business.login.alipay.main;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.AuthTask;

import java.util.Map;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/6/28
 */
public class AlipayLoginUtils {

    /**
     * 授权
     */
    public static void auth(final Activity activity, final String authInfo, final CallBack.AuthorizeCallBack callBack){


        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(activity);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                final AuthResult authResult = new AuthResult(result, true);
                String resultStatus = authResult.getResultStatus();

                // 判断resultStatus 为“9000”且result_code
                // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                    // 获取alipay_open_id，调支付时作为参数extern_token 的value
                    // 传入，则支付账户为该授权账户
                }else {
                    Log.e("AlipayUtils","auth failed : " + resultStatus);
                }
                if (callBack != null && activity.getWindow()!=null && activity.getWindow().getDecorView()!=null){
                    activity.getWindow().getDecorView().post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.callBack(authResult.getAlipayOpenId(),authResult.getAuthCode());
                        }
                    });
                }else {
                    Log.e("AlipayLoginUtils","auth result , activity is empty or window is null");
                }
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }



}
