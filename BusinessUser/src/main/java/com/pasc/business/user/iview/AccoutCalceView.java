package com.pasc.business.user.iview;

import com.pasc.business.user.net.resp.AccoutCalceResp;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/31
 * 更改时间：2019/10/31
 */
public interface AccoutCalceView extends IBaseView{
    void isFinishPay(AccoutCalceResp resp);
    void commit(AccoutCalceResp resp);
}
