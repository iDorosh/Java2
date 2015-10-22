package com.idorosh.i_dorosh_appone;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

//Content Provider
public class ContentProvider extends android.content.ContentProvider {

    //Authority and base path
    private static final String AUTHORITY = "com.idorosh.i_dorosh_appone.contentprovider";
    private static final String BASE_PATH = "dealerCars";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);


    private static final int CARS = 1;
    private static final int CARS_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, CARS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH+ "/#", CARS_ID);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {

        //Setting dbHelper and DB
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Doing a DB query
        return db.query("dealerCars", DatabaseHelper.ALL_COLUMNS, selection, null,null,null, null);

    }


    //All other methods not needed
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long id = db.insert(DatabaseHelper.Table_DealerCars, null, values);
        return Uri.parse(BASE_PATH+ "/" + id);
    }
    

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return db.delete(DatabaseHelper.Table_DealerCars, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
