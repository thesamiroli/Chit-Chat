package com.thesamiroli.chitchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        userName.setText(usersModels.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return usersModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        CircleImageView userImage;

        public ViewHolder(Context context, View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_single_name);
            userImage = itemView.findViewById(R.id.user_thumb);

        }
    }
}
