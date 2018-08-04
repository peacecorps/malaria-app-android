package com.peacecorps.malaria.ui.home_screen;

import com.peacecorps.malaria.ui.base.MvpView;

public interface HomeContract {
    interface IHomeView extends MvpView {
        void startMedicineSettingActivity();
    }

    interface IHomePresenter {
        void resetDatabase();
    }
}
