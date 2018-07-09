/*
* This class's and retrieving Friends List is almost same as retrieving Users List.
* The difference is that, in Users Class we directly start from "Users" node, which makes
* it easier for us to get the values of the UID, but here, we start from "Friends" node in
* Firebase Database which does not contain any information about the user like pname, dname,
* etc. It only contains the date.
* Hence, we should take the child of UID, store it and retrieve its information
* from the "Users" node.
*
*
* */

package com.thesamiroli.chitchat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>{

    DatabaseReference keyReference;

    List<FriendsModel> friendsModels;
    String profileName;

    public FriendsAdapter(List<FriendsModel> friendsModels) {
        this.friendsModels = friendsModels;
    }

    @NonNull
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.users_single_layout, parent, false);

        return new FriendsAdapter.ViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendsAdapter.ViewHolder holder, final int position) {

        final TextView userName = holder.userName;
        final CircleImageView userImage = holder.userImage;
        final View mView = holder.mView;
        final Context context = holder.context;

        //Gets the key of the user that has been clicked
        final String userKey = friendsModels.get(position).getKey();
        Log.d("------", userKey);

        //Setting reference to the user that is the friend of currently logged in user
        keyReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userKey);
        keyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            //Adding the information of each of the friends of currently logged in user
            friendsModels.get(position).setPname(dataSnapshot.child("pname").getValue().toString());
            friendsModels.get(position).setThumb_image(dataSnapshot.child("thumb_image").getValue().toString());
            Log.d("---------", dataSnapshot.child("pname").getValue().toString());

                TextView userName = holder.userName;
                CircleImageView userImage = holder.userImage;
                final View mView = holder.mView;
                final Context context = holder.context;

                userName.setText(friendsModels.get(position).getPname());
                String thumbImage = friendsModels.get(position).getThumb_image();
                Log.d("-----thumb ", " "+thumbImage);

                if(!thumbImage.equals("default"))
                    Picasso.get().load(thumbImage).placeholder(R.drawable.thesamir).into(userImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       // if(!thumbImage.equals("default"))
       //     Picasso.get().load(thumbImage).placeholder(R.drawable.thesamir).into(userImage);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherProfile = new Intent(context, OtherUser.class);
                otherProfile.putExtra("key", userKey);
                otherProfile.putExtra("name", friendsModels.get(position).getPname());
                context.startActivity(otherProfile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        CircleImageView userImage;
        View mView;
        Context context;

        public ViewHolder(Context context, View itemView) {
            super(itemView);

            mView =itemView;
            this.context = context;
            userName = itemView.findViewById(R.id.user_single_name);
            userImage = itemView.findViewById(R.id.user_thumb);


        }
    }

}
