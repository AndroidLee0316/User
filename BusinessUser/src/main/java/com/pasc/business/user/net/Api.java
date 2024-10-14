package com.pasc.business.user.net;

import com.pasc.business.user.net.pamars.AccoutCalcePamars;
import com.pasc.business.user.net.pamars.ChangePhonePamars;
import com.pasc.business.user.net.pamars.SMSNewParams;
import com.pasc.business.user.net.pamars.SMSPamars;
import com.pasc.business.user.net.pamars.SetPhoneParams;
import com.pasc.business.user.net.resp.AccoutCalceResp;
import com.pasc.business.user.net.resp.ChangePhoneResp;
import com.pasc.business.user.net.resp.SMSNewResp;
import com.pasc.business.user.net.resp.SMSResp;
import com.pasc.business.user.net.resp.SetPhoneResp;
import com.pasc.lib.net.resp.BaseV2Resp;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public interface Api {
    @POST(UrlManager.URL_CHANGE_PHONE_SEND_SMS)
    Single<BaseV2Resp<SMSResp>> sendSms(@Header("token") String token,
                                        @Body SMSPamars param);
    @POST(UrlManager.URL_CHANGE_PHONE_LEGALITY)
    Single<BaseV2Resp<ChangePhoneResp>> isLegilaty(@Header("token") String token,
                                                   @Body ChangePhonePamars param);
    @POST(UrlManager.URL_CHANGE_PHONE_CHANGE)
    Single<BaseV2Resp<ChangePhoneResp>> changePhone(@Header("token") String token,
                                        @Body ChangePhonePamars param);

    @POST(UrlManager.URL_ACCOUT_CALCE_PAY)
    Single<BaseV2Resp<AccoutCalceResp>> payCalce(@Header("token") String token);
    @POST(UrlManager.URL_ACCOUT_CALCE_COMMIT)
    Single<BaseV2Resp<AccoutCalceResp>> accoutCalceCommit(@Header("token") String token, @Body AccoutCalcePamars pamars);
    @POST(UrlManager.URL_CHANGE_PHONE_VERFY)
    Single<BaseV2Resp<ChangePhoneResp>> mobileVerify(@Header("token") String token, @Body ChangePhonePamars pamars);

    @POST(UrlManager.URL_NEW_PHONE_SEND_SMS)
    Single<BaseV2Resp<SMSNewResp>> sendNewSms(@Body SMSNewParams param);
    @POST(UrlManager.URL_NEW_PHONE_SET)
    Single<BaseV2Resp<SetPhoneResp>> setPhone(@Body SetPhoneParams param);
}
