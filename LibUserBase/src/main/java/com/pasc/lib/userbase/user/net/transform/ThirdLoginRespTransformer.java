package com.pasc.lib.userbase.user.net.transform;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.pasc.lib.net.ApiError;
import com.pasc.lib.net.ApiV2Error;
import com.pasc.lib.net.NetManager;
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
 * Created by kuangxiangkui192 on 2018/12/25.
 * 三方登录transformer
 */
public class ThirdLoginRespTransformer<T> implements SingleTransformer<BaseV2Resp<T>, T> {

    public SingleSource<T> apply(Single<BaseV2Resp<T>> var1) {
        return var1.doOnSubscribe(new Consumer<Disposable>() {
            public void accept(@NonNull Disposable var1) throws Exception {
                NetManager manager = NetManager.getInstance();
                if (!isNetworkAvailable(manager.globalConfig.context)) {
                    throw new ApiError(-1, "当前网络不佳，请稍后重试");
                }
            }
        }).subscribeOn(Schedulers.io()).flatMap(new Function<BaseV2Resp<T>, SingleSource<? extends T>>() {
            public SingleSource<? extends T> apply(@NonNull BaseV2Resp<T> resp) throws Exception {
                String code = resp.code;
                if ("200".equals(code) || "201".equals(code)) {
                    T data = resp.data;
                    return Single.just(data);
                } else {
                    NetV2ObserverManager.getInstance().notifyObserver(resp);
                    throw new ApiV2Error(resp.code, resp.msg);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    private ThirdLoginRespTransformer() {
    }

    public static <R> ThirdLoginRespTransformer<R> newInstance() {
        return new ThirdLoginRespTransformer();
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}
