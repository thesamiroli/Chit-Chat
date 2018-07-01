package com.thesamiroli.chitchat;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class ChitChat extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Using Firebase's offline capability (It can sync everything as string)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //Using Picasso's offline capability
        //Firebase's offline gets image as String, so we would be needing Picasso
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);


    }
}
