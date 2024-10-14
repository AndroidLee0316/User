package com.pasc.business.cert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.cert.R;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.base.util.DensityUtils;
import com.pasc.lib.router.interceptor.CertificationInterceptor;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.event.EventKey;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 认证失败统一进入该界面
 */
@Route(path = RouterTable.Cert.PATH_CERT_FAIL_ACT)
public class CertifyFailActivity extends BaseStatusBarActivity implements View.OnClickListener {
    /***可用认证次数*/
    public TextView tvRetry;
    public TextView tvReturn;

    //认证方式，默认-1为未知默认方式
    private int certificationType = Constant.CERT_TYPE_UNKNOW;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cert_activity_certify_fail);
        tvRetry = findViewById(R.id.user_activity_certifyFail_tvRetry);
        tvRetry.setOnClickListener(this);
        tvReturn = findViewById(R.id.user_activity_certifyFail_tvReturn);
        tvReturn.setOnClickListener(this);
        initErrorRemind();
    }

    /**
     * 展示错误日志
     */
    private void initErrorRemind() {
        Intent intent = getIntent();
        if (intent != null) {
            String errorMsg = intent.getExtras().getString(Constant.CERT_FAIL_MSG);
            TextView tvErrorMsg = findViewById(R.id.user_activity_certifyFail_tvErrorMsg);
            TextView tvAuthCount = findViewById(R.id.user_activity_certifyFail_tvAuthCount);
            boolean hasRemainCount = intent.hasExtra(Constant.CERT_FAIL_REMAIN_COUNT);
            int allowedAuthCount = intent.getExtras().getInt(Constant.CERT_FAIL_REMAIN_COUNT, 0);
            CommonTitleView commonTitleView = findViewById(R.id.user_ctv_title);
            certificationType = intent.getExtras().getInt(Constant.CERT_TYPE, Constant.CERT_TYPE_UNKNOW);
            if (certificationType == Constant.CERT_TYPE_FACE) {
                commonTitleView.setTitleText(getString(R.string.user_scan_face_cert));
            } else if (certificationType == Constant.CERT_TYPE_BANK) {
                commonTitleView.setTitleText(getString(R.string.user_bank_cert));
            } else if (certificationType == Constant.CERT_TYPE_ALIPAY) {
                commonTitleView.setTitleText(getString(R.string.user_alipay_cert));
            } else {
                commonTitleView.setTitleText(getString(R.string.user_cert));
            }
            String authCount;
            if (allowedAuthCount <= 0) {
                allowedAuthCount = 0;
                tvRetry.setVisibility(View.GONE);
                tvReturn.setTextColor(getResources().getColor(R.color.white_ffffff));
                tvReturn.setBackground(getResources().getDrawable(R.drawable.userbase_button_bg));
                authCount = String.format(getString(R.string.user_no_auth_count_msg),allowedAuthCount);
            } else {
                authCount = String.format(getString(R.string.user_auth_count_msg),allowedAuthCount);
            }
            SpannableString str = new SpannableString(authCount);
            str.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pasc_primary)), 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(17)), 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_999999)), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_999999)), 5, authCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvAuthCount.setText(str);
            if (!hasRemainCount){
                tvAuthCount.setVisibility(View.INVISIBLE);
            }

            tvErrorMsg.setText(TextUtils.isEmpty(errorMsg) ? getString(R.string.user_default_certify_error_msg) : errorMsg);

        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.user_activity_certifyFail_tvRetry) {
            finish();
        } else if (id == R.id.user_activity_certifyFail_tvReturn) {
            keyBack();
        }
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    public void onBackPressed() {
        keyBack();
    }

    private void keyBack(){
        CertificationInterceptor.notifyCallBack(false);
        EventBusOutUtils.postCertificationFailed(certificationType);
        finish();
    }
}
