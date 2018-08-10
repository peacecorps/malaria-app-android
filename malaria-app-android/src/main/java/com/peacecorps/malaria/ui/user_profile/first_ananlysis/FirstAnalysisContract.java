package com.peacecorps.malaria.ui.user_profile.first_ananlysis;

import com.peacecorps.malaria.ui.base.MvpPresenter;
import com.peacecorps.malaria.ui.base.MvpView;

/**
 * Created by Anamika Tripathi on 7/8/18.
 */
public interface FirstAnalysisContract {
    interface FirstAnalysisMvpView extends MvpView {
        void setLastTakenTime(String data);

        void setDosesText(int data);

        void setAdherenceRate(String data);
    }

    interface FirstAnalysisMvpPresenter<V extends FirstAnalysisMvpView> extends MvpPresenter<V> {
        void updateMediLastTime();

        void updateDoses();

        void getAdherenceData();
    }
}
