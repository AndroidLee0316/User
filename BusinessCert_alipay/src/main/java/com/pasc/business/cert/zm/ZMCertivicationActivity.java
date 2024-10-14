package com.pasc.business.cert.zm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.cert.zm.resp.GetAliCertResultResp;
import com.pasc.business.cert.zm.resp.GetAliInitInfoResp;
import com.pasc.lib.base.activity.BaseActivity;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.router.interceptor.CertificationInterceptor;
import com.pasc.lib.statistics.StatisticsManager;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.user.urlconfig.CertiUrlManager;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.userbase.user.util.UserManagerImpl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.pasc.lib.statistics.PageType.APP;


/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/3
 */

@Route(path = RouterTable.Cert_ZM.PATH_CERT_AUTH_TYPE_FACE_MAIN)
public class ZMCertivicationActivity extends BaseActivity{

    private Context mContext;

    private String name;
    private String idCard;
    private String certifyId;

    private static final int REQUEST_CODE_ALIPAY = 100;

    @Override
    protected int layoutResId() {
        return R.layout.cert_activity_certification_zm;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {
        mContext = this;
        idCard = getIntent().getStringExtra("IDcard");
        name = getIntent().getStringExtra("name");

        showLoading();
        AlipayCertModel model = new AlipayCertModel();
        //获取外部设置的 appReturnJumpUrl
        String appReturnJumpUrl = CertiUrlManager.getInstance().getAlipayReturnJumpUrl();
        model.getAliInitInfo(name, idCard, appReturnJumpUrl, new CallBack.GetAliInitInfoCallBack() {
            @Override
            public void onSuccess(GetAliInitInfoResp resp) {
                dismissLoading();
                certifyId = resp.certifyId;
                gotoAlipayCert(resp.certifyUrl);
            }

            @Override
            public void onFailed(String errorCode, String errorMsg) {
                dismissLoading();
                if (!TextUtils.isEmpty(errorMsg)){
                    ToastUtils.toastMsg(errorMsg);
                }else if (!TextUtils.isEmpty(errorCode)){
                    ToastUtils.toastMsg(errorCode);
                }else {
                    Log.e(ZMCertivicationActivity.class.getName(),"getAliInitInfo error");
                }
                finish();
            }
        });

    }

    /**
     * 跳转到支付宝认证
     * @param certUrl
     */
    private void gotoAlipayCert(String certUrl){
        Intent action = new Intent(Intent.ACTION_VIEW);
        StringBuilder builder = new StringBuilder();
        builder.append("alipays://platformapi/startapp?appId=20000067&url=");
        builder.append(URLEncoder.encode(certUrl));
        action.setData(Uri.parse(builder.toString()));
        startActivityForResult(action,REQUEST_CODE_ALIPAY);
    }

    private void queryResult(){
        if (TextUtils.isEmpty(certifyId)){//如果认证ID为空，表示因为某种原因出错了（Activity重新启动？），直接跳转到失败页面
            Map<String, String> map = new HashMap<>();
            map.put("certifyId",certifyId);
            map.put("name",name);
            map.put("idCard",idCard);
            StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_CERT_FAIL, getString(R.string.user_alipay_cert), APP,map);
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.CERT_TYPE, Constant.CERT_TYPE_ALIPAY);
            bundle.putString(Constant.CERT_FAIL_MSG, "certifyId 为空");
            BaseJumper.jumpBundleARouter(RouterTable.Cert.PATH_CERT_FAIL_ACT, bundle);
            finish();
            return;
        }
        showLoading();
        AlipayCertModel model = new AlipayCertModel();
        model.getAliCertResult(name,idCard,certifyId, new CallBack.GetAliCertResultCallBack() {
            @Override
            public void onFailed(String errorCode, String errorMsg) {
                dismissLoading();
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.CERT_TYPE, Constant.CERT_TYPE_ALIPAY);

                if (errorCode != null && GetAliCertResultResp.KEY_REMAIN_TIMES.equals(errorCode)){
                    bundle.putInt(Constant.CERT_FAIL_REMAIN_COUNT, Integer.valueOf(errorMsg));
                }else {
                    bundle.putString(Constant.CERT_FAIL_MSG, errorMsg);
                }

                BaseJumper.jumpBundleARouter(RouterTable.Cert.PATH_CERT_FAIL_ACT, bundle);

                finish();
            }

            @Override
            public void onSuccess(boolean success) {
                dismissLoading();
                ToastUtils.toastMsg(R.string.user_certification_success);

                //保存认证信息
                User user = UserManagerImpl.getInstance().getInnerUser();
                user.userName = name;
                user.idCard = idCard;

                user.addCertType(User.CERTIFY_ALIPAY);
                if (CommonUtils.checkIdcardValid(idCard)) {
                    user.sex = CommonUtils.checkSex(idCard);
                }
                UserManagerImpl.getInstance().updateUser(user);

                CertificationInterceptor.notifyCallBack(true);
                EventBusOutUtils.postCertificationSuccess(Constant.CERT_TYPE_ALIPAY);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ALIPAY){
            queryResult();
        }
    }

    @Override
    public void onBackPressed() {
        EventBusOutUtils.postCertificationCancle(Constant.CERT_TYPE_ALIPAY);
        finish();
    }
}
