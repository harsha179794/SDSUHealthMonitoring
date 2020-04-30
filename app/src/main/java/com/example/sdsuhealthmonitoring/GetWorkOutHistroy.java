package com.example.sdsuhealthmonitoring;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.sdsuhealthmonitoring.WorkOut.WorkOutLog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetWorkOutHistroy {

    private String EmailId;
    private FirebaseAuth mAuth;
    private final DatabaseReference mWorkOutRef;
    private final Context context;

    public GetWorkOutHistroy(Context cont)
    {
        context=cont;
        mAuth.getCurrentUser();
        mWorkOutRef = FirebaseDatabase.getInstance().getReference("Workouts");
    }
    public void getWorkOutSegregateData()
    {

    }

    public void getWorkoutAggregateData()
    {

        EmailId = mAuth.getCurrentUser().getEmail();
        mWorkOutRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    WorkOutLog log = snapshot.getValue(WorkOutLog.class);
                    assert log != null;
                    if(log.EmailId.equals(EmailId)) {
                        Toast.makeText(context,log.Exercise,Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
