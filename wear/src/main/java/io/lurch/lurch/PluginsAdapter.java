package io.lurch.lurch;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    public PluginsAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearableListView.ViewHolder(new TextView(context));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        JSONObject plugin = null;
        try {
            plugin = new JSONObject(items.get(position));
            textView.setText(plugin.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
