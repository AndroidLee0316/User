package com.pingan.smt;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.pasc.business.user.PascUserManager;
import com.pasc.business.user.cert.CertConfig;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/12/20
 */
public class TestCustomCertActivity extends Activity{

    private Button successButton;
    private Button failedButton;
    private Button cancelButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_custom_cert);
        successButton = findViewById(R.id.activity_test_custom_cert_success);
        failedButton = findViewById(R.id.activity_test_custom_cert_failed);
        cancelButton = findViewById(R.id.activity_test_custom_cert_cancel);

        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PascUserManager.getInstance().updateCustomCertResult(CertConfig.CUSTOM_CERT_RESULT_SUCCESS);
                finish();
            }
        });

        failedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PascUserManager.getInstance().updateCustomCertResult(CertConfig.CUSTOM_CERT_RESULT_FAILED);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PascUserManager.getInstance().updateCustomCertResult(CertConfig.CUSTOM_CERT_RESULT_CANCEL);
                finish();
            }
        });
    }
}
