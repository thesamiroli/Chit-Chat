package com.thesamiroli.chitchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class Users extends AppCompatActivity {

    FirebaseDatabase database ;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    List<UsersModel> usersList;

    UsersAdapter usersAdapter;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All Users");
        usersList =  new ArrayList<UsersModel>();

        //Firebase initialization
        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid().toString();

        loadUsers();

        //Just RecyclerView's stuff
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.users_recycler_list);
        usersAdapter = new UsersAdapter(usersList);
        mRecyclerView.setAdapter(usersAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    //This method takes data from Firebase Database and sends the data to UsersModel's constructor
    private void loadUsers() {
        mDatabaseReference.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                UsersModel usersModel = dataSnapshot.getValue(UsersModel.class);
                usersList.add(usersModel);
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}

