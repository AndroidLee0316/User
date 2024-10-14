package com.pasc.business.login.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import com.pasc.lib.userbase.user.urlconfig.LoginUrlManager;

import java.util.List;

/**
 * 第三方工具类
 */
public class ThirdPartUtils {

    /**
     * 支付宝包名
     */
    private static final String ALIPAY_PKG = "com.eg.android.AlipayGphone";

    /**
     * 判断微信是否可用
     */
    @SuppressLint("WrongConstant")
    public static boolean isWxAppInstalledAndSupported(Context context) {
        boolean isInstall = false;
        try {
            isInstall = context.getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_RESOLVED_FILTER) == null ? false : true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return LoginUrlManager.getInstance().getSupportWeChat() && isInstall;
    }

    /**
     * 判断qq是否可用
     */
    @SuppressLint("WrongConstant")
    public static boolean isQQAppInstalledAndSupported(Context context) {
        boolean isInstall = false;
        try {
            isInstall = context.getPackageManager().getPackageInfo("com.tencent.mobileqq", PackageManager.GET_RESOLVED_FILTER) == null ? false : true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return LoginUrlManager.getInstance().getSupportQQ() && isInstall;
    }

    /**
     * 判断支付宝是否可用
     */
    public static boolean isAlipayAppInstalledAndSupported(Context context) {
        return LoginUrlManager.getInstance().getSupportAlipay() && isAlipayInstalled(context);
    }

    /**
     * 判断是否安装了支付宝
     * @param context
     * @return
     */
    public static boolean isAlipayInstalled(Context context) {
        PackageManager manager = context.getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List<ResolveInfo> list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }


}
