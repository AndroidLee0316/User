package com.pasc.business.cert.zm;

import com.pasc.business.cert.zm.param.GetAliCertResultParam;
import com.pasc.business.cert.zm.param.GetAliInitInfoParam;
import com.pasc.business.cert.zm.resp.GetAliCertResultResp;
import com.pasc.business.cert.zm.resp.GetAliInitInfoResp;
import com.pasc.lib.net.resp.BaseV2Resp;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface AlipayCertApi {

    /**
     * 获取支付宝SDK调用加密串
     */
    @POST
    Single<BaseV2Resp<GetAliInitInfoResp>> getAilAuthRequestInfo(@Url String url,
                                                                 @Header("token") String token,
                                                                 @Body GetAliInitInfoParam param);

    /**
     * 查询支付宝认证结果
     */
    @POST
    Single<BaseV2Resp<GetAliCertResultResp>> getAliCertResult(@Url String url,
                                                              @Header("token") String token,
                                                              @Body GetAliCertResultParam param);


}
