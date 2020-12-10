package com.example.testingandroidjava.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testingandroidjava.Albums;
import com.example.testingandroidjava.FirstFragment;
import com.example.testingandroidjava.R;
import com.example.testingandroidjava.ServerCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class SeventhFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seventh, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        httpRequestService httpService = new httpRequestService(getActivity());
        TextView textView = view.findViewById(R.id.http_text_view);

        view.findViewById(R.id.request_http_to_interface).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpService.jsonParse(new ServerCallback() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Albums result) {
                            textView.setText(result.userId + " " + result.id + " " + result.title);
                    }

                    @Override
                    public void onError(JSONObject result) {

                    }
                });
            }
        });

        view.findViewById(R.id.seventh_to_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SeventhFragment.this)
                        .navigate(R.id.action_seventhFragment_to_sixthFragment);
            }
        });
    }
}
