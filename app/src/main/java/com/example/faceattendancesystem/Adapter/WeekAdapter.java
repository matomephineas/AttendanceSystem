package com.example.faceattendancesystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.faceattendancesystem.R;
import com.example.faceattendancesystem.ViewTimeTable;

import org.jetbrains.annotations.NotNull;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {
     Context context;
     String[] weeksList;
     int[] images;
     public static SharedPreferences sharedPreferences;
     public static String SET_DAY;

    public WeekAdapter(Context context, String[] weeksList, int[] images) {
        this.context = context;
        this.weeksList = weeksList;
        this.images = images;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_day_activity,parent,false);
        sharedPreferences= context.getSharedPreferences("MyDay",Context.MODE_PRIVATE);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position)
    {
        holder.weekday.setText(weeksList[position]);
        holder.imageView.setImageResource(images[position]);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, ViewTimeTable.class);
                sharedPreferences.edit().putString(SET_DAY,weeksList[position]).apply();

                //intent.putExtra("weekday",weeksList[position]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weeksList.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView weekday;
        public ImageView imageView;
        public RelativeLayout layout;
        public ViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            weekday = itemView.findViewById(R.id.tvDay);
            imageView = itemView.findViewById(R.id.dayImg);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
