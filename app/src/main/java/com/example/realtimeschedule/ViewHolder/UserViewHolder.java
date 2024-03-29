package com.example.realtimeschedule.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtimeschedule.Interface.ItemClickListener;
import com.example.realtimeschedule.R;


public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public TextView User_Name, Phones,email, userType;
    public ItemClickListener itemClickListener;
    public ImageView userImage;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        User_Name=itemView.findViewById(R.id.userName);
        Phones=itemView.findViewById(R.id.Phone);
        email=itemView.findViewById(R.id.userEmail);
        userType=itemView.findViewById(R.id.userType);
        userImage= itemView.findViewById(R.id.userImageView);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
