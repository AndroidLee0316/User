package com.pasc.business.login.alipay;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.lib.userbase.user.net.param.BindThirdPartParam;
import com.pasc.lib.userbase.user.third.IThridLoginService;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.user.third.ThirdCallBack;

import java.util.HashMap;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/9
 */

@Route(path = RouterTable.Login_alipay.PATH_LOGIN_ACTIVITY)

public class AlipayLogin implements IThridLoginService {

    private AlipayLoginPresenter alipayLoginPresenter;

    private static final String PARAM_MOBILE_NO = "mobileNo";

    private static final String PARAM_IS_LOGIN = "isLogin";

    @Override
    public void auth(Context context, HashMap<String, String> params, ThirdCallBack.IAuthorizeCallBack authorizeCallBack) {
        alipayLoginPresenter = new AlipayLoginPresenter();
        alipayLoginPresenter.auth(context,authorizeCallBack);
    }

    @Override
    public void login(Context context, HashMap<String, String> params, ThirdCallBack.IAuthorizeCallBack authorizeCallBack, ThirdCallBack.ILoginCallBack loginCallBack) {
        alipayLoginPresenter = new AlipayLoginPresenter();
        alipayLoginPresenter.login(context,authorizeCallBack,loginCallBack);
    }

    @Override
    public void bind(Context context, HashMap<String, String> params, ThirdCallBack.IAuthorizeCallBack authorizeCallBack, ThirdCallBack.IBindThirdCallBack bindThirdCallBack) {

        BindThirdPartParam requstParam = new BindThirdPartParam();
        if (params != null){
            if (params.containsKey(PARAM_MOBILE_NO)){
                requstParam.mobile = params.get(PARAM_MOBILE_NO);
            }
            if (params.containsKey(PARAM_IS_LOGIN)){
                requstParam.isLogin = params.get(PARAM_IS_LOGIN);
            }
        }

        alipayLoginPresenter = new AlipayLoginPresenter();
        alipayLoginPresenter.bind(context,requstParam,authorizeCallBack,bindThirdCallBack);
    }

    @Override
    public void unBind(Context context, HashMap<String, String> params, ThirdCallBack.IUnBindThirdCallBack unBindThirdCallBack) {
        alipayLoginPresenter = new AlipayLoginPresenter();

        alipayLoginPresenter.unBind(unBindThirdCallBack);
    }

    @Override
    public void init(Context context) {

    }

}
