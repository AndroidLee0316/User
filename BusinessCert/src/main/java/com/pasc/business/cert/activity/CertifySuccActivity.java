package com.pasc.business.cert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.cert.R;
import com.pasc.lib.router.interceptor.CertificationInterceptor;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.event.EventKey;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;

@Route(path = RouterTable.Cert.PATH_CERT_SUCC_ACT)
public class CertifySuccActivity extends BaseStatusBarActivity implements View.OnClickListener {
    private CertifyTimer certifyTimer;
    private TextView tvCertifyContent;
    private String prefix;
    //认证方式，默认-1为未知默认方式
    private int certificationType = Constant.CERT_TYPE_UNKNOW;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cert_activity_certify_succ);
        tvCertifyContent = findViewById(R.id.user_activity_certifySucc_tvContent);
        findViewById(R.id.user_activity_certifySucc_btnReturn).setOnClickListener(this);
        initSuccRemind();
    }


    private void initSuccRemind() {
        prefix = getString(R.string.user_certification_success);
        Intent intent = getIntent();
        if (intent != null) {
            CommonTitleView commonTitleView = findViewById(R.id.user_ctv_title);
            certificationType = intent.getIntExtra(Constant.CERT_TYPE, Constant.CERT_TYPE_UNKNOW);
            if (certificationType == Constant.CERT_TYPE_FACE) {
                prefix = getString(R.string.user_scan_face_cert);
            } else if (certificationType == Constant.CERT_TYPE_BANK) {
                prefix = getString(R.string.user_bank_cert);
            }
            commonTitleView.setTitleText(prefix);
            certifyTimer = new CertifyTimer(this, 3500L, 1000L);
            certifyTimer.start();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.user_activity_certifySucc_btnReturn) {

            if (certifyTimer != null) {
                certifyTimer.cancel();
            }
            onFinish();
        }
    }

    /**
     * 试试刷新UI
     */
    private void onTick(long time) {
        long timeLg = time / 1000;
        if (timeLg <= 0) {
            return;
        }
        String content = prefix + "已经通过(" + timeLg + "s)";
        tvCertifyContent.setText(content);
    }

    /**
     * 关闭页面
     */
    private void onFinish() {
        CertificationInterceptor.notifyCallBack(true);
        EventBusOutUtils.postCertificationSuccess(certificationType);
        finish();
    }

    private static class CertifyTimer extends CountDownTimer {
        private CertifySuccActivity activity;

        CertifyTimer(CertifySuccActivity activity, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.activity = activity;
        }

        @Override
        public void onTick(long l) {
            if (activity != null) {
                activity.onTick(l);
            }
        }

        @Override
        public void onFinish() {
            if (activity != null) {
                activity.onFinish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (certifyTimer != null) {
            certifyTimer.cancel();
            certifyTimer = null;
        }
        super.onDestroy();
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    public void onBackPressed() {
        onFinish();
    }
}
