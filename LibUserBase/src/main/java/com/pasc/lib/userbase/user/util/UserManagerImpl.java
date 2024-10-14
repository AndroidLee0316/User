package com.pasc.lib.userbase.user.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.pasc.lib.base.user.IUserInfo;
import com.pasc.lib.base.user.IUserManager;
import com.pasc.lib.base.util.CollectionUtils;
import com.pasc.lib.base.util.SPUtils;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.base.UserConstant;
import com.pasc.lib.userbase.base.data.DataBaseContext;
import com.pasc.lib.userbase.base.data.user.User;
import com.pasc.lib.userbase.base.data.user.User_Table;
import com.pasc.lib.userbase.base.utils.CommonUtils;
import com.pasc.lib.userbase.user.LoginConstant;
import com.pasc.lib.userbase.user.UserProxy;
import com.pasc.lib.userbase.user.net.UserBiz;
import com.pasc.lib.userbase.user.net.resp.BaseRespV2Observer;
import com.pasc.lib.userbase.user.urlconfig.UrlDispatcher;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * 用户信息管理类
 * Created by duyuan797 on 17/10/24.
 */
public class UserManagerImpl implements IUserManager {
    private Context context;
    private IUserInfo mUser;

    private boolean isLogin = false;
    private static volatile UserManagerImpl instance;

    private UserManagerImpl() {

    }

    public static UserManagerImpl getInstance() {
        if (instance == null) {
            synchronized (UserManagerImpl.class) {
                if (instance == null) {
                    instance = new UserManagerImpl();
                }
            }
        }
        return instance;
    }

    /**
     * 更新用户信息
     * 1：删除用户信息
     * 2：重新设置用户信息
     * 3：保存新的用户信息
     * 4：使用sp保存用户手机号
     * @param user  用户信息
     */
    @Override
    public void updateUser(IUserInfo user) {
        if (user != null) {
            Delete.table(User.class);
            mUser = user;
            mUser.save();
            SPUtils.getInstance().setParam(UserConstant.USER_ACCOUNT, mUser.getMobileNo());
        }
    }

    /**
     * 登陆成功刷新数据
     */
    public void updateLoginSuccess(){
        isLogin = true;
        SPUtils.getInstance().setParam(UserConstant.HAS_LOGIN, true);
    }

    public User getInnerUser() {
        return (User) getUserInfo();
    }

    @Override
    public void setUserInfo(IUserInfo iUserInfo) {
        updateUser(iUserInfo);
    }

    @Override
    public boolean isLogin() {
        //这里加一个if判断，是为了优化isLogin，如果不加这个，每次调用isLogin函数都会调用SP函数，减少耗时
        if (isLogin){
            return isLogin;
        }
        isLogin = (boolean) SPUtils.getInstance().getParam(UserConstant.HAS_LOGIN, false);
        return isLogin;
    }

    @Override
    public String getUserId() {
        return getUserInfo().getUserId();
    }

    @Override
    public String getUserName() {
        return getUserInfo().getUserName();
    }

    public String getNickName(){
        if (mUser == null){
            return null;
        }
        return mUser.getNickName();
    }

    @Override
    public String getToken() {
        return getUserInfo().getToken();
    }

    @Override
    public String getMobile() {
        return getUserInfo().getMobileNo();
    }

    /**
     * 注销登录
     */
    @Override
    public boolean exitUser(Context context) {

        Log.i(UserManagerImpl.class.getName(), "exit user");
        //更新用户信息，调用
        UserBiz.logOut().subscribe(new BaseRespV2Observer<VoidObject>() {
                    @Override
                    public void onSuccess(VoidObject voidObject) {
                        Log.i(UserManagerImpl.class.getName(), "logout Success from service");
                    }

                    @Override
                    public void onError(String code, String msg) {
                        super.onError(msg);
                        Log.i(UserManagerImpl.class.getName(), "logout Error from service : " + msg);
                    }
                });
        if (ActivityManager.isUserAMonkey()) {
            CommonUtils.toastMsg("Monkey testing, cannot log out!");
            return false;
        } else {
            SPUtils.getInstance().setParam(UserConstant.HAS_LOGIN, false);
            mUser = null;
            isLogin = false;
            return true;
        }
    }

    /**
     * 根据手机号判断用户以前是否登录过
     */
    @Override
    public boolean isLoginAccount(String mobileNo) {
        User user = SQLite.select()
                .from(User.class)
                .where(User_Table.mobileNo.eq(mobileNo))
                .querySingle();
        if (user != null) {
            return mobileNo.equals(user.mobileNo);
        }
        return false;
    }

    /**
     * 判断用户是否开通人脸
     * @return
     */
    @Override
    public boolean isOpenFaceVerify() {
        return isOpenFaceVerify(getMobile());
    }

    /**
     * 传入手机号，判断该手机号是否开通人脸
     * @param mobileNo 手机号
     * @return
     */
    @Override
    public boolean isOpenFaceVerify(String mobileNo) {
        User user = SQLite.select()
                .from(User.class)
                .where(User_Table.mobileNo.eq(mobileNo))
                .querySingle();
        if (user != null) {
            return User.HAS_OPEN_FACE.equals(user.hasOpenface);
        }
        return false;
    }

    /**
     * modify by lcb : 认证方式改为列表形式，认证方式列表字符串，中间以 | 分割(PS：这里会产生一个问题，如果认证方式超过 9 种，判断认证方式的地方需要修改，现在直接是判断是否contains(type))
     * 判断用户是否已经认证（包括银行卡认证、人脸认证、都认证三种）
     * @return
     */
    @Override
    public boolean isCertified() {
        //如果认证方式列表字符串为空，表示没有一个认证方式
        if (TextUtils.isEmpty(getUserInfo().getCertiType())){
            return false;
        }
        return true;
//        return User.CERTIFY_BANK.equals(getUserInfo().getCertiType())
//                || User.CERTIFY_FACE.equals(getUserInfo().getCertiType())
//                || User.CERTIFY_ALIPAY.equals(getUserInfo().getCertiType());
    }

    /**
     * modify by lcb：注意：这里返回的是认证方式列表字符串，中间以 | 分割（libbase已经写死了 IUserInfo，为了适配只能这样）
     * 获取认证方式
     * @return
     */
    @Override
    public String getCertifyType() {
        return getUserInfo().getCertiType();
    }

    public String getBirthday(){
        return getUserInfo().getBirthday();
    }

    public String getAddress(){
        return getUserInfo().getAddress();
    }

    public String getHeadImg(){
        return getUserInfo().getHeadImg();
    }

    /**
     * 初始化用户相关参数（包括认证、人脸、登陆等相关配置项）
     *
     * @param context
     * @param urlAssetsPath 用户相关配置参数json地址
     * @return
     */
    @Override
    public IUserManager init(Context context, String urlAssetsPath) {
        this.context = context;
        UserProxy.getInstance().initDataBaseContext(context);
        UrlDispatcher.dispatchFromAsserts(context, urlAssetsPath);
        return this;
    }

    /**
     * 用户系统相关参数配置，同上面的init函数，因为对外输出aar，所以需要把配置文件放到raw中
     * @param rawName
     * @return
     */
    public IUserManager initUserConfig(int rawName){
        UrlDispatcher.dispatchFromRaw(context, rawName);
        return this;
    }

    /**
     * 获取用户信息
     * 1：通过SP保存的信息判断用户是否已登陆
     * 2：如果用户已经登陆，获取用户信息（如果用户信息再缓存中没拿到，则从数据库里面获取）
     * 3：如果用户未登陆，则拿到SP中保存的用户手机号，生成包含一个手机号的用户信息
     * @return
     */
    @Override
    public IUserInfo getUserInfo() {
        if (isLogin()) {
            if (mUser == null) {
                List<User> userList = SQLite.select().from(User.class).queryList();
                return mUser = CollectionUtils.isEmpty(userList) ? new User() : userList.get(0);
            } else {
                return mUser;
            }
        } else {
            String phoneNumber = (String) SPUtils.getInstance().getParam(UserConstant.USER_ACCOUNT, "");
            if (mUser == null) {
                mUser = new User();
                mUser.setMobileNo(phoneNumber);
                List<User> userList = SQLite.select().from(User.class).queryList();
                if (!CollectionUtils.isEmpty(userList)){
                    User tmpUser = userList.get(0);
                    if (mUser != null && mUser.getMobileNo().equals(tmpUser.getMobileNo())){
                        mUser.setHasOpenFace(tmpUser.getHasOpenFace());
                        mUser.setSex(tmpUser.getSex());
                        mUser.setHeadImg(tmpUser.getHeadImg());
                    }
                }
            }else {
                mUser.setMobileNo(phoneNumber);
            }
            return mUser;
        }
    }

    /**
     * 用户被踢了
     * 1：注销用户（设置SP中的IS_LOGIN为false，缓存User设置为null）
     * 2：跳转到登陆页面
     * @param context
     */
    @Override
    public void userKicked(Context context) {
        if (exitUser(context)) {
            Bundle bundle = new Bundle();
            bundle.putString(LoginConstant.LOGIN_TYPE, LoginConstant.LOGIN_TYPE_KICKED);
            BaseJumper.jumpBundleARouter(RouterTable.Login.PATH_LOGIN_ACTIVITY, bundle);
        }
    }

    @Override
    public Object getUserInfo(int flag, Bundle bundle) {
        return null;
    }

    @Override
    public Object setUserInfo(int flag, Bundle bundle) {
        return null;
    }

    @Override
    public Object updataUserInfo(int flag, Bundle bundle) {
        return null;
    }

    /**
     * 获取额外的用户字段信息
     * @param key
     * @return
     */
    public String getExtraInfo(String key){
        if (mUser == null){
            return null;
        }
        //没办法必须强制转换，不可能每次用户表添加一个字段就得更新libbase,所以用户接口下沉到libbase是不合理的
        return ((User)mUser).getExtraInfo(key);
    }
}