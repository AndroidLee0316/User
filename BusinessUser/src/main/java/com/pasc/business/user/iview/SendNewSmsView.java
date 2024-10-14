package com.pasc.business.user.iview;

import com.pasc.business.user.net.resp.AccoutCalceResp;
import com.pasc.business.user.net.resp.ChangePhoneResp;
import com.pasc.business.user.net.resp.SMSNewResp;
import com.pasc.business.user.net.resp.SMSResp;
import com.pasc.business.user.net.resp.SetPhoneResp;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public interface SendNewSmsView extends IBaseView {
  void sendSms(SMSNewResp resp);

  void commit(SetPhoneResp resp);

  void onTick(long time);

  void onTickFinish();

  void onPhoneError(String msg);
}
