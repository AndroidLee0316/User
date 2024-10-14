package com.pasc.business.cert.zm;
import android.text.TextUtils;

import com.pasc.business.cert.zm.param.GetAliCertResultParam;
import com.pasc.business.cert.zm.param.GetAliInitInfoParam;
import com.pasc.business.cert.zm.resp.GetAliCertResultResp;
import com.pasc.business.cert.zm.resp.GetAliInitInfoResp;
import com.pasc.lib.userbase.user.net.resp.BaseRespObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AlipayCertModel {

    /**
     * 获取支付宝认证所需数据
     * @param userName
     * @param idCard
     * @param callBack
     */
    public void getAliInitInfo(String userName, String idCard, String appReturnJumpUrl, final CallBack.GetAliInitInfoCallBack callBack) {
        GetAliInitInfoParam param = new GetAliInitInfoParam(userName, idCard);
        //如果传入的 appReturnJumpUrl 不为空，表示外部自定义deeplink协议
        if (!TextUtils.isEmpty(appReturnJumpUrl)){
            param.appReturnJumpUrl = appReturnJumpUrl;
        }
        AlipayCertBiz.getAliInitInfo(param)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseRespObserver<GetAliInitInfoResp>() {
                    @Override
                    public void onSuccess(GetAliInitInfoResp resp) {
                        callBack.onSuccess(resp);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        super.onError(code, msg);
                        callBack.onFailed(String.valueOf(code), msg);
                    }
                });
    }

    /**
     * 查询认证结果
     * @param certifyId
     * @param callBack
     */
    public void getAliCertResult(String userName, String idCard, String certifyId, final CallBack.GetAliCertResultCallBack callBack) {
        GetAliCertResultParam param = new GetAliCertResultParam(userName, idCard, certifyId);
        AlipayCertBiz.getAliCertResult(param)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseRespObserver<GetAliCertResultResp>() {
                    @Override
                    public void onSuccess(GetAliCertResultResp resp) {
                        if (resp.certResult){
                            callBack.onSuccess(true);
                        }else {
                            callBack.onFailed(GetAliCertResultResp.KEY_REMAIN_TIMES, String.valueOf(resp.remainTimes));
                        }

                    }

                    @Override
                    public void onError(int code, String msg) {
                        super.onError(code, msg);
                        callBack.onFailed(String.valueOf(code), msg);
                    }
                });
    }


}
