package com.pasc.business.face.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;

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
 * 文件描述：
 * 作者：zoujianbo345
 * 创建时间：2019/10/24
 * 更改时间：2019/10/24
 */
public class  RespV2Transformer<T> implements SingleTransformer<BaseV2Resp<T>, T> {
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public SingleSource<T> apply(Single<BaseV2Resp<T>> var1) {
        return var1.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable var1) throws Exception {
                NetManager var2 = NetManager.getInstance();
                if (!isNetworkAvailable(var2.globalConfig.context)) {
                    throw new ApiV2Error("-1", "当前网络不佳，请稍后重试");
                }
            }
        }).subscribeOn(Schedulers.io()).flatMap(new Function<BaseV2Resp<T>, SingleSource<? extends T>>() {
            @Override
            public SingleSource<? extends T> apply(@NonNull BaseV2Resp<T> var1) throws Exception {
                String var2 = var1.code;
                if ("200".equals(var2)) {
                    Object var3 = var1.data;
                    return Single.just((T) var3);
                }else {
                    NetV2ObserverManager.getInstance().notifyObserver(var1);
                    throw new ApiV2Error(var2, var1.msg);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    private RespV2Transformer() {
    }

    public static <R> RespV2Transformer<R> newInstance() {
        return new RespV2Transformer();
    }

    private static boolean isNetworkAvailable(Context var0) {
        ConnectivityManager var1 = (ConnectivityManager)var0.getSystemService("connectivity");
        NetworkInfo var2 = var1.getActiveNetworkInfo();
        return var2 != null && var2.isConnected();
    }
}
