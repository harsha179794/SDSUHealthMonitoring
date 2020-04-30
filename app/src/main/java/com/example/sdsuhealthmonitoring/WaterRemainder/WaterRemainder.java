package com.example.sdsuhealthmonitoring.WaterRemainder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sdsuhealthmonitoring.R;

import java.util.Calendar;

//import static com.example.sdsuhealthmonitoring.NotificationChannel.channel_Id1;

public class WaterRemainder extends AppCompatActivity {

    private TextView remainder_interval;
    private NotificationManagerCompat notificationCompat;
    private SharedPreferences sharedPreferences;
    private int time_mins;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_remainder);

        sharedPreferences=this.getSharedPreferences("myPrefs",MODE_PRIVATE);
        int interval = sharedPreferences.getInt("Interval",-1);

        ToggleButton water_metric = findViewById(R.id.toggleButton);

        water_metric.setTextOff("ml");

        notificationCompat= NotificationManagerCompat.from(this);

        remainder_interval=findViewById(R.id.textView4);
        SeekBar seekBar = findViewById(R.id.seekBar2);
        Button remainder_set = findViewById(R.id.button2);

        if(interval!=-1)
        {
            remainder_interval.setText((interval)+ "mins");
            seekBar.setProgress(interval);
            remainder_set.setText("Update Remainder");
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                int min =1;
                int max=60;
                if(progress<min)
                    seekBar.setProgress(min);
                if(progress>max)
                    seekBar.setProgress(max);
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        if(progress<=1)
                            remainder_interval.setText(progress + "min");
                        else
                            remainder_interval.setText(progress +"mins");
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                time_mins=seekBar.getProgress();
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("Interval",time_mins);
                editor.apply();
            }
        });
        remainder_set.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int currentHourIn24Format=calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);


                calendar.set(Calendar.HOUR_OF_DAY,currentHourIn24Format);

                calendar.set(Calendar.MINUTE,currentMinute);


                //NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(),channel_Id1);

                Intent intent = new Intent(getApplicationContext(), Notification_Receiver.class);
               // intent.putExtra("Remainder1","From Water Activity");

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager =(AlarmManager) getSystemService(ALARM_SERVICE);

                assert alarmManager != null;
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),60*1000,pendingIntent);

                Toast.makeText(getApplicationContext(),"Remainder is Set",Toast.LENGTH_LONG).show();

            }
        });
    }
}
