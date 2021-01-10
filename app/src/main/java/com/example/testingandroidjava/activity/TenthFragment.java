package com.example.testingandroidjava.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testingandroidjava.FirstFragment;
import com.example.testingandroidjava.R;

public class TenthFragment extends Fragment {

    Button directCall, dial;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tenth, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        directCall = view.findViewById(R.id.direct_call_button);
        dial = view.findViewById(R.id.dial_button);

        view.findViewById(R.id.button_tenth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(TenthFragment.this)
                        .navigate(R.id.action_tenthFragment_to_eleventFragment);
            }
        });

        directCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                NOTE: FOR DIRECT CALL FROM APP
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0179530002"));//change the number
                startActivity(callIntent);
            }
        });

        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NOTE: DIRECT TO DIAL PAGE
                Uri number = Uri.parse("tel:017953002");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);

            }
        });
    }
}