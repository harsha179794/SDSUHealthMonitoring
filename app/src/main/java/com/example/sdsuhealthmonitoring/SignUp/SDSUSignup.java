package com.example.sdsuhealthmonitoring.SignUp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sdsuhealthmonitoring.MainActivity;
import com.example.sdsuhealthmonitoring.R;
import com.example.sdsuhealthmonitoring.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SDSUSignup extends AppCompatActivity {

    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    public static final String TAG = "StepCounter";

    FirebaseAuth mAuth;
    EditText emailId;
    EditText userName;
    DatabaseReference dbRef;
    int steps;
    protected GoogleSignInClient mGoogleSignin;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdsusignup);

        Button mSignIn = findViewById(R.id.button);
        Button mSignUp=findViewById(R.id.button5);
        mAuth = FirebaseAuth.getInstance();
        dbRef= FirebaseDatabase.getInstance().getReference("/users");
        emailId = findViewById(R.id.editText6);
        userName = findViewById(R.id.editText5);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((emailId.getText() != null || emailId.getText().toString().equals("")) || (userName.getText() != null || !userName.getText().toString().equals("")))) {
                   FirebaseSignup();
                }

                else{
                    Toast.makeText(getApplicationContext(),"Please enter both Name and EmailId to register",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((emailId.getText() != null || !emailId.getText().toString().equals("")) && (userName.getText() != null || !userName.getText().toString().equals(""))) {


                    FirebaseSignIn();
                    getPermission();
                }
            }
        });
    }
    public void getPermission() {
        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .build();
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    REQUEST_OAUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        }
        else
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("EmailId", emailId.getText().toString());
                intent.putExtra("UserName", userName.getText().toString());
                startActivity(intent);
        }
    }

    public void FirebaseSignIn() {
        mAuth.signInWithEmailAndPassword(emailId.getText().toString(), userName.getText().toString()).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "User Email :" + user.getEmail() + "User Name: "+ user.getDisplayName() , Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
                Toast.makeText(getApplicationContext(), "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("EmailId", emailId.getText().toString());
                intent.putExtra("UserName", userName.getText().toString());
                startActivity(intent);
            }
        }
    }

    public void FirebaseSignup() {
        mAuth.createUserWithEmailAndPassword(emailId.getText().toString(), userName.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUserser = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "SignedUp for FireDatabase", Toast.LENGTH_LONG).show();
                            Users user = new Users(
                                    emailId.getText().toString(),
                                    userName.getText().toString());
                            dbRef.child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "User added successfully", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

