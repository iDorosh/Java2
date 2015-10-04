package com.idorosh.dorosh_fundementals;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable {

    //Progress bar for showing that the data is being downloaded
    ProgressBar pb;

    //Array that stores the data from the Info custom class
    ArrayList<Info> Info = new ArrayList<>();
    String jsonString;

    //Sets default tag when the app first loads
    String searchFieldText = "the last ship";

    //SharedPreferences to detect if its the first time that the app is running.
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting the fragment as the view
        setContentView(R.layout.fragment_layout);

        //Setting preference when app runs
        prefs = getSharedPreferences("com.idorosh.dorosh_fundementals", MODE_PRIVATE);

        //Sets the progress bar to invisible when the app loads
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);

        //Checking network on run
        if(!checkNetwork()){
            Toast.makeText(MainActivity.this, "Network isn't Available", Toast.LENGTH_SHORT).show();
        }

        //Sets search button variable based on orientation
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            listItem(searchFieldText);
            View myButton = findViewById(R.id.searchButton);
            myButton.setOnClickListener(searchShow);
        }

        //Was planning on using this to get api data on all string in my created xml array.
        /*String[] tvShows = getResources().getStringArray(R.array.shows);
        for(int i =0; i < tvShows.length; i++)
        {
            listItem(tvShows[i]);
        }
        */
    }

    //Displays alert dialog if its the first run.
    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            if(!checkNetwork()){
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("First Run")
                        .setMessage("Please Connect to network first")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
            }
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }



    //Searches shows based on text that the user put onto the text field. Or displays toast that shows text is empty if the search button is clicked
    private View.OnClickListener searchShow = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            EditText searchText = (EditText) findViewById(R.id.searchBar);
            if (searchText.getText().toString().trim().length() != 0) {
                searchFieldText = (searchText.getText().toString());
                listItem(searchFieldText);
                searchText.setText("");
                //Hiding keyboard after search clicked
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                //gets current focus and hides it.
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

            } else {
                Toast.makeText(MainActivity.this, "Show empty", Toast.LENGTH_SHORT).show();
            }
        }
    };




    //Setting information based on the item clicked in list view
    public void listItem(String details){
        searchFieldText = details;

        if (checkNetwork())
        {
            //Requests data and passes through the search field text
            requestData(searchFieldText);
        } else {
            Toast.makeText(MainActivity.this, "Network isn't Available", Toast.LENGTH_SHORT).show();
            URL url = null;
            if(prefs.edit().putBoolean("firstrun", false).commit())
            {
                new GetData().execute(url);
                Info.clear();
            }

        }
    }



    //Creating a new file to save api data
    public void createFile() throws IOException {
        String text = jsonString;

        FileOutputStream fos = openFileOutput("apiInfo.txt", MODE_PRIVATE);
        fos.write(text.getBytes());
        fos.close();
        System.out.println("File written to disk");
    }



    //Method to read data from created file
    public String readFile() throws IOException {

        FileInputStream fis = openFileInput("apiInfo.txt");
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuffer b = new StringBuffer();
        while (bis.available() != 0) {
            char c = (char) bis.read();
            b.append(c);
        }
        bis.close();
        fis.close();
        return b.toString();
    }




    //Checking for network and returning true or false.
    public boolean checkNetwork()
    {

        ConnectivityManager mgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = mgr.getActiveNetworkInfo();

        if(netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }





    //Requesting data with a url that excepts either a selected show from the list or text that was requested by the user
    private void requestData(String tagText) {
        //Clears Info array
        Info.clear();
        //Url that excepts tag from tagText
        String finalShow =  tagText.replace(' ', '+');
        String urlString = "http://www.omdbapi.com/?t="+finalShow+"&y=&plot=full&r=json";
        URL url = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new GetData().execute(url);
    }




    //Getting data with an Async Task
    private class GetData extends AsyncTask<URL, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            //Starting Progress bar loading
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject apiData;

            //Was trying to get the app to save all shows data instead of just one shows data.
            //JSONObject apiDataTest;
            //String allShows = "";

            /*if (firstRun) {
                if (checkNetwork()) {
                    jsonString = "";

                    //Connects to url and saves the json information to json strings
                    for (URL queryURL : urls) {
                        try {
                            URLConnection connection = queryURL.openConnection();
                            jsonString = IOUtils.toString(connection.getInputStream());

                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                }

                if (ShowsArray.contains(jsonString)) {

                } else {
                    ShowsArray.add(jsonString);
                    allShows = ShowsArray.toString().substring(1, ShowsArray.toString().length() - 1);
                }


                //Sets apiData JSONObject using the jsonString
                try {
                    apiDataTest = new JSONObject(allShows);
                } catch (JSONException e) {
                    e.printStackTrace();
                    apiDataTest = null;
                }
                System.out.println(apiDataTest.toString());
                firstRun = false;

            }
            */

                //Getting data and saving it as a JSON object
                if (checkNetwork()) {
                    jsonString = "";

                    //Connects to url and saves the json information to json strings
                    for (URL queryURL : urls) {
                        try {
                            URLConnection connection = queryURL.openConnection();
                            jsonString = IOUtils.toString(connection.getInputStream());

                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        jsonString = readFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    createFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //Sets apiData JSONObject using the jsonString
                try {
                    apiData = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    apiData = null;
                }

            //Returning data to onPostExecute
            return apiData;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            //Setting data to an Array List using a custom class
            try {
                Info.add(new Info(jsonObject.getString("Title"), jsonObject.getString("Released"), jsonObject.getString("Genre"), jsonObject.getString("Plot")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Setting text method
            DisplayItems(Info);

            //Hiding progress bar
            pb.setVisibility(View.INVISIBLE);
        }
    }





    //Method to update text views
    public void DisplayItems(ArrayList<com.idorosh.dorosh_fundementals.Info> info){

        TextView text1 = (TextView) findViewById(R.id.title);
        text1.setText(info.get(0).getmTitle().toString());

        TextView text2 = (TextView) findViewById(R.id.genre);
        text2.setText(info.get(0).getmGenre().toString());

        TextView text3 = (TextView) findViewById(R.id.year);
        text3.setText(info.get(0).getmYear().toString());

        TextView text4 = (TextView) findViewById(R.id.plot);
        text4.setText(info.get(0).getmPlot().toString());

    }

}
