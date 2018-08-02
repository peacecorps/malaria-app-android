package com.peacecorps.malaria.ui.trip_reminder.trip_select_item;

import android.content.Context;
import android.text.TextUtils;

import com.peacecorps.malaria.R;
import com.peacecorps.malaria.data.AppDataManager;
import com.peacecorps.malaria.data.db.DbHelper;
import com.peacecorps.malaria.data.db.entities.Packing;
import com.peacecorps.malaria.ui.base.BasePresenter;
import com.peacecorps.malaria.ui.trip_reminder.trip_select_item.ItemDialogContract.ItemDialogMvpPresenter;
import com.peacecorps.malaria.ui.trip_reminder.trip_select_item.ItemDialogContract.ItemDialogMvpView;
import com.peacecorps.malaria.utils.ToastLogSnackBarUtil;

import java.util.List;

/**
 * Created by Anamika Tripathi on 1/8/18.
 */
public class ItemDialogPresenter<V extends ItemDialogMvpView> extends BasePresenter<V> implements ItemDialogMvpPresenter<V> {

    private List<Packing> itemList;

    ItemDialogPresenter(AppDataManager manager, Context context) {
        super(manager, context);
        setUpItemList();
    }

    private void setUpItemList() {
        getDataManager().getPackingItem(new DbHelper.LoadListPackingCallback() {
            @Override
            public void onDataLoaded(List<Packing> packingList) {
                itemList = packingList;
                if (packingList.size() == 0) {
                    ToastLogSnackBarUtil.showDebugLog("no packing list in db");
                    String drug = getDataManager().getDrugPicked();
                    itemList.add(new Packing(drug, 0, false));
                    getDataManager().insertPackingItem(drug, 0, false);
                }
            }
        });

        getDataManager().getPackingItemChecked(new DbHelper.LoadListPackingCallback() {
            @Override
            public void onDataLoaded(List<Packing> packingList) {
                ToastLogSnackBarUtil.showDebugLog(packingList.size() + "");
            }
        });


    }

    public List<Packing> getItemList() {
        return itemList;
    }

    // util function for testing text is empty of not
    @Override
    public boolean testIsEmpty(String text) {
        return TextUtils.isEmpty(text);
    }

    @Override
    public void addToPackingList(String item) {
        // insert or update (if item already exists) to database
        getDataManager().insertPackingItem(item, 1, false);
        // get update packing list for recycler view (makes sure that no duplication exist in list)
        getDataManager().getPackingItem(new DbHelper.LoadListPackingCallback() {
            @Override
            public void onDataLoaded(List<Packing> packingList) {
                itemList = packingList;
                getView().notifyAdapterForUpdate();
            }
        });
    }

    /**
     * @param pos : medicine selected in alert dialog
     *            0-Malarone(Daily), 1-Doxycycline(Daily), 2-Melfoquine(Weekly)
     * @param qty : total quantity passed to fragment
     */
    @Override
    public void calculateDrugQuantity(int pos, long qty) {
        int quantity = 0;
        switch (pos) {
            case 0:
                quantity = (int) qty;
                break;
            case 1:
                quantity = (int) qty;
                break;
            case 2:
                quantity = (int) ((qty % 7 == 0) ? qty / 7 : (qty / 7) + 1);
                break;
            default:
                ToastLogSnackBarUtil.showErrorLog("ItemDialogPresenter: invalid option selected");
        }
        getView().setDrugQuantity(quantity);
    }

    /**
     * @param med      : changed medicine - selected from alert dialog
     * @param quantity : calculated quantity of medicine
     */
    @Override
    public void replaceMedicineName(String med, int quantity) {
        // updating db with selected medicine & updating item list
        getDataManager().updateMedicinePacking(med, quantity);
        // get update packing list for recycler view (makes sure that no duplication exist in list)
        getDataManager().getPackingItem(new DbHelper.LoadListPackingCallback() {
            @Override
            public void onDataLoaded(List<Packing> packingList) {
                itemList = packingList;
            }
        });
        itemList.set(0, new Packing(med + ": " + quantity, quantity, false));
        getView().notifyAdapterForUpdate();
    }

    /**
     * @param pos    : row number of list
     * @param status : status of checkbox
     */
    @Override
    public void updatePackingStatus(int pos, boolean status) {
        itemList.get(pos).setPackingStatus(status);
        getDataManager().updatePackingStatus(status, itemList.get(pos).getPackingId());
        // get update packing list for recycler view (makes sure list is updated for delete & checkbox changes)
        getDataManager().getPackingItem(new DbHelper.LoadListPackingCallback() {
            @Override
            public void onDataLoaded(List<Packing> packingList) {
                itemList = packingList;
            }
        });
    }

    /**
     * @param pos : row number to be deleted
     */
    @Override
    public void deletePackingRow(int pos) {
        getDataManager().deletePackingById(itemList.get(pos).getPackingId());
        itemList.remove(pos);

        // get update packing list for recycler view (makes sure list is updated for delete & checkbox changes)
        getDataManager().getPackingItem(new DbHelper.LoadListPackingCallback() {
            @Override
            public void onDataLoaded(List<Packing> packingList) {
                itemList = packingList;
            }
        });

        ToastLogSnackBarUtil.showDebugLog("deleting row " + pos);
    }

    @Override
    public void addCashToPacking(int total) {
        // adding valid cash quantity to be packed
        if (getContext() != null) {
            getDataManager().insertPackingItem(getContext().getString(R.string.label_cash) + ": $" + total,
                    total, false);
        }
    }

    @Override
    public void getMedDetailsAndCloseDialog() {
        // passing selected medicine to fragment
        getDataManager().getPackedMedDetails(new DbHelper.LoadPackingCallback() {
            @Override
            public void onDataLoaded(Packing packing) {
                getView().closeDialog(packing);
            }
        });
    }
}
