package com.example.faceattendancesystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faceattendancesystem.DataHelper.Courses;
import com.example.faceattendancesystem.DataHelper.User;
import com.example.faceattendancesystem.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class userAdapter extends RecyclerView.Adapter<userAdapter.viewHolder>
{
    private ArrayList<User> Items;
    private Context context;

    public userAdapter(ArrayList<User> items, Context context) {
        Items = items;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull userAdapter.viewHolder holder, int position) {
        holder.name.setText("Name: "+Items.get(position).getName());
        holder.email.setText("Email: "+Items.get(position).getEmail());
        holder.contact.setText("Phone: "+Items.get(position).getPhone());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });
    }
    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        public TextView name,email,contact;
        public Button btnRegister;
        public RelativeLayout layout;
        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
           email = itemView.findViewById(R.id.email);
            contact = itemView.findViewById(R.id.phone);
            layout = itemView.findViewById(R.id.linearv);
        }
    }
}
