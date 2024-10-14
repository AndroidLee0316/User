package com.pasc.lib.userbase.user.urlconfig;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/9/13
 */
public class OtherConfigManager {
    private static volatile OtherConfigManager instance;

    private UserUrlConfig.OtherConfigBean otherConfigBean;

    private OtherConfigManager() {
        //设置一个默认的对象，不然可能空指针
        otherConfigBean = new UserUrlConfig.OtherConfigBean();
    }

    public static OtherConfigManager getInstance() {
        if (instance == null) {
            synchronized (OtherConfigManager.class) {
                if (instance == null) {
                    instance = new OtherConfigManager();
                }
            }
        }
        return instance;
    }

    public void setOtherConfigBean(UserUrlConfig.OtherConfigBean otherConfigBean) {
        if (otherConfigBean != null){
            this.otherConfigBean = otherConfigBean;
        }

    }

    public UserUrlConfig.OtherConfigBean getOtherConfigBean() {
        return otherConfigBean;
    }
}
