package com.example.testingandroidjava.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testingandroidjava.FirstFragment;
import com.example.testingandroidjava.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NinethFragment extends Fragment {

    TextView now, local, utc;
    Button convert;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nineth, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        now = view.findViewById(R.id.text_now);
        local = view.findViewById(R.id.text_local);
        utc = view.findViewById(R.id.text_utc);
        convert = view.findViewById(R.id.convert_button);

        view.findViewById(R.id.nineth_to_first_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NinethFragment.this)
                        .navigate(R.id.action_ninethFragment_to_tenthFragment);
            }
        });

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                now.setText("2020-11-28T07:43:38.000Z");

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
                try {
                    date = dateFormat.parse("2020-11-28T07:43:38.000Z");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                utc.setText(String.valueOf(date));
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //If you need time just put specific format for time like 'HH:mm:ss'
                String dateStr = formatter.format(date);

                local.setText(dateStr);

//                now.setText("2020-11-28T07:43:38.000Z"); // current time : new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date())
//                String dateStr = "2020-11-28T07:43:38.000Z";
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);
//                df.setTimeZone(TimeZone.getTimeZone("UTC"));
//                Date date = null;
//                try {
//                    date = df.parse(dateStr);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                utc.setText(String.valueOf(date));
//                df.setTimeZone(TimeZone.getDefault());
//                String formattedDate = df.format(date);
//                local.setText(formattedDate);
            }
        });
    }
}
