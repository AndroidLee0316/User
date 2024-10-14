package com.pasc.lib.userbase.base.data.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kuangxiangkui192 on 2018/12/18.
 * QQ登录授权成功后返回数据
 */
public class QQLoginBean {

    /**
     * ret : 0
     * openid : 8F70BD6EF5682798434CBD61DC014120
     * access_token : 56E25072123EBDBBD1C91CE7BAD5CB74
     * pay_token : 5B5FA07EDD46508D78655A6CA67768CD
     * expires_in : 7776000
     * pf : desktop_m_qq-10000144-android-2002-
     * pfkey : f1af4497cb22095f40d405a7fc403868
     * msg :
     * login_cost : 94
     * query_authority_cost : 1292
     * authority_cost : 0
     * expires_time : 1552879119528
     */
    @SerializedName("ret")
    private int ret;
    @SerializedName("openid")
    private String openid;
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("pay_token")
    private String pay_token;
    @SerializedName("expires_in")
    private int expires_in;
    @SerializedName("pf")
    private String pf;
    @SerializedName("pfkey")
    private String pfkey;
    @SerializedName("msg")
    private String msg;
    @SerializedName("login_cost")
    private int login_cost;
    @SerializedName("query_authority_cost")
    private long query_authority_cost;
    @SerializedName("authority_cost")
    private int authority_cost;
    @SerializedName("expires_time")
    private long expires_time;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getPay_token() {
        return pay_token;
    }

    public void setPay_token(String pay_token) {
        this.pay_token = pay_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getPf() {
        return pf;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public String getPfkey() {
        return pfkey;
    }

    public void setPfkey(String pfkey) {
        this.pfkey = pfkey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getLogin_cost() {
        return login_cost;
    }

    public void setLogin_cost(int login_cost) {
        this.login_cost = login_cost;
    }

    public long getQuery_authority_cost() {
        return query_authority_cost;
    }

    public void setQuery_authority_cost(int query_authority_cost) {
        this.query_authority_cost = query_authority_cost;
    }

    public int getAuthority_cost() {
        return authority_cost;
    }

    public void setAuthority_cost(int authority_cost) {
        this.authority_cost = authority_cost;
    }

    public long getExpires_time() {
        return expires_time;
    }

    public void setExpires_time(long expires_time) {
        this.expires_time = expires_time;
    }
}