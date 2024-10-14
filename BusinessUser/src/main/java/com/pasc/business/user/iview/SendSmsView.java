package com.pasc.business.user.iview;

import com.pasc.business.user.net.resp.AccoutCalceResp;
import com.pasc.business.user.net.resp.ChangePhoneResp;
import com.pasc.business.user.net.resp.SMSResp;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public  interface SendSmsView extends IBaseView{
    void sendSms(SMSResp resp);
    void commit(ChangePhoneResp resp);
    void accoutCalce(AccoutCalceResp resp);
   void onTick(long time);
    void onTickFinish();
    void onPhoneError(String msg);
}
