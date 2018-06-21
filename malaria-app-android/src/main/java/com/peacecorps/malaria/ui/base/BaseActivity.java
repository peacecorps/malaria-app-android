package com.peacecorps.malaria.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<V extends MvpView, P extends MvpPresenter<V>> extends  AppCompatActivity
        implements MvpView, BaseFragment.Callback {
    private Context mContext;
    private V view;
    private P presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Todo init view, instantiate presenter & attach views
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Todo detach views
    }
}
