package com.pasc.business.face.net.pamars;

import com.google.gson.annotations.SerializedName;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/22
 * 更改时间：2019/10/22
 */
public class FaceInitPamars {
    public FaceInitPamars(String appid, int type,String aliUrl){
        this.appid = appid;
        this.checkType = type;
        this.appReturnJumpUrl = aliUrl;
    }
    @SerializedName("appid")
    public String appid;
    @SerializedName("checkType")
    public int checkType;
    @SerializedName("appReturnJumpUrl")
    public String appReturnJumpUrl;
}
