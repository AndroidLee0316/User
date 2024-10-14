package com.pasc.lib.userbase.user.certification.net;

import com.pasc.lib.net.resp.BaseV2Resp;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.userbase.user.certification.net.param.CheckIdCardParam;
import com.pasc.lib.userbase.user.certification.net.param.RealNameByBankParam;
import com.pasc.lib.userbase.user.certification.net.param.ConfirmByBankParam;
import com.pasc.lib.userbase.user.certification.net.param.QueryAuthCountParam;
import com.pasc.lib.userbase.user.certification.net.param.QueryIsCertedParam;
import com.pasc.lib.userbase.user.certification.net.param.SendVerifyCodeParam;
import com.pasc.lib.userbase.user.certification.net.resp.CertifyCompleteResp;
import com.pasc.lib.userbase.user.certification.net.resp.RealNameByBankResp;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface CertifyApi {

    @POST
    Single<BaseV2Resp<RealNameByBankResp>> certificationByBank(@Url String url,
                                                               @Header("token") String token,
                                                               @Body RealNameByBankParam param);

    /**
     * 通知服务器发送验证码
     */
    @POST
    Single<BaseV2Resp<VoidObject>> sendVerifyCode(@Url String url,
                                                  @Header("token") String token,
                                                  @Body SendVerifyCodeParam param);

    /**
     * 银行卡确认实名认证接口
     */
    @POST
    Single<BaseV2Resp<CertifyCompleteResp>> confirmByBank(@Url String url,
                                                          @Header("token") String token,
                                                          @Body ConfirmByBankParam param);


    /**
     * 查询认证次数是否超过最大允许次数
     */
    @POST
    Single<BaseV2Resp<VoidObject>> queryAuthCount(@Url String url,
                                                  @Header("token") String token,
                                                  @Body QueryAuthCountParam param);

    /**
     * 查询该身份信息是否被其他用户认证过
     */
    @POST
    Single<BaseV2Resp<String>> queryIsCerted(@Url String url,
                                             @Header("token") String token,
                                             @Body QueryIsCertedParam param);
    /**
     * 查询该身份信息是否被其他用户认证过
     */
    @POST
    Single<BaseV2Resp<String>> checkIsCerted(@Url String url,
                                             @Header("token") String token,
                                             @Body CheckIdCardParam param);

    /**
     * 人脸实名认证
     *
     * @param url      请求地址
     * @param token
     * @param idCard   身份证号
     * @param userName 用户名
     * @param plat     客户端平台类型(1->android,2->ios)
     * @param version  版本号
     * @param model    机型
     * @param type     图片类型
     * @param file     人脸图片
     * @return
     */
    @Multipart
    @POST
    Single<BaseV2Resp<String>> faceAndIdComparison(@Url String url,
                                                       @Header("token") String token,
                                                       @Part("idCard") RequestBody idCard,
                                                       @Part("userName") RequestBody userName,
                                                       @Part("plat") RequestBody plat,
                                                       @Part("version") RequestBody version,
                                                       @Part("model") RequestBody model,
                                                       @Part("type") RequestBody type,
                                                       @Part MultipartBody.Part file);

    /**
     * 用户实名认证后提示开通/覆盖人脸登录接口
     *
     * @param url          请求地址
     * @param token
     * @param mobile       手机号
     * @param content_type 图片类型，只支持（jpg，jpeg，png，bmp）
     * @param type         确认开通结果，0是（新增），1是（覆盖）
     * @param file         人脸图片
     * @return
     */
    @Multipart
    @POST
    Single<BaseV2Resp<VoidObject>> openOrOverrideFacelogin(@Url String url,
                                                           @Header("token") String token,
                                                           @Part("mobile") RequestBody mobile,
                                                           @Part("content_type") RequestBody content_type,
                                                           @Part("checkType") RequestBody type,
                                                           @Part MultipartBody.Part file);
}
