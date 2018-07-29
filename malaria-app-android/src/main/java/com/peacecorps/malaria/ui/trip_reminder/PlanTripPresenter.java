package com.peacecorps.malaria.ui.trip_reminder;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.peacecorps.malaria.R;
import com.peacecorps.malaria.data.AppDataManager;
import com.peacecorps.malaria.data.db.DbHelper;
import com.peacecorps.malaria.ui.base.BasePresenter;
import com.peacecorps.malaria.ui.trip_reminder.PlanTripContract.PlanTripMvpPresenter;
import com.peacecorps.malaria.ui.trip_reminder.PlanTripContract.PlanTripMvpView;
import com.peacecorps.malaria.utils.ToastLogSnackBarUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Anamika Tripathi on 26/7/18.
 */
public class PlanTripPresenter<V extends PlanTripMvpView> extends BasePresenter<V> implements PlanTripMvpPresenter<V> {

    PlanTripPresenter(AppDataManager manager, Context context) {
        super(manager, context);
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void getLocationHistory() {
        getDataManager().getLocation(new DbHelper.loadListStringCallBack() {
            @Override
            public void onDataLoaded(List<String> data) {
                getView().createSelectLocationDialog(data);
            }
        });
    }

    @Override
    public void addLocationToDataBase(String location) {
        getDataManager().insertLocation(location);
    }

    /**
     * @param hours : hour selected in @TimePickerFragment (24 hour format)
     * @param mins  : min selected in Fragment
     */
    @Override
    public String convertToTwelveHours(int hours, int mins) {
        String timeSet;
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes;
        if (mins < 10)
            minutes = getContext().getResources().getString(R.string.add_zero_beginning, mins);
        else
            minutes = String.valueOf(mins);

        // Append the time to a stringBuilder
        return getContext().getResources().getString(R.string.time_picker, hours, minutes, timeSet);
    }

    // util function for testing text is empty of not
    @Override
    public boolean testIsEmpty(String text) {
        return TextUtils.isEmpty(text);
    }

    /**
     * @param s : date in string format, received by getText().toString() on edit Text
     * @return : Date object by parsing the parameter received
     */
    @Override
    public Date getDateObj(String s) {
        Date dobj = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            dobj = sdf.parse(s);
        } catch (ParseException e) {
            ToastLogSnackBarUtil.showErrorLog(s + "PlanTripPresenter: Parsing error in SimpleDateFormat");
        }
        return dobj;
    }

    /**
     * @param date : date is converted to time using toTime()
     * @return : toTime() give long time in milis
     */
    @Override
    public long convertDateToTime(Date date) {
        return date.getTime();
    }

    /**
     * @param dateArr    : arrival date
     * @param dateDepart : departure date
     */
    @Override
    public void checkDateValidity(String dateArr, String dateDepart) {
        if (testIsEmpty(dateDepart))
            ToastLogSnackBarUtil.showToast(getContext(), "Select Departure date first");
        else if (testIsEmpty(dateArr))
            ToastLogSnackBarUtil.showToast(getContext(), "Select Arrival date first");
        else {
            String currDateString = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());
            Date currdate = getDateObj(currDateString);
            Date deptDate = getDateObj(dateDepart);
            Date arrDate = getDateObj(dateArr);

            if (currdate != null && deptDate != null && arrDate != null) {
                long arrLong = convertDateToTime(arrDate);
                long dptrLong = convertDateToTime(deptDate);
                long currLong = convertDateToTime(currdate);

                if (dptrLong < currLong) {
                    ToastLogSnackBarUtil.showToast(getContext(), getContext().getResources().getString(R.string.departuredate_currentdate));
                } else if (arrLong < dptrLong) {
                    ToastLogSnackBarUtil.showToast(getContext(), getContext().getResources().getString(R.string.arrivaldate_departuredate));
                } else if (arrLong >= dptrLong) {
                    // ask Fragment to start fragment, parameter is no of medicines calculated
                    getView().startSelectItemFragment(calculateNumOfMedicine(arrLong, dptrLong));
                }
            }
        }
    }

    /**
     * @param a : arrival time - converted in checkDateValidity()
     * @param d : departure time
     * @return : no of medicine to pack - displayed in select item fragment
     */
    @Override
    public long calculateNumOfMedicine(long a, long d) {
        int oneDay = 24 * 60 * 60 * 1000;

        return (a - d) / oneDay + 1;
    }
}
