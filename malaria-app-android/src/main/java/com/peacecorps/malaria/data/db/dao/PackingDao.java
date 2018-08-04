package com.peacecorps.malaria.data.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.peacecorps.malaria.data.db.entities.Packing;

import java.util.List;

@Dao
public interface PackingDao {

    @Query("DELETE FROM packing_settings")
    void deleteFullPackingList();

    @Query("SELECT packingQuantity FROM packing_settings WHERE packingItem= :item")
    List<Integer> getPackingQuantityList(String item);

    @Query("UPDATE packing_settings SET packingItem= :pItem , packingQuantity= :quantity, packingStatus= :status WHERE packingItem= :pItem")
    void updatePacking(String pItem, int quantity, boolean status);

    @Insert
    void insertPacking(Packing packing);

    @Query("SELECT * FROM PACKING_SETTINGS WHERE packingStatus= :status ORDER BY packingId ASC")
    List<Packing> getPackingItemChecked(boolean status);

    @Query("SELECT * FROM PACKING_SETTINGS ORDER BY packingId ASC")
    List<Packing> getPackingItem();

    @Query("UPDATE packing_settings SET packingStatus= :status")
    void refreshPackingItemStatus(boolean status);

    @Query("DELETE FROM packing_settings WHERE packingId = :id")
    void deletePackingById(int id);

    @Query("UPDATE packing_settings SET packingStatus= :status WHERE packingId= :position")
    void updatePackingStatus(boolean status, int position);

    @Query("SELECT Count(*) FROM packing_settings")
    int getPackingListSize();

    @Query("UPDATE packing_settings SET packingItem= :name , packingQuantity= :quantity, packingStatus= :status WHERE packingId= 1")
    void updateMedicinePacking(String name, int quantity, boolean status);

    @Query("SELECT * FROM packing_settings WHERE packingId = 1")
    Packing getPackedMedicine();
}
