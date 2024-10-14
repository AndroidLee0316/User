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
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.net.ApiGenerator;
import com.pasc.lib.net.transform.RespV2Transformer;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public class PhoneBiz {
    /**
     * 发验证码
     */
    public static Flowable<SMSResp> sendSms(String mobile,String sendSmsType, String preValidCode) {
        SMSPamars pamars = new SMSPamars(mobile, sendSmsType, preValidCode);
        RespV2Transformer<SMSResp> respTransformer = RespV2Transformer.newInstance();

        return ApiGenerator.createApi(Api.class)
                .sendSms(AppProxy.getInstance().getUserManager().getToken(), pamars)
                .compose(respTransformer)
                .toFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 手机号是否合法
     */
    public static Flowable<ChangePhoneResp> phoneLegilaty(String mobile) {
        ChangePhonePamars pamars = new ChangePhonePamars(mobile);
        RespV2Transformer<ChangePhoneResp> respTransformer = RespV2Transformer.newInstance();

        return ApiGenerator.createApi(Api.class)
                .isLegilaty(AppProxy.getInstance().getUserManager().getToken(), pamars)
                .compose(respTransformer)
                .toFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 更换手机号
     */
    public static Flowable<ChangePhoneResp> changePhone(ChangePhonePamars phonePamars) {
        RespV2Transformer<ChangePhoneResp> respTransformer = RespV2Transformer.newInstance();

        return ApiGenerator.createApi(Api.class)
                .changePhone(AppProxy.getInstance().getUserManager().getToken(), phonePamars)
                .compose(respTransformer)
                .toFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 判断支付是否注销
     */
    public static Flowable<AccoutCalceResp> payIsCalce() {
        RespV2Transformer<AccoutCalceResp> respTransformer = RespV2Transformer.newInstance();

        return ApiGenerator.createApi(Api.class)
                .payCalce(AppProxy.getInstance().getUserManager().getToken())
                .compose(respTransformer)
                .toFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 提交注册申请
     */
    public static Flowable<AccoutCalceResp> commitCalce(String checkType, String verificationCode, String verificationType) {
        AccoutCalcePamars pamars = new AccoutCalcePamars(checkType, verificationCode, verificationType);
        RespV2Transformer<AccoutCalceResp> respTransformer = RespV2Transformer.newInstance();

        return ApiGenerator.createApi(Api.class)
                .accoutCalceCommit(AppProxy.getInstance().getUserManager().getToken(), pamars)
                .compose(respTransformer)
                .toFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 提交注册申请
     */
    public static Flowable<AccoutCalceResp> commitCalce(String checkType, String credential) {
        AccoutCalcePamars pamars = new AccoutCalcePamars(checkType, credential);
        RespV2Transformer<AccoutCalceResp> respTransformer = RespV2Transformer.newInstance();

        return ApiGenerator.createApi(Api.class)
                .accoutCalceCommit(AppProxy.getInstance().getUserManager().getToken(), pamars)
                .compose(respTransformer)
                .toFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 手机号是否正确
     */
    public static Flowable<ChangePhoneResp> mobileVerly(String mobile) {
        ChangePhonePamars pamars = new ChangePhonePamars();
        pamars.oldMobile = mobile;
        RespV2Transformer<ChangePhoneResp> respTransformer = RespV2Transformer.newInstance();

        return ApiGenerator.createApi(Api.class)
                .mobileVerify(AppProxy.getInstance().getUserManager().getToken(), pamars)
                .compose(respTransformer)
                .toFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发设置手机号验证码
     */
    public static Flowable<SMSNewResp> sendNewSms(String newMobile,String credential) {
        SMSNewParams params = new SMSNewParams(newMobile, credential);
        RespV2Transformer<SMSNewResp> respTransformer = RespV2Transformer.newInstance();

        return ApiGenerator.createApi(Api.class)
            .sendNewSms(params)
            .compose(respTransformer)
            .toFlowable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
    /**
     * 设置手机号
     */
    public static Flowable<SetPhoneResp> setPhone(SetPhoneParams setPhoneParams) {
        RespV2Transformer<SetPhoneResp> respTransformer = RespV2Transformer.newInstance();

        return ApiGenerator.createApi(Api.class)
            .setPhone(setPhoneParams)
            .compose(respTransformer)
            .toFlowable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
}
