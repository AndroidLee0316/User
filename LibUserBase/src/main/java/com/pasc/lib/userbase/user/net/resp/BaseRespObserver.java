package com.pasc.lib.userbase.user.net.resp;

import com.pasc.lib.log.PascLog;
import com.pasc.lib.net.ExceptionHandler;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * 基本响应Single回调
 * <p>
 * Created by duyuan797 on 2017/3/23.
 */
public abstract class BaseRespObserver<T> implements SingleObserver<T> {

    @Override
    public void onSubscribe(Disposable disposable) {
    }

    @Override
    public void onSuccess(T t) {
    }

    @Override
    public void onError(Throwable throwable) {
        PascLog.e(throwable);
        int errorCode = ExceptionHandler.getExceptionWithCode(throwable);
        //        if (errorCode != -1) {
        onError(errorCode, ExceptionHandler.handleException(throwable));
        //        }
        onError(ExceptionHandler.handleException(throwable));
    }

    public void onError(String msg) {
    }

    public void onError(int code, String msg) {
    }
}
