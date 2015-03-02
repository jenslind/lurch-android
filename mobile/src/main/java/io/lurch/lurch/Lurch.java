package io.lurch.lurch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by jens on 15-03-01.
 */
public class Lurch {

    private static SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContextOfApplication());
    private static String host = sharedPref.getString("io.lurch.lurch.HOST", "");
    private static String token = sharedPref.getString("io.lurch.lurch.TOKEN", "");
    private static int port = 1994;
    private static String username = "lurch";

    private static String lurchUrl = "http://" + host + ":" + port;
    private static String lurchAuth = username + ":" + token;

    public static String getPlugins() throws IOException, URISyntaxException, JSONException {

        BufferedReader in = null;
        String data = null;

        try {
            HttpClient client = new DefaultHttpClient();
            URI uri = new URI(lurchUrl + "/plugins/get/");
            HttpGet request = new HttpGet();
            request.setURI(uri);
            final String basicAuth = "Basic " + Base64.encodeToString(lurchAuth.getBytes(), Base64.NO_WRAP);
            request.setHeader("Authorization", basicAuth);
            HttpResponse response = client.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";

            while((line = in.readLine()) != null) {
                sb.append(line);
            }

            in.close();

            data = sb.toString();

            return data;


        } finally {
            if (in != null) {
                try {
                    in.close();
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void runPlugin(String id) {
        try {
            HttpClient client = new DefaultHttpClient();
            URI uri = new URI(lurchUrl + "/plugin/run/" + id);
            HttpPost request = new HttpPost();
            request.setURI(uri);
            final String basicAuth = "Basic " + Base64.encodeToString("lurch:04ed252949e3".getBytes(), Base64.NO_WRAP);
            request.setHeader("Authorization", basicAuth);
            client.execute(request);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
