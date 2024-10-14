package com.pasc.business.cert.config;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：给外部提供认证自定义的挂历累
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/12/20
 */
public class CertOutConfigManager {


    /**
     * 刷新第三方认证结果：认证成功
     */
    public static final int CUSTOM_CERT_RESULT_SUCCESS = 1;

    /**
     * 刷新第三方认证结果：认证失败
     */
    public static final int CUSTOM_CERT_RESULT_FAILED = 0;

    /**
     * 刷新第三方认证结果：取消认证
     */
    public static final int CUSTOM_CERT_RESULT_CANCEL = -1;

    /**
     * 自定义认证列表
     */
    private List<CustomCertItem> customCertList;

    /**
     * 自定义认证认证状态监听
     */
    private CustomCertResultListener customCertResultListener;

    public static class SingletonHolder{
        public static final CertOutConfigManager certConfigManager = new CertOutConfigManager();
    }

    private CertOutConfigManager(){
        customCertList = new ArrayList<>();
    }

    public static CertOutConfigManager getInstance(){
        return SingletonHolder.certConfigManager;
    }

    public List<CustomCertItem> getCustomCertList() {
        return customCertList;
    }

    /**
     * 添加一个认证项
     * @param position
     * @param icon
     * @param name
     * @param desc
     * @param certType
     * @param callBack
     */
    public void addCert(int position, int icon, String name, String desc, int certType, CustomCertClickCallBack callBack){

        CustomCertInfo certInfo = new CustomCertInfo();
        certInfo.icon = icon;
        certInfo.certName = name;
        certInfo.certDesc = desc;
        certInfo.certType = certType;

        CustomCertItem certItem = new CustomCertItem();
        certItem.certInfo = certInfo;
        certItem.certPosition = position;
        certItem.clickCallBack = callBack;

        customCertList.add(certItem);
    }

    /**
     * 清除认证列表
     */
    public void clearAllCustomCert(){
        customCertList.clear();
    }

    public CustomCertResultListener getCustomCertResultListener() {
        return customCertResultListener;
    }

    public void registerCustomCertResultListener(CustomCertResultListener listener){
        customCertResultListener = listener;
    }


    public void unRegisterCustomCertResultListener(){
        customCertResultListener = null;
    }



    /**
     * 自定义认证显示项
     */
    public class CustomCertItem {
        /**
         * 认证项类型与位置的结合参数
         */
        public int certPosition;
        /**
         * 认证项信息
         */
        public CustomCertInfo certInfo = new CustomCertInfo();
        /**
         * 认证项点击回调
         */
        public CustomCertClickCallBack clickCallBack;
    }

    /**
     * 自定义认证信息
     */
    public class CustomCertInfo {
        /**
         * 图标
         */
        public int icon;
        /**
         * 认证名称
         */
        public String certName;
        /**
         * 认证详情
         */
        public String certDesc;
        /**
         * 认证类型
         */
        public int certType;
    }

    /**
     * 自定义认证item的点击事件回调
     */
    public static interface CustomCertClickCallBack {
        public void onClick(Activity activity);
    }

    /**
     * 认证结果的回调
     */
    public interface CustomCertResultListener{
        public void onSuccess();
        public void onFailed();
        public void onCancel();
    }
}
