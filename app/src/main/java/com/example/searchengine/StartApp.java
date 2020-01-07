package com.example.searchengine;

import android.app.Application;

import com.parse.Parse;

public class StartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("ce8dfe819ce59c546733b88704e0d0a0c6e2d139")
                .clientKey("977d30e08c31c3a2598bc955e21d5133e58ad7e0")
                .server("http://54.218.77.209:80/parse/")
                .build()
        );

    }
}
