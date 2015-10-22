package com.idorosh.i_dorosh_appone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Runs load list method
        loadList();

    }

    //Load list method will start the list class activity
    public void loadList(){
        Intent intent = new Intent();
        intent.setClass(this, List.class);
        startActivity(intent);
    }
}
