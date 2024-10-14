package com.pasc.business.face.iview;

import com.pasc.business.face.net.resp.FaceCheckResp;
import com.pasc.business.face.net.resp.FaceInitResp;

public interface IFaceCheckView {
    void faceCompare(FaceCheckResp resp);

    void onError(String code,String error);

    void showLoadings();

    void dismissLoadings();

}
