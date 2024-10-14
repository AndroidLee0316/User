package com.pingan.smt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pasc.business.user.PascUserCancelAccountListener;
import com.pasc.business.user.PascUserCertListener;
import com.pasc.business.user.PascUserChangePhoneNumListener;
import com.pasc.business.user.PascUserConfig;
import com.pasc.business.user.PascUserFaceCheckListener;
import com.pasc.business.user.PascUserFaceCheckNewListener;
import com.pasc.business.user.PascUserLoginListener;
import com.pasc.business.user.PascUserManager;
import com.pasc.business.user.PascUserUpdateListener;
import com.pasc.business.user.cert.CertConfig;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.router.interceptor.BaseRouterTable;
import com.pasc.lib.router.interceptor.InterceptorUtil;

import java.util.HashMap;
import java.util.Map;


public class TestCertificationActivity22 extends Activity {


    public void testView(View view) {
        Map<String, String> map = new HashMap<>();
        map.put(BaseRouterTable.BundleKey.KEY_NEED_LOGIN, "true");
        map.put(BaseRouterTable.BundleKey.KEY_NEED_IDENTITY, "true");

        InterceptorUtil.instance().startService(this, "", map, new InterceptorUtil.InterceptorCallback() {
            @Override
            public void onSuccess(Activity activity, String s, Map<String, String> map) {
                Log.e("loginTag", "onSuccess: " + map.toString());
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_test_activity_certicition);
        ((Button) findViewById(R.id.test_btnFaceCertiCountActivity)).setText("去登陆");
        findViewById(R.id.test_btnFaceCertiCountActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PascUserManager.getInstance().toLogin(new PascUserLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                    }

                    @Override
                    public void onLoginFailed() {

                    }

                    @Override
                    public void onLoginCancled() {

                    }
                });
            }
        });
        ((Button) findViewById(R.id.test_btnBankCertiCountActivity)).setText("去认证-all");
        findViewById(R.id.test_btnBankCertiCountActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PascUserManager.getInstance().clearAllCustomCert();
                PascUserManager.getInstance().addCustomCert(100, R.drawable.cert_ic_face_alipay_verify, "威海认证", "使用威海自己的认证", 4, new CertConfig.CertClickCallBack() {
                    @Override
                    public void onClick(Activity activity) {
                        Intent intent = new Intent();
                        intent.setClass(TestCertificationActivity22.this, TestCustomCertActivity.class);
                        startActivity(intent);
                    }

                });
                PascUserManager.getInstance().toCertification(PascUserConfig.CERTIFICATION_TYPE_ALL_AND_FINISH_WHEN_SUCCESS, new PascUserCertListener() {
                    @Override
                    public void onCertificationSuccess() {
                    }

                    @Override
                    public void onCertificationFailed() {
                    }

                    @Override
                    public void onCertificationCancled() {
                    }
                });
            }
        });
        ((Button) findViewById(R.id.test_btnAccountSecurityActivity)).setText("去认证-face");
        findViewById(R.id.test_btnAccountSecurityActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PascUserManager.getInstance().toCertification(PascUserConfig.CERTIFICATION_TYPE_FACE_PA, new PascUserCertListener() {
                    @Override
                    public void onCertificationSuccess() {
                    }

                    @Override
                    public void onCertificationFailed() {
                    }

                    @Override
                    public void onCertificationCancled() {
                    }
                });
            }
        });
        ((Button) findViewById(R.id.test_btnUserInfoUpdate)).setText("去认证-bank");
        findViewById(R.id.test_btnUserInfoUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PascUserManager.getInstance().toCertification(PascUserConfig.CERTIFICATION_TYPE_BANK, null);
            }
        });
        ((Button) findViewById(R.id.test_btnGetUserInfo)).setText("去账户中心");
        findViewById(R.id.test_btnGetUserInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PascUserManager.getInstance().toAccount();
            }
        });
        ((Button) findViewById(R.id.test_btnBankCertiActivity)).setText("去人脸设置");
        findViewById(R.id.test_btnBankCertiActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PascUserManager.getInstance().toFaceSetting(null);
            }
        });

        ((Button) findViewById(R.id.test_btnBankCertiActivityAfterFace)).setText("获取用户最新信息");
        findViewById(R.id.test_btnBankCertiActivityAfterFace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PascUserManager.getInstance().updateUserInfo(new PascUserUpdateListener() {
                    @Override
                    public void onSuccess() {
                        String token = PascUserManager.getInstance().getUserInfo("token");
                        ToastUtils.toastMsg(token);
                    }

                    @Override
                    public void onFailed() {
                    }
                });
            }
        });

        ((Button) findViewById(R.id.test_btnCertificationCompletelyActivity0)).setText("人脸核身旧接口");
        findViewById(R.id.test_btnCertificationCompletelyActivity0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PascUserManager.getInstance().toFaceCheck("appid", new PascUserFaceCheckListener() {

                    @Override
                    public void onSuccess(Map<String, String> data) {
                        ToastUtils.toastMsg("人脸核验成功");
                    }

                    @Override
                    public void onFailed() {
                        ToastUtils.toastMsg("人脸核验失败了-旧接口");
                    }


                    @Override
                    public void onCancled() {
                        ToastUtils.toastMsg("人脸核验取消");
                    }
                });
            }
        });

        ((Button) findViewById(R.id.test_btnCertificationCompletelyActivity)).setText("人脸核身");
        findViewById(R.id.test_btnCertificationCompletelyActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PascUserManager.getInstance().toFaceCheck("appid", new PascUserFaceCheckNewListener() {

                    @Override
                    public void onSuccess(Map<String, String> data) {
                        ToastUtils.toastMsg("人脸核验成功");
                    }

                    @Override
                    public void onFailed() {

                    }

                    @Override
                    public void onFailed(String errorCode, String errorMsg) {
                        ToastUtils.toastMsg(errorMsg);
                    }

                    @Override
                    public void onCancled() {
                        ToastUtils.toastMsg("人脸核验取消");
                    }
                });
            }
        });
        ((Button) findViewById(R.id.test_btnChooseCertificationActivity)).setText("获取支付号");
        findViewById(R.id.test_btnChooseCertificationActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payID = PascUserManager.getInstance().getUserInfo(PascUserConfig.USER_INFO_KEY_PAY_ACCOUNT_ID);
                ToastUtils.toastMsg(payID);
            }
        });

        ((Button) findViewById(R.id.test_btnMeProfileActivity)).setText("账号注销");
        findViewById(R.id.test_btnMeProfileActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PascUserManager.getInstance().toCancelAccount(new PascUserCancelAccountListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.toastMsg("账号注销成功 xxx");
                    }

                    @Override
                    public void onFailed() {
                        ToastUtils.toastMsg("账号注销失败 xxx");
                    }

                    @Override
                    public void onCanceld() {
                        ToastUtils.toastMsg("账号注销取消 xxx");
                    }
                });
            }
        });


        ((Button) findViewById(R.id.test_btnNewLoginActivity)).setText("手机号更换");
        findViewById(R.id.test_btnNewLoginActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PascUserManager.getInstance().toChangePhoneNum(new PascUserChangePhoneNumListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.toastMsg("手机号更换成功 xxx");
                    }

                    @Override
                    public void onFailed() {
                        ToastUtils.toastMsg("手机号更换失败 xxx");
                    }

                    @Override
                    public void onCanceld() {
                        ToastUtils.toastMsg("手机号更换取消 xxx");
                    }
                });
            }
        });


        ((Button) findViewById(R.id.test_btnLogout)).setText("退出登录");
        findViewById(R.id.test_btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PascUserManager.getInstance().loginOut();
            }
        });



    }
}
