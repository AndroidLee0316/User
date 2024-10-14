package com.pasc.business.user.presenter;

import android.text.TextUtils;

import com.pasc.business.user.iview.AccoutCalceView;
import com.pasc.business.user.iview.InputPhoneView;
import com.pasc.business.user.net.PhoneBiz;
import com.pasc.business.user.net.resp.AccoutCalceResp;
import com.pasc.business.user.net.resp.ChangePhoneResp;
import com.pasc.lib.net.resp.BaseRespThrowableObserver;
import com.pasc.lib.userbase.base.utils.CommonUtils;

import io.reactivex.functions.Consumer;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public class AccoutCalcePresenter implements IPresenter {
    public AccoutCalceView view;

    public AccoutCalcePresenter(AccoutCalceView view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        if (!disposables.isDisposed()) {
            disposables.clear();
        }
        view = null;
    }

    public void isFinishPay() {
        disposables.add(PhoneBiz.payIsCalce().subscribe(new Consumer<AccoutCalceResp>() {
            @Override
            public void accept(AccoutCalceResp resp) throws Exception {
                view.dismissLoadings();
                if (view != null) {
                    view.isFinishPay(resp);
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
    }

    public void commit(String checkType,  String credential) {
        disposables.add(PhoneBiz.commitCalce(checkType, credential).subscribe(new Consumer<AccoutCalceResp>() {
            @Override
            public void accept(AccoutCalceResp resp) throws Exception {
                view.dismissLoadings();
                if (view != null) {
                    view.commit(resp);
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
    }
}
