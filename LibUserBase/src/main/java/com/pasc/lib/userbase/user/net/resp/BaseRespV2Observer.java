package com.pasc.lib.userbase.user.net.resp;

import com.pasc.lib.log.PascLog;
import com.pasc.lib.net.ApiError;
import com.pasc.lib.net.ApiV2Error;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by kuangxiangkui192 on 2018/12/24.
 */
public class BaseRespV2Observer<T> implements SingleObserver<T> {
    @Override
    public void onSubscribe(Disposable disposable) {
    }

    @Override
    public void onSuccess(T t) {
    }

    @Override
    public void onError(Throwable throwable) {
        PascLog.e(throwable);
        if (throwable instanceof ApiV2Error) {
            ApiV2Error error = (ApiV2Error) throwable;
            onError(error.getMsg());
            onError(error.getCode(), error.getMsg());
        } else if (throwable instanceof ApiError) {
            ApiError error = (ApiError) throwable;
            onError(error.getMsg());
            onError(String.valueOf(error.getCode()), error.getMsg());
        } else {
            onError(throwable.getMessage());
        }
    }

    public void onError(String msg) {
    }

    public void onError(String code, String msg) {
    }
}
