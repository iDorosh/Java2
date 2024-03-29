package com.idorosh.i_dorosh_appone;

import java.io.Serializable;


public class Info implements Serializable {
        //Variables to hold information that was added by the user
        private String mMake;
        private String mModel;
        private int mYear;
        private String mPrice;

        //Setting variables using the parameters
        public Info(String make, String model, int year, String price){
            mMake = make;
            mModel = model;
            mYear = year;
            mPrice = price;
        }

    //Returning variables
        public String getmMake(){
            return mMake;
        }

        public String getmModel(){
            return mModel;
        }

        public int getmYear(){
            return mYear;
        }

        public String getmPrice(){return mPrice;}
}

