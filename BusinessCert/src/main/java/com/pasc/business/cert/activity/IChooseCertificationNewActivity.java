package com.pasc.business.cert.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.cert.R;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.user.urlconfig.CertiUrlManager;
import com.pasc.lib.userbase.user.urlconfig.OtherConfigManager;
import java.util.ArrayList;

/**
 * 选择验证方式界面
 *
 */
@Route(path = RouterTable.Cert.PATH_CERT_AUTH_TYPE_ACT_NEW)
public class IChooseCertificationNewActivity extends IChooseCertificationActivity {

    @Override
    protected void initData(){

        mCtbList = new ArrayList<>();

        if (CertiUrlManager.getInstance().getNeedBankCert()) {

            CertificationTypeBean ctb = new CertificationTypeBean();
            ctb.certType = Integer.valueOf(User.CERTIFY_BANK);
            ctb.certIconID = R.drawable.cert_ic_bank_verify;
            ctb.certTitle = getResources().getString(R.string.user_bank_cert);
            ctb.certSubTitle = getResources().getString(R.string.user_bank_cert_desc);
            ctb.isCert = false;
            ctb.isShow = true;
            mCtbList.add(ctb);
        }


        if (CertiUrlManager.getInstance().getNeedPAFaceCert() || CertiUrlManager.getInstance().getNeedAlipayFaceCert()) {

            CertificationTypeBean ctb1 = new CertificationTypeBean();
            ctb1.certType = CertificationTypeBean.CERTIFICATION_TYPE_FACE_ALL;
            ctb1.certIconID = R.drawable.cert_ic_face_verify;
            ctb1.certTitle = getResources().getString(R.string.user_scan_face_cert);
            ctb1.certSubTitle = getResources().getString(R.string.user_face_cert_desc);
            ctb1.isCert = false;
            ctb1.isShow = true;
            mCtbList.add(ctb1);
        }
        interrupterAndAddCustomCert(false);
        registerCustomCertResult();
    }


}
