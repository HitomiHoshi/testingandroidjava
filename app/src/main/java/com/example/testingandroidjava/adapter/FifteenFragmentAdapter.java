package com.example.testingandroidjava.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingandroidjava.R;

import java.util.ArrayList;
import java.util.List;

public class FifteenFragmentAdapter extends RecyclerView.Adapter<FifteenFragmentAdapter.ViewHolder> {

    private final Context context;
    private List<ScanResult> list;

    public FifteenFragmentAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public void setList(List<ScanResult> result){
        list.clear();
        list = result;

        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public FifteenFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_fifteen_list, parent, false);
        return new FifteenFragmentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FifteenFragmentAdapter.ViewHolder holder, int position) {
        ScanResult result = list.get(position);

        holder.wifiName.setText(result.SSID);

        holder.wifiName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final EditText edittext = new EditText(context);
                builder.setTitle("Enter Your Message");
                builder.setMessage("Enter Your Title");
                builder.setView(edittext);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //What ever you want to do with the value
                        Editable YouEditTextValue = edittext.getText();
                        //OR
//                        String YouEditTextValue = edittext.getText().toString();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No Option", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView wifiName;

        public ViewHolder(View itemView) {
            super(itemView);

            wifiName = itemView.findViewById(R.id.fragment_fifteen_wifi_name);
        }
    }
}
