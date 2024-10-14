package com.pasc.lib.userbase.base.data.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.pasc.lib.base.user.IUserInfo;
import com.pasc.lib.base.util.JsonUtils;
import com.pasc.lib.userbase.base.data.SmtDb;
import com.pasc.lib.userbase.user.login.net.resp.EnvSafeInfoBean;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by duyuan797 on 17/10/25.
 */
@Table(database = SmtDb.class)
public class User extends BaseModel implements Serializable, IUserInfo {
    /***未认证*/
    public static final String CERTIFY_NONE = "0";
    /***银行卡认证*/
    public static final String CERTIFY_BANK = "1";
    /***平安人脸认证*/
    public static final String CERTIFY_FACE = "2";
    /***支付宝人脸认证*/
    public static final String CERTIFY_ALIPAY = "3";
    /***两者皆认证*/
    public static final String CERTIFY_BOTH = "3";
    /**第三方绑定：微信**/
    public static final String THIRD_LOGIN_WX = "1";
    /**第三方绑定：QQ**/
    public static final String THIRD_LOGIN_QQ = "2";
    /**第三方绑定：支付宝**/
    public static final String THIRD_LOGIN_ALIPAY = "3";
    /**是否开通了人脸：1表示开通了人脸**/
    public static final String HAS_OPEN_FACE = "1";
    /**是否开通密码登陆：1表示开通了密码登陆**/
    public static final String HAS_PASSWORD = "1";


    @Column(name = "userId")
    @SerializedName("userId")
    public String userId;//用户id

    @Column(name = "userName")
    @SerializedName("userName")
    public String userName;// 用户姓名

    @Column(name = "sex")
    @SerializedName("sex")
    public String sex;// 用户性别0，未知；1，男；2，女

    @PrimaryKey
    @Column(name = "mobileNo")
    @SerializedName("mobileNo")
    public String mobileNo;// 手机

    @Column(name = "idCard")
    @SerializedName("idCard")
    public String idCard;// 身份证

    @Column(name = "address")
    @SerializedName("address")
    public String address;// 住址

    @Column(name = "headImg")
    @SerializedName("headImg")
    public String headImg;// 头像地址

    /**
     * modify by lcb，该参数废弃，使用 certIdList 代替
     */
    @Column(name = "idPassed")
    @SerializedName("idPassed")
    public String idPassed;//0：未认证，1:银行卡认证 2：人脸认证 3：双重认证

    @Column(name = "email")
    @SerializedName("email")
    public String email;//电子邮箱

    @Column(name = "marry")
    @SerializedName("marry")
    public String marry;// 1,未婚;2,已婚

    @Column(name = "volk")
    @SerializedName("volk")
    public String volk;// 民族

    @Column(name = "birthday")
    @SerializedName("birthday")

    public String birthday;// 出生日期
    @Column(name = "birthPlace")
    @SerializedName("birthPlace")
    public String birthPlace;// 籍贯

    @Column(name = "degree")
    @SerializedName("degree")
    public String degree;// 学历

    @Column(name = "census")
    @SerializedName("census")
    public String census;//户籍类别：1农业户口；2非农户口

    @Column(name = "token")
    @SerializedName("token")
    public String token;//token

    @Column(name = "hasOpenface")
    @SerializedName("hasOpenface")
    public String hasOpenface;

    @Column(name = "collectionNum")
    @SerializedName("collectionNum")
    public int collectionNum;//收藏条目数量

    @Column(name = "businessNum")
    @SerializedName("businessNum")
    public int businessNum;//业务条目数量

    @Column(name = "mid")
    @SerializedName("mid")
    public String mid;//平安付_商户侧用户id

    @Column(name = "yqbToken")
    @SerializedName("yqbToken")
    public String yqbToken;//平安付_token

    @Column(name = "sign")
    @SerializedName("sign")
    public String sign;//平安付 签名

    @Column(name = "logintime")
    @SerializedName("logintime")
    public long logintime;//登陆时间

    @Column(name = "faceComparasionLoginCount")
    @SerializedName("faceComparasionLoginCount")
    public int faceComparasionLoginCount;//人脸登录剩余次数

    //匹配深圳项目业务逻辑新增字段
    @Column(name = "hasPassword")
    @SerializedName("setPsdStatus")
    public String hasPassword;//是否设置密码验证
    @SerializedName("thirdUserInfo")
    public ThirdUserInfo thirdUserInfo;//第三方用户信息
    @Column(name = "thirdUser")
    @SerializedName("thirdUser")
    public String thirdUser;//第三方用户信息，保存数据库

    @Column(name = "nickName")
    @SerializedName("nickName")
    public String nickName;//第三方用户信息，保存数据库

    /**
     * 20190807 by lcb
     * 该参数废弃，改用 bindThirds 表示绑定的第三方登陆
     */
    @Deprecated()
    @Column(name = "isBindThird")
    @SerializedName("isBindThird")
    public String isBindThird;//第三方绑定信息 1.微信QQ均绑定 2.QQ绑定  3.微信绑定 4.微信QQ都不绑定

    @Column(name = "wxName")
    @SerializedName("wxName")
    public String wxName;

    @Column(name = "qqName")
    @SerializedName("qqName")
    public String qqName;

    @Column(name = "alipayName")
    @SerializedName("alipayName")
    public String alipayName;

    /**
     * 绑定的第三方登陆列表
     */
    @SerializedName("bindThirds")
    public List<String> bindThirds;

    /**
     * 绑定的第三方登陆列表是list，dbflow无法保存，所以只能另外弄一个字段保存数据
     */
    @Column(name = "bindThirdsSave")
    @SerializedName("bindThirdsSave")
    public String bindThirdsSave;

    /**
     * 已实名认证方式列表
     */
    @SerializedName("certIdList")
    public List<String> certIdList;

    /**
     * 已实名认证方式列表是list，dbflow无法保存，所以只能另外弄一个字段保存数据
     */
    @Column(name = "certIdListSave")
    @SerializedName("certIdListSave")
    public String certIdListSave;

    @Column(name = "nickNameStatus")
    @SerializedName("nickNameStatus")
    public String nickNameStatus;//用户是否修改过昵称

    @SerializedName("envSafeInfo")
    public EnvSafeInfoBean envSafeInfo;


    //------下面为盐城独特添加的字段-------



    public static final String KEY_payAccountId = "payAccountId";

    /**
     * 支付号
     */
    @Column(name = KEY_payAccountId)
    @SerializedName(KEY_payAccountId)
    public String payAccountId;




    public ThirdUserInfo getThirdUserInfo() {
        if (thirdUserInfo == null && !TextUtils.isEmpty(thirdUser)) {
            try {
                thirdUserInfo = JsonUtils.fromJson(thirdUser, ThirdUserInfo.class);
            } catch (Exception e) {

            }
        }
        return thirdUserInfo;
    }

    @Override
    public String getUserId() {
        return userId == null ? "" : userId;
    }

    @Override
    public String getUserName() {
        return userName == null ? "" : userName;
    }

    @Override
    public String getSex() {
        return sex == null ? "" : sex;
    }

    @Override
    public String getMobileNo() {
        return mobileNo == null ? "" : mobileNo;
    }

    @Override
    public String getIdCard() {
        return idCard == null ? "" : idCard;
    }

    @Override
    public String getAddress() {
        return address == null ? "" : address;
    }

    @Override
    public String getEmail() {
        return email == null ? "" : email;
    }

    @Override
    public String getHeadImg() {
        return headImg == null ? "" : headImg;
    }

    @Deprecated
    @Override
    public String getIdPassed() {
        return idPassed == null ? "" : idPassed;
    }

    /**
     * modify by lcb : 返回的是认证方式列表，中间以 | 隔开
     * @return
     */
    @Override
    public String getCertiType() {
        getCertIdList();
        if (certIdList == null || certIdList.size() == 0){
            return "";
        }else {
            StringBuffer sb = new StringBuffer();
            for (String certID : certIdList){
                sb.append(certID).append("|");
            }
            return sb.toString();
        }
//        return idPassed == null ? "" : idPassed;
    }

    @Override
    public String getMarray() {
        return marry == null ? "" : marry;
    }

    @Override
    public String getVolk() {
        return volk == null ? "" : volk;
    }

    @Override
    public String getBirthday() {
        return birthday == null ? "" : birthday;
    }

    @Override
    public String getBirthPlace() {
        return birthPlace == null ? "" : birthPlace;
    }

    @Override
    public String getCensus() {
        return census == null ? "" : census;
    }

    @Override
    public String getToken() {
        return token == null ? "" : token;
    }

    @Override
    public String getHasOpenFace() {
        return hasOpenface == null ? "" : hasOpenface;
    }

    @Override
    public String getNickName() {
        return nickName == null ? "" : nickName;
    }

    @Override
    public String getNickNameStatus() {
        return nickNameStatus;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    /**
     * modify by lcb：该函数废弃
     * @param idPassed
     */
    @Deprecated
    @Override
    public void setIdPassed(String idPassed) {
        this.idPassed = idPassed;
    }

    /**
     * modify by lcb：该函数废弃，使用 addCertType 代替
     * @param idPassed
     */
    @Deprecated
    @Override
    public void setCertiType(String idPassed) {
//        this.idPassed = idPassed;
        addCertType(idPassed);
    }

    @Override
    public void setMarray(String marry) {
        this.marry = marry;
    }

    @Override
    public void setVolk(String volk) {
        this.volk = volk;
    }

    @Override
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    @Override
    public void setCensus(String census) {
        this.census = census;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void setHasOpenFace(String hasOpenFace) {
        this.hasOpenface = hasOpenFace;
    }

    @Override
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public void setNickNameStatus(String nickNameStatus) {
        this.nickNameStatus = nickNameStatus;
    }

    public List<String> getBindThirds() {
        if (bindThirds == null || bindThirds.size() == 0){
            Gson gson = new Gson();
            bindThirds = gson.fromJson(bindThirdsSave,new TypeToken<List<String>>(){}.getType());
        }
        return bindThirds;
    }

    public void setBindThirds(List<String> bindThirds) {
        this.bindThirds = bindThirds;
    }

    public List<String> getCertIdList() {
        if (certIdList == null || certIdList.size() == 0){
            Gson gson = new Gson();
            certIdList = gson.fromJson(certIdListSave,new TypeToken<List<String>>(){}.getType());
        }
        return certIdList;
    }

    public void setCertIdList(List<String> certIdList) {
        this.certIdList = certIdList;
    }

    /**
     * 添加已进行的认证方式
     * @param type
     */
    public void addCertType(String type){
        if (certIdList == null){
            certIdList = new ArrayList<>();
        }
        if (certIdList.contains(type)){
            return;
        }
        certIdList.add(type);
    }

    @Override
    public boolean save() {

        try {
            thirdUser = JsonUtils.toJson(thirdUserInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        bindThirdsSave = gson.toJson(bindThirds).toString();
        certIdListSave = gson.toJson(certIdList).toString();
        return super.save();
    }

    @Override
    public Object getValue(int flag) {
        return null;
    }

    @Override
    public Object setValue(int flag, Bundle bundle) {
        return null;
    }


    @Override
    public String toString() {
        return "User{"
                + "userId='"
                + userId
                + '\''
                + ", userName='"
                + userName
                + '\''
                + ", nickName='"
                + nickName
                + ", wxName='"
                + wxName
                + ", qqName='"
                + qqName
                + ", nickNameStatus='"
                + nickNameStatus
                + '\''
                + ", sex='"
                + sex
                + '\''
                + ", mobileNo='"
                + mobileNo
                + '\''
                + ", idCard='"
                + idCard
                + '\''
                + ", address='"
                + address
                + '\''
                + ", headImg='"
                + headImg
                + '\''
                + ", idPassed='"
                + idPassed
                + '\''
                + ", email='"
                + email
                + '\''
                + ", marry='"
                + marry
                + '\''
                + ", volk='"
                + volk
                + '\''
                + ", birthday='"
                + birthday
                + '\''
                + ", birthPlace='"
                + birthPlace
                + '\''
                + ", degree='"
                + degree
                + '\''
                + ", census='"
                + census
                + '\''
                + ", token='"
                + token
                + '\''
                + ", hasOpenface='"
                + hasOpenface
                + '\''
                + ", mid='"
                + mid
                + '\''
                + ", sign='"
                + sign
                + '\''
                + '}'
                + '}';
    }


    /**
     * 获取额外的信息，以后如果特殊的城市需要特殊的信息，都通过这个来添加
     * @param key   服务器返回的字段名称
     * @return
     */
    public String getExtraInfo(String key){
        if (TextUtils.isEmpty(key)){
            return null;
        }
        if (KEY_payAccountId.equals(key)){
            return payAccountId;
        }
        return null;
    }

}
