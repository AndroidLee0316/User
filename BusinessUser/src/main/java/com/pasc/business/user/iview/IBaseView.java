package com.pasc.business.user.iview;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/29
 * 更改时间：2019/10/29
 */
public interface IBaseView {
    void onError(String code,String error);

    void showLoadings();

    void dismissLoadings();
}
