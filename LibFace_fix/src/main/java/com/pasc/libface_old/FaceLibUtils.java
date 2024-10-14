package com.pasc.libface_old;

import com.pingan.ai.face.manager.PaFaceDetectorManager;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/8/13
 */
public class FaceLibUtils {

    public static void release(PaFaceDetectorManager detector){
        detector.release();
    }


    //public static void setLiveModel(PaFaceDetectorManager detector){
    //    //该版本已经没有这个函数了
    //    detector.setLiveModel(LiveFaceConfig.LIVE_MODEL_900K);
    //}
}
