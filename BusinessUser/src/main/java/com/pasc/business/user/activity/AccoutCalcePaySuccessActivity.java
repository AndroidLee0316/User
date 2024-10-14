package com.pasc.business.user.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pasc.business.user.R;
import com.pasc.business.user.base.BaseMoreActivity;
import com.pasc.business.user.even.AccoutCalceFinish;
import com.pasc.business.user.even.AccoutSecurityFinish;
import com.pasc.business.user.iview.AccoutCalceView;
import com.pasc.business.user.net.resp.AccoutCalceResp;
import com.pasc.business.user.presenter.AccoutCalcePresenter;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.base.util.SPUtils;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.userbase.base.UserConstant;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.userbase.user.util.UserManagerImpl;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;

import org.greenrobot.eventbus.EventBus;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/31
 * 更改时间：2019/10/31
 */
public class AccoutCalcePaySuccessActivity extends BaseMoreActivity implements View.OnClickListener,AccoutCalceView {

    TextView textView;
    AccoutCalcePresenter mPresenter;
    Button user_commit;

    @Override
    public void initView() {
        onBackClick = true;
        mPresenter = new AccoutCalcePresenter(this);
        textView = findViewById(R.id.tv_himt);
        user_commit = findViewById(R.id.user_commit);
        user_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBack();
            }
        });
    }

    @Override
    public void initData() {

        String mobile = AppProxy.getInstance().getUserManager().getMobile();
        if (!TextUtils.isEmpty(mobile) && mobile.length()>=11){
            mobile = mobile.replace(mobile.substring(3, 9), "******");
            textView.setText(String.format(getString(R.string.user_account_cancel_success_hint), mobile));
        }
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AccoutCalcePaySuccessActivity.class));
    }

    @Override
    protected int layoutResId() {
        return R.layout.user_activity_accout_calce_success;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void isFinishPay(AccoutCalceResp resp) {

    }

    @Override
    public void onBackMore() {
        keyBack();
    }

    @Override
    public void commit(AccoutCalceResp resp) {

    }

    @Override
    public void onError(String code, String error) {

    }

    @Override
    public void showLoadings() {

    }

    @Override
    public void dismissLoadings() {

    }

    @Override
    public void onBackPressed() {
        keyBack();
    }

    private void keyBack(){
        EventBus.getDefault().post(new AccoutCalceFinish());
        AppProxy.getInstance().getUserManager().exitUser(AccoutCalcePaySuccessActivity.this);
        EventBus.getDefault().post(new AccoutSecurityFinish());
        Delete.table(User.class, new SQLOperator[0]);
        SPUtils.getInstance().setParam(UserConstant.USER_ACCOUNT, "");
        UserManagerImpl.getInstance().exitUser(this);
        EventBusOutUtils.postCancelAccountSuccess();
        finish();
    }

}
