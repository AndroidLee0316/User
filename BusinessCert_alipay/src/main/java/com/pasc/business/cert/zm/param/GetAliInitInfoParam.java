package com.pasc.business.cert.zm.param;

import com.google.gson.annotations.SerializedName;
import com.pasc.lib.userbase.user.LoginConstant;
import com.pasc.lib.userbase.user.net.param.BaseTokenParam;

/**
 * 获取（支付宝SDK调用加密串+查询凭证标志）请求参数
 */
public class GetAliInitInfoParam{

    @SerializedName("userName") public String userName; //用户姓名
    @SerializedName("idCard") public String idCard;   //用户身份证
    @SerializedName("appReturnJumpUrl") public String appReturnJumpUrl;   //支付宝回调URL（DeepLink）

    public GetAliInitInfoParam(String userName, String idCard) {
        this.userName = userName;
        this.idCard = idCard;
        this.appReturnJumpUrl = "smt://com.pingan.smt/alipayAuth";
    }
}
