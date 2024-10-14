package com.pasc.business.face.presenter;

import android.util.Log;

import com.pasc.business.face.iview.IFaceInitView;
import com.pasc.business.face.net.FaceBiz;
import com.pasc.business.face.net.resp.FaceInitResp;
import com.pasc.lib.log.PascLog;
import com.pasc.lib.net.resp.BaseRespThrowableObserver;
import com.pasc.lib.net.upload.UploadListener;
import com.pasc.lib.userbase.user.net.UserBiz;

import io.reactivex.functions.Consumer;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/22
 * 更改时间：2019/10/22
 */
public class FaceInitPresenter implements IPresenter {
    private IFaceInitView iFaceInitView;

    public FaceInitPresenter(IFaceInitView view) {
        this.iFaceInitView = view;
    }

    @Override
    public void onDestroy() {
        if (!disposables.isDisposed()) {
            disposables.clear();
        }
        iFaceInitView = null;
    }

    public void faceInit(String appid, int type, String aliUrl) {
        iFaceInitView.showLoadings();
        disposables.add(FaceBiz.faceInit(appid, type, aliUrl).subscribe(new Consumer<FaceInitResp>() {
            @Override
            public void accept(FaceInitResp resp) {
                iFaceInitView.dismissLoadings();
                if (iFaceInitView != null) {
                    iFaceInitView.faceInitview(resp);
                } else {
                    iFaceInitView.onError("201", "人脸初始化失败");
                }
            }
        }, new BaseRespThrowableObserver() {
            @Override
            public void onV2Error(String code, String errorMsg) {
                iFaceInitView.dismissLoadings();
                if (iFaceInitView != null) {
                    if ("404".equals(code)) {
                        iFaceInitView.onError(code, "网络错误请重试");
                    } else {
                        iFaceInitView.onError(code, errorMsg);
                    }
                }
            }
        }));
    }
}
