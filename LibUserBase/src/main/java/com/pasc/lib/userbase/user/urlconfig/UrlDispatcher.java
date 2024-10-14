package com.pasc.lib.userbase.user.urlconfig;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.pasc.lib.userbase.user.util.UserUtils;

/**
 * 将URL分发给认证, 登录和人脸
 */
public class UrlDispatcher {

    /**
     * 解析asserts目录下的用户config json文件
     * @param context
     * @param userConfigAssertsPath
     */
    public static void dispatchFromAsserts(Context context, String userConfigAssertsPath) {
        if (TextUtils.isEmpty(userConfigAssertsPath)) {
            Log.e(UrlDispatcher.class.getName(),"userConfigAssertsPath is null");
            return;
        }
        String userConfigJson = UserUtils.parseFromAssets(context, userConfigAssertsPath);
        dispatch(userConfigJson);
    }

    /**
     * 解析raw目录下的用户config json文件
     * @param context
     * @param userConfigRawName
     */
    public static void dispatchFromRaw(Context context, int userConfigRawName) {
        String userConfigJson = UserUtils.parseFromRaw(context, userConfigRawName);
        dispatch(userConfigJson);
    }

    private static void dispatch(String json) {
        if (TextUtils.isEmpty(json)) {
            Log.e(UrlDispatcher.class.getName(),"userConfigAssertsPath's json is null");
            return;
        }
        try {
            UserUrlConfig userUrlConfig = new Gson().fromJson(json, UserUrlConfig.class);
            dispatchUrlToCerti(userUrlConfig.certiConfigBean);
            dispatchUrlToLogin(userUrlConfig.loginConfigBean);
            dispatchUrlToFace(userUrlConfig.faceConfigBean);
            dispatchOtherConfig(userUrlConfig.otherConfig);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("UserManagerImpl", "UrlDispatcher.dispatch" + e.getMessage());
        }
    }

    /**
     * 分发认证的配置
     */
    private static void dispatchUrlToCerti(UserUrlConfig.CertiConfigBean certiConfigBean) {
        if (certiConfigBean == null) {
            certiConfigBean = new UserUrlConfig.CertiConfigBean();
        }
        CertiUrlManager.getInstance()
                .setCertiConfigBean(certiConfigBean);
    }

    /**
     * 分发登录的配置
     */
    private static void dispatchUrlToLogin(UserUrlConfig.LoginConfigBean loginConfigBean) {
        if (loginConfigBean == null) {
            loginConfigBean = new UserUrlConfig.LoginConfigBean();
        }
        LoginUrlManager.getInstance().setLoginConfigBean(loginConfigBean);
    }

    /**
     * 分发人脸的配置
     */
    private static void dispatchUrlToFace(UserUrlConfig.FaceConfigBean faceConfigBean) {
        if (faceConfigBean == null) {
            faceConfigBean = new UserUrlConfig.FaceConfigBean();
        }
        FaceUrlManager.getInstance().setFaceConfigBean(faceConfigBean);
    }


    /**
     * 分发其它配置
     */
    private static void dispatchOtherConfig(UserUrlConfig.OtherConfigBean otherConfigBean) {
        if (otherConfigBean == null) {
            otherConfigBean = new UserUrlConfig.OtherConfigBean();
        }
        OtherConfigManager.getInstance().setOtherConfigBean(otherConfigBean);
    }

}
