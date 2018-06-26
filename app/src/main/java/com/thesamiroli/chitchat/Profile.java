package com.thesamiroli.chitchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

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
        getSupportActionBar().setTitle("Profile");


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
                if(!image.equals("default"))
                    Picasso.get().load(image).placeholder(R.drawable.thesamir).into(profileImage);

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
                final StorageReference thumbFilePath = mStorageRef.child("thumbs").child(userID+".jpg");

                //Converting the image into Bitmap image (For thumb_image)
                Bitmap thumb_bmp = null;
                File thumb_image = new File(imageUri.getPath());
                try {
                    thumb_bmp = new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(64)
                            .compressToBitmap(thumb_image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();



                //Uploading the profile image to Firebase Storage
                filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){ //If uploading to Firebase Storage is successful

                            //Get a download URL from Firebase Storage
                            final String profileDownloadUrl = task.getResult().getDownloadUrl().toString();

                            //Upload the Thumb Image to Firebase Storage
                            UploadTask uploadTask = thumbFilePath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumbTask) {

                                    /*Put the profile pic's and thumb pic's URL
                                      to Firebase Database inside "image" and "thumb_image" field of current user */
                                    final String thumbDownloadUrl = thumbTask.getResult().getDownloadUrl().toString();
                                    Map imageMap = new HashMap();
                                    imageMap.put("image", profileDownloadUrl);
                                    imageMap.put("thumb_image",thumbDownloadUrl);
                                    if(thumbTask.isSuccessful()){
                                    mReference.updateChildren(imageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                mProgressDialog.dismiss();
                                                Toast.makeText(Profile.this, "Profile picture updated",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                                else{
                                        Toast.makeText(Profile.this, "Error in uploading thumbnail",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                        }
                        else
                            Toast.makeText(Profile.this, "Error in uploading your profile picture",
                                    Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }
}
