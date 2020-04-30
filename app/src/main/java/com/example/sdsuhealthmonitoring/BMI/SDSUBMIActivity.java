package com.example.sdsuhealthmonitoring.BMI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdsuhealthmonitoring.R;


public class SDSUBMIActivity extends AppCompatActivity {

    private EditText mUser_Weight;
    private Spinner weight_dropdown;
    private Spinner inches_dropdown;
    private Button  mCalcBMI;
    private Spinner height_dropdown;
    private TextView bmi_textView;


    UsersBMI usersBMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdsubmi);

        mUser_Weight=findViewById(R.id.Edit_Weight);
        height_dropdown=findViewById(R.id.spinner2);
        inches_dropdown=findViewById(R.id.spinner3);
        weight_dropdown=findViewById(R.id.weight_spinner);
        bmi_textView=findViewById(R.id.textView7);

        usersBMI=new UsersBMI();

        mCalcBMI=findViewById(R.id.BMI_Calculator);




        mCalcBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double bmi;
                double height= Double.parseDouble(height_dropdown.getSelectedItem().toString());
                double inches=Double.parseDouble(inches_dropdown.getSelectedItem().toString());
                inches=inches/100;
                usersBMI.Set_Height(height+inches);
                //Log.d("Users Height",String.valueOf(height+inches));

                if(!mUser_Weight.getText().toString().equals(""))
                {
                    usersBMI.Set_Weight(Double.parseDouble(mUser_Weight.getText().toString()));
                    if(weight_dropdown.getSelectedItem().toString().equals("Kg"))
                    {
                        bmi = (usersBMI.Get_Height() * 0.3048);
                        bmi*=bmi;
                        bmi=(Double.parseDouble(mUser_Weight.getText().toString()))/bmi;
                        Log.d("BMI in KG",String.valueOf(bmi));
                    }
                    else
                        {
                            bmi = (usersBMI.Get_Height() * 0.30);
                            bmi*=bmi;
                            double weight=Double.parseDouble(mUser_Weight.getText().toString());
                            weight=weight/0.453;
                            bmi=weight/(bmi);
                            Log.d("BMI in Lbs",String.valueOf(bmi));
                        }
                }
                else
                    {
                        Toast.makeText(getApplicationContext(), "Please enter your weight", Toast.LENGTH_SHORT).show();
                        return;
                    }

                   if(bmi<18.5)
                   {
                       bmi_textView.setText("Your Body Mass Indes is: "+ bmi +". This is considered UnderWeight");
                   }
                   else if(bmi>=18.5 && bmi<=24.9)
                   {
                       bmi_textView.setText("Your Body Mass Indes is: "+ bmi +". This is considered Normal");
                   }
                   else if(bmi>24.9 && bmi <=29.9)
                   {
                       bmi_textView.setText("Your Body Mass Indes is: "+ bmi +". This is considered OverWeight");
                   }

                   else if(bmi>29.9 && bmi<34.9)
                   {
                       bmi_textView.setText("Your Body Mass Indes is: "+ bmi +". This is considered Obesity-Class1");
                   }
                   else if(bmi>34.9 && bmi<=39.9)
                   {
                       bmi_textView.setText("Your Body Mass Indes is: "+ bmi +". This is considered Obesity-Class2");
                   }
                   else
                       {
                           bmi_textView.setText("Your Body Mass Indes is: "+ bmi +". This is considered extreme Obesity-Class3");
                       }
            }
        });

    }


}
