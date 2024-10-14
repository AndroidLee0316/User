package com.pasc.business.face.net.pamars;

import com.google.gson.annotations.SerializedName;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/22
 * 更改时间：2019/10/22
 */
public class FaceCheckQueryApliPamars {
    public FaceCheckQueryApliPamars(String appid, String certifyId){
        this.appid = appid;
        this.certifyId = certifyId;
    }
    @SerializedName("appid")
    public String appid;
    @SerializedName("certifyId")
    public String certifyId;
}
