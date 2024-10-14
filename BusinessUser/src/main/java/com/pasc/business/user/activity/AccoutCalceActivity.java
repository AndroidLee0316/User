package com.pasc.business.user.activity;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.face.data.FaceConstant;
import com.pasc.business.user.BuildConfig;
import com.pasc.business.user.PascUserFaceCheckListener;
import com.pasc.business.user.PascUserFaceCheckNewListener;
import com.pasc.business.user.PascUserManager;
import com.pasc.business.user.R;
import com.pasc.business.user.base.BaseMoreActivity;
import com.pasc.business.user.data.UserConstants;
import com.pasc.business.user.even.AccoutCalceFinish;
import com.pasc.business.user.iview.AccoutCalceView;
import com.pasc.business.user.net.resp.AccoutCalceResp;
import com.pasc.business.user.presenter.AccoutCalcePresenter;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.user.urlconfig.OtherConfigManager;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/31
 * 更改时间：2019/10/31
 */
@Route(path = RouterTable.User.PATH_USER_ACCOUNT_CALCE_ACT)
public class AccoutCalceActivity extends BaseMoreActivity implements View.OnClickListener, AccoutCalceView {
    CommonTitleView titleView;
    Button btnSureBtn;
    ImageView imgSelect;
    WebView webView;
    private boolean isSelect;
    AccoutCalcePresenter mPersenter;

    /**
     * 默认设置为空，为了让项目接入的时候如果不设置容易发现
     */
    private String URL_CANCLE_HINT = "";

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        titleView = findViewById(R.id.user_ctv_title);
        titleView.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBack();
            }
        });
        mPersenter = new AccoutCalcePresenter(this);
        btnSureBtn = findViewById(R.id.user_accout_sure);
        btnSureBtn.setOnClickListener(this::onClick);
        btnSureBtn.setAlpha(0.3f);
        btnSureBtn.setEnabled(false);
        findViewById(R.id.user_accout_calce).setOnClickListener(this::onClick);
        findViewById(R.id.pasc_user_privacy_ll).setOnClickListener(this);
        imgSelect = findViewById(R.id.pasc_user_privacy_iv);
        webView = findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setUserAgentString(settings.getUserAgentString()
                + "/openweb=paschybrid/CHANGSHUSMT_Android,VERSION:"
                + BuildConfig.VERSION_NAME);
        settings.setJavaScriptEnabled(true);

        //设置参数
        settings.setBuiltInZoomControls(true);
        settings.setAppCacheEnabled(true);// 设置缓存
        //得到网页的状态
        webView.setWebViewClient(new WebViewClient() {
            @Override
            //加载完成
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                //隐藏进度条
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        URL_CANCLE_HINT = OtherConfigManager.getInstance().getOtherConfigBean().getAccoutCancelHintUrl();
        webView.loadUrl(URL_CANCLE_HINT);


    }

    @Override
    public void initData() {

    }


    @Override
    protected int layoutResId() {
        return R.layout.user_activity_accout_calce;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.user_accout_calce) {
            keyBack();
        } else if (id == R.id.user_accout_sure) {
            mPersenter.isFinishPay();
        } else if (id == R.id.pasc_user_privacy_ll) {
            if (isSelect) {
                isSelect = false;
                imgSelect.setImageResource(R.drawable.single_unselect);
                btnSureBtn.setAlpha(0.3f);
                btnSureBtn.setEnabled(false);
            } else {
                isSelect = true;
                imgSelect.setImageResource(R.drawable.check_select);
                btnSureBtn.setAlpha(1f);
                btnSureBtn.setEnabled(true);
            }
        }
    }

    @Override
    public void isFinishPay(AccoutCalceResp resp) {
        if (resp.isPaymentCancel) {
            if (AppProxy.getInstance().getUserManager().isCertified()) {
                PascUserManager.getInstance().toFaceCheck(FaceConstant.TYPE_ACCOUT_CALCE, new PascUserFaceCheckNewListener() {
                    @Override
                    public void onSuccess(Map<String, String> data) {
                        String certId = "";
                        if (data != null) {
                            certId = data.get("certId");
                        }
                        mPersenter.commit("2", certId);
                    }

                    @Override
                    public void onFailed() {

                    }

                    @Override
                    public void onFailed(String errorCode, String errorMsg) {
                        if (TextUtils.isEmpty(errorMsg)){
                            ToastUtils.toastMsg(getString(R.string.user_face_check_failed));
                        }else {
                            ToastUtils.toastMsg(errorMsg);
                        }
                    }

                    @Override
                    public void onCancled() {


                    }
                });
            } else {
                SendSmsActivity.startActivity(AccoutCalceActivity.this, AppProxy.getInstance().getUserManager().getMobile(), UserConstants.TYPE_SEND_SMS_ACCOUT_CALCE, "", "");
            }
        } else {
            AccoutCalcePayErrorActivity.startActivity(this);
        }
    }

    @Override
    public void commit(AccoutCalceResp resp) {
        AccoutCalcePaySuccessActivity.startActivity(this);
    }

    @Override
    public void onError(String code, String error) {
        if ("-1".equals(code) || "404".equals(code) || "10000007".equals(code)) {
            ToastUtils.toastMsg(error);
        } else {
            AccoutCalceErrorActivity.startActivity(this, "人脸比对未通过，请确保为本人操作");
        }

    }

    @Override
    public void showLoadings() {
        showLoading();
    }

    @Override
    public void dismissLoadings() {
        dismissLoading();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinish(AccoutCalceFinish event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPersenter.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        keyBack();
    }

    private void keyBack(){
        EventBusOutUtils.postCancelAccountCancled();
        finish();
    }

}
