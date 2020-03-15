package com.corona.nearbymessages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        id_edit_text = findViewById(R.id.id_text);
        publish_button = findViewById(R.id.publish_button);
        clear_history_button = findViewById(R.id.clear_history_button);

        bluetooth_chckbox = findViewById(R.id.bluetooth_check_box);
        wifi_chckbox = findViewById(R.id.wifi_check_box);
        mic_chckbox = findViewById(R.id.mic_check_box);


        mMessage = new Message("nothing".getBytes());

        publish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringContent = "nearby " + id_edit_text.getText();
                byte[] content = stringContent.getBytes();
                mMessage = new Message(content);

                Nearby.getMessagesClient(MainActivity.this).publish(mMessage);
                Log.d(TAG, "publish button was pushed ");
                Toast.makeText(MainActivity.this, "published " + id_edit_text.getText(), Toast.LENGTH_LONG).show();
            }
        });

        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                String text_to_print = "found device id " + new String(message.getContent());

                Log.d(TAG, "Found message: " + new String(message.getContent()));
                Toast.makeText(MainActivity.this, text_to_print, Toast.LENGTH_LONG).show();

                write_to_history(text_to_print);
            }

            @Override
            public void onLost(Message message) {
                String text_to_print = "lost device id " + new String(message.getContent());

                Log.d(TAG, "Lost sight of message: " + new String(message.getContent()));
                Toast.makeText(MainActivity.this, text_to_print, Toast.LENGTH_LONG).show();

                write_to_history(text_to_print);
            }
        };


        clear_history_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LinearLayout ll = findViewById(R.id.text_container);
                ll.removeAllViews();
            }
        } );
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

    public void write_to_history(String log){
        LinearLayout ll = findViewById(R.id.text_container);
        TextView tv1 = new TextView(this);
        tv1.setText(log);

        ll.addView(tv1);
    }
}

