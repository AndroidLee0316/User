package com.pasc.business.login.base;

import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.pasc.business.login.R;

import java.lang.ref.SoftReference;

/**
 * Created by kuangxiangkui192 on 2019/1/1.
 */
public class CountDownHandler extends Handler {
    public int countDownTime = 60;
    private SoftReference<TextView> srt;
    private SoftReference<Resources> srr;

    public CountDownHandler(TextView textView, Resources resources) {
        srt = new SoftReference<>(textView);
        srr = new SoftReference<>(resources);
    }

    public void setCountDownTime(int time) {
        this.countDownTime = time;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 0:
                if (srt != null && srt.get() != null) {
                    countDownTime--;
                    if (countDownTime > 0) {
                        srt.get().setText(countDownTime + "s后重试");
                        sendEmptyMessageDelayed(0, 1000);
                    } else {
                        srt.get().setClickable(true);
                        srt.get().setText(srr.get().getText(R.string.user_get_code_again));
                        srt.get().setTextColor(srr.get().getColor(R.color.pasc_primary));
                        removeMessages(0);
                        clear();
                    }
                }
                break;

            default:
                break;
        }
    }

    /**
     * 清空软引用
     */
    public void clear() {
        if (srt != null) {
            srt.clear();
            srt = null;
        }
        if (srr != null) {
            srr.clear();
            srr = null;
        }
        removeCallbacksAndMessages(null);
    }
}
