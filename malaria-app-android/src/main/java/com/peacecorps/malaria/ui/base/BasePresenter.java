package com.peacecorps.malaria.ui.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;

public abstract class BasePresenter <V extends MvpView> implements MvpPresenter<V>  {
    private Context context;
    private V mvpView;

    public BasePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void attachView(V view) {
        mvpView = view;
    }

    @Override
    public void detachView() {
        mvpView = null;
    }

    @Override
    public V getView() {
        return mvpView;
    }

    @Override
    public boolean isViewAttached() {
        return mvpView != null;
    }
}
