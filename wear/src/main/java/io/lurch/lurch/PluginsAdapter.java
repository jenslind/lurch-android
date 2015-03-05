package io.lurch.lurch;

import android.content.Context;
import android.graphics.Color;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by jens on 15-02-28.
 */
public class PluginsAdapter extends WearableListView.Adapter {

    private final Context context;
    private final ArrayList<String> items;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    public PluginsAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new WearableListView.ViewHolder(new PluginsItemView(context));
        } else {
            return new WearableListView.ViewHolder(new TextView(context));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        if (position == 0) {
            TextView textView = (TextView) holder.itemView;
            textView.setText("Plugins");
            textView.setTextColor(Color.parseColor("#888888"));
            textView.setTextSize(20);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
        } else {
            PluginsItemView pluginsItemView = (PluginsItemView) holder.itemView;
            TextView textView = (TextView) pluginsItemView.findViewById(R.id.text);
            JSONObject plugin = null;
            try {
                plugin = new JSONObject(items.get(position));
                textView.setText(plugin.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
