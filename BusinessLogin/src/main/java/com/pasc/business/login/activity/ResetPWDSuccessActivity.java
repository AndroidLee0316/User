package com.pasc.business.login.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.pasc.business.login.R;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;

public class ResetPWDSuccessActivity extends BaseStatusBarActivity {

    private Button btnDone;
    private Handler mHandler;
    private int mCount = 3;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mCount--;
            if (mCount > 0) {
                btnDone.setText("完成 (" + String.valueOf(mCount) + "s)");
                mHandler.postDelayed(mRunnable, 1000);
            } else {
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_reset_pwdsuccess);
        btnDone = findViewById(R.id.user_btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected int layoutResId() {
        return 0;
    }
}
