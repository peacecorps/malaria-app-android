package com.peacecorps.malaria.ui.trip_reminder;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;

import com.peacecorps.malaria.R;
import com.peacecorps.malaria.ui.base.BaseFragment;
import com.peacecorps.malaria.ui.trip_reminder.PlanTripContract.PlanTripMvpView;
import com.peacecorps.malaria.utils.DatePickerFragment;
import com.peacecorps.malaria.utils.InjectionClass;
import com.peacecorps.malaria.utils.TimePickerFragment;
import com.peacecorps.malaria.utils.ToastLogSnackBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.OnTouchListener;

/**
 * Created by Anamika Tripathi on 24/7/18.
 */
public class PlanTripFragment extends BaseFragment implements PlanTripMvpView {

    private PlanTripPresenter<PlanTripFragment> presenter;
    private Context context;
    // edit texts
    @BindView(R.id.et_trip_location)
    TextInputEditText etTripLocation;
    @BindView(R.id.et_departure_date)
    TextInputEditText etDepartureDate;
    @BindView(R.id.et_arrival_date)
    TextInputEditText etArrivalDate;
    @BindView(R.id.et_select_items)
    TextInputEditText etSelectItem;
    @BindView(R.id.et_trip_reminder_time)
    TextInputEditText etReminderTime;

    private boolean checkDepartureOrArrivalEt;
    private boolean arr, dptr;
    private OnPlanFragmentListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_trip, container, false);
        context = getContext();
        ButterKnife.bind(this, view);
        presenter = new PlanTripPresenter<>(InjectionClass.provideDataManager(context), context);
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(presenter.isViewAttached()) {
            init();
        } else {
            ToastLogSnackBarUtil.showErrorLog("PlanTripFragment: View not attached");
        }
    }

    @Override
    protected int getContentResource() {
        return R.layout.tripindicator_layout;
    }

    @Override
    protected void init() {
        // setting touch listener on drawable Right -- location history
        addLocationHistoryDialog();
        // setting date time picker on drawable Right -- etDepartureDate, etArrivalDate, etReminderTime
        startDateTimePickerDialog();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addLocationHistoryDialog() {
        etTripLocation.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP &&
                        (event.getRawX() >= (etTripLocation.getRight()
                                - etTripLocation.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))) {
                    // creating a custom dialog with location history
                    presenter.setUpLocationDialog();
                    return true;
                }
                return false;
            }


        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void startDateTimePickerDialog() {
        etDepartureDate.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP &&
                        (event.getRawX() >= (etDepartureDate.getRight() - etDepartureDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))) {
                    checkDepartureOrArrivalEt = true;
                    // creating show date picker dialog
                    showDatePicker();
                    return true;
                }
                return false;
            }


        });

        etArrivalDate.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP &&
                        (event.getRawX() >= (etArrivalDate.getRight() - etArrivalDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))) {
                    checkDepartureOrArrivalEt = false;
                    // creating show date picker dialog
                    showDatePicker();
                    return true;
                }
                return false;
            }


        });

        etReminderTime.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP &&
                        (event.getRawX() >= (etReminderTime.getRight() - etReminderTime.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))) {
                    checkDepartureOrArrivalEt = true;
                    // creating show date picker dialog
                    showTimePicker();
                    return true;
                }
                return false;
            }


        });
    }

    @OnClick(R.id.et_departure_date)
    public void departureDateListener() {
        checkDepartureOrArrivalEt = true;
        showDatePicker();
    }

    @OnClick(R.id.et_arrival_date)
    public void arrivalDateListener() {
        checkDepartureOrArrivalEt = false;
        showDatePicker();
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();

        if (getFragmentManager() != null) {
            date.show(getFragmentManager(), "Date Picker");
        } else {
            ToastLogSnackBarUtil.showErrorLog("getFragmentManager is null in PlanTripFragment");
        }


        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                if (checkDepartureOrArrivalEt) {
                    dptr = true;
                    etDepartureDate.setText(getResources().getString(R.string.et_trip_date, day, month + 1, year));
                } else {
                    arr = true;
                    etArrivalDate.setText(getResources().getString(R.string.et_trip_date, day, month + 1, year));
                }

            }
        };
        /*
         * Set Call back to capture selected date
         */
        date.setCallBack(onDateSetListener);

    }

    private void showTimePicker() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        if (getFragmentManager() != null && getActivity() != null) {
            timePickerFragment.show(getActivity().getFragmentManager(), "Time Picker in PlanTripFragment");
        } else {
            ToastLogSnackBarUtil.showErrorLog("PlanTripFragment: getFragmentManager/getActivity is null");
        }

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                etReminderTime.setText(presenter.convertToTwelveHours(hourOfDay, minute));
            }
        };
        /*
         * Set Call back to capture selected date
         */
        timePickerFragment.setCallBack(listener);

    }

    /**
     * @param view : starts focus on passed edit text as parameter
     */
    private void requestFocus(View view) {
        if (view.requestFocus() && getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } else {
            ToastLogSnackBarUtil.showErrorLog("PlanTripFragment: getActivity is null or focusable is false");
        }
    }

    // set select button listener
    @OnClick(R.id.et_select_items)
    public void selectButtonListener() {
        if (arr && dptr) {
            // departure & arrival value set , checks validity of dates
            presenter.checkDateValidity(etArrivalDate.getText().toString(), etDepartureDate.getText().toString());
        } else {
            ToastLogSnackBarUtil.showToast(context, "Select Arrival & Departure date first!");
        }
    }

    // generate button listener
    @OnClick(R.id.btn_trip_generate)
    public void generateButtonListener() {
        /*
         * check etSelectItem, etReminderTime, etTripLocation is valid - (not null & not empty)
         */
        presenter.validationGenerateButton();
        //Todo create alarm - need to implement alarm services first
        /*
         * Setting Up Alarm for a Week Before
         * A day Before
         * On day of Trip
         */

    }

    /**
     * This interface is implemented in MainActivity which starts SelectItem Dialog fragment
     */
    public interface OnPlanFragmentListener {
        void startSelectItemFragment(long quantity);
    }

    // Container Activity i.e MainActivity must implement this interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception - showing message via Error log
        if (context instanceof OnPlanFragmentListener) {
            listener = (OnPlanFragmentListener) context;
        } else {
            // Show error Log for debugging & display snackbar to user
            ToastLogSnackBarUtil.showErrorLog(context.toString() + " must implement OnPlanFragmentListener");
            if (getActivity() != null) {
                ToastLogSnackBarUtil.showSnackBar(context, getActivity().findViewById(android.R.id.content),
                        "Something went wrong!");
            }
        }
    }

    /**
     * PlanTripContract.PlanTripMvpView's functions
     */


    // return false if view is null else true
    private boolean checkViewNotNull(View view) {
        return view != null;
    }

    // returns true if etTripLocation not null & not empty, else provides appropriate message
    @Override
    public boolean validateTripLocation() {
        if (checkViewNotNull(etTripLocation)) {
            String text = etTripLocation.getText().toString().trim();
            if (!presenter.testIsEmpty(text)) {
                return true;
            } else {
                etTripLocation.setError("can't leave empty");
                requestFocus(etTripLocation);
                return false;
            }
        } else {
            ToastLogSnackBarUtil.showErrorLog("PlanTripFragment: etTripLocation is null");
        }
        return false;
    }

    // returns true if etDepartureDate not null & not empty, else provides appropriate message
    @Override
    public boolean validateDepartureDate() {
        if (checkViewNotNull(etDepartureDate)) {
            String text = etDepartureDate.getText().toString().trim();
            return !presenter.testIsEmpty(text);
        } else {
            ToastLogSnackBarUtil.showErrorLog("PlanTripFragment: etDepartureDate is null");
        }
        return false;
    }

    // returns true if etArrivalDate not null & not empty, else provides appropriate message
    @Override
    public boolean validateArrivalDate() {
        if (checkViewNotNull(etArrivalDate)) {
            String text = etArrivalDate.getText().toString().trim();
            return !presenter.testIsEmpty(text);
        } else {
            ToastLogSnackBarUtil.showErrorLog("PlanTripFragment: etArrivalDate is null");
        }
        return false;
    }

    // returns true if etSelectItem not null & not empty, else provides appropriate message
    @Override
    public boolean validateSelectItem() {
        if (checkViewNotNull(etSelectItem)) {
            String text = etSelectItem.getText().toString().trim();
            if (!presenter.testIsEmpty(text)) {
                return true;
            } else {
                etSelectItem.setError("can't leave empty");
                return false;
            }
        } else {
            ToastLogSnackBarUtil.showErrorLog("PlanTripFragment: etSelectItem is null");
        }
        return false;
    }

    // returns true if etReminderTime not null & not empty, else provides appropriate message
    @Override
    public boolean validateReminderTime() {
        if (checkViewNotNull(etReminderTime)) {
            String text = etReminderTime.getText().toString().trim();
            if (!presenter.testIsEmpty(text)) {
                return true;
            } else {
                etSelectItem.setError("can't leave empty");
                return false;
            }
        } else {
            ToastLogSnackBarUtil.showErrorLog("PlanTripFragment: etReminderTime is null");
        }
        return false;
    }

    /**
     * @param numberOfDrugs : total number of medicine calculated - displayed in SelectItemFragment
     */
    @Override
    public void startSelectItemFragment(long numberOfDrugs) {
        ToastLogSnackBarUtil.showDebugLog("PlanTripF: calling show Dialog");
        listener.startSelectItemFragment(numberOfDrugs);
    }

    // sets text for select item
    @Override
    public void updateSelectItemText(String text) {
        etSelectItem.setText(text);
    }

    /**
     * @param list : List of all history location fetched from sqlite db inside presenter
     */
    @Override
    public void createSelectLocationDialog(final List<String> list) {
        if (list.size() > 0) {
            final Dialog alertDialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
            alertDialog.setContentView(R.layout.trip_location_dialog);
            alertDialog.setCancelable(false);

            ListView lv = alertDialog.findViewById(R.id.list_history_location);
            ArrayAdapter<String> adapter;

            // Setting Dialog Title
            alertDialog.setTitle(getString(R.string.heading_previous_location));
            adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    etTripLocation.setText(list.get(position));
                    alertDialog.cancel();
                }
            });
            // Showing Alert Message
            alertDialog.show();
            // doKeepDialog(alertDialog);
        } else {
            if (getActivity() != null) {
                ToastLogSnackBarUtil.showSnackBar(context, getActivity().findViewById(android.R.id.content), "No location History found!");
            } else {
                ToastLogSnackBarUtil.showErrorLog("getActivity return null in PlanTripFragment");
            }
        }

    }

    // returns location (validity of text checked before calling this function)
    @Override
    public String getLocationText() {
        return etTripLocation.getText().toString().trim();
    }

    // returns departure date (validity of text checked before calling this function)
    @Override
    public String getDepartureTimeText() {
        return etDepartureDate.getText().toString().trim();
    }

    // displays custom bar with appropriate message received
    @Override
    public void showCustomSnackBar(String message) {
        if (getActivity() != null) {
            ToastLogSnackBarUtil.showSnackBar(context, getActivity().findViewById(android.R.id.content), message);
        }

    }


}