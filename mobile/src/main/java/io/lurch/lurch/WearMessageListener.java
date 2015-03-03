package io.lurch.lurch;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by jens on 15-03-01.
 */
public class WearMessageListener extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        switch (messageEvent.getPath()) {
            case "/runPlugin":
                String pluginId = new String(messageEvent.getData());
                Lurch.runPlugin(pluginId);
                break;
            case "/getPlugins":
                try {
                    String plugins = Lurch.getPlugins();
                    MainActivity.sendMessage("/plugins", plugins);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
