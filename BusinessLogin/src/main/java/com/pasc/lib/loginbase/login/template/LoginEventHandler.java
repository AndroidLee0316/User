package com.pasc.lib.loginbase.login.template;

public interface LoginEventHandler<T> {
    void handleLoginFailEvent(int errorCode, String errorMsg);

    void handleLoginSuccessEvent(T obj);
}