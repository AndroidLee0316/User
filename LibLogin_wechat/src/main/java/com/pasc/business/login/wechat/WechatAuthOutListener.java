package com.pasc.business.login.wechat;

/**
 * 功能：微信授权回调监听
 * 在 WXEntryActivity 需调用 onWechatAuthFailed 或者 onWechatAuthSuccess 函数
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/10
 */
public class WechatAuthOutListener {

    private CallBack mCallback;

    private static WechatAuthOutListener outListener;

    public static WechatAuthOutListener getInstance(){
        if (outListener == null){
            synchronized (WechatAuthOutListener.class){
                if (outListener == null){
                    outListener = new WechatAuthOutListener();
                }
            }
        }
        return outListener;
    }

    private WechatAuthOutListener(){

    }

    /**
     * 调用微信授权前设置监听
     * @param mCallback
     */
    protected void setCallback(CallBack mCallback) {
        this.mCallback = mCallback;
    }

    /**
     * 微信授权失败
     * 注：在WXEntryActivity中回调
     * @param errorCode 错误码
     * @param errorMsg  错误信息
     */
    public void onWechatAuthFailed(String errorCode, String errorMsg){
        if (mCallback != null){
            mCallback.authFailed(errorCode, errorMsg);
        }
    }

    /**
     * 微信授权成功
     * 注：在WXEntryActivity中回调
     * @param code  授权code
     */
    public void onWechatAuthSuccess(String code){
        if (mCallback != null){
            mCallback.authSuccess(code);
        }
    }

    public static interface CallBack{
        /**
         * 授权失败
         * @param errorCode 错误码
         * @param errorMsg  错误信息
         */
        void authFailed(String errorCode, String errorMsg);

        /**
         * 授权成功
         * @param code  授权code
         */
        void authSuccess(String code);
    }


}
