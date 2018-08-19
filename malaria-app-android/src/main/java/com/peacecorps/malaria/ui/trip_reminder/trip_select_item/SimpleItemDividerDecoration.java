package com.peacecorps.malaria.ui.trip_reminder.trip_select_item;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.peacecorps.malaria.R;

/**
 * Created by Anamika Tripathi on 1/8/18.
 */
public class SimpleItemDividerDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private Context context;

    SimpleItemDividerDecoration(Context context) {
        this.context = context;
        mDivider = context.getResources().getDrawable(R.drawable.divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        c.save();
        final int leftWithMargin = getMarginInPixel(64);
        int right = parent.getWidth() - getMarginInPixel(8);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(leftWithMargin, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private int getMarginInPixel(int i) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r.getDisplayMetrics());
    }
}
