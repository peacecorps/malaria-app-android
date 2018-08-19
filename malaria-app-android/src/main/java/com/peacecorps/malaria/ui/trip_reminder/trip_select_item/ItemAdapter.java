package com.peacecorps.malaria.ui.trip_reminder.trip_select_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.peacecorps.malaria.R;

/**
 * Created by Anamika Tripathi on 1/8/18.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private ItemDialogPresenter<ItemDialogFragment> presenter;
    private ItemOnClickListener listener;

    ItemAdapter(ItemDialogPresenter<ItemDialogFragment> presenter, ItemOnClickListener listener) {
        this.presenter = presenter;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_selected, parent, false);

        return new ItemViewHolder(view);
    }

    /**
     * @param holder   : holds the views for recycling
     * @param position : row number
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        /* set text on packing item for row - position
            get function are being checking inside presenter, refer ItemDialogPresenter
         */
        holder.selectedItem.setText(presenter.getItemList().get(position).getPackingItem());
        holder.bind(position);
    }

    /**
     * @return : size of presenter's item list, if null - returns 0
     */
    @Override
    public int getItemCount() {
        if (presenter.getItemList() == null)
            return 0;
        return presenter.getItemList().size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView selectedItem;
        private ImageView deleteView;
        private AppCompatCheckBox checkBox;

        ItemViewHolder(View itemView) {
            super(itemView);
            // initialize row's views
            selectedItem = itemView.findViewById(R.id.tv_packing_item);
            deleteView = itemView.findViewById(R.id.img_delete_row);
            checkBox = itemView.findViewById(R.id.packing_item_check_box);
            deleteView.setOnClickListener(this);
            checkBox.setOnClickListener(this);
        }

        /**
         * @param v : view on which click event occurred, either delete button click or checkbox status changed
         */
        @Override
        public void onClick(View v) {
            // checking id of view & position in list
            int id = v.getId();
            int pos = getAdapterPosition();

            if (id == R.id.img_delete_row) {
                // call delete row listener - implemented in ItemDialogFragment
                listener.onDeleteRowListener(pos);
            } else if (id == R.id.packing_item_check_box) {
                /*
                 * checking current status of checkbox at adapter's position from model arraylist
                 * inverse status of checkbox & status in array list
                 */
                if (presenter.getItemList().get(pos).getPackingStatus()) {
                    checkBox.setChecked(false);
                    presenter.updatePackingStatus(pos, false);
                } else {
                    checkBox.setChecked(true);
                    presenter.updatePackingStatus(pos, true);
                }
            }

        }

        public void bind(int position) {
            if (presenter.getItemList().get(position).getPackingStatus()) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }
    }

    interface ItemOnClickListener {
        void onDeleteRowListener(int position);
    }
}
