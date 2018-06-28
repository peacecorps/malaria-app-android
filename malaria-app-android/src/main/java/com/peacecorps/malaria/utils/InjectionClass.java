package com.peacecorps.malaria.utils;

import android.content.Context;

import com.peacecorps.malaria.data.db.AppDatabase;
import com.peacecorps.malaria.data.db.AppDbHelper;



public class InjectionClass {

    public static AppDbHelper provideSqliteHelper(Context context) {
        AppDatabase database = AppDatabase.getAppDatabase(context);
        return AppDbHelper.getInstance(new AppExecutors(), database.appSettingDao(), database.locationDao(), database.packingDao(),
                database.userMedicineDao());
    }


}
