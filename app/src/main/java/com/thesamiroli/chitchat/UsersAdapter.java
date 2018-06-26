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

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//Nothing new here, if you get confused , you need to understand RecyclerView properly.
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    List<UsersModel> usersModels;

    public UsersAdapter(List<UsersModel> usersModels) {
        this.usersModels = usersModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.users_single_layout, parent, false);

        return new ViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView userName = holder.userName;
        CircleImageView userImage = holder.userImage;
        final View mView = holder.mView;
        final Context context = holder.context;

        userName.setText(usersModels.get(position).getName());
        String thumbImage = usersModels.get(position).getThumb_image();

        if(!thumbImage.equals("default"))
            Picasso.get().load(thumbImage).placeholder(R.drawable.thesamir).into(userImage);

        //Gets the key of the user that has been clicked
        final String userKey = usersModels.get(position).getKey();
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherProfile = new Intent(context, OtherUser.class);
                otherProfile.putExtra("key", userKey);
                context.startActivity(otherProfile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersModels.size();
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
