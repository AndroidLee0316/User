package com.pasc.business.user.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.pasc.business.user.R;
import com.pasc.business.user.activity.AccoutCalcePayErrorActivity;
import com.pasc.lib.base.activity.BaseActivity;
import com.pasc.lib.base.util.StatusBarUtils;
import com.pasc.lib.statistics.StatisticsManager;
import com.pasc.lib.userbase.base.view.CommonTitleView;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public abstract class BaseMoreActivity extends BaseActivity {
    public CommonTitleView titleBar;
    public boolean onBackClick =false;

    public BaseMoreActivity() {
    }

    public static void startActivity(Context context, Class<BaseMoreActivity> cLass) {
        context.startActivity(new Intent(context, cLass));
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {
        StatusBarUtils.setStatusBarColor(this, true);
        titleBar = findViewById(R.id.user_ctv_title);
        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBackClick){
                    onBackMore();
                }else {
                    finish();
                }

            }
        });
        initView();
        initData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtils.setStatusBarColor(this, true);
        StatisticsManager.getInstance().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatisticsManager.getInstance().onPause(this, "app");
    }

    public abstract void initData();

    public abstract void initView();

    public void onBackMore() {
    }
}
