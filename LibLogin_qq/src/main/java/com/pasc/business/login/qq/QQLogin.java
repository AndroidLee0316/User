package com.pasc.business.login.qq;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.lib.share.ShareManager;
import com.pasc.lib.share.config.AppSecretConfig;
import com.pasc.lib.userbase.user.net.param.BindThirdPartParam;
import com.pasc.lib.userbase.user.third.IThridLoginService;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.user.third.ThirdCallBack;

import java.util.HashMap;

/**
 * 功能：QQ login /
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/9
 */

@Route(path = RouterTable.Login_qq.PATH_LOGIN_ACTIVITY)

public class QQLogin implements IThridLoginService {

    private static final String TAG = "QQLogin";
    private QQLoginPresenter qqLoginPresenter;

    /**
     * QQ初始化需要的appid,如果未使用ShareManager初始化，需params传入
     */
    private static final String PARAM_QQ_APPID = "QQ_APP_ID";

    private static final String PARAM_LOGIN_TYPE = "loginType";

    private static final String PARAM_MOBILE_NO = "mobileNo";

    private static final String PARAM_IS_LOGIN = "isLogin";



    @Override
    public void auth(Context context, HashMap<String, String> params, ThirdCallBack.IAuthorizeCallBack authorizeCallBack) {

        //如果传入了appid，使用appid初始化
        if (params != null && params.containsKey(PARAM_QQ_APPID)){
            initQQ(context,params.get(PARAM_QQ_APPID));
        }else if (ShareManager.getInstance().getAppSecretConfig() == null || ShareManager.getInstance().getAppSecretConfig().getQqAppId() == null){
            //如果未初始化，且params不包含appid，则日志提示失败
            Log.e(TAG,"qq appid not init,please put appid in params");
            return;
        }
        qqLoginPresenter = new QQLoginPresenter();
        qqLoginPresenter.auth(context,authorizeCallBack);
    }

    @Override
    public void login(Context context, HashMap<String, String> params, ThirdCallBack.IAuthorizeCallBack authorizeCallBack, ThirdCallBack.ILoginCallBack loginCallBack) {
        if (params != null && params.containsKey(PARAM_QQ_APPID)){
            initQQ(context,params.get(PARAM_QQ_APPID));
        }else if (ShareManager.getInstance().getAppSecretConfig() == null || ShareManager.getInstance().getAppSecretConfig().getQqAppId() == null){
            Log.e(TAG,"qq appid not init,please put appid in params");
            return;
        }
        qqLoginPresenter = new QQLoginPresenter();
        qqLoginPresenter.login(context,authorizeCallBack,loginCallBack);
    }

    @Override
    public void bind(Context context, HashMap<String, String> params, ThirdCallBack.IAuthorizeCallBack authorizeCallBack, ThirdCallBack.IBindThirdCallBack bindThirdCallBack) {
        if (params != null && params.containsKey(PARAM_QQ_APPID)){
            initQQ(context,params.get(PARAM_QQ_APPID));
        }else if (ShareManager.getInstance().getAppSecretConfig() == null || ShareManager.getInstance().getAppSecretConfig().getQqAppId() == null){
            Log.e(TAG,"qq appid not init,please put appid in params");
            return;
        }


        BindThirdPartParam requestParam = new BindThirdPartParam();
        if (params != null){
            if (params.containsKey(PARAM_MOBILE_NO)){
                requestParam.mobile = params.get(PARAM_MOBILE_NO);
            }
            if (params.containsKey(PARAM_IS_LOGIN)){
                requestParam.isLogin = params.get(PARAM_IS_LOGIN);
            }
        }

        qqLoginPresenter = new QQLoginPresenter();
        qqLoginPresenter.bind(context,requestParam,authorizeCallBack,bindThirdCallBack);

    }

    @Override
    public void unBind(Context context, HashMap<String, String> params, ThirdCallBack.IUnBindThirdCallBack unBindThirdCallBack) {

        qqLoginPresenter = new QQLoginPresenter();
        qqLoginPresenter.unBind(unBindThirdCallBack);
    }

    @Override
    public void init(Context context) {

//        try {
//            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
//            String qqAppid = info.metaData.getString(PARAM_QQ_APPID);
//            initQQ(context, qqAppid);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * 初始化QQ所需的appID
     * @param context
     * @param appID
     */
    private void initQQ(Context context, String appID){
        AppSecretConfig.Builder builder = new AppSecretConfig.Builder();
        builder.setQqAppId(appID);
        ShareManager.getInstance().init((Application) context.getApplicationContext(), builder.build());
    }

}
