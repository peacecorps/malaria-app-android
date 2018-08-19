package com.peacecorps.malaria.ui.trip_reminder.trip_select_item;

import com.peacecorps.malaria.data.db.entities.Packing;
import com.peacecorps.malaria.ui.base.MvpPresenter;
import com.peacecorps.malaria.ui.base.MvpView;

/**
 * Created by Anamika Tripathi on 1/8/18.
 */
public interface ItemDialogContract {
    interface ItemDialogMvpView extends MvpView {
        void notifyAdapterForUpdate();
        void setDrugQuantity(int quantity);
        void closeDialog(Packing packing);
    }

    interface ItemDialogMvpPresenter<V extends ItemDialogMvpView> extends MvpPresenter<V> {
        boolean testIsEmpty(String text);
        void addToPackingList(String item);
        void calculateDrugQuantity(int num, long q);
        void replaceMedicineName(String med, int quantity);
        void updatePackingStatus(int pos, boolean status);
        void deletePackingRow(int pos);
        void addCashToPacking(int total);
        void getMedDetailsAndCloseDialog();
    }
}
