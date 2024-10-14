package com.pasc.lib.userbase.user.net;

import android.text.TextUtils;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.util.DeviceUtils;
import com.pasc.lib.base.util.EncryptUtils;
import com.pasc.lib.net.ApiGenerator;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.net.transform.RespV2Transformer;
import com.pasc.lib.net.upload.CountingRequestBody;
import com.pasc.lib.net.upload.UploadListener;
import com.pasc.lib.userbase.base.data.user.ThirdLoginUser;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.user.net.param.BindThirdPartParam;
import com.pasc.lib.userbase.user.net.param.ForgetPWDParam;
import com.pasc.lib.userbase.user.net.param.IsOpenFaceParam;
import com.pasc.lib.userbase.user.net.param.OtpLoginParam;
import com.pasc.lib.userbase.user.net.param.PwdLoginParam;
import com.pasc.lib.userbase.user.net.param.ResetPWDParam;
import com.pasc.lib.userbase.user.net.param.SendVerifyCodeParam;
import com.pasc.lib.userbase.user.net.param.ThirdLoginParam;
import com.pasc.lib.userbase.user.net.param.UnbindThirdParam;
import com.pasc.lib.userbase.user.net.param.UpdateOpenFaceParam;
import com.pasc.lib.userbase.user.net.param.UploadHeadImgParam;
import com.pasc.lib.userbase.user.net.param.VerifyForgetParam;
import com.pasc.lib.userbase.user.net.param.VerifyOpenFaceParam;
import com.pasc.lib.userbase.user.net.resp.CheckVerifyCodeResp;
import com.pasc.lib.userbase.user.net.transform.CSRespTransformer;
import com.pasc.lib.userbase.user.net.transform.RespTransformerData;
import com.pasc.lib.userbase.user.net.transform.ThirdLoginRespTransformer;
import com.pasc.lib.userbase.user.urlconfig.FaceUrlManager;
import com.pasc.lib.userbase.user.urlconfig.LoginUrlManager;
import com.pasc.lib.userbase.user.util.UserManagerImpl;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class UserBiz {

    /* 模板工程已有接口开始 */

    /**
     * 发送短信验证码
     */
    public static Single<VoidObject> sendVerifyCode(String phoneNumber, String type) {
        SendVerifyCodeParam sendVerifyCodeParam = new SendVerifyCodeParam();
        sendVerifyCodeParam.mobile = phoneNumber;
        sendVerifyCodeParam.type = type;
        CSRespTransformer<VoidObject> transformer = CSRespTransformer.newInstance();

        String url = LoginUrlManager.getInstance().getSendVerifyCodeUrl();
        return ApiGenerator.createApi(UserApi.class)
                .sendVerifyCode(url, sendVerifyCodeParam)
                .compose(transformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

//    /**
//     * 根据手机号获取用户信息
//     *
//     * @param mobile 手机号
//     * @return
//     */
//    public static Single<GetUserInfoResp> getUserInfoByMobile(String mobile) {
//        GetUserInfoByPhoneParam getUserInfoParam = new GetUserInfoByPhoneParam(mobile);
//        CSRespTransformer<GetUserInfoResp> transformer = CSRespTransformer.newInstance();
//        String url = LoginUrlManager.getInstance().getUserInfoUrl();
//        return ApiGenerator.createApi(UserApi.class)
//                .getUserInfoByMobile(url, getUserInfoParam)
//                .compose(transformer)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    /**
     * 通过短信验证码注册及登录
     */
    public static Single<User> verifyAndLogin(String verifyCode,
                                              String osType,
                                              String phoneNumber) {
        OtpLoginParam.Env env = new OtpLoginParam.Env(osType,
                DeviceUtils.getDeviceInfo(AppProxy.getInstance().getApplication()).get("deviceId"));
        OtpLoginParam otpLoginParam = new OtpLoginParam(phoneNumber, verifyCode, osType, env);
        CSRespTransformer<User> respTransformer = CSRespTransformer.newInstance();

        String url = LoginUrlManager.getInstance().getMobileLoginUrl();
        return ApiGenerator.createApi(UserApi.class)
                .verifyAndLogin(url, otpLoginParam)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 平安人证对比
     */
    public static Single<String> faceAndIdComparison(byte[] bytes, String idCard,
        String userName, String plat, String version,
        String model, String type, String imageBase64,
        String imageMessageDigest, UploadListener uploadListener) {
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
        RequestBody imageBase64Key = RequestBody.create(MediaType.parse("multipart/form-data"), imageBase64);
        RequestBody imageMessageDigestKey = RequestBody.create(MediaType.parse("multipart/form-data"), imageMessageDigest);
        return ApiGenerator.createApi(UserApi.class)
            .faceAndIdComparison(url, AppProxy.getInstance().getUserManager().getToken(),
                idCardKey, userNameKey, platKey, versionKey, modelKey, typeKey, imageBase64Key,
                imageMessageDigestKey, filePart)
            .compose(RespTransformerData.<String>newInstance())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 实名认证成功后开通或者覆盖人脸
     */
    public static Single<VoidObject> openFaceCertificationLogin(byte[] bytes, String mobile, String contentType,
                                                                int checkType, UploadListener uploadListener) {
        String url = FaceUrlManager.getInstance().getOpenFaceCertificationLoginUrl();
        CountingRequestBody countingRequestBody = new CountingRequestBody(RequestBody.create(MediaType.parse("image/jpeg"), bytes), uploadListener);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("cssmt_uploadAttr", "face.jpg", countingRequestBody);
        RequestBody mobileKey = RequestBody.create(MediaType.parse("multipart/form-data"), mobile);
        RequestBody contentTypeKey = RequestBody.create(MediaType.parse("multipart/form-data"), contentType);
        RequestBody checkTypeKey = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(checkType));

        return ApiGenerator.createApi(UserApi.class)
                .openOrOverrideFacelogin(url, AppProxy.getInstance().getUserManager().getToken(), mobileKey, contentTypeKey, checkTypeKey, filePart)
                .compose(RespV2Transformer.<VoidObject>newInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<User> loginFace(byte[] bytes, String mobile, String osType,
        String contentType, String type, String system, String imageBase64, String imageMessageDigest) {

        RespTransformerData<User> respTransformer = RespTransformerData.newInstance();

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
        MultipartBody.Part filePart =
                MultipartBody.Part.createFormData("cssmt_uploadAttr", "face.jpg", fileBody);
        RequestBody mobileKey = RequestBody.create(MediaType.parse("multipart/form-data"), mobile);
        RequestBody contentTypeKey = RequestBody.create(MediaType.parse("multipart/form-data"), contentType);
        RequestBody typeKey = RequestBody.create(MediaType.parse("multipart/form-data"), type);
        RequestBody systemKey = RequestBody.create(MediaType.parse("multipart/form-data"), system);
        RequestBody osTypeKey = RequestBody.create(MediaType.parse("multipart/form-data"), osType);
        RequestBody imageBase64Key = RequestBody.create(MediaType.parse("multipart/form-data"), imageBase64);
        RequestBody imageMessageDigestKey = RequestBody.create(MediaType.parse("multipart/form-data"), imageMessageDigest);
        String url = FaceUrlManager.getInstance().getFaceComparisonUrl();
        return ApiGenerator.createApi(UserApi.class).loginFace(url,
            mobileKey,
            contentTypeKey,
            typeKey,
            systemKey,
            osTypeKey,
            imageBase64Key,
            imageMessageDigestKey,
            filePart)
            .compose(respTransformer)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 开通人脸登录功能时校验验证码
     *
     * @param mobile
     */
    public static Single<VoidObject> getVerifyOpenFace(final String mobile
            , final String verificationType, final String verificationCode) {
        if (TextUtils.isEmpty(mobile)) {
            throw new IllegalArgumentException("mobile is null");
        } else if (TextUtils.isEmpty(verificationType)) {
            throw new IllegalArgumentException("verificationType is null");
        } else if (TextUtils.isEmpty(verificationCode)) {
            throw new IllegalArgumentException("verificationCode is null");
        }

        UploadHeadImgParam fileUpload = new UploadHeadImgParam();
        fileUpload.token = AppProxy.getInstance().getUserManager().getToken();
        CSRespTransformer<VoidObject> respTransformer = CSRespTransformer.newInstance();

        String url = FaceUrlManager.getInstance().getVerifyOpenFaceUrl();
        return ApiGenerator.createApi(UserApi.class)
                .verifyOpenFace(url, fileUpload.token, new VerifyOpenFaceParam(mobile, verificationType, verificationCode))
                .compose(respTransformer)
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 重置人脸
     */
    public static Single<VoidObject> resetFace(byte[] bytes, String mobile, String contentType,
        String type, String system, String imageBase64, String imageMessageDigest) {

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
        MultipartBody.Part filePart =
                MultipartBody.Part.createFormData("cssmt_pingface_imgs", "face.jpg", fileBody);

        RequestBody mobileKey = RequestBody.create(MediaType.parse("multipart/form-data"), mobile);
        RequestBody contentTypeKey = RequestBody.create(MediaType.parse("multipart/form-data"), contentType);
        RequestBody typeKey = RequestBody.create(MediaType.parse("multipart/form-data"), type);
        RequestBody systemKey = RequestBody.create(MediaType.parse("multipart/form-data"), system);
        RequestBody imageBase64Key = RequestBody.create(MediaType.parse("multipart/form-data"), imageBase64);
        RequestBody imageMessageDigestKey = RequestBody.create(MediaType.parse("multipart/form-data"), imageMessageDigest);
        CSRespTransformer<VoidObject> respTransformer = CSRespTransformer.newInstance();
        String url = FaceUrlManager.getInstance().getResetFaceUrl();
        return ApiGenerator.createApi(UserApi.class)
                .resetFace(
                        url,
                        AppProxy.getInstance().getUserManager().getToken(),
                        mobileKey,
                        contentTypeKey,
                        typeKey,
                        systemKey,
                        imageBase64Key,
                        imageMessageDigestKey,
                        filePart)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 根据手机号校验是否存在人脸
     */
    public static Single<IsOpenFaceParam> IsOpenFace(String mobile) {
        IsOpenFaceParam isOpenFaceParam = new IsOpenFaceParam(mobile);
        CSRespTransformer<IsOpenFaceParam> respTransformer = CSRespTransformer.newInstance();
        String url = FaceUrlManager.getInstance().getOpenfaceStateByMobileUrl();
        return ApiGenerator.createApi(UserApi.class)
                .isOpenFace(url, AppProxy.getInstance().getUserManager().getToken(), isOpenFaceParam)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 注册人脸
     */
    public static Single<VoidObject> registerFace(byte[] bytes, String mobile, String contentType,
        String type, String system, String imageBase64, String imageMessageDigest) {

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
        MultipartBody.Part filePart =
                MultipartBody.Part.createFormData("cssmt_pingface_imgs", "face.jpg", fileBody);
        RequestBody mobileKey = RequestBody.create(MediaType.parse("multipart/form-data"), mobile);
        RequestBody contentTypeKey = RequestBody.create(MediaType.parse("multipart/form-data"), contentType);
        RequestBody typeKey = RequestBody.create(MediaType.parse("multipart/form-data"), type);
        RequestBody systemKey = RequestBody.create(MediaType.parse("multipart/form-data"), system);
        RequestBody imageBase64Key = RequestBody.create(MediaType.parse("multipart/form-data"), imageBase64);
        RequestBody imageMessageDigestKey = RequestBody.create(MediaType.parse("multipart/form-data"), imageMessageDigest);
        CSRespTransformer<VoidObject> respTransformer = CSRespTransformer.newInstance();
        String url = FaceUrlManager.getInstance().getRegisterFaceUrl();
        return ApiGenerator.createApi(UserApi.class)
            .registerFace(
                url,
                AppProxy.getInstance().getUserManager().getToken(),
                mobileKey,
                contentTypeKey,
                typeKey,
                systemKey,
                imageBase64Key,
                imageMessageDigestKey,
                filePart)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 开启或者关闭人脸
     *
     * @param hasOpenFace 0:关闭人脸，1:开启人脸
     * @return
     */
    public static Single<VoidObject> updateOpenFace(String token, String hasOpenFace) {

        UpdateOpenFaceParam param = new UpdateOpenFaceParam();
        param.hasOpenFace = hasOpenFace;
        CSRespTransformer<VoidObject> respTransformer = CSRespTransformer.newInstance();
        String url = FaceUrlManager.getInstance().getUpdateOpenFaceUrl();
        return ApiGenerator.createApi(UserApi.class)
                .updateOpenFace(url, token, param)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 校验验证码
     */
    public static Single<CheckVerifyCodeResp> checkVerifyCode(String mobile, String verifyType, String verifyCode) {
        String url = FaceUrlManager.getInstance().getVerifyOpenFaceUrl();

        VerifyForgetParam param = new VerifyForgetParam(mobile, verifyType, verifyCode);
        CSRespTransformer<CheckVerifyCodeResp> respTransformer = CSRespTransformer.newInstance();
        return ApiGenerator.createApi(UserApi.class)
                .checkVerifyCode(url, param)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 密码登录
     */
    public static Single<User> pwdLogin(String loginName, String password) {
        String url = LoginUrlManager.getInstance().getPwdLoginUrl();
        password = EncryptUtils.getMD5(password);
        PwdLoginParam param = new PwdLoginParam(loginName, password);
        RespV2Transformer<User> respTransformer = RespV2Transformer.newInstance();
        return ApiGenerator.createApi(UserApi.class)
                .pwdLogin(url, param)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 重设密码
     */
    public static Single<VoidObject> pwdUpdate(String mobileNo, String password,String validateCode) {
        String url = LoginUrlManager.getInstance().getPwdUpdateUrl();
        String token = UserManagerImpl.getInstance().getToken();
        ResetPWDParam param = new ResetPWDParam(mobileNo,EncryptUtils.getMD5(password),validateCode);
        RespV2Transformer<VoidObject> respTransformer = RespV2Transformer.newInstance();
        return ApiGenerator.createApi(UserApi.class)
                .updatePassword(url, token, param)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 重设密码
     */
    public static Single<VoidObject> pwdReset(String mobileNo, String password,String validateCode) {
        String url = LoginUrlManager.getInstance().getPwdResetUrl();
        String token = UserManagerImpl.getInstance().getToken();
        ResetPWDParam param = new ResetPWDParam(mobileNo,EncryptUtils.getMD5(password),validateCode);
        RespV2Transformer<VoidObject> respTransformer = RespV2Transformer.newInstance();
        return ApiGenerator.createApi(UserApi.class)
                .resetPassword(url, token, param)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 忘记密码
     */
    public static Single<VoidObject> pwdForget(String mobile, String password, String validateCode) {
        String url = LoginUrlManager.getInstance().getPwdForgetUrl();
        ForgetPWDParam param = new ForgetPWDParam(mobile, EncryptUtils.getMD5(password), validateCode);
        RespV2Transformer<VoidObject> respTransformer = RespV2Transformer.newInstance();
        return ApiGenerator.createApi(UserApi.class)
                .forgetPassword(url, param)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 绑定第三方账号
     */
    public static Single<User> bindThird(BindThirdPartParam param) {

        String url = LoginUrlManager.getInstance().getThirdBindUrl();
        CSRespTransformer<User> respTransformer = CSRespTransformer.newInstance();
        return ApiGenerator.createApi(UserApi.class)
                .bindThird(url, param)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 解绑第三方账号
     */
    public static Single<VoidObject> unbindThird(UnbindThirdParam param) {

        String url = LoginUrlManager.getInstance().getThirdUnBindUrl();
        CSRespTransformer<VoidObject> respTransformer = CSRespTransformer.newInstance();
        return ApiGenerator.createApi(UserApi.class)
                .unbindThird(url,
                        UserManagerImpl.getInstance().getToken(), param)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 三方账号登录
     */
    public static Single<ThirdLoginUser> thirdLogin(ThirdLoginParam param) {
        String url = LoginUrlManager.getInstance().getThirdLoginUrl();

        ThirdLoginRespTransformer<ThirdLoginUser> respTransformer = ThirdLoginRespTransformer.newInstance();
        return ApiGenerator.createApi(UserApi.class)
                .thirdLogin(url, param)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    /**
     * 获取当前用户信息接口(盐城项目提供的接口)
     *
     * @return
     */
    public static Single<User> getCurrentUserInfo() {
        CSRespTransformer<User> transformer = CSRespTransformer.newInstance();
        String url = LoginUrlManager.getInstance().getCurrentUserInfo();
        return ApiGenerator.createApi(UserApi.class)
                .getCurrentUserInfo(url, UserManagerImpl.getInstance().getToken())
                .compose(transformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 解绑第三方账号
     */
    public static Single<VoidObject> unRegister() {

        String url = LoginUrlManager.getInstance().getUnregisterUrl();
        RespV2Transformer<VoidObject> respTransformer = RespV2Transformer.newInstance();
        return ApiGenerator.createApi(UserApi.class)
            .unregister(url)
            .compose(respTransformer)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }


    public static Single<VoidObject> logOut() {

        String url = LoginUrlManager.getInstance().getLogOutUrl();
        RespV2Transformer<VoidObject> respTransformer = RespV2Transformer.newInstance();
        return ApiGenerator.createApi(UserApi.class)
                .logOut(url)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}