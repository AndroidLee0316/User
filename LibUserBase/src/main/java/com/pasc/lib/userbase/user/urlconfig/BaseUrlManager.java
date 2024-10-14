package com.pasc.lib.userbase.user.urlconfig;
import android.util.Log;

import com.pasc.lib.base.AppProxy;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/9/13
 */
public class BaseUrlManager {


    /**
     * @param url 接口地址
     * @return 给地址添加动态域名
     */
    public static String addPrefixHost(String url) {
        if (url != null && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file///android_asset/"))){
            return url;
        }
        return AppProxy.getInstance().getHost() + url;

    }

    public static String addPrefixH5Host(String url) {
        if (url != null && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file:///android_asset/"))){
            return url;
        }
        return AppProxy.getInstance().getH5Host() + url;
    }


}
