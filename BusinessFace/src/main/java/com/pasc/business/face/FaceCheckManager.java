package com.pasc.business.face;

import android.os.Bundle;
import android.util.Log;

import com.pasc.business.face.data.FaceConstant;
import com.pasc.business.face.net.FaceBiz;
import com.pasc.business.face.net.resp.FaceInitResp;
import com.pasc.lib.log.PascLog;
import com.pasc.lib.net.resp.BaseRespThrowableObserver;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.userbase.base.RouterTable;
import com.pasc.lib.userbase.user.util.EventBusOutUtils;

import io.reactivex.functions.Consumer;

/**
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/11/6
 * 更改时间：2019/11/6
 */
public class FaceCheckManager {
    private FaceInitLinster linster;
    private static FaceCheckManager mInstance;

    public static FaceCheckManager getInstance() {
        if (mInstance == null) {
            mInstance = new FaceCheckManager();
        }
        return mInstance;
    }

    private FaceCheckManager() {

    }

    public void faceCheck(String appid, FaceInitLinster linster) {

        FaceBiz.faceInit(appid, 1, FaceConstant.URL_APLI).subscribe(new Consumer<FaceInitResp>() {
            @Override
            public void accept(FaceInitResp resp) {
                linster.isValidity(resp);
            }
        }, new BaseRespThrowableObserver() {
            @Override
            public void onV2Error(String code, String errorMsg) {

                linster.error(code, errorMsg);
            }
        });
    }

    public interface FaceInitLinster {

        void error(String code, String errorMsg);

        void isValidity(FaceInitResp resp);
    }
}
