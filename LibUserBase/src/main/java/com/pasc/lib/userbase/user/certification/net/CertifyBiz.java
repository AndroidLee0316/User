package com.pasc.lib.userbase.user.certification.net;

import com.pasc.lib.base.AppProxy;
import com.pasc.lib.net.ApiGenerator;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.net.transform.RespV2Transformer;
import com.pasc.lib.net.upload.CountingRequestBody;
import com.pasc.lib.net.upload.UploadListener;
import com.pasc.lib.userbase.user.certification.net.param.CheckIdCardParam;
import com.pasc.lib.userbase.user.net.transform.RespTransformerData;
import com.pasc.lib.userbase.user.urlconfig.CertiUrlManager;
import com.pasc.lib.userbase.user.urlconfig.FaceUrlManager;
import com.pasc.lib.userbase.user.certification.net.param.RealNameByBankParam;
import com.pasc.lib.userbase.user.certification.net.param.ConfirmByBankParam;
import com.pasc.lib.userbase.user.certification.net.param.QueryAuthCountParam;
import com.pasc.lib.userbase.user.certification.net.param.QueryIsCertedParam;
import com.pasc.lib.userbase.user.certification.net.param.SendVerifyCodeParam;
import com.pasc.lib.userbase.user.certification.net.resp.CertifyCompleteResp;
import com.pasc.lib.userbase.user.certification.net.resp.RealNameByBankResp;
import com.pasc.lib.userbase.user.net.transform.RespTransformerData;
import com.pasc.lib.userbase.user.urlconfig.CertiUrlManager;
import com.pasc.lib.userbase.user.urlconfig.FaceUrlManager;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class CertifyBiz {

    /**
     * 数据匹配
     *
     * @param idNo       身份证号
     * @param name       姓名
     * @param bankCardNo 银行卡号
     * @param mobileNo   手机号
     */
    public static Single<RealNameByBankResp> realNameByBank(String idNo, String name, String bankCardNo, String mobileNo) {
        String url = CertiUrlManager.getInstance().getByBankUrl();
        RealNameByBankParam certificationParam = new RealNameByBankParam(idNo, "0", name, bankCardNo, mobileNo);
        RespTransformerData<RealNameByBankResp> respV2Transformer = RespTransformerData.newInstance();
        return ApiGenerator.createApi(CertifyApi.class)
                .certificationByBank(url, AppProxy.getInstance().getUserManager().getToken(), certificationParam)
                .compose(respV2Transformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发送短信验证码y
     */
    public static Single<VoidObject> sendVerifyCode(String mobile) {
        String url = CertiUrlManager.getInstance().getSendVerifyCodeUrl();
        SendVerifyCodeParam param = new SendVerifyCodeParam(mobile, "SMS_REAL_NAME_AUTH");
        RespV2Transformer<VoidObject> transformer = RespV2Transformer.newInstance();
        return ApiGenerator.createApi(CertifyApi.class)
                .sendVerifyCode(url, AppProxy.getInstance().getUserManager().getToken(), param)
                .compose(transformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查询认证次数是否超过最大允许次数
     *
     * @param certType 1: 银行卡认证, 2:人脸认证
     */
    public static Single<VoidObject> queryAuthCount(String certType) {
        String url = CertiUrlManager.getInstance().getQueryAuthCountUrl();
        QueryAuthCountParam param = new QueryAuthCountParam(certType);
        RespV2Transformer<VoidObject> transformer = RespV2Transformer.newInstance();
        return ApiGenerator.createApi(CertifyApi.class)
                .queryAuthCount(url, AppProxy.getInstance().getUserManager().getToken(), param)
                .compose(transformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查询该身份信息是否被其他用户认证过
     * 20191230 过期，使用函数{@checkIsCerted}替换
     * @param idNo     身份证号
     * @param certType 1: 银行卡认证, 2:人脸认证
     */
    @Deprecated
    public static Single<String> queryIsCerted(String idNo, String certType) {
        String url = CertiUrlManager.getInstance().getQueryIsCertedUrl();
        QueryIsCertedParam param = new QueryIsCertedParam(idNo, certType);
        RespV2Transformer<String> transformer = RespV2Transformer.newInstance();
        return ApiGenerator.createApi(CertifyApi.class)
                .queryIsCerted(url, AppProxy.getInstance().getUserManager().getToken(), param)
                .compose(transformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 实名认证接口完成
     */
    public static Single<CertifyCompleteResp> confirmByBank(RealNameByBankResp resp, String verifyCode) {
        ConfirmByBankParam confirmByBankParam =
                new ConfirmByBankParam(resp.idNo, resp.idType, resp.name, resp.bankCardno, resp.mobileNo, resp.exists, resp.conflict, verifyCode, "SMS_REAL_NAME_AUTH");
        RespV2Transformer<CertifyCompleteResp> respV2Transformer = RespV2Transformer.newInstance();
        String url = CertiUrlManager.getInstance().getConfirmByBankUrl();
        return ApiGenerator.createApi(CertifyApi.class)
                .confirmByBank(url, AppProxy.getInstance().getUserManager().getToken(), confirmByBankParam)
                .compose(respV2Transformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 平安人证对比
     */
    public static Single<String> faceAndIdComparison(byte[] bytes, String idCard,
                                                         String userName, String plat, String version,
                                                         String model, String type, UploadListener uploadListener) {
        String url = FaceUrlManager.getInstance().getFaceAndIdComparisonUrl();

        CountingRequestBody countingRequestBody = new CountingRequestBody(RequestBody.create(MediaType.parse("image/jpeg"), bytes), uploadListener);

        String fileKeyName = "basmt_pingface_imgs";

        MultipartBody.Part filePart = MultipartBody.Part.createFormData(fileKeyName, "face.jpg", countingRequestBody);

        RequestBody idCardKey = RequestBody.create(MediaType.parse("multipart/form-data"), idCard);
        RequestBody userNameKey = RequestBody.create(MediaType.parse("multipart/form-data"), userName);
        RequestBody platKey = RequestBody.create(MediaType.parse("multipart/form-data"), plat);
        RequestBody versionKey = RequestBody.create(MediaType.parse("multipart/form-data"), version);
        RequestBody modelKey = RequestBody.create(MediaType.parse("multipart/form-data"), model);
        RequestBody typeKey = RequestBody.create(MediaType.parse("multipart/form-data"), type);
        return ApiGenerator.createApi(CertifyApi.class)
                .faceAndIdComparison(url, AppProxy.getInstance().getUserManager().getToken(), idCardKey, userNameKey, platKey, versionKey, modelKey, typeKey, filePart)
                .compose(RespTransformerData.<String>newInstance())
//                .compose(RespV2Transformer.<VoidObject>newInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 实名认证成功后开通或者覆盖人脸
     *
     * @param checkType 0: 新增, 1:覆盖
     */
    public static Single<VoidObject> openFaceCertificationLogin(byte[] bytes, String mobile, String contentType,
                                                                int checkType, UploadListener uploadListener) {
        String url = FaceUrlManager.getInstance().getOpenFaceCertificationLoginUrl();
        CountingRequestBody countingRequestBody = new CountingRequestBody(RequestBody.create(MediaType.parse("image/jpeg"), bytes), uploadListener);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("cssmt_uploadAttr", "face.jpg", countingRequestBody);
        RequestBody mobileKey = RequestBody.create(MediaType.parse("multipart/form-data"), mobile);
        RequestBody contentTypeKey = RequestBody.create(MediaType.parse("multipart/form-data"), contentType);
        RequestBody checkTypeKey = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(checkType));

        return ApiGenerator.createApi(CertifyApi.class)
                .openOrOverrideFacelogin(url, AppProxy.getInstance().getUserManager().getToken(), mobileKey, contentTypeKey, checkTypeKey, filePart)
                .compose(RespV2Transformer.<VoidObject>newInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查询该身份信息是否被其他用户认证过
     *
     * @param idNo     身份证号
     * @param certType 1: 银行卡认证, 2:人脸认证
     */
    public static Single<String> checkIsCerted(String userName,String idNo, String certType) {
        String url = CertiUrlManager.getInstance().getCheckIdCardUser();
        CheckIdCardParam param = new CheckIdCardParam(userName,idNo, certType);
        RespV2Transformer<String> transformer = RespV2Transformer.newInstance();
        return ApiGenerator.createApi(CertifyApi.class)
                .checkIsCerted(url, AppProxy.getInstance().getUserManager().getToken(), param)
                .compose(transformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
