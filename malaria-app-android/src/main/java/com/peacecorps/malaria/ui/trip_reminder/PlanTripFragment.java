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

import static android.view.View.*;

/**
 * Created by Anamika Tripathi on 24/7/18.
 */
public class PlanTripFragment extends BaseFragment implements PlanTripMvpView {

    private PlanTripPresenter<PlanTripFragment> presenter;
    private Context context;
    @BindView(R.id.et_trip_location)
    TextInputEditText tvTripLocation;
    @BindView(R.id.et_trip_reminder_time)
    TextInputEditText tvReminderTime;
    @BindView(R.id.et_departure_date)
    TextInputEditText tvDepartureDate;
    @BindView(R.id.et_arrival_date)
    TextInputEditText tvArrivalDate;
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
        init();
    }

    @Override
    protected int getContentResource() {
        return R.layout.tripindicator_layout;
    }

    @Override
    protected void init() {
        // setting touch listener on drawable Right -- location history
        addLocationHistoryDialog();
        startDateTimePickerDialog();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addLocationHistoryDialog() {
        tvTripLocation.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP &&
                        (event.getRawX() >= (tvTripLocation.getRight() - tvTripLocation.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))) {
                    // creating a custom dialog with location history
                    presenter.getLocationHistory();
                    return true;
                }
                return false;
            }


        });
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
                    tvTripLocation.setText(list.get(position));
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

    @SuppressLint("ClickableViewAccessibility")
    private void startDateTimePickerDialog() {
        tvDepartureDate.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP &&
                        (event.getRawX() >= (tvDepartureDate.getRight() - tvDepartureDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))) {
                    checkDepartureOrArrivalEt = true;
                    // creating show date picker dialog
                    showDatePicker();
                    return true;
                }
                return false;
            }


        });

        tvArrivalDate.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP &&
                        (event.getRawX() >= (tvArrivalDate.getRight() - tvArrivalDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))) {
                    checkDepartureOrArrivalEt = false;
                    // creating show date picker dialog
                    showDatePicker();
                    return true;
                }
                return false;
            }


        });

        tvReminderTime.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP &&
                        (event.getRawX() >= (tvReminderTime.getRight() - tvReminderTime.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))) {
                    checkDepartureOrArrivalEt = false;
                    // creating show date picker dialog
                    showTimePicker();
                    return true;
                }
                return false;
            }


        });
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();

        if (getFragmentManager() != null) {
            date.show(getFragmentManager(), "Date Picker");
        } else {
            ToastLogSnackBarUtil.showErrorLog("getFragmentManager is null in PlanTripFragment");
        }


        DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                if (checkDepartureOrArrivalEt) {
                    dptr = true;
                    tvDepartureDate.setText(getResources().getString(R.string.et_trip_date, day, month + 1, year));
                } else {
                    arr = true;
                    tvArrivalDate.setText(getResources().getString(R.string.et_trip_date, day, month + 1, year));
                }

            }
        };
        /*
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);

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
                tvReminderTime.setText(presenter.convertToTwelveHours(hourOfDay, minute));
            }
        };
        /*
         * Set Call back to capture selected date
         */
        timePickerFragment.setCallBack(listener);

    }

    @OnClick(R.id.btn_trip_generate)
    public void generateButtonListener(View view) {
        //Todo Need to implement it

    }

    @OnClick(R.id.et_select_items)
    public void selectButtonListener(View view) {
        if (arr && dptr) {
            // departure & arrival value set , checks validity of dates
            presenter.checkDateValidity(tvArrivalDate.getText().toString(), tvDepartureDate.getText().toString());
        } else {
            ToastLogSnackBarUtil.showToast(context, "Select Arrival & Departure date first!");
        }
    }

    /**
     * @param numberOfDrugs : total number of medicine to pack - displayed in SelectItemFragment
     */
    @Override
    public void startSelectItemFragment(long numberOfDrugs) {
        listener.startSelectItemFragment();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnPlanFragmentListener {
        void startSelectItemFragment();
    }

    // Container Activity must implement this interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
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
}
