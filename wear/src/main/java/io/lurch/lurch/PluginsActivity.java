package io.lurch.lurch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.widget.TextView;

import com.google.android.gms.wearable.Node;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PluginsActivity extends Activity implements WearableListView.ClickListener {

    private ArrayList<String> pluginItems = new ArrayList<String>();
    private WearableListView pluginsList;
    private PluginsAdapter pluginsAdapter = new PluginsAdapter(PluginsActivity.this, pluginItems);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugins);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                // Set the list adapter here
                pluginsList = (WearableListView) findViewById(R.id.pluginsListView);
                pluginsList.setAdapter(pluginsAdapter);
                pluginsList.setClickListener(PluginsActivity.this);
            }
        });
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        // Get clicked plugin
        try {
            JSONObject plugin = new JSONObject(pluginItems.get(viewHolder.getPosition()));
            String pluginId = plugin.getString("_id");

            // Send message to phone
            //sendMessage("/runPlugin", pluginId);

            // Show a success message
            showSuccess("Action sent");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTopEmptyRegionClick() {

    }

    private void showSuccess(String msg) {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
        startActivity(intent);
    }
}
