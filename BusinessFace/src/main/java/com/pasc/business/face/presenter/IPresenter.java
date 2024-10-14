package com.pasc.business.face.presenter;

import io.reactivex.disposables.CompositeDisposable;

public interface IPresenter {
    public CompositeDisposable disposables = new CompositeDisposable();
    void onDestroy();
}
