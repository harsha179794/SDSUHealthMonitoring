package com.example.sdsuhealthmonitoring;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "SDSUFitness";
    private static final String USERS_TABLE = "USERS_TABLE";



    private static final String WORKOUT_DATA = "WORKOUT_TABLE";



    public DataBaseHelper(Context context,  String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);



    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
