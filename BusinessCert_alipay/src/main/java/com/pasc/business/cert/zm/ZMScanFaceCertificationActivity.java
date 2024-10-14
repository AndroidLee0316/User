package com.pasc.business.cert.zm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.cert.CertifyUtils;
import com.pasc.business.cert.activity.ScanFaceCertificationActivity;
import com.pasc.business.cert.view.CertSelectAgreementView;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.user.urlconfig.CertiUrlManager;


/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/3
 */

@Route(path = RouterTable.Cert_ZM.PATH_CERT_AUTH_TYPE_FACE_PRE)
public class ZMScanFaceCertificationActivity extends ScanFaceCertificationActivity {

    /**
     * 跳转到输入验证码页面
     */
    @Override
    protected void intentToFaceCertificationActivity() {

        Bundle bundle = new Bundle();
        bundle.putString("IDcard", etIdNumber.getOriginalText());
        bundle.putString("name", CertifyUtils.getTvContent(etRealName));
        BaseJumper.jumpBundleARouter(RouterTable.Cert_ZM.PATH_CERT_AUTH_TYPE_FACE_MAIN, bundle);

    }

    @Override
    public int getCertSelectAgreementViewType() {
        return CertSelectAgreementView.CERT_TYPE_FACE_ALIPAY;
    }

    /**
     * 复写，不需要检测是否有拍照权限
     */
    @Override
    protected void scanFaceClick() {
        if (check()) {
            queryIsCerted();
        }
    }
}
