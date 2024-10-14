package com.pasc.business.login.alipay;

import com.pasc.business.login.alipay.param.GetAilAuthRequestInfoParam;
import com.pasc.lib.net.resp.BaseV2Resp;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.userbase.base.data.user.ThirdLoginUser;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.user.net.param.BindThirdPartParam;
import com.pasc.lib.userbase.user.net.param.ForgetPWDParam;
import com.pasc.lib.userbase.user.net.param.GetUserInfoByPhoneParam;
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
import com.pasc.lib.userbase.user.net.resp.GetUserInfoResp;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface AlipayLoginApi {

    /**
     * 获取支付宝SDK调用加密串
     */
    @POST
    Single<BaseV2Resp<String>> getAilAuthRequestInfo(@Url String url,
                                                     @Header("token") String token,
                                                  @Body GetAilAuthRequestInfoParam param);


}
