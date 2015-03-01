package io.lurch.lurch;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

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
        }
    }

}
