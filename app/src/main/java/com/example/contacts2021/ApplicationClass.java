package com.example.contacts2021;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class ApplicationClass extends Application {
    public static final String APPLICATION_ID = "B39D3D29-0058-DDA9-FF0F-7EB6945B0900";
    public static final String API_KEY = "04DB6ED2-21A7-4C2A-8A9C-8BEACDA05DD0";
    public static final String SERVER_URL = "https://api.backendless.com";
    public  static List<Contact> contacts; // initialised and called in contact list to store the list in order to get the position while user touches in the list.
    public  static BackendlessUser user; // Initialised and called in login activity

    //public static List<Contact> contactts;
    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.initApp( getApplicationContext(), APPLICATION_ID, API_KEY );
        Backendless.setUrl( SERVER_URL );
    }
}
