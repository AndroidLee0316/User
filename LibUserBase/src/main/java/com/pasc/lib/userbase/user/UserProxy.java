package com.pasc.lib.userbase.user;

import android.content.Context;

import com.pasc.lib.userbase.base.data.DataBaseContext;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by yintangwen952 on 18/11/14.
 * 初始化用户功能组件
 */

public class UserProxy {

    //数据库Context
    private static DataBaseContext dataBaseContext;
    private static volatile UserProxy sInstance;

    public static UserProxy getInstance() {
        if (sInstance == null) {
            synchronized (UserProxy.class) {
                if (sInstance == null) {
                    sInstance = new UserProxy();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取数据库操作Context
     * @return
     */
    public DataBaseContext getDataBaseContext() {
        return dataBaseContext;
    }

    /**
     * 初始化数据库，用户系统使用的数据库为dbFlow
     * @param context
     */
    public void initDataBaseContext(Context context) {
        dataBaseContext = new DataBaseContext(context);
        FlowConfig flowConfig = new FlowConfig.Builder(dataBaseContext).build();
        FlowManager.init(flowConfig);
    }
}
