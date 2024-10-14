package com.pasc.lib.userbase.base;

public interface CallBack<T> {
    void onSuccess(T data);
    void onFail(String code, String msg);
}
