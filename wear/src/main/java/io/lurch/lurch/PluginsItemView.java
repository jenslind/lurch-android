package io.lurch.lurch;

import android.content.Context;
import android.graphics.Color;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by jens on 15-03-01.
 */
public class PluginsItemView extends FrameLayout implements WearableListView.OnCenterProximityListener {

    final TextView text;

    public PluginsItemView(Context context) {
        super(context);
        View.inflate(context, R.layout.plugin_item, this);
        text = (TextView) findViewById(R.id.text);
    }

    @Override
    public void onCenterPosition(boolean b) {
        text.setTextColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public void onNonCenterPosition(boolean b) {
        text.setTextColor(Color.parseColor("#AAAAAA"));
    }
}
