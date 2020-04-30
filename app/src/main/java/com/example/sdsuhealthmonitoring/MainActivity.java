package com.example.sdsuhealthmonitoring;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdsuhealthmonitoring.BMI.SDSUBMIActivity;
import com.example.sdsuhealthmonitoring.MedicineRemainder.MedicinesRemainder;
import com.example.sdsuhealthmonitoring.StepTracker.StepCountHistory;
import com.example.sdsuhealthmonitoring.StepTracker.StepDetector;
import com.example.sdsuhealthmonitoring.StepTracker.StepListener;
import com.example.sdsuhealthmonitoring.WaterRemainder.WaterRemainder;
import com.example.sdsuhealthmonitoring.WorkOut.WorkOutActivity;
import com.example.sdsuhealthmonitoring.WorkOut.WorkOutLog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener, StepListener {

    TextView tv;
    TextView set_day;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    final Calendar myCalendar = Calendar.getInstance();
    private int numSteps;
    private StepDetector simpleStepDetector;
    private int steps=0;
    StepsActivity stepsActivity;
    Intent intent;
    CardView cv;
    StepCountHistory stepCountHistory;
    FirebaseAuth mAuth;
    DatabaseReference mWorkOutRef;
    TextView workOut_view;
    CardView workOut_card;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        Sensor mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        tv=findViewById(R.id.textView11);
        cv=findViewById(R.id.cardView);
        workOut_card=findViewById(R.id.cardView1);
        workOut_view=findViewById(R.id.textView16);

        cv.setVisibility(View.INVISIBLE);
        workOut_card.setVisibility(View.INVISIBLE);
        set_day=findViewById(R.id.editText2);
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser();
        mWorkOutRef = FirebaseDatabase.getInstance().getReference("Workouts");

        stepsActivity=new StepsActivity(this,cv,tv);
        stepsActivity.subscribe();
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_FASTEST);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stepCountHistory=new StepCountHistory(this,cv,tv);
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        set_day.setText(myCalendar.get(Calendar.DAY_OF_MONTH) + " "+ MONTHS[myCalendar.get(Calendar.MONTH)]);

        displayWorkOutdata();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dayOfMonth,monthOfYear,year);
            }
        };

        set_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void updateLabel(int day, int month,int year) {
        //Calendar cal = Calendar.getInstance();
        set_day.setText(day + " "+ MONTHS[month]);

        int dayOfMonth=myCalendar.get(Calendar.DAY_OF_MONTH);

        if(dayOfMonth==Calendar.DAY_OF_MONTH)
        {
            displayTodayData();

        }
        else
            {
                displaySelectedDateData(day,month,year);
            }
    }

    public void displayTodayData()
    {
        displayWorkOutdata();
        displayStepCount();
        //Toast.makeText(getApplicationContext(),"Today's date is selected",Toast.LENGTH_SHORT).show();
    }

    public void displayStepCount()
    {

    }
    public void displayWorkOutdata()
    {
        long totalCalories;
        final String EmailId = mAuth.getCurrentUser().getEmail();
        mWorkOutRef.addValueEventListener(new ValueEventListener() {
            @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren())
                    {
                        WorkOutLog log = snapshot.getValue(WorkOutLog.class);
                        assert log != null;
                        if(Objects.equals(log.EmailId,(EmailId))) {
                            Toast.makeText(getApplicationContext(), log.Exercise +" " +log.Duration,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    public void displaySelectedDateData(int day, int month, int year)
    {


        stepCountHistory.getStepsHistory(myCalendar.getTime(),day,month,year);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bmi) {
            intent=new Intent(getApplicationContext(), SDSUBMIActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_foodCounter) {

        } else if (id == R.id.nav_stepCounter) {

            intent = new Intent(getApplicationContext(),StepCounter.class);
            startActivity(intent);


        } else if (id == R.id.nav_workout) {

            intent=new Intent(getApplicationContext(), WorkOutActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_remainders)
        {
            intent=new Intent(getApplicationContext(), WaterRemainder.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_medicine_remainder)
        {
            intent=new Intent(getApplicationContext(), MedicinesRemainder.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void step(long timeNs) {

        steps=stepsActivity.getStepsHistory();

        if(steps!=0)
        {
            numSteps++;
            steps+=numSteps;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText("Total Step Count: "+ " "+(steps));
                }
            });
        }
        else {
            numSteps++;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText("Total Step Count: "+ " "+(numSteps));
                }
            });
        }
    }
}
