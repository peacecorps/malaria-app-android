package com.peacecorps.malaria.data.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "packing_settings")
public class Packing {
    @PrimaryKey(autoGenerate = true)
    private int packingId;
    private String packingItem;
    private int packingQuantity;
    private String packingStatus;

    public Packing(String packingItem, int packingQuantity, String packingStatus) {
        this.packingItem = packingItem;
        this.packingQuantity = packingQuantity;
        this.packingStatus = packingStatus;
    }

    public int getPackingId() {
        return packingId;
    }

    public void setPackingId(int packingId) {
        this.packingId = packingId;
    }

    public String getPackingItem() {
        return packingItem;
    }

    public void setPackingItem(String packingItem) {
        this.packingItem = packingItem;
    }

    public int getPackingQuantity() {
        return packingQuantity;
    }

    public void setPackingQuantity(int packingQuantity) {
        this.packingQuantity = packingQuantity;
    }

    public String getPackingStatus() {
        return packingStatus;
    }

    public void setPackingStatus(String packingStatus) {
        this.packingStatus = packingStatus;
    }
}
