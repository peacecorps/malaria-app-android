package com.peacecorps.malaria.data.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.peacecorps.malaria.data.db.entities.Location;

import java.util.List;

@Dao
public interface LocationDao {

    @Insert
    void insertLocation(Location location);

    @Query("SELECT * FROM Location WHERE location= :location")
    List<Location> getLocationListByLocation(String location);

    @Query("SELECT * FROM Location ORDER BY locationId ASC")
    List<Location> getLocationList();

    @Query("UPDATE Location SET time= :a WHERE location= :location ")
    void updateLocation(int a, String location);

    @Query("DELETE FROM Location")
    void deleteTableRows();
}
