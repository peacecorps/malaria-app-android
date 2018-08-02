package com.peacecorps.malaria.ui.trip_reminder;

import com.peacecorps.malaria.ui.base.MvpPresenter;
import com.peacecorps.malaria.ui.base.MvpView;

import java.util.Date;
import java.util.List;

/**
 * Created by Anamika Tripathi on 24/7/18.
 */
public interface PlanTripContract {
    interface PlanTripMvpView extends MvpView {

        void createSelectLocationDialog(List<String> list);

        void startSelectItemFragment(long numberOfDrugs);

        void updateSelectItemText(String text);

        String getLocationText();

        String getDepartureTimeText();

        void showCustomSnackBar(String message);

        boolean validateTripLocation();

        boolean validateDepartureDate();

        boolean validateArrivalDate();

        boolean validateSelectItem();

        boolean validateReminderTime();
    }

    interface PlanTripMvpPresenter<V extends PlanTripMvpView> extends MvpPresenter<V> {
        void setUpLocationDialog();

        String convertToTwelveHours(int hours, int mins);

        boolean testIsEmpty(String text);

        Date getDateObj(String s);

        long convertDateToTime(Date date);

        void checkDateValidity(String dateArr, String dateDepart);

        long calculateNumOfMedicine(long a, long d);

        void saveTripDetails();

        void validationGenerateButton();
    }
}
