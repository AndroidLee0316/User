package com.pasc.business.cert.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasc.business.cert.R;
import com.pasc.lib.hybrid.PascHybrid;
import com.pasc.lib.userbase.user.urlconfig.CertiUrlManager;
import com.pasc.lib.userbase.user.urlconfig.UserUrlConfig;
import com.pasc.lib.widget.DensityUtils;
import com.pasc.lib.widget.dialog.OnConfirmListener;
import com.pasc.lib.widget.dialog.common.ConfirmDialogFragment;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/12/10
 */
public class CertSelectAgreementView extends LinearLayout{

    public static final int CERT_TYPE_BANK = 0;
    public static final int CERT_TYPE_FACE_PA = 1;
    public static final int CERT_TYPE_FACE_ALIPAY = 2;

    private Context mContext;

    private ImageView selectIV;

    private TextView preTV;

    private TextView agreementTV;

    /**
     * 仅显示提示信息时候显示
     */
    private TextView hintTV;

    /**
     * 认证警告类型：默认是 CERT_WARNINNG_TYPE_CHOOSE_WARNING_AND_DIALOG
     */
    private int certWarningType = UserUrlConfig.CertiConfigBean.CERT_WARNINNG_TYPE_NOTHING;

    /**
     * 是否勾选了同意
     */
    private boolean select = false;

    private SelectCallback selectCallback;

    public CertSelectAgreementView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.cert_view_select_agreement,this);
        initView(rootView);
        initListener();
        initData();
    }


    private void initView(View view){
        selectIV = view.findViewById(R.id.pasc_cert_view_select_agreement_iv);
        preTV = view.findViewById(R.id.pasc_cert_view_select_agreement_title_pre);
        agreementTV = view.findViewById(R.id.pasc_cert_view_select_agreement_title);
        hintTV = view.findViewById(R.id.pasc_cert_view_select_agreement_hint);
    }

    private void initListener(){
        selectIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select){
                    select = false;
                    selectIV.setImageResource(R.drawable.single_unselect);
                }else {
                    select = true;
                    selectIV.setImageResource(R.drawable.check_select);
                }
                if (selectCallback != null){
                    selectCallback.onSelectClick(select);
                }
            }
        });
        preTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectIV.callOnClick();
            }
        });
        agreementTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = CertiUrlManager.getInstance().addPrefixH5Host(getResources().getString(R.string.user_cert_agreement_url));
                PascHybrid.getInstance().start(mContext,url);
            }
        });
    }

    private void initData(){
        certWarningType = CertiUrlManager.getInstance().getCertWarningType();
        switch (certWarningType){
            case UserUrlConfig.CertiConfigBean.CERT_WARNINNG_TYPE_NOTHING:
            case UserUrlConfig.CertiConfigBean.CERT_WARNINNG_TYPE_JUST_DIALOG:
                select = true;//这种情况下默认就是选中了
                selectIV.setVisibility(GONE);
                agreementTV.setVisibility(GONE);
                preTV.setVisibility(GONE);
                hintTV.setVisibility(GONE);
            case UserUrlConfig.CertiConfigBean.CERT_WARNINNG_TYPE_JUST_WARNING:
            case UserUrlConfig.CertiConfigBean.CERT_WARNINNG_TYPE_WARNING_AND_DIALOG:
                select = true;//这种情况下默认就是选中了
                selectIV.setVisibility(GONE);
                preTV.setVisibility(GONE);
                agreementTV.setVisibility(GONE);
                hintTV.setVisibility(VISIBLE);
                break;
                default:
        }

    }

    private void setHint(int type){
        if (certWarningType == UserUrlConfig.CertiConfigBean.CERT_WARNINNG_TYPE_JUST_WARNING
                || certWarningType == UserUrlConfig.CertiConfigBean.CERT_WARNINNG_TYPE_WARNING_AND_DIALOG){
            String desc = null;
            switch (type){
                case CERT_TYPE_BANK:
                    desc = String.format(getResources().getString(R.string.user_cert_bank_agreement_hint_warning),getResources().getString(R.string.user_cert_agreement_warning_type_bank));
                    break;
                case CERT_TYPE_FACE_PA:
                    desc = String.format(getResources().getString(R.string.user_cert_face_agreement_hint_warning),getResources().getString(R.string.user_cert_agreement_warning_type_face_pa));
                    break;
                case CERT_TYPE_FACE_ALIPAY:
                    desc = String.format(getResources().getString(R.string.user_cert_face_agreement_hint_warning),getResources().getString(R.string.user_cert_agreement_warning_type_face_alipay));
                    break;
                default:
                    desc = String.format(getResources().getString(R.string.user_cert_face_agreement_hint_warning),getResources().getString(R.string.user_cert_agreement_warning_type_default));
            }
            hintTV.setText(desc);
        }
    }

    public boolean isSelect(){
        return select;
    }


    /**
     * 弹认证提示框，如果不需要弹认证提示，则直接返回
     * 注意：外层不要使用 onConfirm 里面的 DialogFragment,因为如果直接返回的话 DialogFragment 为空
     * @param fragmentManager
     * @param listener
     */
    public void showCertDialog(FragmentManager fragmentManager, int certType, OnConfirmListener listener){

        //如果警告类型为只是显示警告或者只是显示选择，则不弹框
        if (certWarningType == UserUrlConfig.CertiConfigBean.CERT_WARNINNG_TYPE_NOTHING
                || certWarningType == UserUrlConfig.CertiConfigBean.CERT_WARNINNG_TYPE_CHOOSE_WARNING
                || certWarningType == UserUrlConfig.CertiConfigBean.CERT_WARNINNG_TYPE_JUST_WARNING){
            listener.onConfirm(null);
            return;
        }


        ConfirmDialogFragment dialog = new ConfirmDialogFragment.Builder()
                .setCancelable(false)
                .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
                    @Override
                    public void onConfirm(ConfirmDialogFragment confirmDialogFragment) {
                        confirmDialogFragment.dismiss();
                        if (listener != null){
                            listener.onConfirm(confirmDialogFragment);
                        }
                    }
                })
                .setDesc(getCertDialogDesc(certType))
                .setDescColor(getResources().getColor(R.color.gray_333333))
                .setHideCloseButton(true)
                .setConfirmText(getResources().getString(R.string.user_iknow))
                .setConfirmTextColor(getResources().getColor(R.color.user_dialog_confirm_textColor))
                .build();
        dialog.show(fragmentManager,"certAgreement");
    }

    private String getCertDialogDesc(int type){
        String desc = null;
        switch (type){
            case CERT_TYPE_BANK:
                desc = String.format(getResources().getString(R.string.user_cert_bank_agreement_warning),getResources().getString(R.string.user_cert_agreement_warning_type_bank));
                break;
            case CERT_TYPE_FACE_PA:
                desc = String.format(getResources().getString(R.string.user_cert_face_agreement_warning),getResources().getString(R.string.user_cert_agreement_warning_type_face_pa));
                break;
            case CERT_TYPE_FACE_ALIPAY:
                desc = String.format(getResources().getString(R.string.user_cert_face_agreement_warning),getResources().getString(R.string.user_cert_agreement_warning_type_face_alipay));
                break;
            default:
                desc = String.format(getResources().getString(R.string.user_cert_agreement_warning),getResources().getString(R.string.user_cert_agreement_warning_type_default));

        }
        return desc;
    }

    public void setSelectCallback(int certType, SelectCallback selectCallback) {
        setHint(certType);
        this.selectCallback = selectCallback;
    }

    public interface SelectCallback{
        void onSelectClick(boolean select);
    }

}
