package com.example.sdsuhealthmonitoring;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;




public class StepsActivity {

    private final Context context;
    CardView cardView;
    TextView textView;

int steps;
    public StepsActivity(Context cont, CardView cv,TextView tv)
    {
        context=cont;
        this.cardView=cv;
        this.textView=tv;
       // tv=findViewById()
       // this.cardView.setVisibility(View.INVISIBLE);
    }
    protected void subscribe() {

        Fitness.getRecordingClient(context, GoogleSignIn.getLastSignedInAccount(context))
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                   // Log.i(TAG, "Successfully subscribed!");
                                  //  Toast.makeText(context,"The recording of steps is success",Toast.LENGTH_LONG ).show();
                                    readData();
                                } else {
                                   // Log.w(TAG, "There was a problem subscribing.", task.getException());
                                }
                            }
                        });
    }
    private void readData() {
        Fitness.getHistoryClient(context, GoogleSignIn.getLastSignedInAccount(context))
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                long total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                               // Log.i(TAG, "Total steps: " + total);
                                //Toast.makeText(context,"Total Steps: " + (total) ,Toast.LENGTH_SHORT).show();
                                steps=(int)total;
                               getStepsHistory();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                               // Log.w(TAG, "There was a problem getting the step count.", e);
                            }
                        });

    }
    public int getStepsHistory()
    {
        cardView.setVisibility(View.VISIBLE);
        textView.setText("Total Steps Count: "+" "+ steps);
        return steps;

    }
}
