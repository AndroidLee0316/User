package com.pasc.business.face.net.resp;

import com.google.gson.annotations.SerializedName;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/22
 * 更改时间：2019/10/22
 */
public class FaceCheckResp {
    public FaceCheckResp(){}
    @SerializedName("passed")
    public boolean passed;
    @SerializedName("credential")
    public String credential;
    @SerializedName("remainErrorTimes")
    public int remainErrorTimes;
    @SerializedName("certResult")
    public boolean certResult;
    @SerializedName("remainTimes")
    public int remainTimes;


}
