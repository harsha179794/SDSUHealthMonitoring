package com.example.sdsuhealthmonitoring.WorkOut;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdsuhealthmonitoring.GetWorkOutHistroy;
import com.example.sdsuhealthmonitoring.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

public class WorkOutActivity extends AppCompatActivity {

    EditText workout_name;
    SeekBar duration;
    EditText calories;
    Button save;
    GoogleSignInAccount acc;
    FirebaseAuth mAuth;
    DatabaseReference mWorkOutRef;
    TextView mins;
    int totalmins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out);

        workout_name = findViewById(R.id.editText3);
        duration = findViewById(R.id.duration);
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser();
        mWorkOutRef = FirebaseDatabase.getInstance().getReference("Workouts");
        mins = findViewById(R.id.textView15);
        calories = findViewById(R.id.editText4);
        save = findViewById(R.id.button4);


        duration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {

                final int min = 1;
                int max = 60;
                if (progress < min)
                    duration.setProgress(min);
                if (progress > max)
                    duration.setProgress(max);
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        if (progress <= 1)
                            mins.setText((progress) + "min");
                        else
                            mins.setText((progress) + "mins");

                    }
                });
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                totalmins = seekBar.getProgress();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUserActivity();

            }
        });
    }

    public void uploadUserActivity()
    {
//Toast.makeText(getApplicationContext(),String.valueOf(DateFormat.format("EEEE", date)),Toast.LENGTH_SHORT).show();

        Date date = new Date();

        WorkOutLog log = new WorkOutLog(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName(),mAuth.getCurrentUser().getEmail(),
                workout_name.getText().toString(),totalmins, calories.getText().toString(), date);
        String UploadId= mWorkOutRef.push().getKey();

        if(UploadId!=null){
        mWorkOutRef.child(UploadId).setValue(log);
        Toast.makeText(getApplicationContext(),"Uploaded Successfully",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Uploaded Unsuccess",Toast.LENGTH_SHORT).show();
        }
        //getWorkoutAggregateData();
    }
    public void getWorkoutAggregateData()
    {
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
}

