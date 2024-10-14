package com.pasc.business.face.presenter;

import android.util.Log;

import com.pasc.business.face.iview.IFaceCheckFialView;
import com.pasc.business.face.iview.IFaceCheckView;
import com.pasc.business.face.net.FaceBiz;
import com.pasc.business.face.net.resp.FaceCheckResp;
import com.pasc.business.face.net.resp.FaceInitResp;
import com.pasc.lib.log.PascLog;
import com.pasc.lib.net.resp.BaseRespThrowableObserver;

import io.reactivex.functions.Consumer;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/24
 * 更改时间：2019/10/24
 */
public class FaceCheckFailPresenter implements IPresenter{
    private IFaceCheckFialView iFaceCheckFialView;

    public FaceCheckFailPresenter(IFaceCheckFialView iFaceCheckFialView) {
        this.iFaceCheckFialView = iFaceCheckFialView;
    }

    @Override
    public void onDestroy() {
        if (!disposables.isDisposed()) {
            disposables.clear();
        }
        iFaceCheckFialView = null;
    }
    public void delectFace(){
        iFaceCheckFialView.showLoadings();
        disposables.add(FaceBiz.faceDelect().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                iFaceCheckFialView.dismissLoadings();
                if (iFaceCheckFialView != null){
                    iFaceCheckFialView.faceDelect(o);
                }

            }
        },new BaseRespThrowableObserver(){
            @Override
            public void onV2Error(String code, String error) {
                super.onV2Error(code, error);
                iFaceCheckFialView.dismissLoadings();
                iFaceCheckFialView.onError(code,error);
            }
        }));
    }
    public void faceInit(String appid, int type, String aliUrl) {
        iFaceCheckFialView.showLoadings();
        disposables.add(FaceBiz.faceInit(appid, type, aliUrl).subscribe(new Consumer<FaceInitResp>() {
            @Override
            public void accept(FaceInitResp resp) {
                iFaceCheckFialView.dismissLoadings();
                if (iFaceCheckFialView != null) {
                    iFaceCheckFialView.faceInitview(resp);
                } else {
                    iFaceCheckFialView.onError("201", "人脸初始化失败");
                }
            }
        }, new BaseRespThrowableObserver() {
            @Override
            public void onV2Error(String code, String errorMsg) {
                iFaceCheckFialView.dismissLoadings();
                if (iFaceCheckFialView != null) {
                    if ("404".equals(code)) {
                        iFaceCheckFialView.onError(code, "网络错误请重试");
                    } else {
                        iFaceCheckFialView.onError(code, errorMsg);
                    }
                }
            }
        }));}
        public void queryResult(String appId,String aliId){
            iFaceCheckFialView.showLoadings();
            disposables.add(FaceBiz.faceQueryAli(appId, aliId).subscribe(new Consumer<FaceCheckResp>() {
                @Override
                public void accept(FaceCheckResp resp) {
                    iFaceCheckFialView.dismissLoadings();
                    if (iFaceCheckFialView != null) {
                        iFaceCheckFialView.faceResult(resp);
                    } else {
                        iFaceCheckFialView.onError("201", "支付宝认证失败，请重试");
                    }
                }
            }, new BaseRespThrowableObserver() {
                @Override
                public void onV2Error(String code, String errorMsg) {
                    iFaceCheckFialView.dismissLoadings();
                    if (iFaceCheckFialView != null) {
                        if ("404".equals(code)) {
                            iFaceCheckFialView.onError(code, "网络错误请重试");
                        } else {
                            iFaceCheckFialView.onError(code, errorMsg);
                        }
                    }
                }
            }));}
}
