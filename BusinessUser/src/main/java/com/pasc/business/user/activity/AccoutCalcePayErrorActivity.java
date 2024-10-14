package com.pasc.business.user.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasc.business.user.R;
import com.pasc.business.user.base.BaseMoreActivity;
import com.pasc.lib.hybrid.PascHybrid;
import com.pasc.lib.userbase.user.urlconfig.OtherConfigManager;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/31
 * 更改时间：2019/10/31
 */
public class AccoutCalcePayErrorActivity extends BaseMoreActivity implements View.OnClickListener {

    /**
     * 默认设置为空，为了让项目接入的时候如果不设置容易发现
     */
    public String URL_PAY_CALCE = "";

    @Override
    public void initView() {
        if (!TextUtils.isEmpty(OtherConfigManager.getInstance().getOtherConfigBean().getAccoutCancelPayUrl())){
            URL_PAY_CALCE = OtherConfigManager.getInstance().getOtherConfigBean().getAccoutCancelPayUrl();
        }

        findViewById(R.id.rl_calce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PascHybrid.getInstance()
                        .start(AccoutCalcePayErrorActivity.this, URL_PAY_CALCE);
            }
        });
    }

    @Override
    public void initData() {

    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AccoutCalcePayErrorActivity.class));
    }

    @Override
    protected int layoutResId() {
        return R.layout.user_activity_accout_calce_pay_error;
    }

    @Override
    public void onClick(View view) {

    }
}
