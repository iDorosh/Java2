package com.idorosh.i_dorosh_apptwo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import java.util.ArrayList;

public class Details extends AppCompatActivity {

    //Variables to hold strings coming from the inten
    String carMake;
    String carModel;
    String carYear;
    String carPrice;

    //Int to hold the current index of the selected item
    int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Getting extras from intents
        currentIndex = getIntent().getExtras().getInt("currentIndex");
        carMake = getIntent().getExtras().getString("carMake");
        carModel = getIntent().getExtras().getString("carModel");
        carYear = getIntent().getExtras().getString("carYear");
        carPrice = getIntent().getExtras().getString("carPrice");

        //Back button in the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Display items
        displayItems();
    }


    //Setting all the text labels in the display screen
    public void displayItems(){
        TextView detailMakeText = (TextView)findViewById(R.id.detailMakeText);
        detailMakeText.setText(carMake);

        TextView detailModelText = (TextView)findViewById(R.id.detailModelText);
        detailModelText.setText(carModel);

        TextView detailYearText = (TextView)findViewById(R.id.detailYearText);
        detailYearText.setText(carYear);

        TextView detailPriceText = (TextView)findViewById(R.id.detailPriceText);
        detailPriceText.setText(carPrice);
    }
}

