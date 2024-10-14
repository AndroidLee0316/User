package com.pasc.business.user.net.resp;

import com.google.gson.annotations.SerializedName;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/31
 * 更改时间：2019/10/31
 */
public class AccoutCalceResp {
    @SerializedName("result")
    public String result;
    @SerializedName("isPaymentCancel")
    public boolean isPaymentCancel;
}
