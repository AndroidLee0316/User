package com.pasc.business.user.net.pamars;

import com.google.gson.annotations.SerializedName;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */

public class SetPhoneParams {

  @SerializedName("newMobile")
  public String newMobile;
  @SerializedName("credential")
  public String credential;
  @SerializedName("messageCode")
  public String messageCode;

  public SetPhoneParams(String newMobile, String credential, String messageCode) {
    this.newMobile = newMobile;
    this.credential = credential;
    this.messageCode = messageCode;
  }
}
