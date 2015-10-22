package com.idorosh.i_dorosh_appone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper{
    //Creating Data bae
    private static final String DATABASE_FILE = "database.db";
    private static final int DATABASE_VERSION = 10;
    //Create Table String
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS dealerCars (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, make TEXT, model TEXT, year TEXT, price TEXT)";

    //All Columns Array
    public static final String[] ALL_COLUMNS = {"make", "model", "year", "price"};


    public DatabaseHelper(Context c) {
        super(c, DATABASE_FILE, null, DATABASE_VERSION);
    }
    @Override
    //Creating Table
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    //Adding information into database using parameters
    public void addInformation(String model, String make, String year, String price, SQLiteDatabase db){
        ContentValues v = new ContentValues();
        v.put("make", model);
        v.put("model", make);
        v.put("year", year);
        v.put("price", price);
        db.insert("dealerCars", null, v);
    }

    //Getting information from the db and creating a cursor
    public Cursor getInformation(SQLiteDatabase db){
        Cursor cursor;
        cursor = db.query("dealerCars", ALL_COLUMNS, null,null,null,null,null,null);
        return cursor;
    }

    //Deleting information from the DB that matches all the parameters
    public void deleteInformation(String model, String make, String year, String price, SQLiteDatabase sqLiteDatabase){
        String selection =  "model=? AND make=? AND year=? AND price=?";
        String[] selection_argument = {model,make,year,price};
        sqLiteDatabase.delete("dealerCars", selection, selection_argument);
    }

    //On Upgrade not needed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
