package com.pasc.business.cert.activity;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.user.urlconfig.CertiUrlManager;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/12/23
 */
@Interceptor(priority = 100, name = "IChooseCertificationInterrupter")
public class IChooseCertificationInterrupter implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if (postcard.getPath().equals(RouterTable.Cert.PATH_CERT_AUTH_TYPE_ACT)){
            if (CertiUrlManager.getInstance().isCertFaceNewWay()){
                postcard.setPath(RouterTable.Cert.PATH_CERT_AUTH_TYPE_ACT_NEW);
            }
        }
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {

    }
}
