package com.peacecorps.malaria.ui.home_screen;

import com.peacecorps.malaria.ui.base.MvpPresenter;
import com.peacecorps.malaria.ui.base.MvpView;

/**
 * Created by Anamika Tripathi on 5/8/18.
 */
public interface HomeContract {

    interface HomeMvpView extends MvpView{
        void setCurrentDayAndDate(String date, String day);
        void setWarningText();
        void isDrugTakenUI();
        void isDrugNotTakenUI();
        void newDayUI();
        void missedWeekUI();
        void startAlarmServiceClass();

    }

    interface HomeMvpPresenter<V extends HomeMvpView> extends MvpPresenter<V> {
        void checkDayDateOfWeek();
        void checkWarningVisibility();
        void increaseDrugAcceptedCount();
        void updateUserMedicineSelection(double adherenceRate, boolean isAcceptButton);
        void checkDrugIntervalFirstRunTime(boolean isAcceptButton);
        void decideDrugTakenUIBoolean(boolean isWeekly, boolean isTaken);
        void increaseDrugRejectCount();
        void decideDrugTakenForUI();
        void checkDrugIntervalWeeklyDate();
        void storeMediTimeLastChecked();

    }
}
