package com.idorosh.i_dorosh_appone;

import android.app.AlertDialog;
import android.content.*;
import android.content.ContentProvider;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Details extends AppCompatActivity {

    //String Variables to hold intent extras from list class
    String carMake;
    String carModel;
    int carYear;
    String carPrice;
    SQLiteDatabase sqLiteDB;
    int currentIndex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //Getting extras from intents and placing them in their corresponding variables
        currentIndex = getIntent().getExtras().getInt("currentIndex");
        carMake = getIntent().getExtras().getString("carMake");
        carModel = getIntent().getExtras().getString("carModel");
        carYear = getIntent().getExtras().getInt("carYear");
        carPrice = getIntent().getExtras().getString("carPrice");

        //Back Button for the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        displayItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Checking ID of clicked action item
        if (id == R.id.action_mode_close_button) {
            removeCar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Populate text views in the display screen
    public void displayItems(){
        TextView detailMakeText = (TextView)findViewById(R.id.detailMakeText);
        detailMakeText.setText(carMake);

        TextView detailModelText = (TextView)findViewById(R.id.detailModelText);
        detailModelText.setText(carModel);


        TextView detailYearText = (TextView)findViewById(R.id.detailYearText);
        detailYearText.setText(String.valueOf(carYear));

        TextView detailPriceText = (TextView)findViewById(R.id.detailPriceText);
        detailPriceText.setText(carPrice);
    }

    //Remove Selected Car
    private void removeCar(){
        //Alert Dialog to verify that the user wants to delete the item
        new AlertDialog.Builder(this)
                .setTitle("Remove Posting")
                .setMessage("Confirm listing of " +carYear+" "+carMake+" "+carModel+" for "+carPrice)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String selection =  "model=? AND make=? AND price=?";
                        String[] selection_argument = {carModel,carMake,carPrice};

                        //Getting Content resolver and running delete method
                        ContentResolver cr = getContentResolver();
                        cr.delete(com.idorosh.i_dorosh_appone.ContentProvider.CONTENT_URI, selection, selection_argument);

                        //Toast verifying deletion.
                        Toast.makeText(getBaseContext(), "Car Listing Removed", Toast.LENGTH_SHORT).show();
                        deleteCar();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    //Result will return the current index back to list view to remove it from the adapter
    private void deleteCar(){
        Intent returnIntent = new Intent();
        returnIntent.setClass(this, MainActivity.class);
        returnIntent.putExtra("currentIndex", currentIndex);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
