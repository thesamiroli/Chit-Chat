package com.thesamiroli.chitchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Users extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
