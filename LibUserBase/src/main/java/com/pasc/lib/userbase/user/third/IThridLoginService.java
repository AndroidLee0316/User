package com.pasc.lib.userbase.user.third;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.pasc.lib.userbase.user.net.param.BindThirdPartParam;
import com.pasc.lib.userbase.user.third.ThirdCallBack;

import java.util.HashMap;

/**
 * 功能：第三方登陆/签约/绑定/解绑服务
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/9
 */
public interface IThridLoginService extends IProvider {

    /**
     * 授权
     * @param context
     * @param params
     * @param authorizeCallBack
     */
    void auth(Context context, HashMap<String,String> params, ThirdCallBack.IAuthorizeCallBack authorizeCallBack);

    /**
     * 第三方登陆
     * @param context   上下文
     * @param params    第三方登陆必须使用的参数
     */
    void login(Context context, HashMap<String,String> params, ThirdCallBack.IAuthorizeCallBack authorizeCallBack, ThirdCallBack.ILoginCallBack loginCallBack);

    /**
     * 第三方绑定
     * @param context
     * @param params    请求参数
     * @param authorizeCallBack
     * @param bindThirdCallBack
     */
    void bind(Context context, HashMap<String,String> params, ThirdCallBack.IAuthorizeCallBack authorizeCallBack, ThirdCallBack.IBindThirdCallBack bindThirdCallBack);

    /**
     * 第三方解绑
     * @param context
     * @param params
     * @param unBindThirdCallBack
     */
    void unBind(Context context, HashMap<String,String> params, ThirdCallBack.IUnBindThirdCallBack unBindThirdCallBack);
}
