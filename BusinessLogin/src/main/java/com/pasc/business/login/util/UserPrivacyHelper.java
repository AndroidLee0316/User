package com.pasc.business.login.util;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasc.business.login.R;
import com.pasc.lib.hybrid.PascHybrid;
import com.pasc.lib.hybrid.nativeability.WebStrategy;
import com.pasc.lib.userbase.user.urlconfig.LoginUrlManager;
import com.pasc.lib.userbase.user.urlconfig.UserUrlConfig;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/8/6
 */
public class UserPrivacyHelper {


    private LinearLayout mPrivacyLL;

    /**
     * 协议是否同意勾选按钮
     */
    private ImageView mPrivacyIV;
    /**
     * 同意text
     */
    private TextView mPreTV;
    /**
     * 隐私协议
     */
    private TextView mPrivacyTV;
    /**
     * 服务协议
     */
    private TextView mAgreementTV;
    /**
     * 隐私协议、服务协议分割顿号
     */
    private TextView mSplitTV;

    /**
     * 是否勾选了同意用户协议
     */
    private boolean isSelectPrivacy = false;

//    /**
//     * 是否以前已经同意了隐私协议
//     */
//    private boolean isAlreadyAgreePrivacy = false;

    private CallBack mCallBack;

    public UserPrivacyHelper(View parantView, CallBack mCallBack) {
        this.mCallBack = mCallBack;
        mPrivacyLL = parantView.findViewById(R.id.pasc_user_privacy_ll);
        mPrivacyIV = parantView.findViewById(R.id.pasc_user_privacy_iv);
        mPreTV = parantView.findViewById(R.id.pasc_user_privacy_pre_tv);
        mPrivacyTV = parantView.findViewById(R.id.pasc_user_privacy_tv);
        mAgreementTV = parantView.findViewById(R.id.pasc_user_agreement_tv);
        mSplitTV = parantView.findViewById(R.id.pasc_user_privacy_agreement_split);
        initView();
    }

    private void initView(){

        mPrivacyIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelectPrivacy){
                    isSelectPrivacy = false;
                }else {
                    isSelectPrivacy = true;
                }
                mPrivacyIV.setImageResource(isSelectPrivacy ? R.drawable.check_select : R.drawable.single_unselect);
                if (mCallBack != null){
                    mCallBack.onSelectClick(isSelectPrivacy);
                }
            }
        });

        if (!TextUtils.isEmpty(LoginUrlManager.getInstance().getPrivacyText())){
            mPrivacyTV.setText(LoginUrlManager.getInstance().getPrivacyText());
        }else {
            mPrivacyTV.setVisibility(View.GONE);
            mSplitTV.setVisibility(View.GONE);
        }

        mPrivacyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PascHybrid.getInstance().start(view.getContext(),
                        new WebStrategy().setUrl(LoginUrlManager.getInstance().getPrivacyUrl()));
            }
        });


        if (!TextUtils.isEmpty(LoginUrlManager.getInstance().getAgreementText())){
            mAgreementTV.setText(LoginUrlManager.getInstance().getAgreementText());
        }else {
            mAgreementTV.setVisibility(View.GONE);
            mSplitTV.setVisibility(View.GONE);
        }

        mAgreementTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PascHybrid.getInstance().start(view.getContext(),
                        new WebStrategy().setUrl(LoginUrlManager.getInstance().getAgreementUrl()));
            }
        });

        if (LoginUrlManager.getInstance().getAgreementLocation() == UserUrlConfig.LoginConfigBean.AGREEMENT_LOCATION_IN_BOTTOM){
            mAgreementTV.setVisibility(View.GONE);
            mSplitTV.setVisibility(View.GONE);
        }
    }

    public void updateSelectPrivacy(boolean isSelectPrivacy){
        this.isSelectPrivacy = isSelectPrivacy;
        mPrivacyIV.setImageResource(isSelectPrivacy ? R.drawable.check_select : R.drawable.single_unselect);

    }

    public boolean isSelectPrivacy(){
        return isSelectPrivacy;
    }

    /**
     * 切换为无选择按钮的模式:强制显示两个协议
     */
    public void switchNoSelectType(){
        isSelectPrivacy = true;
        mPrivacyIV.setVisibility(View.GONE);
        mPreTV.setText(R.string.user_register_agree_server);
        mAgreementTV.setVisibility(View.VISIBLE);
        mSplitTV.setVisibility(View.VISIBLE);
    }

    public static interface CallBack{
        void onSelectClick(boolean isSelect);
    }


}
