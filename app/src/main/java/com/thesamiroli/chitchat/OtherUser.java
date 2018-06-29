package com.thesamiroli.chitchat;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUser extends AppCompatActivity {

    Button sendRequest, cancelRequest;
    TextView mDName, mPName, mEmail, mBio, mGender;
    CircleImageView userImage;

    DatabaseReference mDatabaseReference;

    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chit Chat");

        String userId = getIntent().getStringExtra("key");
        String name = getIntent().getStringExtra("name");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userId);

        sendRequest = (Button) findViewById(R.id.send_friend_request);
        cancelRequest = (Button) findViewById(R.id.cancel_request);
        mDName = (TextView) findViewById(R.id.other_user_dName);
        mPName = (TextView)findViewById(R.id.other_user_pName);
        mEmail = (TextView) findViewById(R.id.other_user_email);
        mBio = (TextView) findViewById(R.id.other_user_bio);
        mGender = (TextView) findViewById(R.id.other_user_gender);
        userImage = (CircleImageView) findViewById(R.id.other_user_image);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please wait while loading " +name+"'s data");
        mProgress.setTitle("Loading");
        mProgress.setCanceledOnTouchOutside(false);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String dName = dataSnapshot.child("dname").getValue().toString();
                String pName = dataSnapshot.child("pname").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String bio = dataSnapshot.child("bio").getValue().toString();
                String gender = dataSnapshot.child("gender").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                mDName.setText("@" +dName);
                mPName.setText(pName);
                mEmail.setText(email);
                mBio.setText(bio);
                mGender.setText(gender);

                Picasso.get().load(image).placeholder(R.drawable.thesamir).into(userImage);

                mProgress.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
