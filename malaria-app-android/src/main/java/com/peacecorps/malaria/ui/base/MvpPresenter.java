package com.peacecorps.malaria.ui.base;

public interface MvpPresenter<V extends MvpView> {

    /**
     * called when view is attached to presenter
     *
     * @param view
     */
    void attachView(V view);

    /**
     * called when view is detached from presenter
     */
    void detachView();
}