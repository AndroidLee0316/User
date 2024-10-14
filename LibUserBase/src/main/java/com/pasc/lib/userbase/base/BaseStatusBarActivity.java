package com.pasc.lib.userbase.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pasc.lib.base.activity.BaseActivity;
import com.pasc.lib.base.util.StatusBarUtils;
import com.pasc.lib.statistics.PageType;
import com.pasc.lib.statistics.StatisticsManager;

/**
 * Created by kuangxiangkui192 on 2018/12/7.
 */
public abstract class BaseStatusBarActivity extends BaseActivity {
    @Override
    protected void onInit(@Nullable Bundle bundle) {
        StatusBarUtils.setStatusBarColor(this,true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtils.setStatusBarColor(this,true);
        StatisticsManager.getInstance().onResume(this);
    }

    @Override protected void onPause() {
        super.onPause();
        StatisticsManager.getInstance().onPause(this, PageType.APP);
    }
}
