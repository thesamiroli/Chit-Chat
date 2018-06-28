package com.thesamiroli.chitchat;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    //Android
    private Button updateButton;
    private TextInputLayout mDisplayName, mPersonName, mPhone, mBio;
    private ProgressDialog mProgressDialog;

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

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        
            }
        });


    }
}
