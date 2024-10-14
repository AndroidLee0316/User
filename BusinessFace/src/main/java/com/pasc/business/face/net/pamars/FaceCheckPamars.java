package com.pasc.business.face.net.pamars;

import com.google.gson.annotations.SerializedName;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/22
 * 更改时间：2019/10/22
 */
public class FaceCheckPamars {
    public FaceCheckPamars(String initCode,String appid,String userId,String plat,String version,String model,String type, byte[] bytes){
        this.initCode = initCode;
        this.appid = appid;
        this.userId = userId;
        this.plat = plat;
        this.version = version;
        this.model = model;
        this.type = type;
        this.basmt_pingface_imgs = bytes;
    }
    @SerializedName("initCode")
    public String initCode;
    @SerializedName("appid")
    public String appid;
    @SerializedName("userId")
    public String userId;
    @SerializedName("plat")
    public String plat;
    @SerializedName("version")
    public String version;
    @SerializedName("model")
    public String model;
    @SerializedName("type")
    public String type;
    @SerializedName("basmt_pingface_imgs")
    public byte[]  basmt_pingface_imgs;

}
