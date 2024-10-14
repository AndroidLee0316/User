package com.pasc.business.user.iview;

import com.pasc.business.user.net.resp.ChangePhoneResp;
import com.pasc.business.user.net.resp.SMSNewResp;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public  interface InputPhoneView extends IBaseView{
    void isLegatily(ChangePhoneResp resp);
    void mobileVerly(ChangePhoneResp resp);
    void onPhoneError(String erroe);
    void sendSms(SMSNewResp resp);
}
