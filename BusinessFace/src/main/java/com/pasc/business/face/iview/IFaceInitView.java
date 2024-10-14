package com.pasc.business.face.iview;

import com.pasc.business.face.net.resp.FaceInitResp;

public interface IFaceInitView {
    void faceInitview(FaceInitResp resp);

    void onError(String code,String error);

    void showLoadings();

    void dismissLoadings();

}
