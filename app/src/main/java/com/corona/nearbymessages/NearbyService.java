package com.corona.nearbymessages;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;

public class NearbyService extends IntentService {

    public static final String TAG = "nearbytest";

    MessageListener mMessageListener = new MessageListener() {
        @Override
        public void onFound(Message message) {
            Log.d(TAG, "Found message: " + new String(message.getContent()));
            Toast.makeText(NearbyService.this, "FF " + new String(message.getContent()), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLost(Message message) {
            Log.d(TAG, "Lost sight of message: " + new String(message.getContent()));
            Toast.makeText(NearbyService.this, "LL " + new String(message.getContent()), Toast.LENGTH_LONG).show();
        }
    };

    public NearbyService() {
        super("NearbyService");
        Log.d(TAG, "service was created");
    }

    public class subcallback extends SubscribeCallback
    {
        public subcallback() {
            super();
        }

        @Override
        public void onExpired() {
            super.onExpired();
            Log.d(TAG, "onExpired called");
        }

    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .setCallback(new subcallback())
                .build();
        Nearby.getMessagesClient(this).subscribe(mMessageListener, options);
        Log.d(TAG, "service was started");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
