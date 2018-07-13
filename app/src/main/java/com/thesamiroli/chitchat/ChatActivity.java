package com.thesamiroli.chitchat;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    private String userKey;
    DatabaseReference rootReference;
    String pName, thumb, presence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        userKey = getIntent().getStringExtra("key");
        pName = getIntent().getStringExtra("name");
        thumb = getIntent().getStringExtra("thumb");
        presence = getIntent().getStringExtra("presence");

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle(pName);
        actionBar.setDisplayShowCustomEnabled(true);

        Log.d("---", pName+ thumb+ presence);




        }
    }

