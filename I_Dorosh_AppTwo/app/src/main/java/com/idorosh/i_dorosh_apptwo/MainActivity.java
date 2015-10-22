package com.idorosh.i_dorosh_apptwo;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Runs loadList method
        loadList();

    }

    public void loadList(){
        //Opening List Activity
        Intent intent = new Intent();
        intent.setClass(this, List.class);
        startActivity(intent);
    }
}
