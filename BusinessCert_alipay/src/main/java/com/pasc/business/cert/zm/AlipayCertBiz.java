package com.pasc.business.cert.zm;

import com.pasc.business.cert.zm.param.GetAliCertResultParam;
import com.pasc.business.cert.zm.param.GetAliInitInfoParam;
import com.pasc.business.cert.zm.resp.GetAliCertResultResp;
import com.pasc.business.cert.zm.resp.GetAliInitInfoResp;
import com.pasc.lib.net.ApiGenerator;
import com.pasc.lib.userbase.user.net.transform.ThirdLoginRespTransformer;
import com.pasc.lib.userbase.user.urlconfig.LoginUrlManager;
import com.pasc.lib.userbase.user.util.UserManagerImpl;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class AlipayCertBiz {

    /**
     * 获取请求支付宝认证的数据
     */
    public static final String URL_PATH_GET_ALI_INIT_INFO = "/api/platform/cert/getAliInitInfo ";

    /**
     * 查询支付宝认证结果
     */
    public static final String URL_PATH_GET_ALI_CERT_RESULT = "/api/platform/cert/getAliCertResult ";

    /**
     * 获取请求支付宝认证的数据
     */
    public static Single<GetAliInitInfoResp> getAliInitInfo(GetAliInitInfoParam param) {
        String url = LoginUrlManager.getInstance().addPrefixHost(URL_PATH_GET_ALI_INIT_INFO);
        String token = UserManagerImpl.getInstance().getToken();
        ThirdLoginRespTransformer<GetAliInitInfoResp> respTransformer = ThirdLoginRespTransformer.newInstance();
        return ApiGenerator.createApi(AlipayCertApi.class)
                .getAilAuthRequestInfo(url, token, param)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 查询支付宝认证结果
     */
    public static Single<GetAliCertResultResp> getAliCertResult(GetAliCertResultParam param) {
        String url = LoginUrlManager.getInstance().addPrefixHost(URL_PATH_GET_ALI_CERT_RESULT);
        String token = UserManagerImpl.getInstance().getToken();
        ThirdLoginRespTransformer<GetAliCertResultResp> respTransformer = ThirdLoginRespTransformer.newInstance();
        return ApiGenerator.createApi(AlipayCertApi.class)
                .getAliCertResult(url, token, param)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}