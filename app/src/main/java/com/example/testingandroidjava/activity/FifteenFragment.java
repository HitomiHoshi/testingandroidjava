package com.example.testingandroidjava.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingandroidjava.FirstFragment;
import com.example.testingandroidjava.R;
import com.example.testingandroidjava.adapter.FifteenFragmentAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FifteenFragment extends Fragment {

    WifiManager wifiManager;
    Button wifiScanner;
    RecyclerView recyclerView;
    FifteenFragmentAdapter fifteenFragmentAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fifteen, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wifiScanner = view.findViewById(R.id.wifi_scanner_button);
        recyclerView = view.findViewById(R.id.fragment_fifteen_list);
        fifteenFragmentAdapter = new FifteenFragmentAdapter(requireActivity());

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(fifteenFragmentAdapter);

        view.findViewById(R.id.button_fifteen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FifteenFragment.this)
                        .navigate(R.id.action_fifteenFragment_to_sixteenFragment);
            }
        });

        wifiManager = (WifiManager)
                requireActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()){
            Log.e("WIFI enable", "not enabled");
            wifiManager.setWifiEnabled(true);
        }
        else{
            Log.e("WIFI enable", "enabled");
        }

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceive(Context c, Intent intent) {
                Log.e("WIFI scanner", "onReceive");

                List<ScanResult> results = wifiManager.getScanResults();
                requireActivity().unregisterReceiver(this);
                fifteenFragmentAdapter.setList(results);

                for (ScanResult scanResult : results)
                    Log.e("WIFI success", scanResult.SSID);

//                boolean success = intent.getBooleanExtra(
//                        WifiManager.EXTRA_RESULTS_UPDATED, false);
//                if (success) {
//                    scanSuccess();
//                } else {
//                    // scan failure handling
//                    scanFailure();
//                }
            }
        };


        wifiScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("WIFI scanner", "enter");
                requireActivity().registerReceiver((wifiScanReceiver),new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                wifiManager.startScan();
            }
        });
    }
    private void scanSuccess() {
        List<ScanResult> results = wifiManager.getScanResults();

        for (ScanResult scanResult : results)
        Log.e("WIFI success", scanResult.SSID);
//  ... use new scan results ...
    }

    private void scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        List<ScanResult> results = wifiManager.getScanResults();
        for (ScanResult scanResult : results)
            Log.e("WIFI fail", scanResult.SSID);
//  ... potentially use older scan results ...
    }
}