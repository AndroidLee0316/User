package com.pasc.business.face.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.face.R;
import com.pasc.business.face.data.FaceConstant;
import com.pasc.business.face.iview.IFaceCheckFialView;
import com.pasc.business.face.net.resp.FaceCheckResp;
import com.pasc.business.face.net.resp.FaceInitResp;
import com.pasc.business.face.presenter.FaceCheckFailPresenter;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.user.urlconfig.FaceUrlManager;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.userbase.user.util.UserManagerImpl;

import java.net.URLEncoder;
import java.util.List;

/**
 * 人脸核验失败
 */
@Route(path = RouterTable.Face.PATH_FACE_CHECK_FAILED_ACT)
public class FaceCheckFailActivity extends BaseStatusBarActivity implements IFaceCheckFialView {
    public static final int TYPE_CHECK_COMPARE = 1001;
    public static final int TYPE_CHECK_ID_COMPARE = 1002;
    public static final int TYPE_CHECK_APLI = 1003;
    public static final int TYPE_INFO_CHECK_COMPARE = 1004;

    private CommonTitleView commonTitleView;
    /***可用认证次数*/
    private TextView tvRetry;

    private TextView tvHint;
    private TextView textSelect;
    private LinearLayout llSelect;
    private ImageView faceCheckImageSelect;
    private TextView textReset;
    private TextView faceCheckTextSelect;
    private String appId;
    private TextView faceIdRety;

    //认证方式，默认-1为未知默认方式
    private int checkType = TYPE_CHECK_COMPARE;

    //认证次数，-1表示没有认证次数返回
    private int account = -1;

    private String msg;
    private FaceCheckFailPresenter mPresenter;

    //认证方式
    public static final String EXTRA_CHECK_TYPE = "checkType";
    //错误提示
    public static final String EXTRA_MSG = "msg";
    //剩余认证次数
    public static final String EXTRA_ACCOUNT = "account";
    public static final String EXTRA_USER_NAME = "userName";
    public static final String EXTRA_ID_CARD = "idCard";
    public String userName, idCard;

    public static void start(Context context, String appId, int checkType, String msg, int account) {

        Intent intent = new Intent();
        intent.setClass(context, FaceCheckFailActivity.class);
        intent.putExtra(EXTRA_CHECK_TYPE, checkType);
        intent.putExtra("appId", appId);
        if (!TextUtils.isEmpty(msg)) {
            intent.putExtra(EXTRA_MSG, msg);
        }
        intent.putExtra(EXTRA_ACCOUNT, account);

        context.startActivity(intent);

    }

    public static void start(Context context, String userName, String idCard, int checkType,
                             String msg, int account) {

        Intent intent = new Intent();
        intent.setClass(context, FaceCheckFailActivity.class);
        intent.putExtra(EXTRA_CHECK_TYPE, checkType);
        intent.putExtra(EXTRA_USER_NAME, userName);
        intent.putExtra(EXTRA_ID_CARD, idCard);
        if (!TextUtils.isEmpty(msg)) {
            intent.putExtra(EXTRA_MSG, msg);
        }
        intent.putExtra(EXTRA_ACCOUNT, account);

        context.startActivity(intent);
    }

    @Override
    protected int layoutResId() {
        return R.layout.face_activity_face_check_fail;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {
        super.onInit(bundle);
        userName = getIntent().getStringExtra(EXTRA_USER_NAME);
        idCard = getIntent().getStringExtra(EXTRA_ID_CARD);
        mPresenter = new FaceCheckFailPresenter(this);
        tvRetry = findViewById(R.id.face_activity_face_check_fail_retry);
        tvHint = findViewById(R.id.face_activity_face_check_fail_hint);
        textSelect = findViewById(R.id.text_select);
        faceCheckImageSelect = findViewById(R.id.face_check_selecr_icon);
        faceIdRety = findViewById(R.id.face_activity_face_check_id_reTy);
        faceCheckTextSelect = findViewById(R.id.face_check_selecr_text);
        llSelect = findViewById(R.id.ll_select);
        textReset = findViewById(R.id.text_reset);
        commonTitleView = findViewById(R.id.face_activity_face_check_fail_title);
        commonTitleView.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        faceIdRety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (account <= 0) {
                    onBackPressed();
                } else {
                    if (checkType == TYPE_CHECK_APLI) {
                        toCheck(2);
                    } else if (checkType == TYPE_INFO_CHECK_COMPARE) {
                        Bundle paFaceBundle = new Bundle();
                        paFaceBundle.putString("userName", userName);
                        paFaceBundle.putString("idCard", idCard);
                        BaseJumper.jumpBundleARouter(RouterTable.Face.PATH_FACE_INFO_CHECK_ACT, paFaceBundle);
                    }  else {
                        toCheck(1);
                    }
                }
            }
        });
        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account <= 0) {
                    onBackPressed();
                } else {
                    if (checkType == TYPE_CHECK_APLI) {
                        toCheck(2);
                    }  else if (checkType == TYPE_INFO_CHECK_COMPARE) {
                        Bundle paFaceBundle = new Bundle();
                        paFaceBundle.putString("userName", userName);
                        paFaceBundle.putString("idCard", idCard);
                        BaseJumper.jumpBundleARouter(RouterTable.Face.PATH_FACE_INFO_CHECK_ACT, paFaceBundle);
                        finish();
                    }else {
                        toCheck(1);
                    }
                }
            }
        });
        textReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.delectFace();
            }
        });
        llSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkType == TYPE_CHECK_APLI) {
                    toCheck(1);
                } else {
                    toCheck(2);
                }
            }
        });
        initErrorRemind();

        //如果是找回账号跳转到这里，这时候用户是没有登录的，所以没有重置的概念
        if (!UserManagerImpl.getInstance().isLogin()){
            textReset.setVisibility(View.GONE);
        }

    }


    /**
     * 展示错误日志
     */
    private void initErrorRemind() {
        Intent intent = getIntent();
        if (intent != null) {
            checkType = intent.getExtras().getInt(EXTRA_CHECK_TYPE, TYPE_CHECK_COMPARE);
            account = intent.getExtras().getInt(EXTRA_ACCOUNT, 0);
            appId = intent.getExtras().getString("appId");
            msg = intent.getExtras().getString(EXTRA_MSG);
            if (checkType == TYPE_CHECK_APLI) {
                commonTitleView.setTitleText(getString(R.string.face_check_apli));
                faceCheckTextSelect.setText(getString(R.string.face_check_compare));
                faceCheckImageSelect.setImageResource(R.drawable.cert_ic_face_verify);
                faceIdRety.setVisibility(View.VISIBLE);
            } else if (checkType == TYPE_CHECK_ID_COMPARE) {
                faceIdRety.setVisibility(View.VISIBLE);
                commonTitleView.setTitleText(getString(R.string.face_check_compare));
                faceCheckTextSelect.setText(getString(R.string.face_check_apli));
                faceCheckImageSelect.setImageResource(R.drawable.face_check_icon_alipay);
                //需要隐藏支付宝人脸核验入口
                if (!FaceUrlManager.getInstance().needAlipayFaceCheck()){
                    llSelect.setVisibility(View.GONE);
                    textSelect.setVisibility(View.GONE);
                }
            } else {
                tvRetry.setVisibility(View.VISIBLE);
                commonTitleView.setTitleText(getString(R.string.face_check));
                textReset.setVisibility(View.VISIBLE);
                llSelect.setVisibility(View.GONE);
                textSelect.setVisibility(View.GONE);
            }
            String authCount;
            if (account <= 0) {
                textReset.setVisibility(View.GONE);
                llSelect.setVisibility(View.GONE);
                textSelect.setVisibility(View.GONE);
                account = 0;
                faceIdRety.setText(R.string.face_return);
                authCount = getResources().getString(R.string.face_check_failed_more_then_five);
                textReset.setVisibility(View.GONE);
                tvRetry.setText(R.string.face_return);
                SpannableString str = new SpannableString(authCount);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.pasc_primary));
                str.setSpan(colorSpan, 10, 11, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                str.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(17)), 10, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_999999)), 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_999999)), 11, authCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvHint.setText(str);
            } else {

                authCount = getResources().getString(R.string.face_check_failed_hint);
                tvHint.setText(authCount);
            }

        }
    }

    @Override
    public void onBackPressed() {
        EventBusOutUtils.postFaceCheckCancled();
        finish();
    }

    /**
     * 返回
     */

    private void toCheck(int compare) {
        mPresenter.faceInit(appId, compare, FaceConstant.URL_APLI);
    }

    @Override
    public void faceDelect(Object o) {
        toCheck(1);
    }

    @Override
    public void faceResult(FaceCheckResp resp) {
        if (resp.certResult) {
            EventBusOutUtils.postFaceCheckSuccess(resp.credential,"false");
        } else {
            FaceCheckFailActivity.start(this, appId, FaceCheckFailActivity.TYPE_CHECK_APLI, "请重新识别完成认证", resp.remainErrorTimes);
        }
        finish();
    }

    @Override
    public void faceInitview(FaceInitResp resp) {
        if ("1".equals(resp.validType) || "2".equals(resp.validType)) {
            Bundle bundle = new Bundle();
            bundle.putString("initCode", resp.initcode);
            bundle.putString("appId", appId);
            bundle.putString("compare", resp.validType);
            BaseJumper.jumpBundleARouter(RouterTable.Face.PATH_FACE_CHECK_ACT, bundle);
            finish();
        } else {
            if (resp.aliVO != null) {
                certifyId = resp.aliVO.certifyId;
                gotoAlipayCert(resp.aliVO.certifyUrl);
            } else {
                ToastUtils.toastMsg("支付宝认证初始化失败，请选择其他认证方式");
            }
        }
    }

    @Override
    public void onError(String code, String error) {
        if ("40001".equals(code)) {
            FaceCheckFailActivity.start(this, appId, checkType, "请重新识别完成认证", 0);
            finish();

        } else {
            ToastUtils.toastMsg(error);
        }
    }

    @Override
    public void showLoadings() {
        showLoading();
    }

    @Override
    public void dismissLoadings() {
        dismissLoading();
    }

    private static final int REQUEST_CODE_ALIPAY = 100;
    private String certifyId;

    /**
     * 跳转到支付宝认证
     *
     * @param url
     */
    private void gotoAlipayCert(String url) {
        if (!isAlipayInstalled(this)) {//未安装支付宝
            ToastUtils.toastMsg(R.string.face_check_no_apli);
            return;
        }
        Intent action = new Intent(Intent.ACTION_VIEW);
        StringBuilder builder = new StringBuilder();
        builder.append("alipays://platformapi/startapp?appId=20000067&url=");
        builder.append(URLEncoder.encode(url));
        action.setData(Uri.parse(builder.toString()));
        startActivityForResult(action, REQUEST_CODE_ALIPAY);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ALIPAY) {
            mPresenter.queryResult(appId, certifyId);
        }
    }

    /**
     * 判断是否安装了支付宝
     *
     * @param context
     * @return
     */
    public static boolean isAlipayInstalled(Context context) {
        PackageManager manager = context.getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List<ResolveInfo> list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }

}
