package com.pasc.business.cert.iview;

public interface IFaceCompareView {
    void faceAndIdComparisonSucc(byte[] bytes);

    void faceAndIdComparisonFail(String code, String errorMsg);

    void openFaceCertificationLoginSucc(int type);

    void openFaceCertificationLoginFail(int type, String code, String errorMsg);
}
