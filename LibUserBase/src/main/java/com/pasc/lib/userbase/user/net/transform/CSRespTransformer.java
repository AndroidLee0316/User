package com.pasc.lib.userbase.user.net.transform;

import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.util.NetworkUtils;
import com.pasc.lib.net.ApiError;
import com.pasc.lib.net.ApiV2Error;
import com.pasc.lib.net.ErrorCode;
import com.pasc.lib.net.resp.BaseV2Resp;
import com.pasc.lib.net.resp.VoidObject;
import com.pasc.lib.net.transform.NetV2ObserverManager;
import com.pasc.lib.userbase.R;

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
 * Resp数据转换器
 *
 * Created by duyuan797 on 16/11/22.
 */
public class CSRespTransformer<T> implements SingleTransformer<BaseV2Resp<T>, T> {

    @Override public SingleSource<T> apply(Single<BaseV2Resp<T>> upstream) {
        return upstream.doOnSubscribe(new Consumer<Disposable>() {
            @Override public void accept(@NonNull Disposable disposable) throws Exception {
                if (!NetworkUtils.isNetworkAvailable()) {
                    throw new ApiError(ErrorCode.ERROR, AppProxy.getInstance().getContext()
                            .getString(R.string.user_temp_network_unavailable));
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<BaseV2Resp<T>, SingleSource<? extends T>>() {
                    @Override public SingleSource<? extends T> apply(@NonNull BaseV2Resp<T> baseResp)
                            throws Exception {
                        String code = baseResp.code;
                        if ( (ErrorCode.SUCCESS+"").equals(code) ) {
                            if (baseResp.data == null) {
                                return Single.just((T) VoidObject.getInstance());
                            }
                            return Single.just(baseResp.data);
                        } else {
                            NetV2ObserverManager.getInstance().notifyObserver(baseResp);
                            throw new ApiV2Error(baseResp.code+"", baseResp.msg);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private CSRespTransformer() {
    }

    public static <R> CSRespTransformer<R> newInstance() {
        return new CSRespTransformer<>();
    }
}
