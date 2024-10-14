package com.pasc.lib.userbase.user.net.resp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kuangxiangkui192 on 2018/12/3.
 */
public class GetUserInfoResp {
    public static final String SEX_UNKNOWN = "0";
    public static final String SEX_MALE = "1";
    public static final String SEX_FEMALE = "2";

    /**
     * 是否已经同意了用户隐私协议：是
     */
    public static final String SIGN_PRIVATE_AGREEMENT_ALREADY = "1";

    /**
     * 是否已经同意了用户隐私协议：否
     */
    public static final String SIGN_PRIVATE_AGREEMENT_UN = "0";

    /**
     * userId : 89
     * mobileNo : 18926598470
     * headImg : de6ded3458d54b8686243c43ca5a23b9
     */

    @SerializedName("userId")
    private String userId;

    @SerializedName("mobileNo")
    private String mobileNo;

    @SerializedName("headImg")
    private String headImg;

    @SerializedName("sex")
    private String sex;  //0: 未知 1:男 2:女

    @SerializedName("setPsdStatus")
    private String hasPassword;

    @SerializedName("hasOpenface")
    private String hasOpenface;

    /**
     * 是否同意了隐私协议
     */
    @SerializedName("signPrivateAgreement")
    private String signPrivateAgreement;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(String hasPassword) {
        this.hasPassword = hasPassword;
    }

    public String getHasOpenface() {
        return hasOpenface;
    }

    public void setHasOpenface(String hasOpenface) {
        this.hasOpenface = hasOpenface;
    }

    public String getSignPrivateAgreement() {
        return signPrivateAgreement;
    }

    public void setSignPrivateAgreement(String signPrivateAgreement) {
        this.signPrivateAgreement = signPrivateAgreement;
    }
}
