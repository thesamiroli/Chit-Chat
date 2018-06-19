package com.thesamiroli.chitchat;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    //Android
    private Button registerButton, loginButton;
    private TextInputLayout mEmail, mPassword;
    private ProgressDialog mProgressDialog;

    //Firevase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mEmail = (TextInputLayout) findViewById(R.id.start_email_textInput);
        mPassword = (TextInputLayout) findViewById(R.id.start_password_textInput);
        registerButton = (Button) findViewById(R.id.start_register_button);
        loginButton = (Button) findViewById(R.id.start_login_button);

        mAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getEditText().getText().toString().trim();
                String password = mPassword.getEditText().getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()){

                    Toast.makeText(StartActivity.this,
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    mProgressDialog.setTitle("Signing in User");
                    mProgressDialog.setMessage("Please wait while we sign you in");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    loginUser(email, password);
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mProgressDialog.dismiss();
                           Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
                           startActivity(mainIntent);
                           finish();
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            mProgressDialog.hide();
                            Toast.makeText(StartActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }
}
