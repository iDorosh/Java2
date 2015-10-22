package com.idorosh.i_dorosh_appone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Add extends AppCompatActivity {

    //Array list to hold the current objects before a new one is added
    ArrayList<Info> carsInfo = new ArrayList<>();

    //Context DB Helper and SQLite DB
    Context context = this;
    DatabaseHelper dbHelper;
    SQLiteDatabase sqLiteDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //Getting array list from intent
        carsInfo = (ArrayList<Info>) getIntent().getSerializableExtra("carsInfo");

        //Back button for the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Running save car method once save button in action bar is clicked.
        if (id == R.id.action_add) {
            saveCar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCar(){

        //Text fields in add view.
        final EditText makeText = (EditText) findViewById(R.id.carMake);
        final EditText modelText = (EditText) findViewById(R.id.carModel);
        final EditText yearText = (EditText) findViewById(R.id.carYear);
        final EditText priceText = (EditText) findViewById(R.id.carPrice);

            //Alert diolog to verify that the user wished to add the new infomration.
            new AlertDialog.Builder(context)
                    .setTitle("Add Posting")
                    .setMessage("Confirm listing of " + yearText.getText().toString() + " " + makeText.getText().toString() + " " + modelText.getText().toString() + " for " + priceText.getText().toString())
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            //Adding new info into arraylist
                            carsInfo.add(new Info(makeText.getText().toString(), modelText.getText().toString(), yearText.getText().toString(), priceText.getText().toString()));

                            //Adding information into the data base
                            dbHelper = new DatabaseHelper(context);
                            sqLiteDb = dbHelper.getWritableDatabase();
                            dbHelper.addInformation(makeText.getText().toString(), modelText.getText().toString(), yearText.getText().toString(), priceText.getText().toString(), sqLiteDb);
                            //Toast for car listed
                            Toast.makeText(getBaseContext(), "Car Listed", Toast.LENGTH_SHORT).show();
                            dbHelper.close();

                            //Runs Add car Method
                            addCar();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();
    }

    //Result that send the new array list back to the List class
    private void addCar(){
        Intent returnIntent = new Intent();
        returnIntent.setClass(this, MainActivity.class);
        returnIntent.putExtra("carsInfo", carsInfo);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
