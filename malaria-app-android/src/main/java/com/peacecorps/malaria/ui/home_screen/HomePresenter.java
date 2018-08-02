package com.peacecorps.malaria.ui.home_screen;

import android.content.Context;

import com.peacecorps.malaria.data.AppDataManager;
import com.peacecorps.malaria.ui.base.BasePresenter;
import com.peacecorps.malaria.ui.home_screen.HomeContract.IHomePresenter;

public class HomePresenter<V extends HomeContract.IHomeView> extends BasePresenter<V> implements IHomePresenter {

    HomePresenter(AppDataManager manager, Context context) {
        super(manager, context);
    }

    @Override
    public void resetDatabase() {
        //Todo reset preferences too
        getDataManager().resetDatabase();
    }
}
