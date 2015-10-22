package com.idorosh.i_dorosh_apptwo;

import android.content.ContentProviderClient;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class List extends AppCompatActivity {

    //Array list to hold all the objects that are coming from the cursor
    ArrayList<Info> carsInfo = new ArrayList<>();

    //Global list and text views also the adapter and cursor
    ListView listView;
    TextView listEmpty;
    ArrayAdapter adapter;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //getting cursor from db
        getCursor();
        //Load list
        loadList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //refresh list on resume
        getCursor();
        adapter.notifyDataSetChanged();
    }

    //Getting Cursor method
    public void getCursor(){
        //Clears out current list
        carsInfo.clear();

        //Parsing URI
        Uri uri = Uri.parse("content://com.idorosh.i_dorosh_appone.contentprovider/dealerCars");
        ContentProviderClient data = getContentResolver().acquireContentProviderClient(uri);
        try {
            //Setting Cursor
            cursor = data.query(uri, null, null, null, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        //Getting individual data from cursor object and setting to list array
        if(cursor.moveToFirst()){
            do {
                String make;
                String model;
                int year;
                String price;
                make = cursor.getString(0);
                model = cursor.getString(1);
                year = cursor.getInt(2);
                price = cursor.getString(3);
                carsInfo.add (new Info(make,model,year,price));
            }while (cursor.moveToNext());
        }

        //Label for list is empty
        listEmpty = (TextView) findViewById(R.id.listEmpty);

        //Sets label to visible or invisible
        if (!(cursor.moveToFirst()) ||cursor.getCount() ==0){
            listEmpty.setVisibility(View.VISIBLE);
        }else{
            listEmpty.setVisibility(View.INVISIBLE);
        }
    }

    public void loadList() {
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<Info>(this, android.R.layout.simple_list_item_1, android.R.id.text1, carsInfo) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);

                //Setting the text in the listview to the corresponding makes.
                text1.setText(carsInfo.get(position).getmMake().toString());

                return view;
            }

        };
        //Setting adapter
        listView.setAdapter(adapter);

        //Running pass data method once item is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                passData(position);
            }
        });

    }

    //Creating intent and putting extras into the intent then starting Details activity
    public void passData(int index){
        Intent intent = new Intent();
        intent.setClass(this, Details.class);
        intent.putExtra("carMake", carsInfo.get(index).getmMake().toString());
        intent.putExtra("carModel", carsInfo.get(index).getmModel().toString());
        intent.putExtra("carYear", carsInfo.get(index).getmYear());
        intent.putExtra("carPrice", carsInfo.get(index).getmPrice().toString());
        intent.putExtra("carsInfo" , carsInfo);
        intent.putExtra("currentIndex", index);
        startActivity(intent);
    }
}
