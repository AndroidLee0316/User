package com.pasc.business.cert.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.cert.R;
import com.pasc.business.cert.config.CertOutConfigManager;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.user.urlconfig.CertiUrlManager;
import com.pasc.lib.userbase.user.urlconfig.OtherConfigManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择验证方式界面
 *
 */
@Route(path = RouterTable.Cert.PATH_CERT_AUTH_TYPE_FACE_CHOOSE)
public class IChooseCertificationFaceListActivity extends IChooseCertificationActivity {

    /**
     * 是否是从 IChooseCertificationActivity 跳转过来，如果是的话，按返回不能发送取消认证的通知
     */
    private boolean isFromIChooseCertificationActivity = false;

    private static final String EXTRA_FROM_ICHOOSE = "EXTRA_FROM_ICHOOSE";

    public static void startFaceListActivity(Context context) {
        Intent intent = new Intent(context, IChooseCertificationFaceListActivity.class);
        context.startActivity(intent);
    }

    public static void startFaceListActivityFromIChooseCertification(Context context) {
        Intent intent = new Intent(context, IChooseCertificationFaceListActivity.class);
        intent.putExtra(EXTRA_FROM_ICHOOSE,true);
        intent.putExtra(EXTRA_PARAM_FINISH_WHEN_CERT_SUCCESS,true);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        commonTitleView.setTitleText("人脸认证");
    }

    @Override
    protected void initData(){

        isFromIChooseCertificationActivity = getIntent().hasExtra(EXTRA_FROM_ICHOOSE);

        mCtbList = new ArrayList<>();

        if (CertiUrlManager.getInstance().getNeedPAFaceCert()) {
            CertificationTypeBean ctb1 = new CertificationTypeBean();
            ctb1.certType = Integer.valueOf(User.CERTIFY_FACE);
            ctb1.certIconID = R.drawable.cert_ic_face_verify;
            ctb1.certTitle = "APP人脸认证";
            ctb1.certSubTitle = "使用APP人脸认证系统认证";
            ctb1.isCert = false;
            ctb1.isShow = true;
            mCtbList.add(ctb1);
        }


        if (CertiUrlManager.getInstance().getNeedAlipayFaceCert()) {
            CertificationTypeBean ctb2 = new CertificationTypeBean();
            ctb2.certType = Integer.valueOf(User.CERTIFY_ALIPAY);
            ctb2.certIconID = R.drawable.cert_ic_face_alipay_verify;
            ctb2.certTitle = "支付宝人脸认证";
            ctb2.certSubTitle = "使用支付宝身份认证系统认证";
            ctb2.isCert = false;
            ctb2.isShow = true;
            mCtbList.add(ctb2);
        }

        interrupterAndAddCustomCert(true);
        registerCustomCertResult();

    }


    @Override
    protected void keyBack() {
        if (isFromIChooseCertificationActivity){
            finish();
        }else {
            super.keyBack();
        }
    }
}
