package com.pasc.lib.userbase.user.methodconfig;

import android.os.Bundle;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class MethodUpdateManager {
    private static volatile MethodUpdateManager instance;
    private final Map<String, Method> methodCache;

    private MethodUpdateManager() {
        methodCache = new HashMap<>();
    }

    public static MethodUpdateManager getInstance() {
        if (instance == null) {
            synchronized (MethodUpdateManager.class) {
                if (instance == null) {
                    instance = new MethodUpdateManager();
                }
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T getUserInfo(String url, Bundle bundle, Class<T> tClass) {
        Method method = getMethod(url);
        try {
            if (method != null) {
                return (T) method.invoke(this, bundle, tClass);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }




    /**
     * @param url Method对应的url
     */
    private Method getMethod(String url) {
        Method method = methodCache.get(url);
        if (method == null) {
            Method[] methods = MethodUpdateManager.class.getDeclaredMethods();
            if (methods != null) {
                for (Method innerMethod : methods) {
                    int modifiers = innerMethod.getModifiers();
                    if ((modifiers & Modifier.PUBLIC) != 0) {
                        MethodGetAnno annotation = innerMethod.getAnnotation(MethodGetAnno.class);
                        if (annotation == null) {
                            continue;
                        }
                        String path = annotation.path();
                        if (url.equals(path)) {
                            method = innerMethod;
                            methodCache.put(url, innerMethod);
                            break;
                        }
                    }
                }
            }
        }
        return method;
    }
}
