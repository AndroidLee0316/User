package com.pasc.business.face.presenter;

import com.pasc.business.face.iview.IFaceCompareView;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.net.resp.BaseRespThrowableObserver;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.net.upload.UploadListener;
import com.pasc.lib.userbase.user.net.UserBiz;
import io.reactivex.functions.Consumer;

public class FaceComparePresenter implements IPresenter {
    private IFaceCompareView faceCompareView;


    public FaceComparePresenter(IFaceCompareView faceCompareView) {
        this.faceCompareView = faceCompareView;
    }

    public void faceAndIdComparison(byte[] bytes, String idCard, String userName, String plat,
        String version, String model, String type, String imageBase64, String imageMessageDigest) {
        disposables.add(UserBiz.faceAndIdComparison(bytes, idCard, userName, plat, version, model,
            type, imageBase64, imageMessageDigest, new UploadListener() {
            @Override
            public void progress(float v, long l, long l1, boolean b) {

            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String voidObject) {
                if (faceCompareView != null) {
                    faceCompareView.faceAndIdComparisonSucc(bytes);
                }
            }
        }, new BaseRespThrowableObserver() {
            @Override
            public void onV2Error(String code, String errorMsg) {
                if (faceCompareView != null) {
                    faceCompareView.faceAndIdComparisonFail(code, errorMsg);
                }
            }
        }));
    }

    /**
     * 是否开通或者覆盖请求
     */
    public void openFaceCertificationLogin(byte[] bytes) {
        String mobile = AppProxy.getInstance().getUserManager().getMobile();
        //0: 开通人脸, 1: 覆盖人脸, 2: 关闭人脸
        int checkType = AppProxy.getInstance().getUserManager().isOpenFaceVerify() ? 1 : 0;
        disposables.add(
                UserBiz.openFaceCertificationLogin(bytes, mobile, "jpg", checkType,
                        new UploadListener() {
                            @Override
                            public void progress(float v, long l, long l1, boolean b) {

                            }
                        })
                        .subscribe(new Consumer<VoidObject>() {
                            @Override
                            public void accept(VoidObject voidObject) {
                                if (faceCompareView != null) {
                                    faceCompareView.openFaceCertificationLoginSucc(checkType);
                                }
                            }
                        }, new BaseRespThrowableObserver() {
                            @Override
                            public void onV2Error(String code, String errorMsg) {
                                if (faceCompareView != null) {
                                    faceCompareView.openFaceCertificationLoginFail(checkType, code, errorMsg);
                                }
                            }
                        }));
    }

    @Override
    public void onDestroy() {
        if (!disposables.isDisposed()) {
            disposables.clear();
        }
        faceCompareView = null;
    }
}
