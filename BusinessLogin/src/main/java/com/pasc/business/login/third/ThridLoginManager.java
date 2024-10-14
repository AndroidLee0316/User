package com.pasc.business.login.third;

import java.util.HashMap;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/7/9
 */
public class ThridLoginManager {

    private HashMap<String, ThridLogin> thirdLoginMap;


    public void registerLogin(String type, ThridLogin thirdLogin){
        if (thirdLoginMap == null){
            thirdLoginMap = new HashMap<>();
        }
        thirdLoginMap.put(type,thirdLogin);
    }

    public void Login(String type, HashMap<String, String> params){
        if (thirdLoginMap.containsKey(type)){
            thirdLoginMap.get(type).login(params);
        }
    }

    public static interface ThridLogin{
        void login(HashMap<String, String> params);
    }

}
