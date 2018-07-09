package com.thesamiroli.chitchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Friends extends AppCompatActivity {
    private DatabaseReference friendsReference;
    private FirebaseAuth mFirebaseAuth;

    String userID;
    List<FriendsModel> friendsList;
    FriendsAdapter friendsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        mFirebaseAuth = FirebaseAuth.getInstance();
        userID = mFirebaseAuth.getCurrentUser().getUid();

        friendsReference = FirebaseDatabase.getInstance().getReference()
                .child("Friends").child(userID);

        friendsList =  new ArrayList<FriendsModel>();

        loadFriends();

        RecyclerView friendsRecyclerView = (RecyclerView) findViewById(R.id.friends_recycler_view);
        friendsAdapter = new FriendsAdapter(friendsList);
        friendsRecyclerView.setAdapter(friendsAdapter);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void loadFriends() {

        friendsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                FriendsModel friendsModel = dataSnapshot.getValue(FriendsModel.class);
                friendsModel.setKey(dataSnapshot.getKey()); //Key is required on onClickListener
                friendsList.add(friendsModel);
                friendsAdapter.notifyDataSetChanged();
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
