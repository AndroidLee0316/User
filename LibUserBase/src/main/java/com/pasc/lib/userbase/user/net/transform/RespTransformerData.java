package com.pasc.lib.userbase.user.net.transform;


import com.google.gson.Gson;
import com.pasc.lib.base.util.NetworkUtils;
import com.pasc.lib.net.ApiV2Error;
import com.pasc.lib.net.resp.BaseV2Resp;
import com.pasc.lib.net.transform.NetV2ObserverManager;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 用于错误情况包含code, errorMsg, data的情况
 */
public class RespTransformerData<SUCC_DATA> implements SingleTransformer<BaseV2Resp<SUCC_DATA>, SUCC_DATA> {

    public SingleSource<SUCC_DATA> apply(Single<BaseV2Resp<SUCC_DATA>> var1) {
        return var1.doOnSubscribe(new Consumer<Disposable>() {
            public void accept(@NonNull Disposable var1) {
                if (!NetworkUtils.isNetworkAvailable()) {
                    throw new ApiV2Error("-1", "当前网络不佳，请稍后重试");
                }
            }
        }).subscribeOn(Schedulers.io()).flatMap(new Function<BaseV2Resp<SUCC_DATA>, SingleSource<? extends SUCC_DATA>>() {
            public SingleSource<? extends SUCC_DATA> apply(@NonNull BaseV2Resp<SUCC_DATA> var1) {
                String var2 = var1.code;
                if ("200".equals(var2)) {
                    SUCC_DATA var3 = var1.data;
                    return Single.just(var3);
                } else {
                    NetV2ObserverManager.getInstance().notifyObserver(var1);
                    if (var1.data != null) {
                        throw new ApiV2Error(var2, new Gson().toJson(var1));
                    } else {
                        throw new ApiV2Error(var2, var1.msg);
                    }
                }
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    private RespTransformerData() {

    }

    public static <R> RespTransformerData<R> newInstance() {
        return new RespTransformerData<>();
    }
}
