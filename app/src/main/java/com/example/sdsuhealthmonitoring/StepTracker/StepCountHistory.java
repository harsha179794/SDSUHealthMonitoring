package com.example.sdsuhealthmonitoring.StepTracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

public class StepCountHistory {
    private final Context context;
    private final CardView cardView;
    private Date selectedDate;
    private static final String TAG = "StepCounter";
    private int endDay;
    private int endmonth;
    private int endYear;

    public StepCountHistory(Context cont,CardView cv,TextView tv)
    {
        context=cont;
        cardView=cv;
        TextView textView = tv;

    }

    public void getStepsHistory(Date date, int day, int month,int year)
    {
        selectedDate=date;
        endDay=day;
        endmonth=month;
        endYear=year;
        readStepsData();
    }

    public Task<DataReadResponse> readStepsData()
    {
        DataReadRequest readRequest = queryFitnessData();

        return Fitness.getHistoryClient(context, GoogleSignIn.getLastSignedInAccount(context))
                .readData(readRequest)
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                printData(dataReadResponse);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "There was a problem reading the data.", e);
                            }
                        });
    }

    private DataReadRequest queryFitnessData()  {

        Calendar cal = Calendar.getInstance();


        cal.setTime(selectedDate);
        cal.set(Calendar.MILLISECOND,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.HOUR_OF_DAY,0);

        Toast.makeText(context,"Start Date:" + cal.getTime(),Toast.LENGTH_SHORT).show();

        long startTime = cal.getTimeInMillis();
        //cal.setTime(selectedDate);
        endDay+=1;
        //String strDate = (endmonth) + "/"+ (endDay) +"/" + (endYear);



        Toast.makeText(context,String.valueOf(cal.get(Calendar.DAY_OF_MONTH)),Toast.LENGTH_SHORT).show();

        //cal.add(Calendar.DATE,1);

        cal.set(Calendar.MILLISECOND,0);
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,59);
        Toast.makeText(context,String.valueOf(cal.getTime()),Toast.LENGTH_SHORT).show();
        long endTime = cal.getTimeInMillis();
        java.text.DateFormat dateFormat = getDateInstance();
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        return new DataReadRequest.Builder()

                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

    }
    private void printData(final DataReadResponse dataReadResult) {

        if (dataReadResult.getBuckets().size() > 0) {

            Toast.makeText(context, "Number of Returned Buckets: " + (dataReadResult.getBuckets().size()), Toast.LENGTH_LONG).show();

            Log.i(
                    TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {

                List<DataSet> dataSets = bucket.getDataSets();
                if(dataSets.size()!=0) {
                    for (DataSet dataSet : dataSets) {
                        dumpDataSet(dataSet);
                    }
                }
                else
                    {
                        Toast.makeText(context,"No step records are found sorry",Toast.LENGTH_SHORT).show();
                    }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
    }
    private  void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();



        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                Toast.makeText(context,"Field :" + field.getName() + " Value: " +dp.getValue(field),Toast.LENGTH_SHORT ).show();
            }
        }
    }

}
