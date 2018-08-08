package com.peacecorps.malaria.ui.main;

import com.peacecorps.malaria.ui.base.MvpView;

public interface MainContract {
    interface IHomeView extends MvpView {
        void startMedicineSettingActivity();
    }

    interface IHomePresenter {
        void resetDatabase();
    }
}
