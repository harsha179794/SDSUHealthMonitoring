package com.example.sdsuhealthmonitoring.MedicineRemainder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sdsuhealthmonitoring.R;

import java.util.Calendar;

public class MedicinesRemainder extends AppCompatActivity {

    private EditText set_time;
    private TimePicker tp;
    TimePickerDialog picker;
    private Button set_remainder;
    private NotificationManagerCompat notificationCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicines_remainder);

        notificationCompat= NotificationManagerCompat.from(this);

       // medicine_name=findViewById(R.id.editText2);
        //tp=findViewById(R.id.timePicker1);

        set_time=findViewById(R.id.setTime);

        set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                picker = new TimePickerDialog(MedicinesRemainder.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                set_time.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();

            }
        });
        set_remainder=findViewById(R.id.button3);
        set_remainder.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("ShortAlarm")
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                tp.getCurrentHour();

                Intent intent = new Intent(getApplicationContext(), Medicine_Notification_Receiver.class);
                //intent.putExtra("Remainder1","From Water Activity");

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager =(AlarmManager) getSystemService(ALARM_SERVICE);

                assert alarmManager != null;
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),10000 , pendingIntent);

                Toast.makeText(getApplicationContext(), "Remainder is set", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
