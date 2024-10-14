package com.pasc.business.face.net.resp;

import com.google.gson.annotations.SerializedName;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/22
 * 更改时间：2019/10/22
 */
public class FaceInitResp {
    @SerializedName("isValidate")
    public boolean isValidate;
    @SerializedName("initcode")
    public String initcode;
    @SerializedName("validType")
    public String validType;
    @SerializedName("aliVO")
    public AliVo aliVO;
    @SerializedName("credential")
    public String credential;


    public static class AliVo {
        @SerializedName("certifyUrl")
        public String certifyUrl;
        @SerializedName("credential")
        public String credential;
        @SerializedName("certifyId")
        public String certifyId;
    }
}
