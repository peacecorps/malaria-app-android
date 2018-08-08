package com.peacecorps.malaria.data.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class UserMedicine {
    @PrimaryKey(autoGenerate = true)
    private int userMedicineId;
    private String drug;
    private String choice;
    private int month;
    private int year;
    private int date;
    private String status;
    private double percentage;
    private String timeStamp;

    public UserMedicine(String drug, String choice, int month, int year, int date, String status,
                        double percentage, String timeStamp) {
        this.drug = drug;
        this.choice = choice;
        this.month = month;
        this.year = year;
        this.date = date;
        this.status = status;
        this.percentage = percentage;
        this.timeStamp = timeStamp;
    }

    public int getUserMedicineId() {
        return userMedicineId;
    }

    public void setUserMedicineId(int userMedicineId) {
        this.userMedicineId = userMedicineId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }
}
