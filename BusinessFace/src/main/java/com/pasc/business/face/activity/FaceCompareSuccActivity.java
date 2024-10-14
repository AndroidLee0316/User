package com.pasc.business.face.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.face.R;
import com.pasc.lib.router.interceptor.CertificationInterceptor;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;

@Route(path = RouterTable.Face.PATH_FACE_COMPARE_SUCC_ACT)
public class FaceCompareSuccActivity extends BaseStatusBarActivity implements View.OnClickListener {
    private CertifyTimer certifyTimer;
    private TextView tvCertifyContent;
    private String prefix;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_activity_compare_succ);
        tvCertifyContent = findViewById(R.id.user_activity_certifySucc_tvContent);
        findViewById(R.id.user_activity_certifySucc_btnReturn).setOnClickListener(this);
        initSuccRemind();
    }


    private void initSuccRemind() {
        prefix = getString(R.string.face_cert_title);
        certifyTimer = new CertifyTimer(this, 3500L, 1000L);
        certifyTimer.start();
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
        EventBusOutUtils.postCertificationSuccess(Constant.CERT_TYPE_FACE);
        finish();
    }

    private static class CertifyTimer extends CountDownTimer {
        private FaceCompareSuccActivity activity;

        CertifyTimer(FaceCompareSuccActivity activity, long millisInFuture, long countDownInterval) {
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
