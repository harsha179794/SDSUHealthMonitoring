package com.example.sdsuhealthmonitoring.BMI;

public class UsersBMI {

    private double user_height;

    private double user_weight;

    private double user_bmi;

    public void Set_Height(double height)
    {
        user_height = height;

    }

    public double Get_Height()
    {
        return user_height;
    }

    public void Set_Weight(double weight)
    {
        user_weight=weight;
    }

    public double Get_Weight()
    {
        return user_weight;
    }

    public  double Get_BMI()
    {
        return user_bmi;
    }

    public  void Set_BMI(double bmi)
    {
        user_bmi =bmi;
    }
}
