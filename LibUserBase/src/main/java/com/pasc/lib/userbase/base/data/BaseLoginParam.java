package com.pasc.lib.userbase.base.data;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class BaseLoginParam {
  @SerializedName("extraMsg") public final LoginExtParams extraMsg;

  public BaseLoginParam(String json) {
    extraMsg = new Gson().fromJson(json, LoginExtParams.class);
    if (null != extraMsg) {
      extraMsg.platform = "Android";
    }
  }

  public static class LoginExtParams {
    @SerializedName("from") public String loginFrom;
    /**
     * 运营活动Id
     */
    @SerializedName("activityId") public String actId;
    @SerializedName("vt") public String vt;
    @SerializedName("platform") public String platform;
  }
}
