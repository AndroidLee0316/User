package com.pasc.lib.userbase.user.net.param;

import com.google.gson.annotations.SerializedName;

import com.pasc.lib.userbase.base.data.BaseLoginParam;

/**
 * Created by duyuan797 on 17/10/25.
 */

public class SZOtpLoginParam extends BaseLoginParam {
  public SZOtpLoginParam(String paramJson, String mobile,
                         String verifyCode,
                         String osType,
                         long clientVersion,
                         String channel) {
    super(paramJson);
    this.mobile = mobile;
    this.verifyCode = verifyCode;
    this.osType = osType;
    this.clientVersion = clientVersion;
    this.channelSrc = channel;
  }

  public SZOtpLoginParam(String mobile, String verifyCode, String osType) {
    this(mobile, verifyCode, osType, 0, "");
  }

  public SZOtpLoginParam(String mobile,
                         String verifyCode,
                         String osType,
                         long clientVersion,
                         String channel) {
    this("",
        mobile,
        verifyCode,
        osType,
        clientVersion,
        channel);
  }

  @SerializedName("mobile") public String mobile;
  @SerializedName("verifyCode") public String verifyCode;
  @SerializedName("osType") public String osType;
  @SerializedName("clientVersion") public long clientVersion;
  @SerializedName("channelSrc") public String channelSrc;
}
