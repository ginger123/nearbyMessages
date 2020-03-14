package com.corona.nearbymessages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

public class BeaconBroadcast extends BroadcastReceiver {

    public static final String TAG = MainActivity.TAG;
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "got broadcast intent");
        // an Intent broadcast.
        Nearby.
        Nearby.getMessagesClient(context).handleIntent(intent, new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.i(TAG, "Found message via PendingIntent: " + message);
                Toast.makeText(context, "FF " + new String(message.getContent()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLost(Message message) {
                Log.i(TAG, "Lost message via PendingIntent: " + message);
                Toast.makeText(context, "LL " + new String(message.getContent()), Toast.LENGTH_LONG).show();

            }
        });

    }
}
