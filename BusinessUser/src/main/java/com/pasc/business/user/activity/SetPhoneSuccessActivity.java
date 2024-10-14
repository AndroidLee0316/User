package com.pasc.business.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import com.pasc.business.login.activity.NewLoginActivity;
import com.pasc.business.user.R;
import com.pasc.lib.base.activity.BaseActivity;
import com.pasc.lib.base.util.StatusBarUtils;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangxu678 on 2020-03-05.
 */
public class SetPhoneSuccessActivity extends BaseActivity {
  private final int COUNT_DOWN = 3;
  private CommonTitleView commonTitleView;
  private Button btnCommit;
  private Disposable disposable;

  @Override protected int layoutResId() {
    return R.layout.user_activity_set_phone_success;
  }

  @Override protected void onInit(@Nullable Bundle savedInstanceState) {
    StatusBarUtils.setStatusBarColor(this,true);
    commonTitleView = findViewById(R.id.user_ctv_title);
    commonTitleView.setOnLeftClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });
    btnCommit = findViewById(R.id.user_btn_commit);
    btnCommit.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });

    btnCommit.setText(
            String.format(getString(R.string.user_back_countdown), COUNT_DOWN));
    disposable = Observable.interval(1L, TimeUnit.SECONDS)
        .take(COUNT_DOWN)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<Long>() {
          @Override
          public void onNext(Long aLong) {
            if (isFinishing()) {
              return;
            }
            btnCommit.setText(
                String.format(getString(R.string.user_back_countdown), COUNT_DOWN - aLong));
          }

          @Override
          public void onError(Throwable e) {
          }

          @Override
          public void onComplete() {
            onBackPressed();
          }
        });
  }

  @Override public void onBackPressed() {
    if (disposable != null) {
      disposable.dispose();
    }
    startActivity(new Intent(this, NewLoginActivity.class));
    finish();
  }
}
