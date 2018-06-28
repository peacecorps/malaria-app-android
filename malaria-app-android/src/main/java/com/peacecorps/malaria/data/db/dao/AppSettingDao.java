package com.peacecorps.malaria.data.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.peacecorps.malaria.data.db.entities.AppSetting;

@Dao
public interface AppSettingDao {

    @Insert
    void insertAppSettings(AppSetting appSetting);

    @Query("SELECT freshInstall FROM AppSetting ORDER BY appSettingId ASC LIMIT 1")
    String checkFirstInstall();

    @Query("DELETE FROM AppSetting WHERE appSettingId = 1")
    void deleteFirstRow();

    @Query("DELETE FROM UserMedicine")
    void deleteTableRows();


}
