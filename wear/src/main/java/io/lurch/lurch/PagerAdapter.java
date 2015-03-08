package io.lurch.lurch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jens on 15-03-06.
 */
public class PagerAdapter extends GridPagerAdapter {

    private Context context;

    public PagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        return 2;
    }

    @Override
    public int getCurrentColumnForRow(int row, int currentColumn) {
        return currentColumn;
    }

    @Override
    protected Object instantiateItem(ViewGroup viewGroup, int i, int i2) {
        if (i2 == 1) {
            viewGroup.addView(MainActivity.pluginsView);
            return MainActivity.pluginsView;
        } else {
            TextView textView = new TextView(context);
            textView.setText("Empty page");
            viewGroup.addView(textView);
            textView.setGravity(Gravity.CENTER);
            return textView;
        }
    }

    @Override
    protected void destroyItem(ViewGroup viewGroup, int i, int i2, Object o) {
        viewGroup.removeView((View) o);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view.equals(o);
    }
}
