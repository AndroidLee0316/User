package com.pasc.business.face.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.pasc.business.face.R;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;

/**
 * 人脸照片重置成功
 * Created by huanglihou519 on 18/01/11.
 */
public class FaceDetectResetSuccessActivity extends BaseStatusBarActivity {
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
                keyBack();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_activity_face_detect_reset_success);
        btnDone = findViewById(R.id.user_btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBack();
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

    @Override
    public void onBackPressed() {
        keyBack();
    }

    private void keyBack(){
        EventBusOutUtils.postRestFaceSuccess();
        finish();

    }
}
