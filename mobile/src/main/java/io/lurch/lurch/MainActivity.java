package io.lurch.lurch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static GoogleApiClient googleApiClient;
    private static Node deviceNode;

    private Button sendButton;
    private Button connectButton;
    String plugins;

    private EditText host;
    private EditText token;

    public static String LURCH_HOST = "";
    public static String LURCH_TOKEN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect to google play services
        googleApiInit();

        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (!wifi()) {
            // Show status activity
            Intent intent = new Intent(MainActivity.this, StatusActivity.class);
            intent.putExtra("status", "wifi");
            startActivity(intent);
        }
        else {
            host = (EditText) findViewById(R.id.host);
            token = (EditText) findViewById(R.id.token);

            SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("io.lurch.lurch", Context.MODE_PRIVATE);
            host.setText(sharedPref.getString("io.lurch.lurch.host", ""));
            token.setText(sharedPref.getString("io.lurch.lurch.token", ""));

            LURCH_HOST = sharedPref.getString("io.lurch.lurch.host", "");
            LURCH_TOKEN = sharedPref.getString("io.lurch.lurch.token", "");
        }

        connectButton = (Button) findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("io.lurch.lurch", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("io.lurch.lurch.host", host.getText().toString());
                editor.putString("io.lurch.lurch.token", token.getText().toString());
                editor.apply();

                LURCH_HOST = host.getText().toString();
                LURCH_TOKEN = token.getText().toString();
            }
        });
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

    public static void sendMessage(final String path, final String text) {
        if (deviceNode != null && googleApiClient != null && googleApiClient.isConnected()) {
            Wearable.MessageApi.sendMessage(googleApiClient, deviceNode.getId(), path, text.getBytes()).setResultCallback(

                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {

                            if (!sendMessageResult.getStatus().isSuccess()) {
                                Log.d("Fail", "Failed to send message");
                            } else {
                                Log.d("Success", "SEND MESSAGE");
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

    @Override
    public void onConnected(Bundle bundle) {
        resolveNode();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Log.d("Connection suspened", ""+connectionResult.getErrorCode());
    }

    private boolean wifi() {
        // Check if device is connected to wifi
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mWifi.isConnected()) {
            return false;
        }

        return true;
    }
}
