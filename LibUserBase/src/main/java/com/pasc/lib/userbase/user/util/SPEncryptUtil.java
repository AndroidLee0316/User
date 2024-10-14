//package com.pasc.lib.userbase.user.util;
//
//import android.text.TextUtils;
//import android.util.Base64;
//import android.util.Log;
//
//import com.pasc.lib.base.util.EncryptUtils;
//import com.pasc.lib.base.util.SPUtils;
//
///**
// * 功能：
// *
// * @author lichangbao702
// * @email : lichangbao702@pingan.com.cn
// * @date : 2020-04-08
// */
//public class SPEncryptUtil {
//
//
//    private SPEncryptUtil() {
//    }
//
//    public static SPEncryptUtil getInstance() {
//        return SPEncryptUtilsHolder.instance;
//    }
//
//    private static class SPEncryptUtilsHolder {
//        public static SPEncryptUtil instance = new SPEncryptUtil();
//    }
//
//
//    /**
//     * 保存string类型数据，且使用base64加密
//     *
//     * @param key
//     * @param value
//     */
//    public void setParam(String key, String value) {
//
//        String encryptKey = EncryptUtils.getMD5(key);
//        String encryptValue = null;
//        try {
//            if (TextUtils.isEmpty(value)){
//                SPUtils.getInstance().setParam(encryptKey,value);
//            }else {
//                encryptValue = Base64.encodeToString(value.getBytes(), Base64.DEFAULT);
//                SPUtils.getInstance().setParam(encryptKey,encryptValue);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
//     *
//     * @param key
//     * @param defaultObject
//     * @return
//     */
//    public String getParam(String key, String defaultObject) {
//
//        String encryptKey = EncryptUtils.getMD5(key);
//        String result = (String) SPUtils.getInstance().getParam(encryptKey,defaultObject);
//        try {
//            String value = null;
//            if (TextUtils.isEmpty(result)){
//                value = result;
//            }else {
//                value = new String(Base64.decode(result.getBytes(), Base64.DEFAULT)); ;
//            }
//            return value;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return defaultObject;
//    }
//
//
//}
