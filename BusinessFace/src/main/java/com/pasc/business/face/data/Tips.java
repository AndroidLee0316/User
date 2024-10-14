package com.pasc.business.face.data;

import com.pingan.reai.face.common.RePaFaceConstants;

/**
 * Created by zhanqiang545 on 17/6/30.
 */

public class Tips {

    public static String getDescriptionV2(int id) {
        switch (id) {
            case RePaFaceConstants.EnvironmentalTips.NORMAL:
                return "人脸检测中";
            case RePaFaceConstants.EnvironmentalTips.MULTI_FACE:
                return "请保持单人脸";
            case RePaFaceConstants.EnvironmentalTips.NO_FACE:
                return "未检测到人脸";
            case RePaFaceConstants.EnvironmentalTips.FACE_YAW_LEFT:
                return "请正对摄像头";
            case RePaFaceConstants.EnvironmentalTips.FACE_YAW_RIGHT:
                return "请正对摄像头";
            case RePaFaceConstants.EnvironmentalTips.FACE_PITCH_UP:
                return "请稍微低头";
            case RePaFaceConstants.EnvironmentalTips.FACE_PITCH_DOWN:
                return "请稍微抬头";
            case RePaFaceConstants.EnvironmentalTips.FACE_ROLL_LEFT:
                return "请正对摄像头";
            case RePaFaceConstants.EnvironmentalTips.FACE_ROLL_RIGHT:
                return "请正对摄像头";
            case RePaFaceConstants.EnvironmentalTips.TOO_BRIGHT:
                return "光线太亮,请调整";
            case RePaFaceConstants.EnvironmentalTips.TOO_DARK:
                return "光线太暗,请调整";
            case RePaFaceConstants.EnvironmentalTips.TOO_FUZZY:
                return "图像模糊,请调整";
            case RePaFaceConstants.EnvironmentalTips.TOO_CLOSE:
                return "手机拿远一点";
            case RePaFaceConstants.EnvironmentalTips.TOO_FAR:
                return "手机拿近一点";
            //case RePaFaceConstants.EnvironmentalTips.FACE_ILLEGAL:
            //    return "请进行真人采集";
            //case RePaFaceConstants.EnvironmentalTips.NORMAL_LIVE_DONE:
            //    return "人脸检测中...";
            case RePaFaceConstants.MotionType.BLINK_EYE:
                return "请缓慢眨眼";
            //case RePaFaceConstants.MotionType.SHAKE_LEFT_HEAD:
            //    return "向左缓慢转头";
            //case RePaFaceConstants.MotionType.SHAKE_RIGHT_HEAD:
            //    return "向右缓慢转头";
            case RePaFaceConstants.MotionType.NOD_HEAD:
                return "请缓慢点头";
            case RePaFaceConstants.MotionType.SHAKE_HEAD:
                return "请缓慢摇头";
            case RePaFaceConstants.MotionType.OPEN_MOUTH:
                return "请缓慢张嘴";
            //            case ReRePaFaceConstants.AliveType.ALIVE:
            //                return "活体";
            //            case ReRePaFaceConstants.AliveType.UNALIVE:
            //                return "非活体";
        }
        return "";
    }

    /**
     * 是否是正常提示语
     *
     * @param id 提示语id
     * @return
     */
    public static boolean isNormalTip(int id) {
        return RePaFaceConstants.EnvironmentalTips.NORMAL == id
                //|| RePaFaceConstants.EnvironmentalTips.NORMAL_LIVE_DONE == id
                || RePaFaceConstants.MotionType.BLINK_EYE == id
                //|| RePaFaceConstants.MotionType.SHAKE_LEFT_HEAD == id
                //|| RePaFaceConstants.MotionType.SHAKE_RIGHT_HEAD == id
                || RePaFaceConstants.MotionType.NOD_HEAD == id
                || RePaFaceConstants.MotionType.SHAKE_HEAD == id
                || RePaFaceConstants.MotionType.OPEN_MOUTH == id;
    }
}
