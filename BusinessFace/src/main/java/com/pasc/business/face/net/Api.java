package com.pasc.business.face.net;

import com.pasc.business.face.net.pamars.FaceCheckQueryApliPamars;
import com.pasc.business.face.net.pamars.FaceInitPamars;
import com.pasc.business.face.net.resp.FaceCheckResp;
import com.pasc.business.face.net.resp.FaceInitResp;
import com.pasc.lib.net.resp.BaseV2Resp;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/22
 * 更改时间：2019/10/22
 */
public interface Api {
    @POST(UrlManager.URL_FACE_INIT)
    Single<BaseV2Resp<FaceInitResp>> faceInit(
        @Header("token") String token,
        @Body FaceInitPamars param);

    @Multipart
    @POST(UrlManager.URL_FACE_COMPARE)
    Single<BaseV2Resp<FaceCheckResp>> faceCompare(
        @Header("token") String token,
        @Part("initCode") RequestBody initCode,
        @Part("appid") RequestBody appid,
        @Part("plat") RequestBody plat,
        @Part("version") RequestBody version,
        @Part("model") RequestBody model,
        @Part("type") RequestBody type,
        @Part("imageContent") RequestBody imageContent,
        @Part("imageMessageDigest") RequestBody imageMessageDigest,
        @Part MultipartBody.Part file);

    @Multipart
    @POST(UrlManager.URL_FACE_ID_COMPARE)
    Single<BaseV2Resp<FaceCheckResp>> faceIdCompare(
        @Header("token") String token,
        @Part("initCode") RequestBody initCode,
        @Part("appid") RequestBody appid,
        @Part("plat") RequestBody plat,
        @Part("version") RequestBody version,
        @Part("model") RequestBody model,
        @Part("type") RequestBody type,
        @Part("imageContent") RequestBody imageContent,
        @Part("imageMessageDigest") RequestBody imageMessageDigest,
        @Part MultipartBody.Part file);

    @POST(UrlManager.URL_FADE_DELECT)
    Single<BaseV2Resp<Object>> faceDelet(@Header("token") String token);

    @POST(UrlManager.URL_CHECK_QUERY_APLI)
    Single<BaseV2Resp<FaceCheckResp>> faceCheckQueryAli(
        @Header("token") String token,
        @Body FaceCheckQueryApliPamars pamars);

    @Multipart
    @POST(UrlManager.URL_FACE_INFO_COMPARE)
    Single<BaseV2Resp<FaceCheckResp>>faceInfoCompare(
        @Part("userName") RequestBody userName,
        @Part("idCard") RequestBody idCard,
        @Part("plat") RequestBody plat,
        @Part("version") RequestBody version,
        @Part("model") RequestBody model,
        @Part("type") RequestBody type,
        @Part("imageContent") RequestBody imageContent,
        @Part("imageMessageDigest") RequestBody imageMessageDigest,
        @Part MultipartBody.Part file);

}
