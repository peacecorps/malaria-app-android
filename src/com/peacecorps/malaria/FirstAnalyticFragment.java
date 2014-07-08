package com.example.viewpagertest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FirstAnalyticFragment extends Fragment {


    static SharedPreferenceStore mSharedPreferenceStore;
    static View rootView;
    public static TextView checkMediLastTakenTime =null;
    public static TextView doses =null;
    public static TextView adherence =null;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.fragment_first_analytic_screen, null);


        checkMediLastTakenTime = (TextView)rootView.findViewById(R.id.checkMediLastTakenTime);
        doses = (TextView)rootView.findViewById(R.id.doses);
        adherence = (TextView)rootView.findViewById(R.id.adherence);

        if(checkMediLastTakenTime != null) {
            checkMediLastTakenTime.setText(mSharedPreferenceStore.mPrefsStore.getString("com.pc.checkMediLastTakenTime", "").toString());
            doses.setText(""+mSharedPreferenceStore.mPrefsStore.getInt("com.pc.acceptedCount", 0));
        }


        return rootView;

	}


}