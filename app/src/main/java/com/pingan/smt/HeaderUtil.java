package com.pingan.smt;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.util.AppUtils;
import com.pasc.lib.base.util.ScreenUtils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author yangzijian
 * @date 2018/7/16
 * @des 公共头信息
 * @modify
 **/
public class HeaderUtil {

    public static Map<String, String> getHeaders(boolean isDebug) {
        Context context = AppProxy.getInstance ().getContext ();
        Map<String, String> commonHeaders = new HashMap<>();
        /*****设备号****/
        commonHeaders.put ("x-device-id", getDeviceId (context));
//        /****机型信息****/
//        commonHeaders.put("MO", Build.MODEL);
        /******设备的厂商名****/
        commonHeaders.put ("x-app-platform", Build.BRAND);
        /****设备系统类型 2：android、1：ios、1|2：h5-pc h5-ios ****/
        commonHeaders.put ("x-os-type", "2");
        /****屏幕分辨率  屏幕宽x屏幕高*****/
        commonHeaders.put ("x-screen-dpi", ScreenUtils.getScreenWidth () + "x" + ScreenUtils.getScreenHeight ());
        /****ROM版本号****/
        commonHeaders.put ("x-rom-version", Build.MANUFACTURER);
        /*****操作系统版本****/
        commonHeaders.put ("x-os-version", "Android" + Build.VERSION.RELEASE);
        /*****app version*****/
//        commonHeaders.put ("x-app-version", AppUtils.getVersionCode (context) + "");
        /*****app version name *****/
//        commonHeaders.put ("x-app-version-name", AppUtils.getVersionName ());
         commonHeaders.put ("x-app-version", AppUtils.getVersionName ());

        /*****app 名称*****/

        try {
            commonHeaders.put ("x-app-name", URLEncoder.encode (getAppName (context), "UTF-8"));
        }catch (Exception e){}

        /**** 渠道号***/
        commonHeaders.put ("x-channel", getAppMetaData (AppProxy.getInstance ().getContext (), "SMT_CHANNEL", "product"));
        /***后台版本***/
        commonHeaders.put ("x-api-version", "1.2.0");
        commonHeaders.put ("Content-Type", "application/json");
        return commonHeaders;

    }

    public static Map<String, String> dynamicHeaders() {
        /****以下三个header 为动态****/
        /*****网络类型
         * 枚举：1,2 ,3
         1: 3g/4G网络
         2: WIFI网络
         3: Edge网络(2G网络)
         4:Unknown网络
         * ****/
        Map<String, String> dynamicHeaders = new HashMap<>();
        dynamicHeaders.put("x-net-type", GetNetworkType ());
        dynamicHeaders.put ("timestamp", System.currentTimeMillis () + "");
        dynamicHeaders.put ("token", AppProxy.getInstance ().getUserManager ().getToken ());
        dynamicHeaders.put ("x-api-userId", AppProxy.getInstance().getUserManager().getUserId());
        return dynamicHeaders;
    }

    //不同的类型要区别获取，以下是布尔类型的
    public static String getAppMetaData(Context context, String metaName, String defaultValue) {
        try {
            //application标签下用getApplicationinfo，如果是activity下的用getActivityInfo
            String value = context.getPackageManager ()
                    .getApplicationInfo (context.getPackageName (), PackageManager.GET_META_DATA)
                    .metaData.getString (metaName, defaultValue);
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
            return defaultValue;
        }
    }

    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager ();
            PackageInfo packageInfo = packageManager.getPackageInfo (
                    context.getPackageName (), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources ().getString (labelRes);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return "";
    }

    public static String getDeviceId(Context context) {
        String androidID = Settings.Secure.getString (context.getContentResolver (), Settings.Secure.ANDROID_ID);
        // 模拟器 Build.SERIAL 为 unknow
        // 红米手机 Build.SERIAL 是 0123456789ABCDE
        String uniqueId = androidID + "_" + Build.SERIAL;
        return uniqueId;
    }
    /*****网络类型
     * 枚举：1,2 ,3
     1: 3g/4G网络
     2: WIFI网络
     3: Edge网络(2G网络)
     4:Unknown网络
     * ****/
    public static String GetNetworkType() {
        String strNetworkType = "Unknown";
        ConnectivityManager activeNetworkInfo = (ConnectivityManager) AppProxy.getInstance ().getContext ().getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = activeNetworkInfo.getActiveNetworkInfo ();
        if (networkInfo != null && networkInfo.isConnected ()) {
            if (networkInfo.getType () == ConnectivityManager.TYPE_WIFI) {
//                strNetworkType = "WIFI";
                strNetworkType ="2";
            } else if (networkInfo.getType () == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName ();
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype ();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
//                        strNetworkType = "2G";
                        strNetworkType ="3";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
//                        strNetworkType = "3G";
                        strNetworkType ="1";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
//                        strNetworkType = "4G";
                        strNetworkType ="1";

                        break;
                    default:
                        if (TextUtils.isEmpty (_strSubTypeName)) {
                            // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                            if (_strSubTypeName.equalsIgnoreCase ("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase ("WCDMA") || _strSubTypeName.equalsIgnoreCase ("CDMA2000")) {
//                            strNetworkType = "3G";
                                strNetworkType = "1";
                            } else {
//                                strNetworkType = _strSubTypeName;
                            }
                        }
                        break;
                }
            }
        }

        return strNetworkType;
    }

}
