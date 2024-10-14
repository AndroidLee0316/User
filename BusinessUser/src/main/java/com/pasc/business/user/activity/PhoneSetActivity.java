package com.pasc.business.user.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.face.data.FaceConstant;
import com.pasc.business.user.PascUserFaceCheckListener;
import com.pasc.business.user.PascUserFaceCheckNewListener;
import com.pasc.business.user.PascUserManager;
import com.pasc.business.user.R;
import com.pasc.business.user.base.BaseMoreActivity;
import com.pasc.business.user.even.ChangePhoneFinish;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.activity.BaseActivity;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.Map;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
@Route(path = RouterTable.User.PATH_USER_ACCOUNT_CHANGE_PHONE_ACT)
public class PhoneSetActivity extends BaseMoreActivity implements View.OnClickListener {
    TextView textView;

    @Override
    protected int layoutResId() {
        return R.layout.user_activity_phone_set;
    }

    @Override
    public void initData() {
        onBackClick = true;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        textView = findViewById(R.id.user_tv_phone);
        String phone = AppProxy.getInstance().getUserManager().getUserInfo().getMobileNo();
        if (!TextUtils.isEmpty(phone) && phone.length() >=11){
            textView.setText("您的手机号：" + phone.substring(0,3) + "******" +phone.substring(phone.length()-2));
        }

        findViewById(R.id.user_change_phone_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.user_change_phone_btn) {
            if (AppProxy.getInstance().getUserManager().isCertified()) {
                PascUserManager.getInstance().toFaceCheck(FaceConstant.TYPE_CHANGE_PHONE, new PascUserFaceCheckNewListener() {
                    @Override
                    public void onSuccess(Map<String, String> data) {
                       String certId = data.get("certId");
                        InputPhoneActivity.satrActivity(PhoneSetActivity.this,certId);
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
                ChangePhoneActivity.startActivity(PhoneSetActivity.this);
            }
        }
    }

    /**
     *
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinish(ChangePhoneFinish event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
        EventBusOutUtils.postChangePhoneNumCancled();
        finish();
    }
}
