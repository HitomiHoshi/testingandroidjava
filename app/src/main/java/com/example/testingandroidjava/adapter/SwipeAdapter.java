package com.example.testingandroidjava.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingandroidjava.R;
import com.example.testingandroidjava.data.ControllerSensorSensor;

import java.util.ArrayList;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.ViewHolder>{

    private final Context context;
    private ArrayList<ControllerSensorSensor> list;
    private final Handler handler;

    public SwipeAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        handler = new Handler();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setControllerSensorList(ArrayList<ControllerSensorSensor> controllerSensorList) {
        list.clear();
        list = controllerSensorList;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.swipe_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ControllerSensorSensor controllerSensor = list.get(position);

        holder.sensorText.setText(controllerSensor.sensor.name);
        holder.armText.setText(String.valueOf(controllerSensor.sensor.armStatus));

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sensorText, armText;

        public ViewHolder(View itemView) {
            super(itemView);

            sensorText = itemView.findViewById(R.id.sensorNameText);
            armText = itemView.findViewById(R.id.armStatus);

        }
    }
}
