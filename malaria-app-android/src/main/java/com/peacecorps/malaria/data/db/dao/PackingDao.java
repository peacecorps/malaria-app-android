package com.peacecorps.malaria.data.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.peacecorps.malaria.data.db.entities.Packing;

import java.util.List;

@Dao
public interface PackingDao {

    @Query("DELETE FROM UserMedicine")
    void deleteTableRows();

    @Query("SELECT packingQuantity FROM packing_settings WHERE packingItem= :item")
    List<Integer> getPackingQuantityList(String item);

    @Query("UPDATE packing_settings SET packingItem= :pItem , packingQuantity= :quantity, packingStatus= :status WHERE packingItem= :pItem")
    void updatePacking(String pItem,int quantity, String status);

    @Insert
    void insertPacking(Packing packing);

    @Query("SELECT * FROM PACKING_SETTINGS WHERE packingStatus= :status ORDER BY packingId ASC")
    List<Packing> getPackingItemChecked(String status);

    @Query("SELECT * FROM PACKING_SETTINGS ORDER BY packingId ASC")
    List<Packing> getPackingItem();

    @Query("UPDATE packing_settings SET packingStatus= :status")
    void refreshPackingItemStatus(String status);

}
