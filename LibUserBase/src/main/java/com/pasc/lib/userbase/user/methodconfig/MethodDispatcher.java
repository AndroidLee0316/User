package com.pasc.lib.userbase.user.methodconfig;

import android.os.Bundle;
import android.text.TextUtils;

public class MethodDispatcher {
    @SuppressWarnings("unchecked")
    public static <T> T interactionWithUser(String url, Bundle bundle, Class<T> tClass) {
        if (TextUtils.isEmpty(url)) {
            return (T) new Object();
        }
        String[] split = url.split("/");
        if ("set".equals(split[1])) {
            return MethodGetManager.getInstance().getUserInfo(url, bundle, tClass);
        } else if ("get".equals(split[1])) {
            return MethodGetManager.getInstance().getUserInfo(url, bundle, tClass);
        } else if ("update".equals(split[1])) {
            return MethodGetManager.getInstance().getUserInfo(url, bundle, tClass);
        }
        return MethodGetManager.getInstance().getUserInfo(url, bundle, tClass);
    }
}
