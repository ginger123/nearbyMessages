package com.corona.nearbymessages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private MessageListener mMessageListener;
    public static final String TAG = "nearbytest";
    private Message mMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Random r = new Random();
        String stringContent = "nearby" + r.nextInt();
        byte[] content = stringContent.getBytes();
        mMessage = new Message(content);
        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.d(TAG, "Found message: " + new String(message.getContent()));
                Toast.makeText(MainActivity.this, "FF " + new String(message.getContent()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLost(Message message) {
                Log.d(TAG, "Lost sight of message: " + new String(message.getContent()));
                Toast.makeText(MainActivity.this, "LL " + new String(message.getContent()), Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nearby.getMessagesClient(MainActivity.this).publish(mMessage);
                Log.d(TAG, "publish button was pushed ");

            }
        });
        Nearby.getMessagesClient(this).subscribe(mMessageListener);
}

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        Nearby.getMessagesClient(this).unpublish(mMessage);
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener);
        super.onStop();
    }
}
