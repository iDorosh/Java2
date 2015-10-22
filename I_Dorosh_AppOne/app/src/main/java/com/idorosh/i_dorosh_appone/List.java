package com.idorosh.i_dorosh_appone;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    //Requests for getting result intents
    public static final int addRequest = 1;
    public static final int deleteRequest = 2;

    //Array list will hold the information from the Cursor
    ArrayList<Info> carsInfo = new ArrayList<>();

    //Global variables for the list view, text view, adapter databasehelper, sqlite db and cursor
    ListView listView;
    TextView listEmpty;
    ArrayAdapter adapter;
    SQLiteDatabase sqLiteDB;
    DatabaseHelper dbHelper;
    Cursor cursor;

    int currentIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //Get the cursor from the Database helper
        getCursor();
        //Load the list view
        loadList();
    }

    public void getCursor(){
        //Getting query from content resolver and setting cursor to the values;
        ContentResolver cr = getContentResolver();
        cursor = cr.query(ContentProvider.CONTENT_URI,null,null,null,null);

        //Getting individual strings from cursor and placing them into the array list.
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

        //Setting viability of the list empty label
        listEmpty = (TextView) findViewById(R.id.listEmpty);

        if (!(cursor.moveToFirst()) ||cursor.getCount() ==0){
            listEmpty.setVisibility(View.VISIBLE);
        }else{
            listEmpty.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Starting Add class activity
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this, Add.class);
            intent.putExtra("carsInfo" , carsInfo);
            startActivityForResult(intent, addRequest);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //On activity result will run based which request code is used
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Adding the new array list to the list class carsInfo then adding the array list to the adapter
        if (resultCode == RESULT_OK && requestCode == addRequest) {
            carsInfo = ((ArrayList<Info>) data.getSerializableExtra("carsInfo"));
            adapter.add(carsInfo);
            setVisiblity();
        }else if (resultCode == RESULT_OK && requestCode == deleteRequest) {
            //Remove Item from the adapter
            currentIndex = data.getExtras().getInt("currentIndex");
            adapter.remove(adapter.getItem(currentIndex));
            setVisiblity();
        }

    }

    //Changing visibility of no items label
    public void setVisiblity(){
        System.out.println(carsInfo.size());
        if (carsInfo.isEmpty())
        {
            listEmpty.setVisibility(View.VISIBLE);
        }else{
            listEmpty.setVisibility(View.INVISIBLE);
        }
    }

    //Creating List view and adapter then setting the adapter
    public void loadList() {
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<Info>(this, android.R.layout.simple_list_item_1, android.R.id.text1, carsInfo) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);

                //Setting the text in the list view to the corresponding names and numbers.
                text1.setText(carsInfo.get(position).getmMake().toString());
                return view;
            }

        };

        listView.setAdapter(adapter);

        //On click listener to pass data to details class
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                passData(position);
            }
        });

    }

    public void passData(int index){
        Intent intent = new Intent();
        intent.setClass(this, Details.class);
        intent.putExtra("carMake", carsInfo.get(index).getmMake().toString());
        intent.putExtra("carModel", carsInfo.get(index).getmModel().toString());
        intent.putExtra("carYear", carsInfo.get(index).getmYear());
        intent.putExtra("carPrice", carsInfo.get(index).getmPrice().toString());
        intent.putExtra("carsInfo" , carsInfo);
        intent.putExtra("currentIndex", index);
        startActivityForResult(intent, deleteRequest);
    }
}
