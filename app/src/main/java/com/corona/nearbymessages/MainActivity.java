package com.corona.nearbymessages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "nearbytest";
    private Message mMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Random r = new Random();
        int randInt = r.nextInt(2048);
        TextView tv = findViewById(R.id.id_view);
        tv.setText("" + randInt);
        String stringContent = "nearby" + randInt;
        byte[] content = stringContent.getBytes();
        mMessage = new Message(content);

//        Intent cbIntent =  new Intent();
//        cbIntent.setClass(this, NearbyService.class);
//        startService(cbIntent);
        backgroundSubscribe();
    }

    @Override
    public void onStart() {
        super.onStart();
        Button b = findViewById(R.id.button_publish);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nearby.getMessagesClient(MainActivity.this).publish(mMessage);
                Log.d(TAG, "publish button was pushed ");

            }
        });

        Button bun = findViewById(R.id.button_unpublish);
        bun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nearby.getMessagesClient(MainActivity.this).unpublish(mMessage);
                Log.d(TAG,"unpublishing");
            }
        });
}

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        Nearby.getMessagesClient(this).unpublish(mMessage);
        super.onStop();
    }


    // Subscribe to messages in the background.
    private void backgroundSubscribe() {
        Log.i(TAG, "Subscribing for background updates.");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build();
        Nearby.getMessagesClient(this).subscribe(getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        return PendingIntent.getBroadcast(this, 0, new Intent(this, BeaconBroadcast.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
