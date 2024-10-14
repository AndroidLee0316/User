package com.pasc.business.cert.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.cert.CertifyUtils;
import com.pasc.business.cert.R;
import com.pasc.business.cert.config.CertOutConfigManager;
import com.pasc.business.cert.config.CertifyErrorCodeConfig;
import com.pasc.business.cert.iview.IChooseCertifyView;
import com.pasc.business.cert.presenter.ChooseCertifyPresenter;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.router.interceptor.CertificationInterceptor;
import com.pasc.lib.statistics.StatisticsManager;
import com.pasc.lib.userbase.base.data.Constant;
import com.pasc.lib.userbase.base.event.EventTag;
import com.pasc.lib.userbase.user.urlconfig.CertiUrlManager;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;
import com.pasc.lib.widget.dialog.OnConfirmListener;
import com.pasc.lib.widget.dialog.common.ConfirmDialogFragment;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.base.BaseStatusBarActivity;
import com.pasc.lib.userbase.user.util.UserManagerImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.pasc.lib.statistics.PageType.APP;

/**
 * 选择验证方式界面
 * Created by ruanwei489 on 2018/4/17.
 */
@Route(path = RouterTable.Cert.PATH_CERT_AUTH_TYPE_ACT)
public class IChooseCertificationActivity extends BaseStatusBarActivity implements IChooseCertifyView {

    protected Context mContext;
    protected User user;
    protected ChooseCertifyPresenter chooseCertifyPresenter;

    protected CommonTitleView commonTitleView;
    private RelativeLayout rlCertifiedView;
    private TextView tvRealName;
    private TextView tvIdNum;
    private ConfirmDialogFragment noMoreCertifyTimesDialog;


    /**
     * 当认证成功当时候是否关闭 IChooseCertificationActivity 页面
     */
    public static final String EXTRA_PARAM_FINISH_WHEN_CERT_SUCCESS = "finish_when_cert_success";
    private boolean finishWhenCertSuccess = false;

    /**
     * 点击的认证方式
     */
    public int certTypeAction;

    /**
     * 认证类型列表
     */
    protected List<CertificationTypeBean> mCtbList;


    protected ListView ctListView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        chooseCertifyPresenter = new ChooseCertifyPresenter(this);
        setContentView(R.layout.cert_activity_choose_certification);
        EventBus.getDefault().register(this);
        initView();
        initData();
        updateData();
        finishWhenCertSuccess = getIntent().getBooleanExtra(EXTRA_PARAM_FINISH_WHEN_CERT_SUCCESS,false);
    }


    protected void initView() {
        commonTitleView = findViewById(R.id.user_ctv_title);
        ctListView = findViewById(R.id.cert_activity_choose_certification_lv);
        user = UserManagerImpl.getInstance().getInnerUser();
        CommonTitleView ctvTitle = findViewById(R.id.user_ctv_title);

        rlCertifiedView = findViewById(R.id.user_activity_certifyChoose_rlCertifyStatus);
        tvRealName = findViewById(R.id.user_activity_certifyChoose_tvRealName);
        tvIdNum = findViewById(R.id.user_activity_certifyChoose_tvIdNum);
        ctvTitle.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBack();
            }
        });
        initListView();
        refreshCertifiedView();
    }

    protected void initListView(){
        ctListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mCtbList != null && mCtbList.size()>i){
                    if (CommonUtils.isFastClick()) {
                        return;
                    }
                    if (mCtbList.get(i).isCert){
                        return;
                    }
                    certTypeAction = mCtbList.get(i).certType;
                    if (mCtbList.get(i).certType == CertificationTypeBean.CERTIFICATION_TYPE_FACE_ALL) {
                        IChooseCertificationFaceListActivity.startFaceListActivityFromIChooseCertification(mContext);
                        return;
                    }else if (mCtbList.get(i).isCustom){
                        //如果认证类型大于这个系统自带的默认的认证类型最大值，意味着这个认证类型是自定义的认证
                        //如果不在默认添加的认证当中，我们就当他是外部添加的自定义的认证
                        //modify in 2020/05/08 ，添加是否是自定义认证参数 isCustom ,因为后端不愿意配合新增certType，而是直接用certFace
                        List<CertOutConfigManager.CustomCertItem> customCertList = CertOutConfigManager.getInstance().getCustomCertList();
                        if (customCertList != null && customCertList.size() > 0){
                            for (CertOutConfigManager.CustomCertItem item : customCertList){
                                if (item.certInfo.certType == certTypeAction && item.clickCallBack != null){
                                    item.clickCallBack.onClick((Activity) mContext);
                                }
                            }
                        }
                        return;
                    }else {//默认认证类型
                        StatisticsManager.getInstance().onEvent(Constant.ACCOUNT_CERT_START, mCtbList.get(i).certTitle,APP,null);
                    }
                    queryAuthCount(String.valueOf(certTypeAction));
                }
            }
        });
    }


    /**
     * 根据认证状态刷新用户信息
     */
    protected void refreshCertifiedView() {
        if (TextUtils.isEmpty(user.getCertiType())) {
            rlCertifiedView.setVisibility(View.GONE);
        } else {
            rlCertifiedView.setVisibility(View.VISIBLE);
            String username = AppProxy.getInstance().getUserManager().getUserName();
            tvRealName.setText(CertifyUtils.encrypted(username));
            tvIdNum.setText(CommonUtils.hideIDNum(UserManagerImpl.getInstance().getInnerUser().idCard));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        CertOutConfigManager.getInstance().unRegisterCustomCertResultListener();
        chooseCertifyPresenter.onDestroy();
    }

    /**
     * 接收到实名认证成功后关闭掉自身
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCertification(BaseEvent event) {
        if (EventTag.USER_CERTIFICATE_SUCCEED.equals(event.getTag()) || EventTag.USER_CERTIFICATE_FAILED.equals(event.getTag()) ){
            if (finishWhenCertSuccess){
                finish();
                return;
            }
            user = UserManagerImpl.getInstance().getInnerUser();
            refreshCertifiedView();
            updateData();
        }
    }

    /**
     * 跳转到平安人脸认证
     */
    private void gotoFaceCompareActivity() {
        if (!TextUtils.isEmpty(user.getUserName()) && !TextUtils.isEmpty(user.getIdCard())) {//如果已经是银行卡认证
            Bundle bundle = new Bundle();
            bundle.putString("IDcard", user.getIdCard());
            bundle.putString("name", user.getUserName());
            BaseJumper.jumpBundleARouter(RouterTable.Face.PATH_FACE_COMPARE_ACT, bundle);
        }else {
            Intent intent = new Intent();
            intent.setClass(mContext, ScanFaceCertificationActivity.class);
            intent.putExtra(ScanFaceCertificationActivity.EXTRA_FROM_ICHOOSE,true);
            startActivity(intent);
        }

    }

    /**
     * 跳转到芝麻认证
     */
    private void gotoZMFaceCompareActivity(){
        if (!CertifyUtils.isAlipayInstalled(mContext)){//未安装支付宝
            ToastUtils.toastMsg(R.string.user_un_install_alipay);
            return;
        }
        if (!TextUtils.isEmpty(user.getUserName()) && !TextUtils.isEmpty(user.getIdCard())){
            //直接跳到获取支付宝信息页面
            Bundle bundle = new Bundle();
            bundle.putString("IDcard", user.getIdCard());
            bundle.putString("name", user.getUserName());
            BaseJumper.jumpBundleARouter(RouterTable.Cert_ZM.PATH_CERT_AUTH_TYPE_FACE_MAIN, bundle);
        }else {
            //跳转到填写姓名、身份证页面
            BaseJumper.jumpBundleARouter(RouterTable.Cert_ZM.PATH_CERT_AUTH_TYPE_FACE_PRE, null);
        }
    }

    @Override
    public void queryAuthCountSucc() {
        dismissLoading();
        if (certTypeAction == Integer.valueOf(User.CERTIFY_FACE)) {
            gotoFaceCompareActivity();
        }else if (certTypeAction == Integer.valueOf(User.CERTIFY_ALIPAY)) {
            gotoZMFaceCompareActivity();
        }else if (certTypeAction == Integer.valueOf(User.CERTIFY_BANK)) {
//            //跳转不可编辑姓名、身份证号界面
            Intent intent = new Intent();
            intent.setClass(mContext,ICardCertifyActivity.class);
            intent.putExtra(ICardCertifyActivity.EXTRA_FROM_ICHOOSE,true);
            startActivity(intent);

        }else {
            Log.e(getClass().getSimpleName(),"queryAuthCount success , bug unknow cert type");
        }
    }

    /**
     * 认证次数大于三次的提醒
     */
    private void showQueryAuthCountFailDialog(String content) {
        if (noMoreCertifyTimesDialog == null) {
            noMoreCertifyTimesDialog = new ConfirmDialogFragment.Builder()
                    .setHideCloseButton(true)
                    .setCancelable(false)
                    .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
                        @Override
                        public void onConfirm(ConfirmDialogFragment confirmDialogFragment) {
                            confirmDialogFragment.dismiss();
                        }
                    })
                    .setDesc(content)
                    .setConfirmText(getResources().getString(R.string.user_iknow))
                    .setConfirmTextColor(getResources().getColor(R.color.pasc_primary))
                    .setDescColor(getResources().getColor(R.color.user_certify_chooseCertify_dialog_contentColor))
                    .build();
        }
        if (noMoreCertifyTimesDialog.getDialog() == null || !noMoreCertifyTimesDialog.getDialog().isShowing()) {
            noMoreCertifyTimesDialog.show(getSupportFragmentManager(), "NoMoreCertifyTimes");
        } else {
            noMoreCertifyTimesDialog.dismiss();
        }
    }

    @Override
    public void queryAuthCountFail(String code, String errorMsg) {
        dismissLoading();
        if (TextUtils.isEmpty(code)){
            ToastUtils.toastMsg(R.string.user_unknow_error);
            return;
        }
        switch (code) {
            case CertifyErrorCodeConfig.USER_CERT_BY_BANK_MAX_TIME:
            case CertifyErrorCodeConfig.USER_CERT_BY_FACE_MAX_TIME:
                showQueryAuthCountFailDialog(errorMsg);
                break;
            default:
                CommonUtils.toastMsg(errorMsg);
                break;
        }
    }

    /**
     * 获取当前账号认证次数
     */
    public void queryAuthCount(String type) {
        showLoading("");
        chooseCertifyPresenter.queryAuthCount(type);
    }

    @Override
    protected int layoutResId() {
        return 0;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {

    }



    public static class CertificationTypeAdapter extends BaseAdapter {

        private Context mContext;

        private List<CertificationTypeBean> ctbList;


        public CertificationTypeAdapter(Context mContext, List<CertificationTypeBean> ctbList) {
            this.mContext = mContext;
            this.ctbList = ctbList;
        }

        @Override
        public int getCount() {
            if (ctbList == null){
                return 0;
            }
            return ctbList.size();
        }

        @Override
        public Object getItem(int position) {
            if (ctbList == null){
                return null;
            }
            return ctbList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null){
                View view = LayoutInflater.from(mContext).inflate(R.layout.cert_item_choose_certification,null);
                viewHolder.iconIV = view.findViewById(R.id.user_iv_face_vertify);
                viewHolder.titleTV = view.findViewById(R.id.user_tv_scan_face);
                viewHolder.subTitleTV = view.findViewById(R.id.user_tv_face_subtitle);
                viewHolder.cert1TV = view.findViewById(R.id.user_tv_face_label);
                viewHolder.cert2TV = view.findViewById(R.id.user_activity_certiChoose_tvFaceUnCerti);
                viewHolder.recommendTV = view.findViewById(R.id.user_tv_scan_face_recommend);
                viewHolder.nextIV = view.findViewById(R.id.user_img_face_right_arrow);
                convertView = view;
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.iconIV.setImageResource(ctbList.get(position).certIconID);
            viewHolder.titleTV.setText(ctbList.get(position).certTitle);
            viewHolder.subTitleTV.setText(ctbList.get(position).certSubTitle);
            if (ctbList.get(position).isCert) {

                viewHolder.cert2TV.setVisibility(View.INVISIBLE);
                viewHolder.nextIV.setVisibility(View.INVISIBLE);

                viewHolder.cert1TV.setText(R.string.user_certed);
                viewHolder.cert1TV.setVisibility(View.VISIBLE);
                viewHolder.recommendTV.setVisibility(View.GONE);
            } else {
                viewHolder.cert1TV.setVisibility(View.GONE);
                if (ctbList.get(position).certType == CertiUrlManager.getInstance().getCertRecommend()){
                    viewHolder.recommendTV.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.recommendTV.setVisibility(View.GONE);
                }

                viewHolder.cert2TV.setText(R.string.user_uncert);
                viewHolder.cert2TV.setVisibility(View.VISIBLE);
                viewHolder.nextIV.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        class ViewHolder{
            public ImageView iconIV;
            public TextView titleTV;
            public TextView subTitleTV;
            public TextView cert1TV;
            public TextView cert2TV;
            public TextView recommendTV;
            public ImageView nextIV;
        }
    }

    /**
     * 认证类型
     */
    class CertificationTypeBean{

        /**
         * 人脸认证(支付宝+平安人脸)
         */
        public static final int CERTIFICATION_TYPE_FACE_ALL = 11;

        /**
         * 第三方自定义的认证方式，也需要给一个type，从100开始，且在for循环的时候根据加入的顺序添加
         */
        public static final int CERTIFICATION_TYPE_CUSTOM = 100;

        /**
         * 第三方自定义的认证方式，也需要给一个type，从1000开始，且在for循环的时候根据加入的顺序添加
         */
        public static final int CERTIFICATION_TYPE_CUSTOM_FACE = 1000;

        /**
         * 认证类型
         */
        public int certType;
        /**
         * 认证类型图标
         */
        public int certIconID;
        /**
         * 认证类型名称
         */
        public String certTitle;
        /**
         * 认证类型描述
         */
        public String certSubTitle;
        /**
         * 是否已经认证
         */
        public boolean isCert = false;
        /**
         * 是否显示
         */
        public boolean isShow = false;
        /**
         * 是否是自定义的认证方式
         */
        public boolean isCustom = false;

    }

    protected void initData(){

        mCtbList = new ArrayList<>();

        if (CertiUrlManager.getInstance().getNeedBankCert()) {

            CertificationTypeBean ctb = new CertificationTypeBean();
            ctb.certType = Integer.valueOf(User.CERTIFY_BANK);
            ctb.certIconID = R.drawable.cert_ic_bank_verify;
            ctb.certTitle = getResources().getString(R.string.user_bank_cert);
            ctb.certSubTitle = getResources().getString(R.string.user_bank_cert_desc);
            ctb.isCert = false;
            ctb.isShow = true;
            mCtbList.add(ctb);

        }

        if (CertiUrlManager.getInstance().getNeedPAFaceCert()) {

            CertificationTypeBean ctb1 = new CertificationTypeBean();
            ctb1.certType = Integer.valueOf(User.CERTIFY_FACE);
            ctb1.certIconID = R.drawable.cert_ic_face_verify;
            ctb1.certTitle = getResources().getString(R.string.user_app_face_cert);
            ctb1.certSubTitle = getResources().getString(R.string.user_app_face_cert_desc);
            ctb1.isCert = false;
            ctb1.isShow = true;
            mCtbList.add(ctb1);
        }

        if (CertiUrlManager.getInstance().getNeedAlipayFaceCert()) {

            CertificationTypeBean ctb2 = new CertificationTypeBean();
            ctb2.certType = Integer.valueOf(User.CERTIFY_ALIPAY);
            ctb2.certIconID = R.drawable.cert_ic_face_alipay_verify;
            ctb2.certTitle = getResources().getString(R.string.user_alipay_cert);
            ctb2.certSubTitle = getResources().getString(R.string.user_alipay_cert_desc);
            ctb2.isCert = false;
            ctb2.isShow = true;
            mCtbList.add(ctb2);
        }

        interrupterAndAddCustomCert(false);
        registerCustomCertResult();
    }


    /**
     * 添加插入自定义的认证方式
     */
    protected void interrupterAndAddCustomCert(boolean isFaceList){
        List<CertOutConfigManager.CustomCertItem> customCertList = CertOutConfigManager.getInstance().getCustomCertList();
        if (customCertList != null && customCertList.size() > 0){
            for (CertOutConfigManager.CustomCertItem item : customCertList){

                CertificationTypeBean ctb = new CertificationTypeBean();
                ctb.certType = item.certInfo.certType;
                ctb.certIconID = item.certInfo.icon;
                ctb.certTitle = item.certInfo.certName;
                ctb.certSubTitle = item.certInfo.certDesc;
                ctb.isCert = false;
                ctb.isShow = true;
                //外部添加的认证方式 isCustom 为true, modify in 2020/05/08
                ctb.isCustom = true;

                if (item.certPosition >= CertificationTypeBean.CERTIFICATION_TYPE_CUSTOM_FACE){//人脸类型的认证
                    if (isFaceList){//人脸认证列表页面
                        mCtbList.add(item.certPosition - CertificationTypeBean.CERTIFICATION_TYPE_CUSTOM_FACE, ctb);
                    }
                }else if (item.certPosition >= CertificationTypeBean.CERTIFICATION_TYPE_CUSTOM){//一般类型的认证
                    if (!isFaceList){//一般类型的认证页面
                        mCtbList.add(item.certPosition - CertificationTypeBean.CERTIFICATION_TYPE_CUSTOM, ctb);
                    }
                }else {
                    Log.e(getClass().getSimpleName(),"not custom cert : " + item.certInfo.certName);
                }
            }
        }
    }

    protected void registerCustomCertResult(){

        //注册自定义认证的认证状态监听，如果认证状态改变了，则刷新认证信息
        CertOutConfigManager.getInstance().registerCustomCertResultListener(new CertOutConfigManager.CustomCertResultListener() {
            @Override
            public void onSuccess() {
                showLoading();
                chooseCertifyPresenter.queryUserInfo(certTypeAction, new ChooseCertifyPresenter.QueryUserInfoCallback() {
                    @Override
                    public void onSuccess() {
                        dismissLoading();
                        EventBusOutUtils.postCertificationSuccess(Constant.CERT_TYPE_UNKNOW);
                    }

                    @Override
                    public void onFailed(String code, String errorMsg) {
                        dismissLoading();
                        EventBusOutUtils.postCertificationFailed(Constant.CERT_TYPE_UNKNOW);
                    }
                });
            }

            @Override
            public void onFailed() {
                EventBusOutUtils.postCertificationFailed(Constant.CERT_TYPE_UNKNOW);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    protected void updateData(){
        for (CertificationTypeBean ctb : mCtbList){

            //如果认证列表中的认证类型在用户已经认证的类型中，设置为已认证
            if (user.getCertiType().contains(String.valueOf(ctb.certType))){
                ctb.isCert = true;

            }

            //如果认证列表是一级 人脸认证， 则需要判断是否是人脸认证
            if (ctb.certType == CertificationTypeBean.CERTIFICATION_TYPE_FACE_ALL){
                //人脸认证包括 平安人脸认证，支付宝人脸认证，
                if (user.getCertiType().contains(User.CERTIFY_ALIPAY) || user.getCertiType().contains(User.CERTIFY_FACE)) {
                    ctb.isCert = true;
                }

                //如果是自定义的人脸认证呢？暂时没有办法做判断咯，等有需求的时候再说把
            }
        }

        CertificationTypeAdapter adapter = new CertificationTypeAdapter(this, mCtbList);
        ctListView.setAdapter(adapter);

    }


    @Override
    public void onBackPressed() {
        keyBack();
    }

    /**
     * 返回（取消认证）
     */
    protected void keyBack(){
        CertificationInterceptor.notifyCallBack(false);
        EventBusOutUtils.postCertificationCancle(Constant.CERT_TYPE_UNKNOW);
        finish();
    }




}
