package com.pasc.lib.userbase.user.net;

import com.pasc.lib.net.resp.BaseV2Resp;
import com.pasc.lib.net.resp.VoidObject;
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
import com.pasc.lib.userbase.user.net.param.VerifyForgetParam;
import com.pasc.lib.userbase.user.net.param.VerifyOpenFaceParam;
import com.pasc.lib.userbase.user.net.resp.CheckVerifyCodeResp;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface UserApi {

    /*模板工程已有接口开始*/

    /**
     * 发送短信验证码（对参数进行加密）
     */
    @POST
    Single<BaseV2Resp<VoidObject>> sendVerifyCode(@Url String url, @Body SendVerifyCodeParam param);

    /**
     * 通过短信验证码注册及登录
     */
    @POST
    Single<BaseV2Resp<User>> verifyAndLogin(@Url String url, @Body OtpLoginParam param);


    /**
     * 人脸登录
     */
    @Multipart
    @POST
    Single<BaseV2Resp<User>> loginFace(
        @Url String url,
        @Part("mobile") RequestBody mobile,
        @Part("content_type") RequestBody content_type,
        @Part("type") RequestBody type,
        @Part("system") RequestBody system,
        @Part("osType") RequestBody osType,
        @Part("imageContent") RequestBody imageContent,
        @Part("imageMessageDigest") RequestBody imageMessageDigest,
        @Part MultipartBody.Part file);

//    /**
//     * 根据手机号获取用户信息(头像)
//     */
//    @POST
//    Single<BaseV2Resp<GetUserInfoResp>> getUserInfoByMobile(@Url String url,
//                                                            @Body GetUserInfoByPhoneParam param);

    //找回账号验证新手机号码是否已注册
    @POST
    Single<BaseV2Resp<VoidObject>> verifyOpenFace(
            @Url String url,
            @Header("token") String token,
            @Body VerifyOpenFaceParam param);

    /**
     * 注册人脸识别
     */
    @Multipart
    @POST
    Single<BaseV2Resp<VoidObject>> registerFace(
        @Url String url,
        @Header("token") String token,
        @Part("mobile") RequestBody mobile,
        @Part("content_type") RequestBody content_type,
        @Part("type") RequestBody type,
        @Part("system") RequestBody system,
        @Part("imageContent") RequestBody imageContent,
        @Part("imageMessageDigest") RequestBody imageMessageDigest,
        @Part MultipartBody.Part file);

    /**
     * 是否开通人脸识别
     */
    @POST
    Single<BaseV2Resp<IsOpenFaceParam>> isOpenFace(
            @Url String url,
            @Header("token") String token,
            @Body IsOpenFaceParam param);

    /**
     * 重置人脸识别
     */
    @Multipart
    @POST
    Single<BaseV2Resp<VoidObject>> resetFace(
        @Url String url,
        @Header("token") String token,
        @Part("mobile") RequestBody mobile,
        @Part("content_type") RequestBody content_type,
        @Part("type") RequestBody type,
        @Part("system") RequestBody system,
        @Part("imageContent") RequestBody imageContent,
        @Part("imageMessageDigest") RequestBody imageMessageDigest,
        @Part MultipartBody.Part file);

    /**
     * 开启或者关闭人脸
     */

    @POST
    Single<BaseV2Resp<VoidObject>> updateOpenFace(@Url String url,
                                                  @Header("token") String token,
                                                  @Body UpdateOpenFaceParam param);

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
    Single<BaseV2Resp<String>> faceAndIdComparison(
        @Url String url,
        @Header("token") String token,
        @Part("idCard") RequestBody idCard,
        @Part("userName") RequestBody userName,
        @Part("plat") RequestBody plat,
        @Part("version") RequestBody version,
        @Part("model") RequestBody model,
        @Part("type") RequestBody type,
        @Part("imageContent") RequestBody imageContent,
        @Part("imageMessageDigest") RequestBody imageMessageDigest,
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

    /**
     * 三方账号登录
     */
    @POST
    Single<BaseV2Resp<ThirdLoginUser>> thirdLogin(@Url String url,
                                                  @Body ThirdLoginParam param);

    /**
     * 绑定三方账号
     */
    @POST
    Single<BaseV2Resp<User>> bindThird(@Url String url,
                                     @Body BindThirdPartParam param);

    /**
     * 解绑三方账号
     */
    @POST
    Single<BaseV2Resp<VoidObject>> unbindThird(@Url String url,
                                             @Header("token") String token,
                                             @Body UnbindThirdParam param);

    /**
     * 校验验证码
     */
    @POST
    Single<BaseV2Resp<CheckVerifyCodeResp>> checkVerifyCode(@Url String url,
                                                            @Body VerifyForgetParam param);

    /**
     * 密码登录
     */
    @POST
    Single<BaseV2Resp<User>> pwdLogin(@Url String url,
                                      @Body PwdLoginParam param);

    /**
     * 设置、修改密码
     */
    @POST
    Single<BaseV2Resp<VoidObject>> updatePassword(@Url String url,
                                                  @Header("token") String token,
                                                  @Body ResetPWDParam param);

    /**
     * 设置、修改密码
     */
    @POST
    Single<BaseV2Resp<VoidObject>> resetPassword(@Url String url,
                                                 @Header("token") String token,
                                                 @Body ResetPWDParam param);

    /**
     * 找回密码
     */
    @POST
    Single<BaseV2Resp<VoidObject>> forgetPassword(@Url String url,
                                                  @Body ForgetPWDParam param);


    /*模板工程已有接口结束*/


    /**
     * 获取当前用户信息接口
     */

    @POST
    Single<BaseV2Resp<User>> getCurrentUserInfo(@Url String url,
                                                            @Header("token") String token);


    @POST
    Single<BaseV2Resp<VoidObject>> unregister(@Url String url);


    @POST
    Single<BaseV2Resp<VoidObject>> logOut(@Url String url);

}
