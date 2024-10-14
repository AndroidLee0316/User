package com.pasc.business.login.alipay;

import android.text.TextUtils;

import com.pasc.business.login.alipay.param.GetAilAuthRequestInfoParam;
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
import com.pasc.lib.userbase.user.net.UserApi;
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
import com.pasc.lib.userbase.user.net.param.UploadHeadImgParam;
import com.pasc.lib.userbase.user.net.param.VerifyForgetParam;
import com.pasc.lib.userbase.user.net.param.VerifyOpenFaceParam;
import com.pasc.lib.userbase.user.net.resp.CheckVerifyCodeResp;
import com.pasc.lib.userbase.user.net.resp.GetUserInfoResp;
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


public class AlipayLoginBiz {

    public static final String URL_PATH = "/api/platform/thirdUser/getAilAuthRequestInfo";
    /**
     * 三方账号登录
     */
    public static Single<String> getAilAuthRequestInfoParam(GetAilAuthRequestInfoParam param) {
        String url = LoginUrlManager.getInstance().addPrefixHost(URL_PATH);

        ThirdLoginRespTransformer<String> respTransformer = ThirdLoginRespTransformer.newInstance();
        return ApiGenerator.createApi(AlipayLoginApi.class)
                .getAilAuthRequestInfo(url, AppProxy.getInstance().getUserManager().getToken(), param)
                .compose(respTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}