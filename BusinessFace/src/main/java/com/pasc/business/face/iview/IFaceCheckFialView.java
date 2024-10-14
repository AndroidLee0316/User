package com.pasc.business.face.iview;

import com.pasc.business.face.net.resp.FaceCheckResp;
import com.pasc.business.face.net.resp.FaceInitResp;

public interface IFaceCheckFialView {
    void faceDelect(Object o);
    void faceResult(FaceCheckResp resp);

    void faceInitview(FaceInitResp resp);

    void onError(String code, String error);

    void showLoadings();

    void dismissLoadings();

}
