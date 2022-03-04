package com.example.juwelierbehrendt.ExternalLibs;


import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

public class StartApplication extends Application
{
    public static final String APPLICATION_ID = "A4D5BCB8-1BF2-225C-FF5E-C1AC3B479500";
    public static final String API_KEY = "1E31FAEF-EFA1-4C03-92DD-9A8B6977F416";
    public static final String SERVER_URL = "https://eu-api.backendless.com";

    public static BackendlessUser user;
    @Override
    public void onCreate() {
        super.onCreate();
       Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
        Backendless.setUrl( SERVER_URL );
    }
    public static EasyNW getEasyNW()
    {
        return new EasyNW("https://eu.backendlessappcontent.com/"+ StartApplication.APPLICATION_ID+"/"+StartApplication.API_KEY+"/files/");
    }
}
