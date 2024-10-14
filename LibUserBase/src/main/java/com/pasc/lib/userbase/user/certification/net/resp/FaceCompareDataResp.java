package com.pasc.lib.userbase.user.certification.net.resp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kuangxiangkui192 on 2018/12/11.
 * 解析 FaceAndIdComparisonErrorResp.java 里面的data
 */
public class FaceCompareDataResp {

    @SerializedName("idPassed")
    public int idPassed;
    @SerializedName("allowedAuthCount")
    public String allowedAuthCount;
}
