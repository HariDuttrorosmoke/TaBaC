package com.example.tabacv3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Ringtone ringtone;
    protected EditText target;
    Button start_button;
    int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        textView = findViewById(R.id.textView);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        start_button = findViewById(R.id.button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target = findViewById(R.id.editTextNumber);
                int n = Integer.parseInt(target.getText().toString());
                if(n <= 100 && n!=0)
                {
                    Toast.makeText(MainActivity.this, "Target Set!", Toast.LENGTH_SHORT).show();
                    // Storing data into SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("percentage_shared_preference",MODE_PRIVATE);

                    // Creating an Editor object to edit(write to the file)
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    // Storing the key and its value as the data fetched from edit text
                    myEdit.putInt("percent_entered_by_user",n);

                    // Once the changes have been made,
                    // we need to commit to apply those changes made,
                    // otherwise, it will throw an error
                    myEdit.apply();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Invalid %", Toast.LENGTH_SHORT).show();
                }

            }
        });
        BroadcastReceiver broadcastReceiverBattery =new BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onReceive(Context context, Intent intent) {
                int integerBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
                String percentage = "%";
                textView.setText(Integer.toString(integerBatteryLevel)+percentage);

                /////*****
                // Retrieving the value using its keys the file name
                // must be same in both saving and retrieving the data
                SharedPreferences sh = getSharedPreferences("percentage_shared_preference", MODE_APPEND);

                // The value will be default as empty string because for
                // the very first time when the app is opened, there is nothing to show
                int a = sh.getInt("percent_entered_by_user", 0);
                if(integerBatteryLevel == a)
                {
                    ringtone.play();
                    SharedPreferences sharedPreferences = getSharedPreferences("percentage_shared_preference",MODE_PRIVATE);

                    // Creating an Editor object to edit(write to the file)
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    // Storing the key and its value as the data fetched from edit text
                    myEdit.putInt("percent_entered_by_user",1);

                    // Once the changes have been made,
                    // we need to commit to apply those changes made,
                    // otherwise, it will throw an error
                    myEdit.apply();
                }
            }
        };
        registerReceiver(broadcastReceiverBattery,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public void stopButton(View view)
    {
        ringtone.stop();
        Toast.makeText(this, "Process stopped.", Toast.LENGTH_SHORT).show();
    }



}