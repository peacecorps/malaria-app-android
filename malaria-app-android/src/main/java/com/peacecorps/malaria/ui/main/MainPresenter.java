package com.peacecorps.malaria.ui.main;

import android.content.Context;

import com.peacecorps.malaria.data.AppDataManager;
import com.peacecorps.malaria.ui.base.BasePresenter;
import com.peacecorps.malaria.ui.main.MainContract.IHomePresenter;

public class MainPresenter<V extends MainContract.IHomeView> extends BasePresenter<V> implements IHomePresenter {

    MainPresenter(AppDataManager manager, Context context) {
        super(manager, context);
    }

    @Override
    public void resetDatabase() {
        //Todo reset preferences too
        getDataManager().resetDatabase();
    }
}
