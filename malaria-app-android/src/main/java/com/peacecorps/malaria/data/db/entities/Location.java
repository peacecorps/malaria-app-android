package com.peacecorps.malaria.data.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Location {

    @PrimaryKey(autoGenerate = true)
    private int locationId;

    private String locationName;

    private int time;

    public Location(String locationName, int time) {
        this.locationName = locationName;
        this.time = time;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
