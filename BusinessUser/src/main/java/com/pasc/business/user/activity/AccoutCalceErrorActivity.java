package com.pasc.business.user.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.pasc.business.user.R;
import com.pasc.business.user.base.BaseMoreActivity;
import com.pasc.business.user.even.AccoutCalceFinish;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/31
 * 更改时间：2019/10/31
 */
public class AccoutCalceErrorActivity extends BaseMoreActivity implements View.OnClickListener {
    private TextView content;

    @Override
    public void initView() {
        findViewById(R.id.user_activity_accout_calce_retry).setOnClickListener(this::onClick);
        findViewById(R.id.text_back).setOnClickListener(this::onClick);
        content =  findViewById(R.id.tv_content);

    }

    @Override
    public void initData() {
        onBackClick = true;
        String error = getIntent().getStringExtra("error");
        content.setText(error);

    }

    public static void startActivity(Context context,String error) {
        Intent intent = new Intent(context, AccoutCalceErrorActivity.class);
        intent.putExtra("error",error);
        context.startActivity(intent);
    }

    @Override
    protected int layoutResId() {
        return R.layout.user_activity_accout_calce_error;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.user_activity_accout_calce_retry) {
            finish();
        } else {
            keyBack();
        }
    }

    @Override
    public void onBackMore() {
        keyBack();
    }

    @Override
    public void onBackPressed() {
        keyBack();
    }

    private void keyBack(){
        EventBus.getDefault().post(new AccoutCalceFinish());
        EventBusOutUtils.postCancelAccountFailed();
        finish();
    }
}
