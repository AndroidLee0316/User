package com.pasc.business.user.presenter;

import android.text.TextUtils;

import com.pasc.business.user.iview.InputPhoneView;
import com.pasc.business.user.net.PhoneBiz;
import com.pasc.business.user.net.resp.ChangePhoneResp;
import com.pasc.business.user.net.resp.SMSNewResp;
import com.pasc.lib.net.resp.BaseRespThrowableObserver;
import com.pasc.lib.userbase.base.utils.CommonUtils;

import io.reactivex.functions.Consumer;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public class InputPhonePresenter implements IPresenter{
    public InputPhoneView view;
    public InputPhonePresenter(InputPhoneView view){
        this.view = view;
    }
    @Override
    public void onDestroy() {
        if (!disposables.isDisposed()) {
            disposables.clear();
        }
        view = null;
    }
    public void phoneLegilaty(String mobile){
        view.showLoadings();
        disposables.add(PhoneBiz.phoneLegilaty(mobile).subscribe(new Consumer<ChangePhoneResp>() {
            @Override
            public void accept(ChangePhoneResp resp) throws Exception {
                view.dismissLoadings();
                if (view != null){
                    view.isLegatily(resp);
                }

            }
        },new BaseRespThrowableObserver(){
            @Override
            public void onV2Error(String code, String error) {
                super.onV2Error(code, error);
                view.dismissLoadings();
                view.onError(code,error);
            }
        }));
    }
    public void moblieVerly(String mobile){
        view.showLoadings();
        disposables.add(PhoneBiz.mobileVerly(mobile).subscribe(new Consumer<ChangePhoneResp>() {
            @Override
            public void accept(ChangePhoneResp resp) throws Exception {
                view.dismissLoadings();
                if (view != null){
                    view.mobileVerly(resp);
                }

            }
        },new BaseRespThrowableObserver(){
            @Override
            public void onV2Error(String code, String error) {
                super.onV2Error(code, error);
                view.dismissLoadings();
                view.onError(code,error);
            }
        }));
    }
    public boolean checkVerifyCode(String verifyCode) {
        return !TextUtils.isEmpty(verifyCode) && verifyCode.matches("^[0-9]{6}$");
    }
    public void sendSms(String mobile, String credential) {
        view.showLoadings();
        disposables.add(PhoneBiz.sendNewSms(mobile, credential).subscribe(new Consumer<SMSNewResp>() {
            @Override
            public void accept(SMSNewResp resp) throws Exception {
                view.dismissLoadings();
                if (view != null) {
                    view.sendSms(resp);
                }
            }
        }, new BaseRespThrowableObserver() {
            @Override
            public void onV2Error(String code, String error) {
                super.onV2Error(code, error);
                view.dismissLoadings();
                view.onError(code, error);
            }
        }));
        //        ToastUtils.toastMsg("验证码已发送");
    }

}
