package com.thesamiroli.chitchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OtherUser extends AppCompatActivity {

    TextView init;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chit Chat");

        String userKey = getIntent().getStringExtra("key");
        init = (TextView) findViewById(R.id.user_name);
        init.setText(userKey);

    }
}
