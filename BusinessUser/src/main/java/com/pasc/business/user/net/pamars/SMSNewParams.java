package com.pasc.business.user.net.pamars;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public class SMSNewParams implements Serializable {
  @SerializedName("newMobile")
  public String newMobile;
  @SerializedName("credential")
  public String credential;

  public SMSNewParams(String newMobile, String credential) {
    this.newMobile = newMobile;
    this.credential = credential;
  }
}
