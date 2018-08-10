package com.peacecorps.malaria.ui.user_profile.second_analysis;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peacecorps.malaria.R;
import com.peacecorps.malaria.ui.base.BaseFragment;
import com.peacecorps.malaria.ui.user_profile.second_analysis.SecondAnalysisContract.SecondAnalysisMvpView;
import com.peacecorps.malaria.utils.InjectionClass;
import com.peacecorps.malaria.utils.ToastLogSnackBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Anamika Tripathi on 7/8/18.
 */
public class SecondAnalysisFragment extends BaseFragment implements SecondAnalysisMvpView {
    private SecondAnalysisPresenter<SecondAnalysisFragment> presenter;

    private List<AnalysisModel> modelList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_analytic_screen, container, false);
        ButterKnife.bind(this, view);
        Context context = getContext();
        presenter = new SecondAnalysisPresenter<>(InjectionClass.provideDataManager(context), context);
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
        return R.layout.fragment_second_analytic_screen;
    }

    @Override
    protected void init() {
        modelList = new ArrayList<>();
        presenter.getDataForProgressBar();
    }

    @Override
    public void addToList(String month, String percentage) {
        modelList.add(new AnalysisModel(month, percentage));
        for (AnalysisModel model : modelList) {
            ToastLogSnackBarUtil.showDebugLog(model.getMonth() + " " + model.getPercentage());
        }
    }
}
