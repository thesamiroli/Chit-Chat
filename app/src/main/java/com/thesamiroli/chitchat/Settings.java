package com.thesamiroli.chitchat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {

    //Android
    private Button updateButton;
    private TextInputLayout mDisplayName, mPersonName, mPhone, mBio;
    private ProgressDialog mProgressDialog;

    //Firebase
    FirebaseDatabase database ;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseUser currentUser;

    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Setings");

        updateButton = (Button) findViewById(R.id.settings_save_button);
        mDisplayName = (TextInputLayout) findViewById(R.id.settings_displayName_input);
        mPersonName = (TextInputLayout) findViewById(R.id.settings_name_input);
        mPhone = (TextInputLayout) findViewById(R.id.settings_number_input);
        mBio = (TextInputLayout) findViewById(R.id.settings_bio_input);
        mProgressDialog = new ProgressDialog(this);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid().toString();
        mReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String displayName = mDisplayName.getEditText().getText().toString().trim().toLowerCase();
                String personName = mPersonName.getEditText().getText().toString().trim();
                String phone = mPhone.getEditText().getText().toString().trim();
                String bio = mBio.getEditText().getText().toString().trim();

                //Storing the fields in a HashMap
                Map updatedInfo = new HashMap();

                //Adding the infos into the HashMap only if they are not empty
                if(!displayName.equals("")){
                        updatedInfo.put("dname", displayName);
                }
                if(!personName.equals("")){
                    updatedInfo.put("pname", personName);
                }
                if(!phone.equals("")){
                    updatedInfo.put("phone", phone);
                }
                if(!bio.equals("")){
                    updatedInfo.put("bio", bio);
                }
                mReference.updateChildren(updatedInfo).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Settings.this, "Information updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}
