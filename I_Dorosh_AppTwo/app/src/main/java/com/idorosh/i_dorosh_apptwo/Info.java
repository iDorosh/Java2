package com.idorosh.i_dorosh_apptwo;

import java.io.Serializable;


public class Info implements Serializable {
    //variables for the data that is created by the cursor
    private String mMake;
    private String mModel;
    private String mYear;
    private String mPrice;

    //Setting variables to the data that's being passed in.
    public Info(String make, String model, String year, String price){
        mMake = make;
        mModel = model;
        mYear = year;
        mPrice = price;
    }

    //Returning variables to list activity
    public String getmMake(){return mMake;
    }

    public String getmModel(){
        return mModel;
    }

    public String getmYear(){
        return mYear;
    }

    public String getmPrice(){return mPrice;}
}
