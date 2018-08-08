package com.peacecorps.malaria.ui.base;

import android.app.Application;

import com.peacecorps.malaria.utils.TypefaceUtil;

/**
 * Created by Anamika Tripathi on 7/8/18.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF",
                "fonts/garreg.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
    }
}