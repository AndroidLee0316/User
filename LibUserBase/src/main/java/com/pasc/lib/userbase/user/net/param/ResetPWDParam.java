package com.pasc.lib.userbase.user.net.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ex-luyang001 on 2017/11/30.
 */

public class ResetPWDParam {

  @SerializedName("mobileNo")
  public String mobileNo;
  @SerializedName("password")
  public String password;
  @SerializedName("envSafeInfo")
  public ForgetPWDParam.EnvSafeInfo envSafeInfo;

  public ResetPWDParam(String mobileNo, String password, String validateCode) {
    this.mobileNo = mobileNo;
    this.password = password;
    this.envSafeInfo = new ForgetPWDParam.EnvSafeInfo(validateCode);
  }
}
