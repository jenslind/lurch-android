package io.lurch.lurch;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class StatusActivity extends ActionBarActivity {

    private String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        status = intent.getStringExtra("status");

        TextView textView = (TextView) findViewById(R.id.status_msg);
        switch (status) {
            case "wifi":
                textView.setText("Not connected to WIFI.");
                break;
        }
    }
}
