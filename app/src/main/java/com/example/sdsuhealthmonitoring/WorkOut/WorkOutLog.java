package com.example.sdsuhealthmonitoring.WorkOut;

import java.sql.Timestamp;
import java.util.Date;

public class WorkOutLog {

        public String Name;
        public String EmailId;
        public String Exercise;
        public int Duration;
        public String CaloriesBurn;
        public Date WorkOutDate;

        WorkOutLog(String name, String emailId, String exercise, int duration, String caloriesBurn,Date date)
        {
            Name=name;
            EmailId=emailId;
            Exercise=exercise;
            Duration=duration;
            CaloriesBurn=caloriesBurn;
            WorkOutDate=date;

        }
        public WorkOutLog()
        {

        }






}
