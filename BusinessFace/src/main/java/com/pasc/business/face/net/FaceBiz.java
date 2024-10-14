package com.pasc.business.face.net;

import com.pasc.business.face.net.pamars.FaceCheckQueryApliPamars;
import com.pasc.business.face.net.pamars.FaceInitPamars;
import com.pasc.business.face.net.resp.FaceCheckResp;
import com.pasc.business.face.net.resp.FaceInitResp;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.net.ApiGenerator;
import com.pasc.lib.net.upload.CountingRequestBody;
import com.pasc.lib.net.upload.UploadListener;
import com.pasc.lib.userbase.user.net.transform.RespTransformerData;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/22
 * 更改时间：2019/10/22
 */
public class FaceBiz {
    /**
     * 人脸核验初始化
     */
    public static Flowable<FaceInitResp> faceInit(String appid, int type,String aliUrl) {
        FaceInitPamars params = new FaceInitPamars(appid,type, aliUrl);
        RespV2Transformer<FaceInitResp> respTransformer = RespV2Transformer.newInstance();

        return ApiGenerator.createApi(Api.class)
                .faceInit(AppProxy.getInstance().getUserManager().getToken(),params)
                .compose(respTransformer)
                .toFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    /**
     * 二次核验人人对比
     */
    public static Single<FaceCheckResp> faceCompace(String initCode, String appid, String userId,
        String plat, String version, String model, String type, byte[] bytes, String imageBase64,
        String imageMessageDigest, UploadListener uploadListener) {
        Map<String,String> parms = new HashMap<>();
        parms.put("",initCode);

        RespTransformerData<FaceCheckResp> respTransformer = RespTransformerData.newInstance();
        CountingRequestBody countingRequestBody = new CountingRequestBody(RequestBody.create(MediaType.parse("image/jpeg"), bytes), uploadListener);

        String fileKeyName = "basmt_pingface_imgs";
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(fileKeyName, "face.jpg", countingRequestBody);

        RequestBody initCodeBody = RequestBody.create(MediaType.parse("multipart/form-data"), initCode);
        RequestBody appidBody = RequestBody.create(MediaType.parse("multipart/form-data"), appid);
        RequestBody platKey = RequestBody.create(MediaType.parse("multipart/form-data"), plat);
        RequestBody versionKey = RequestBody.create(MediaType.parse("multipart/form-data"), version);
        RequestBody modelKey = RequestBody.create(MediaType.parse("multipart/form-data"), model);
        RequestBody typeKey = RequestBody.create(MediaType.parse("multipart/form-data"), type);
        RequestBody imageBase64Key = RequestBody.create(MediaType.parse("multipart/form-data"), imageBase64);
        RequestBody imageMessageDigestKey = RequestBody.create(MediaType.parse("multipart/form-data"), imageMessageDigest);
        return ApiGenerator.createApi(Api.class)
                .faceCompare( AppProxy.getInstance().getUserManager().getToken(), initCodeBody, appidBody,
                platKey, versionKey, modelKey, typeKey, imageBase64Key, imageMessageDigestKey, filePart)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }
    public static Single<FaceCheckResp> faceInfoCompare(String userName, String idCard, String plat,
        String version, String model, String type, byte[] bytes, String imageBase64, String imageMessageDigest, UploadListener uploadListener) {
        //        FaceCheckPamars params = new FaceCheckPamars( initCode, appid, userId, plat, version, model, type, bytes);
        RespV2Transformer<FaceCheckResp> respTransformer = RespV2Transformer.newInstance();
        CountingRequestBody countingRequestBody = new CountingRequestBody(RequestBody.create(MediaType.parse("image/jpeg"), bytes), uploadListener);

        String fileKeyName = "basmt_pingface_imgs";
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(fileKeyName, "face.jpg", countingRequestBody);

        RequestBody userNameBody = RequestBody.create(MediaType.parse("multipart/form-data"), userName);
        RequestBody idCardBody = RequestBody.create(MediaType.parse("multipart/form-data"), idCard);
        RequestBody platKey = RequestBody.create(MediaType.parse("multipart/form-data"), plat);
        RequestBody versionKey = RequestBody.create(MediaType.parse("multipart/form-data"), version);
        RequestBody modelKey = RequestBody.create(MediaType.parse("multipart/form-data"), model);
        RequestBody typeKey = RequestBody.create(MediaType.parse("multipart/form-data"), type);
        RequestBody imageBase64Key = RequestBody.create(MediaType.parse("multipart/form-data"), imageBase64);
        RequestBody imageMessageDigestKey = RequestBody.create(MediaType.parse("multipart/form-data"), imageMessageDigest);
        return ApiGenerator.createApi(Api.class)
            .faceInfoCompare(userNameBody, idCardBody, platKey, versionKey, modelKey, typeKey,
                imageBase64Key, imageMessageDigestKey, filePart)
            .compose(respTransformer)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());


    }
    /**
     * 二次核验认证对比
     */
    public static Single<FaceCheckResp> faceIdCompace(String initCode,String appid,String userId,
        String plat,String version,String model,String type, byte[] bytes, String imageBase64,
        String imageMessageDigest, UploadListener uploadListener) {
        RespTransformerData<FaceCheckResp> respTransformer = RespTransformerData.newInstance();
        CountingRequestBody countingRequestBody = new CountingRequestBody(RequestBody.create(MediaType.parse("image/jpeg"), bytes), uploadListener);

        String fileKeyName = "basmt_pingface_imgs";
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(fileKeyName, "face.jpg", countingRequestBody);

        RequestBody initCodeBody = RequestBody.create(MediaType.parse("multipart/form-data"), initCode);
        RequestBody appidBody = RequestBody.create(MediaType.parse("multipart/form-data"), appid);
        RequestBody platKey = RequestBody.create(MediaType.parse("multipart/form-data"), plat);
        RequestBody versionKey = RequestBody.create(MediaType.parse("multipart/form-data"), version);
        RequestBody modelKey = RequestBody.create(MediaType.parse("multipart/form-data"), model);
        RequestBody typeKey = RequestBody.create(MediaType.parse("multipart/form-data"), type);
        RequestBody imageBase64Key = RequestBody.create(MediaType.parse("multipart/form-data"), imageBase64);
        RequestBody imageMessageDigestKey = RequestBody.create(MediaType.parse("multipart/form-data"), imageMessageDigest);
        return ApiGenerator.createApi(Api.class)
            .faceIdCompare( AppProxy.getInstance().getUserManager().getToken(), initCodeBody,
                appidBody, platKey, versionKey, modelKey, typeKey, imageBase64Key, imageMessageDigestKey, filePart)
            .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    /**
     * 删除人脸
     */
    public static Flowable<Object> faceDelect() {
        RespV2Transformer<Object> respTransformer = RespV2Transformer.newInstance();

        return ApiGenerator.createApi(Api.class)
                .faceDelet(AppProxy.getInstance().getUserManager().getToken())
                .compose(respTransformer)
                .toFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    /**
     * 支付宝结果查询
     */
    public static Flowable<FaceCheckResp> faceQueryAli(String appId,String aliId) {
        FaceCheckQueryApliPamars pamars = new FaceCheckQueryApliPamars(appId,aliId);
        RespV2Transformer<FaceCheckResp> respTransformer = RespV2Transformer.newInstance();

        return ApiGenerator.createApi(Api.class)
                .faceCheckQueryAli(AppProxy.getInstance().getUserManager().getToken(),pamars)
                .compose(respTransformer)
                .toFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
