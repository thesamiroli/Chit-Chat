package com.thesamiroli.chitchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //Android
    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mRegisterButton;
    protected String mGender;
    Spinner genderSelection;
    private  ProgressDialog mProgressDialog;


    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();


        mDisplayName = (TextInputLayout) findViewById(R.id.register_displayName_textInput);
        mEmail = (TextInputLayout) findViewById(R.id.register_email_textInput);
        mPassword = (TextInputLayout) findViewById(R.id.register_password_textInput);
        mRegisterButton = (Button) findViewById(R.id.register_register_button);

        mProgressDialog = new ProgressDialog(this);

        genderSelection = (Spinner) findViewById(R.id.gender_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        genderSelection.setAdapter(adapter);

        //Listener for spinner
        genderSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                mGender = "Male";

            }
        });

        //Listener for the button
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String displayName = mDisplayName.getEditText().getText().toString().trim();
                String email = mEmail.getEditText().getText().toString().trim();
                String password = mPassword.getEditText().getText().toString().trim();
                String gender = mGender;
                if (displayName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "One or more fields are emtpy", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser(displayName, email, password, gender);
                    mProgressDialog.setTitle("Registering User");
                    mProgressDialog.setMessage("Please wait while we create your accout");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                }
            }
        });
    }

    private void registerUser(final String displayName, final String email, String password, final String gender){

        //Creating a user with the parameters on Firebase Databse
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //Taking the current User ID and storing it
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String userID = currentUser.getUid();

                            //mReference points to the root directory of the Firebase Database now
                            mReference = FirebaseDatabase.getInstance().getReference();

                            /*Creating a child of root named "Users" and
                            creating another child of "Users" with the current user's UID.
                            mReference now points to the UID of logged in user */
                            mReference = mReference.child("Users").child(userID);

                            //Storing the information of the user into a HashMap
                            HashMap<String, String> userInfo = new HashMap<>();
                            userInfo.put("name", displayName);
                            userInfo.put("email", email);
                            userInfo.put("image", "default");
                            userInfo.put("thumb_image", "default");
                            userInfo.put("gender", gender);

                            //Storing the values of HashMap into the Fireabsae Database
                            mReference.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mProgressDialog.dismiss();
                                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this, "Something is wrong",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });



                        } else {
                            // If registering fails, display a message to the user.
                            mProgressDialog.hide();
                            Toast.makeText(RegisterActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
