package com.example.juwelierbehrendt;


import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

public class StartApplication extends Application
{
    public static final String APPLICATION_ID = "4BF690EB-CCB1-3331-FF23-647DD2DFA100";
    public static final String API_KEY = "AB897D9A-8E6C-4026-A45B-80DF607645C2";
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
}
