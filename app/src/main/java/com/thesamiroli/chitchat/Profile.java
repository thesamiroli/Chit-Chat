package com.thesamiroli.chitchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    //Firebase
    private DatabaseReference mReference;
    private FirebaseUser mFirebaseUser;
    private StorageReference mStorageRef;

    //Android
    private TextView profileName;
    private CircleImageView profileImage;
    private Button changeImage;
    private ProgressDialog mProgressDialog;

    //Info
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        changeImage = (Button) findViewById(R.id.profile_changeImage_button);
        profileName = (TextView) findViewById(R.id.profile_name);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Getting the reference of Firebase Storage
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Getting the UID of current user and storing it
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = mFirebaseUser.getUid();

        //Pointing the Firebase Database Reference to the UID of current user
        mReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        //To get the data from Firebase Database
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayName = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String thumbnail = dataSnapshot.child("thumb_image").getValue().toString();

                profileName.setText(displayName);

                //http://square.github.io/picasso/ Displaying profile picture
                Picasso.get().load(image).into(profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://github.com/ArthurHub/Android-Image-Cropper
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(Profile.this);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) { //If the file is selected and cropped

                mProgressDialog = new ProgressDialog(Profile.this);
                mProgressDialog.setTitle("Uploading Image");
                mProgressDialog.setMessage("Please wait while we upload your image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri imageUri = result.getUri();

                /* Getting the reference of Firebase Storage and
                creating a directory and file inside that */
                StorageReference filePath = mStorageRef.child("profile_images")
                        .child(userID+".jpg");

                //Uploading the image to Firebase Storage
                filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){ //If uploading to Firebase Storage is successful

                            //Get a download URL from Firebase Storage
                            String downloadUrl = task.getResult().getDownloadUrl().toString();

                            //Put the URL to Firebase Database inside "image" field of current user
                            mReference.child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    mProgressDialog.dismiss();
                                    Toast.makeText(Profile.this, "Image uploaded",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                            Toast.makeText(Profile.this, "Error in uploading",
                                    Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }
}
