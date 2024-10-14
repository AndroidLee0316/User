package com.pasc.business.face.presenter;

import android.util.Log;
import com.pasc.business.face.iview.IFaceCheckView;
import com.pasc.business.face.net.FaceBiz;
import com.pasc.business.face.net.resp.FaceCheckResp;
import com.pasc.lib.net.resp.BaseRespThrowableObserver;
import com.pasc.lib.net.upload.UploadListener;
import io.reactivex.functions.Consumer;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/22
 * 更改时间：2019/10/22
 */
public class FaceCheckPresenter implements IPresenter {
    private IFaceCheckView iFaceCheckView;

    public FaceCheckPresenter(IFaceCheckView iFaceCheckView) {
        this.iFaceCheckView = iFaceCheckView;
    }

    @Override
    public void onDestroy() {
        if (!disposables.isDisposed()) {
            disposables.clear();
        }
        iFaceCheckView = null;
    }

    public void faceCompare(String compare, String initCode, String appid, String userId, String plat,
        String version, String model, String type, byte[] bytes, String imageBase64, String imageMessageDigest) {
        iFaceCheckView.showLoadings();
        if ("2".equals(compare)) {
            disposables.add(FaceBiz.faceCompace(initCode, appid, userId, plat, version, model, type, bytes, imageBase64, imageMessageDigest, new UploadListener() {
                @Override
                public void progress(float v, long l, long l1, boolean b) {

                }
            }).subscribe(new Consumer<FaceCheckResp>() {
                @Override
                public void accept(FaceCheckResp resp) {
                    Log.e("test==",resp.passed+"==");
                    iFaceCheckView.dismissLoadings();
                    if (iFaceCheckView != null) {
                        iFaceCheckView.faceCompare(resp);
                    } else {
                        iFaceCheckView.onError("201", "人脸核验失败");
                    }
                }
            }, new BaseRespThrowableObserver() {
                @Override
                public void onV2Error(String code, String errorMsg) {
                    Log.e("test==",errorMsg +"=errorMsg=");
                    iFaceCheckView.dismissLoadings();
                    if (iFaceCheckView != null) {
                        if ("404".equals(code)) {
                            iFaceCheckView.onError(code, "网络错误请重试");
                        } else {
                            iFaceCheckView.onError(code, errorMsg);
                        }
                    }
                }
            }));
        } else {
            disposables.add(FaceBiz.faceIdCompace(initCode, appid, userId, plat, version, model,
                type, bytes, imageBase64, imageMessageDigest, new UploadListener() {
                @Override
                public void progress(float v, long l, long l1, boolean b) {

                }
            }).subscribe(new Consumer<FaceCheckResp>() {
                @Override
                public void accept(FaceCheckResp resp) {
                    iFaceCheckView.dismissLoadings();
                    if (iFaceCheckView != null) {
                        iFaceCheckView.faceCompare(resp);
                    } else {
                        iFaceCheckView.onError("201", "人脸核验失败");
                    }
                }
            }, new BaseRespThrowableObserver() {
                @Override
                public void onV2Error(String code, String errorMsg) {
                    iFaceCheckView.dismissLoadings();
                    if (iFaceCheckView != null) {
                        if ("404".equals(code)) {
                            iFaceCheckView.onError(code, "网络错误请重试");
                        } else {
                            iFaceCheckView.onError(code, errorMsg);
                        }
                    }
                }
            }));
        }
    }
    public void faceInfoCompare(String userName, String idCard, String plat, String version,
        String model, String type, byte[] bytes, String imageBase64, String imageMessageDigest) {
        iFaceCheckView.showLoadings();
        disposables.add(FaceBiz.faceInfoCompare(userName, idCard, plat, version, model, type, bytes, imageBase64, imageMessageDigest, new UploadListener() {
            @Override
                public void progress(float v, long l, long l1, boolean b) {

                }
            }).subscribe(new Consumer<FaceCheckResp>() {
                @Override
                public void accept(FaceCheckResp resp) {
                    Log.e("test==",resp.passed+"==");
                    iFaceCheckView.dismissLoadings();
                    if (iFaceCheckView != null) {
                        iFaceCheckView.faceCompare(resp);
                    } else {
                        iFaceCheckView.onError("201", "人脸核验失败");
                    }
                }
            }, new BaseRespThrowableObserver() {
                @Override
                public void onV2Error(String code, String errorMsg) {
                    Log.e("test==",errorMsg +"=errorMsg=");
                    iFaceCheckView.dismissLoadings();
                    if (iFaceCheckView != null) {
                        if ("404".equals(code)) {
                            iFaceCheckView.onError(code, "网络错误请重试");
                        } else {
                            iFaceCheckView.onError(code, errorMsg);
                        }
                    }
                }
            }));

    }
}
