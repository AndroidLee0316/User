package com.pasc.business.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.cert.CertifyUtils;
import com.pasc.business.user.PascUserFaceCheckListener;
import com.pasc.business.user.PascUserManager;
import com.pasc.business.user.R;
import com.pasc.lib.base.activity.BaseActivity;
import com.pasc.lib.base.util.StatusBarUtils;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.keyboard.KeyboardBaseView;
import com.pasc.lib.keyboard.KeyboardPopWindow;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.base.view.CommonTitleView;
import com.pasc.lib.userbase.base.view.FormatEditText;
import com.pasc.lib.widget.ClearEditText;

import java.util.Map;

/**
 * Created by zhangxu678 on 2020-03-05.
 */
@Route(path = RouterTable.User.PATH_USER_ACCOUNT_CERTIFICATION_ACT)
public class AccountCertificationActivity extends BaseActivity implements View.OnClickListener {
  private ClearEditText etName;
  private FormatEditText etId;
  private Button btnNext;
  private CommonTitleView titleView;

  @Override protected int layoutResId() {
    return R.layout.user_activity_account_certification;
  }

  @Override protected void onInit(@Nullable Bundle savedInstanceState) {
    StatusBarUtils.setStatusBarColor(this,true);
    etName = findViewById(R.id.et_name);
    etId = findViewById(R.id.et_id);
    titleView=findViewById(R.id.user_top_bar);
    etId.setFormatType(FormatEditText.TYPE_ID_CARD);
    btnNext=findViewById(R.id.btn_next);
    btnNext.setOnClickListener(this);
    KeyboardPopWindow idKPW = KeyboardPopWindow.bindEdit(this, etId, KeyboardBaseView.KeyboardNumberTheme.TYPE_ID_CARD);
    etId.setOnFocusChangeOutListener(new FormatEditText.OnFocusChangeOutListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        idKPW.onEditFocesChange(v, hasFocus);
      }
    });
    titleView.setOnLeftClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    etName.setLimited(true);
    etName.setEditTextChangeListener(new ClearEditText.EditTextChangeListener() {
      @Override
      public void afterChange(String s) {
        setBtnNextClickable();
      }
    });
    etId.setEditTextChangeListener(new FormatEditText.EditTextChangeListener() {
      @Override
      public void afterChange(String s) {
        setBtnNextClickable();
      }
    });

  }

  @Override public void onClick(View v) {
    int id=v.getId();
     if(id==R.id.btn_next){
       String realName = etName.getText().toString().trim();
       String idCard = etId.getOriginalText().trim();
       if (!CommonUtils.isUsernameLegal(realName)) {
         ToastUtils.toastMsg("姓名格式有误");
         return;
       } else if (!CommonUtils.checkIdcardValid(idCard)){
         ToastUtils.toastMsg("身份证格式有误，请重试");
         return;
       }
       PascUserManager.getInstance().toFaceCheck(realName, idCard, new PascUserFaceCheckListener() {
             @Override public void onSuccess(Map<String, String> data) {
               String certId = data.get("certId");
               Intent intent=new Intent(AccountCertificationActivity.this,NewPhoneActivity.class);
               intent.putExtra("certId",certId);
               startActivity(intent);

             }

             @Override public void onFailed() {

             }

             @Override public void onCancled() {

             }
           });
     }

  }
  private void setBtnNextClickable() {
    boolean b1 = etName.getText().toString().trim().length() >= 2;
    boolean b2 = etId.getOriginalText().trim().length() >= 18;
    if (b1 && b2 ) {
      btnNext.setEnabled(true);
      btnNext.setAlpha(1f);
    } else {
      btnNext.setEnabled(false);
      btnNext.setAlpha(0.3f);
    }
  }

}
