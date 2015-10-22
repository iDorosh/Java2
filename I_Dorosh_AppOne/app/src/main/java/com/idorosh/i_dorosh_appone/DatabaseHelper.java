package com.idorosh.i_dorosh_appone;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper{
    //Creating Data bae
    private static final String DATABASE_FILE = "database.db";
    private static final int DATABASE_VERSION = 10;
    //Create Table String
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS dealerCars (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, make TEXT, model TEXT, year INTERGER, price TEXT)";

    //All Columns Array
    public static final String[] ALL_COLUMNS = {"make", "model", "year", "price"};

    public static final String Table_DealerCars = "dealerCars";

    public DatabaseHelper(Context c) {
        super(c, DATABASE_FILE, null, DATABASE_VERSION);
    }

    @Override
    //Creating Table
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }
    //On Upgrade not needed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
