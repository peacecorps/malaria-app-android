package com.peacecorps.malaria.ui.user_profile.first_ananlysis;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peacecorps.malaria.R;
import com.peacecorps.malaria.ui.base.BaseFragment;
import com.peacecorps.malaria.ui.user_profile.first_ananlysis.FirstAnalysisContract.FirstAnalysisMvpView;
import com.peacecorps.malaria.utils.InjectionClass;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anamika Tripathi on 7/8/18.
 */
public class FirstAnalysisFragment extends BaseFragment implements FirstAnalysisMvpView {

    private FirstAnalysisPresenter<FirstAnalysisFragment> presenter;

    @BindView(R.id.tv_check_last_time)
    TextView tvCheckMediLastTakenTime;
    @BindView(R.id.tv_doses_in_row)
    TextView tvDoses;
    @BindView(R.id.tv_adherence)
    TextView adherence;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_analytic_screen, container, false);
        ButterKnife.bind(this, view);
        Context context = getContext();
        presenter = new FirstAnalysisPresenter<>(InjectionClass.provideDataManager(context), context);
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
        return R.layout.fragment_first_analytic_screen;
    }

    /**
     * initialize everyTime this fragment's onViewCreated gets called
     * calls presenter's function which further calls setter method to set data for Textview
     */
    @Override
    protected void init() {
        presenter.updateMediLastTime();
        presenter.updateDoses();
        presenter.getAdherenceData();
    }

    /**
     * @param data : string value of last time medicine was taken
     *             desc: sets value to checkMediLastTakenTime
     */
    @Override
    public void setLastTakenTime(String data) {
        tvCheckMediLastTakenTime.setText(data);
    }

    /**
     * @param data : doses in a row value (checked before if it's weekly or daily)
     *             desc: sets data to doses textView
     */
    @Override
    public void setDosesText(int data) {
        tvDoses.setText(getString(R.string.blank_text, data));
    }

    @Override
    public void setAdherenceRate(String data) {
        adherence.setText(data);
    }
}
