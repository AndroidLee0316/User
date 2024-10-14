package com.pingan.smt;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.ImageView;

import com.pasc.business.user.PascUserConfig;
import com.pasc.business.user.PascUserFaceListener;
import com.pasc.business.user.PascUserCertListener;
import com.pasc.business.user.PascUserLoginListener;
import com.pasc.business.user.PascUserManager;
import com.pasc.business.user.PascUserManagerImpl;
import com.pasc.business.user.cert.CertConfig;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.hybrid.HybridInitConfig;
import com.pasc.lib.hybrid.PascHybrid;
import com.pasc.lib.hybrid.callback.HybridInitCallback;
import com.pasc.lib.imageloader.PascImageLoader;
import com.pasc.lib.net.ApiV2Error;
import com.pasc.lib.net.HttpCommonParams;
import com.pasc.lib.net.NetConfig;
import com.pasc.lib.net.NetManager;
import com.pasc.lib.net.resp.BaseV2Resp;
import com.pasc.lib.net.transform.NetV2Observer;
import com.pasc.lib.net.transform.NetV2ObserverManager;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.router.RouterManager;
import com.pasc.lib.router.interceptor.ApiGet;
import com.pasc.lib.share.ShareManager;
import com.pasc.lib.share.config.AppSecretConfig;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.user.util.UserManagerImpl;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by duyuan797 on 17/10/3.
 */

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initShare();
        AppProxy.getInstance()
                .init((Application) getApplicationContext(), BuildConfig.DEBUG)
                .setIsDebug(BuildConfig.DEBUG)
                .setHost(BuildConfig.HOST)
                .setH5Host(BuildConfig.HOST)
                .setVersionName(BuildConfig.VERSION_NAME);

        initImageLoader(this);
//        initStatistics();
        initARouter((Application) getApplicationContext());
        initNet((Application)getApplicationContext(),BuildConfig.HOST);
        initWebPage();
        registerUserStatus(this);


        PascUserManager.getInstance().init(this, new PascUserManagerImpl(),null).setCertListener(new PascUserCertListener() {

            @Override
            public void onCertificationSuccess() {


            }

            @Override
            public void onCertificationFailed() {

            }

            @Override
            public void onCertificationCancled() {

            }
        }).setLoginListener(new PascUserLoginListener() {
            @Override
            public void onLoginSuccess() { }

            @Override
            public void onLoginFailed() {

            }

            @Override
            public void onLoginCancled() {
            }
        }).setFaceListener(new PascUserFaceListener() {
            @Override
            public void onRegisterSuccess() {
            }

            @Override
            public void onRegisterCancled() {
            }

            @Override
            public void onResetSuccess() {
            }

            @Override
            public void onResetCancled() {
            }

            @Override
            public void onSetFaceResult(boolean isFaceOpen) {
            }
        });
    }

    private void initShare() {
        String PARAM_QQ_APPID = "QQ_APP_ID";
        String PARAM_WECHAT_APPID = "WECHAT_APP_ID";

        ApplicationInfo info = null;
        try {
            info = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String qqAppid = String.valueOf(info.metaData.getInt(PARAM_QQ_APPID));
        String wechatAppid = info.metaData.getString(PARAM_WECHAT_APPID);
        AppSecretConfig.Builder builder = new AppSecretConfig.Builder();
        builder.setQqAppId(qqAppid)
                .setWechatAppId(wechatAppid);
        ShareManager.getInstance().init(this, builder.build());
    }


    private void initWebPage() {

        PascHybrid.getInstance().init(new HybridInitConfig().setHybridInitCallback(new HybridInitCallback() {
            @Override
            public void loadImage(ImageView imageView, String s) {

            }

            @Override
            public void setWebSettings(WebSettings webSettings) {
                webSettings.setUserAgent(webSettings.getUserAgentString()
                        + "/openweb=paschybrid/NTSMT_Android,VERSION:"
                        + "1.4.0");
            }

            @Override
            public void onWebViewCreate(WebView webView) {

            }

            @Override
            public void onWebViewProgressChanged(WebView webView, int i) {

            }

            @Override
            public void onWebViewPageFinished(WebView webView, String s) {

            }
        }));

    }

    /**
     * 初始化图片加载框架
     */
    private void initImageLoader(Context context) {
        PascImageLoader.getInstance().init(context, PascImageLoader.GLIDE_CORE, R.color.C_EAF7FF);
    }

    /**
     * 初始化路由
     */
    private void initARouter(Application application) {
        RouterManager.initARouter(application, BuildConfig.DEBUG);
        RouterManager.instance().setApiGet(new ApiGet() {
            @Override
            public boolean isLogin() {
                return UserManagerImpl.getInstance().isLogin();
            }

            @Override
            public boolean isCertification() {
                return UserManagerImpl.getInstance().isCertified();
            }

            @Override
            public void beforeInterceptor(String s, Bundle bundle) {
                super.beforeInterceptor(s, bundle);
            }

            @Override
            public void gotoLogin(String s, Bundle bundle) {
                BaseJumper.jumpARouter(RouterTable.Login.PATH_LOGIN_ACTIVITY);
            }

            @Override
            public void gotoCertification(String s, Bundle bundle) {
                super.gotoCertification(s, bundle);
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("finish_when_cert_success",true);
                BaseJumper.jumpARouter(RouterTable.Cert.PATH_CERT_AUTH_TYPE_ACT);
            }
        });

    }

    /****初始化网络****/
    private void initNet(Context context, String host) {
        NetConfig config = new NetConfig.Builder(context)
                .baseUrl(host)
                .headers(getHeaders())
                .isDebug(BuildConfig.DEBUG)
                .build();
        HttpCommonParams.getInstance()
                .setInjectHandler(new HttpCommonParams.InjectCommonHeadersHandler() {
                    @Override public void onInjectCommonHeaders(Map<String, String> map) {
                        map.putAll(HeaderUtil.getHeaders(BuildConfig.DEBUG));
                        map.putAll(HeaderUtil.dynamicHeaders());
                    }
                });
        NetManager.init(config);
    }

    /***监听网络 用户状态**/
    private void registerUserStatus(Context context) {
        NetV2ObserverManager.getInstance().registerObserver(new NetV2Observer() {
            @Override
            public void notifyErrorNet(BaseV2Resp baseV2Resp) {
                String code = baseV2Resp.code;
                if ("200".equals(code)) {
                } else {
                    if (("USER_TOKEN_INVALID".equals(code))) {//token失效了
                        AppProxy.getInstance().getUserManager().exitUser(context);
                        BaseJumper.jumpARouter(RouterTable.Login.PATH_LOGIN_ACTIVITY);

                        throw new ApiV2Error(code, baseV2Resp.msg);
                    } else if ("USER_TOKEN_KICK".equals(code)) {//账号被踢了
//                        Intent intent = new Intent(context, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                        UserManagerImpl.getInstance().userKicked(getApplicationContext());
                        //这里需要添加账号被踢回调
                        throw new ApiV2Error(code, "");
                    }
                }
            }
        });
    }

//    private void initStatistics(String channel) {
//        // UMeng统计
//        UmengStatisticsConfig umengStatisticsConfig = new UmengStatisticsConfig();
//        umengStatisticsConfig.setAppKey(BuildConfig.PRODUCT_MODE ? AppStatsConfigs.UMENG_APPKEY_PRODUCT : AppStatsConfigs.UMENG_APPKEY_BETA);
//        umengStatisticsConfig.setPushSecret(AppStatsConfigs.UMENG_PUSH_KEY);
//        umengStatisticsConfig.setChannel(channel);
//        umengStatisticsConfig.setLogEnable(AppProxy.getInstance().isDebug());
//        StatisticsManager.getInstance().addStatistics(umengStatisticsConfig.createPascStats(this));
//
//        //天眼
//        TDStatisticsConfig tdStatisticsConfig = new TDStatisticsConfig();
//        tdStatisticsConfig.setAppID(BuildConfig.PRODUCT_MODE ? AppStatsConfigs.TD_APPKEY_PRODUCT : AppStatsConfigs.TD_APPKEY_BETA);
//        tdStatisticsConfig.setChannel(channel);
//        tdStatisticsConfig.setLogOn(true);
//        StatisticsManager.getInstance().addStatistics(tdStatisticsConfig.createPascStats(this));
//    }



    private Map<String, String> getHeaders() {
        Map<String, String> commonHeaders = new HashMap<>();

        /***后台版本***/
        commonHeaders.put("x-api-version", "1.2.0");
        commonHeaders.put("Content-Type", "application/json");
        try {
            Log.e("token==",AppProxy.getInstance().getUserManager().getToken()+"");
            commonHeaders.put("token",AppProxy.getInstance().getUserManager().getToken());
        }catch (Exception e){

        }

        return commonHeaders;

    }

}
