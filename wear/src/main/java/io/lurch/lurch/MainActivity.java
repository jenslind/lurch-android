package io.lurch.lurch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends Activity implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, WearableListView.ClickListener {

    private GoogleApiClient googleApiClient;
    private ArrayList<String> pluginItems = new ArrayList<String>();
    private WearableListView pluginsList;
    private Node deviceNode;
    private PluginsAdapter pluginsAdapter = new PluginsAdapter(MainActivity.this, pluginItems);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                // Set the list adapter here
                pluginsList = (WearableListView) findViewById(R.id.pluginsListView);
                pluginsList.setAdapter(pluginsAdapter);
                pluginsList.setClickListener(MainActivity.this);

            }
        });

        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        googleApiInit();
    }

    private void googleApiInit() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("SOMETHINK WRONk HERE?", "Connected :DDDDDDD");

        Wearable.MessageApi.addListener(googleApiClient, this);

        // Resolve node
        resolveNode();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Connection suspened", ""+connectionResult.getErrorCode());
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("MSG RECEIVED", "A new msg was received");

        pluginItems.clear();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(new String(messageEvent.getData()));
            for (int i = 0; i < jsonArray.length(); i++) {
                pluginItems.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("Adapter", "ADapter change ?!?!?!");
                pluginsAdapter.notifyDataSetChanged();
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
            sendMessage("/runPlugin", pluginId);

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

    private void sendMessage(final String path, final String text) {
        Log.v("Node", deviceNode.toString());
        if (deviceNode != null && googleApiClient != null && googleApiClient.isConnected()) {
            Wearable.MessageApi.sendMessage(googleApiClient, deviceNode.getId(), path, text.getBytes()).setResultCallback(

                new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {

                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.d("Fail", "Failed to send message");
                        }
                        else {
                            Log.d("YAY", "WTF?!?!??!?!");
                        }
                    }
                }

            );
        }
    }

    private void resolveNode() {
        Wearable.NodeApi.getConnectedNodes(googleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult nodes) {
            for (Node node : nodes.getNodes()) {
                deviceNode = node;
            }
            }
        });
    }
}
