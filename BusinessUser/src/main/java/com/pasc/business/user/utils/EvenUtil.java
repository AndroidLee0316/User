package com.pasc.business.user.utils;

import com.pasc.business.user.even.ChangePhoneFinish;

import org.greenrobot.eventbus.EventBus;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/30
 * 更改时间：2019/10/30
 */
public class EvenUtil {
public static void sendChangePhoneFinish(){
    EventBus.getDefault().post(new ChangePhoneFinish());
}
}
