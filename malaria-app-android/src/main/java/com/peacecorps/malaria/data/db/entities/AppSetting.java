package com.peacecorps.malaria.data.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class AppSetting {
    @PrimaryKey(autoGenerate = true)
    private int appSettingId;
    private String drugName;
    private String choice;
    private int weeklyDay;
    private long firstTime;
    private String freshInstall;

    public AppSetting(String drugName, String choice, int weeklyDay, long firstTime, String freshInstall) {
        this.drugName = drugName;
        this.choice = choice;
        this.weeklyDay = weeklyDay;
        this.firstTime = firstTime;
        this.freshInstall = freshInstall;
    }

    public int getAppSettingId() {
        return appSettingId;
    }

    public void setAppSettingId(int appSettingId) {
        this.appSettingId = appSettingId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public int getWeeklyDay() {
        return weeklyDay;
    }

    public void setWeeklyDay(int weeklyDay) {
        this.weeklyDay = weeklyDay;
    }

    public long getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(long firstTime) {
        this.firstTime = firstTime;
    }

    public String getFreshInstall() {
        return freshInstall;
    }

    public void setFreshInstall(String freshInstall) {
        this.freshInstall = freshInstall;
    }
}
