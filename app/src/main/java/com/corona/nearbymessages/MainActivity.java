package com.corona.nearbymessages;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private MessageListener mMessageListener;
    public static final String TAG = "nearbytest";
    private Message mMessage;

    private EditText id_edit_text;
    private Button publish_button;
    private CheckBox bluetooth_chckbox;
    private CheckBox wifi_chckbox;
    private CheckBox mic_chckbox;
    private Button clear_history_button;
    private ScrollView history_list;

    private int index;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        index = 0;

        id_edit_text = findViewById(R.id.id_text);
        publish_button = findViewById(R.id.publish_button);
        bluetooth_chckbox = findViewById(R.id.bluetooth_check_box);
        wifi_chckbox = findViewById(R.id.wifi_check_box);
        mic_chckbox = findViewById(R.id.mic_check_box);
        clear_history_button = findViewById(R.id.clear_history_button);
        history_list = findViewById(R.id.textAreaScroller);

        publish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringContent = "nearby " + id_edit_text.getText();
                byte[] content = stringContent.getBytes();
                mMessage = new Message(content);

                Nearby.getMessagesClient(MainActivity.this).publish(mMessage);
                Log.d(TAG, "publish button was pushed ");

            }
        });

        clear_history_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LinearLayout ll = findViewById(R.id.text_container);s
                ll.removeAllViews();
            }
        } );

        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                String text_to_print = "FF " + new String(message.getContent());

                Log.d(TAG, "Found message: " + new String(message.getContent()));
                Toast.makeText(MainActivity.this, text_to_print, Toast.LENGTH_LONG).show();

                LinearLayout ll = findViewById(R.id.text_container);
                TextView tv1 = new TextView(MainActivity.this);
                tv1.setText(text_to_print);

                ll.addView(tv1);
            }

            @Override
            public void onLost(Message message) {
                String text_to_print = "LL " + new String(message.getContent());

                Log.d(TAG, "Lost sight of message: " + new String(message.getContent()));
                Toast.makeText(MainActivity.this, text_to_print, Toast.LENGTH_LONG).show();

                LinearLayout ll = findViewById(R.id.text_container);
                TextView tv1 = new TextView(MainActivity.this);
                tv1.setText(text_to_print);

                ll.addView(tv1);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
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

