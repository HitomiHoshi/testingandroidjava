package com.example.testingandroidjava.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testingandroidjava.FirstFragment;
import com.example.testingandroidjava.R;

import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SixthFragment extends Fragment {
    int REQUEST_ENABLE_BT = 0;

    private BluetoothLeScanner bluetoothLeScanner =
            BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
    private boolean mScanning;
    private Handler handler = new Handler();

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
//    private LeDeviceListAdapter leDeviceListAdapter;

    // Device scan callback.
    private ScanCallback leScanCallback =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    Log.e("BLE", "onScanResult: " + result.getDevice().getName() );
                    Log.e("BLE", "onScanResult: " + result.getDevice().getAddress() );
                    byte[] mScanRecord = result.getScanRecord().getBytes();
                    final StringBuilder stringBuilder = new StringBuilder(mScanRecord.length*2);
                    for (byte byteChar : mScanRecord) { stringBuilder.append(String.format("%02x", byteChar));}
                    String advData = stringBuilder.toString();
                    Log.e("BLE", "onScanResult: " + Arrays.toString(result.getScanRecord().getBytes()));
                    Log.e("BLE", "onScanResult: " +  advData);
//                    leDeviceListAdapter.addDevice(result.getDevice());
//                    leDeviceListAdapter.notifyDataSetChanged();
                }
                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                    Log.i("BLE", "error");
                }
            };
    private void scanLeDevice() {
        if (!mScanning) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothLeScanner.stopScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            bluetoothLeScanner.startScan(leScanCallback);
        } else {
            mScanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sixth, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText inputByte = view.findViewById(R.id.text_byte_to_hex);
        Button byteToHexButton = view.findViewById(R.id.button_bye_to_hex);
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        view.findViewById(R.id.sixth_to_first_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SixthFragment.this)
                        .navigate(R.id.action_sixthFragment_to_FirstFragment);
            }
        });

        byteToHexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 2, 1, 6, // 9, 8, 105, 83, 101, 110, 115, 111, 114, 32, // 9, -1,
                byte[] mScanRecord = {16, -38, 127, 39, 70, 2, -115, 101, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                final StringBuilder stringBuilder = new StringBuilder(mScanRecord.length*2);
                for (byte byteChar : mScanRecord) { stringBuilder.append(String.format("%02x", byteChar));}
                String advData = stringBuilder.toString();
                Log.e("Testing", "onScanResult: " + Arrays.toString(mScanRecord));
                Log.e("Testing", "onScanResult: " +  advData);
            }
        });

        view.findViewById(R.id.ble_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanLeDevice();
            }
        });
    }
}
