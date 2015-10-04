package com.idorosh.dorosh_fundementals;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class DetailsActivity extends Activity {


    //Info array to hold api data
    ArrayList<Info> Info = new ArrayList<>();

    //Shows Array was suppose to hold all tvshows from the api
    ArrayList<String> ShowsArray = new ArrayList<>();
    String jsonString;

    //Default selection
    String searchFieldText = "the last ship";

    //Dialog variable for the detail activity which will shows up when loading the api information
    ProgressDialog pDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting detail activity xml file as current view
        setContentView(R.layout.a_item);

        //Minimize keyboard on start of the app
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //If the orientation is landscape then the detail fragment will display in the activities place
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        //Getting array from strings xml
        String[] tvShows = getResources().getStringArray(R.array.shows);

        //Intent that is passed from titles fragment that holds the index of the selected row in the list view.
        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("index", 0);

        //Starts the process of checking network and then getting the api data
        listItem(tvShows[intValue]);

        //Runs search method when the search button is hit.
        View myButton = findViewById(R.id.searchShowsButton);
        myButton.setOnClickListener(searchShow);
    }





    //Onclick listener to search for a show that has been put into a text field by a user.
    private View.OnClickListener searchShow = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            EditText searchText = (EditText) findViewById(R.id.searchShows);
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
                //Will display a toast when the text field is empty
                Toast.makeText(DetailsActivity.this, "Show empty", Toast.LENGTH_SHORT).show();
            }
        }
    };





    //Will display data based on the name of the show
    public void listItem(String details){
        searchFieldText = details;

        //Checks network
        if (checkNetwork())
        {
            requestData();
        } else {
            //
            Toast.makeText(DetailsActivity.this, "Network isn't Available", Toast.LENGTH_SHORT).show();
            URL url = null;

            new GetData().execute(url);
            Info.clear();

        }
    }




    //Creating a new file to store information that is gathered from the api
    public void createFile() throws IOException {
        String text = jsonString;

        FileOutputStream fos = openFileOutput("apiInfo.txt", MODE_PRIVATE);
        fos.write(text.getBytes());
        fos.close();

        System.out.println("File written to disk");
    }



    //Reading file that stores api data
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




    //Checking network and returning a true or false.
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





    //Requesting data based on a string in the search field text
    private void requestData() {
        //Clears Info array
        Info.clear();
        //Url that excepts tag from tagText
        String finalShow =  searchFieldText.replace(' ', '+');
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
            //Starting a dialog to show that the show is being loaded
            pDialog = new ProgressDialog(DetailsActivity.this);
            pDialog.setMessage("Loading Show....");
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject apiData;
            String allShows = "";

            //Checking network again
            if (checkNetwork()) {
                //Stores data from the api
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
                    //Reads file if there is not network
                    jsonString = readFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                //creates file if there is network
                createFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (ShowsArray.contains(jsonString)) {

            } else {
                ShowsArray.add(jsonString);
                allShows = ShowsArray.toString().substring(1, ShowsArray.toString().length() - 1);
            }


            //Sets apiData JSONObject using the jsonString
            try {
                apiData = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
                apiData = null;
            }


            return apiData;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            //Getting jsonObject and setting that information into the custom classes.
            try {
                Info.add(new Info(jsonObject.getString("Title"), jsonObject.getString("Actors"), jsonObject.getString("Genre"), jsonObject.getString("Plot")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                DisplayItems(Info);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }



    //Method to update text views
    public void DisplayItems(ArrayList<Info> info) throws InterruptedException {

        TextView text1 = (TextView) findViewById(R.id.detailTitle);
        text1.setText(info.get(0).getmTitle().toString());

        TextView text2 = (TextView) findViewById(R.id.detailGenre);
        text2.setText(info.get(0).getmGenre().toString());

        TextView text3 = (TextView) findViewById(R.id.detailYear);
        text3.setText(info.get(0).getmYear().toString());

        TextView text4 = (TextView) findViewById(R.id.detailPlot);
        text4.setText(info.get(0).getmPlot().toString());

        //Timer to make sure that the text views are updated
        Thread.sleep(500);
        //Dismissing dialog
        pDialog.dismiss();

    }
}
