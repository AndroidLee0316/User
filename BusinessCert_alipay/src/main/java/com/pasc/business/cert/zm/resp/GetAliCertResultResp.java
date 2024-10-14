package com.pasc.business.cert.zm.resp;

import com.google.gson.annotations.SerializedName;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/8/14
 */
public class GetAliCertResultResp {

    public static final String KEY_REMAIN_TIMES = "remainTimes";

    @SerializedName("certResult") public boolean certResult; //查询凭证标志
    @SerializedName("remainTimes") public int remainTimes;   //支付宝SDK调用加密串

    public boolean isCertResult() {
        return certResult;
    }

    public void setCertResult(boolean certResult) {
        this.certResult = certResult;
    }

    public int getRemainTimes() {
        return remainTimes;
    }

    public void setRemainTimes(int remainTimes) {
        this.remainTimes = remainTimes;
    }
}
