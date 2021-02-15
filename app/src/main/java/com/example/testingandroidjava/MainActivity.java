package com.example.testingandroidjava;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.testingandroidjava.helper.PermissionsHelper;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    private PermissionsHelper permissionsHelper;
    // to check if we are connected to Network
    boolean isConnected = true;

    // to check if we are monitoring Network
    private boolean monitoringConnectivity = false;

    ConnectivityManager.NetworkCallback connectionCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            isConnected = true;
            Log.e("Internet", "INTERNET CONNECTED");
        }

        @Override
        public void onLost(Network network) {
            isConnected = false;
            Log.e("Internet", "INTERNET LOST");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("newTitle");

        checkPermissions();

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        SpeedDialView speedDialView = findViewById(R.id.speedDial);
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_no_label, R.drawable.ic_code_scanner_auto_focus_off).setLabel("Testing").create());
        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.fab_no_label:
                        Toast.makeText(MainActivity.this, "No label action clicked!\nClosing with animation", Toast.LENGTH_SHORT).show();
                        speedDialView.close(); // To close the Speed Dial with animation
                        return true; // false will close it without animation
                }

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void checkPermissions() {
        permissionsHelper = new PermissionsHelper();
        permissionsHelper.checkAndRequestPermissions(this,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.CALL_PHONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        checkConnectivity();

        Toast.makeText(this, "onResumeActivity", Toast.LENGTH_SHORT).show();

//        TODO:FIRST
//        KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//
//        if (km.isKeyguardSecure()) {
//            Log.e("Status", String.valueOf(km.isDeviceSecure()));
//            Intent authIntent = km.createConfirmDeviceCredentialIntent(null, null);
//            startActivityForResult(authIntent, 101);
//        } else {
//            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
//            startActivity(intent);
//        }


//        TODO:SECOND
//        Executor executor;
//        BiometricPrompt biometricPrompt;
//        BiometricPrompt.PromptInfo promptInfo;
//
//        executor = ContextCompat.getMainExecutor(this);
//
//        biometricPrompt = new BiometricPrompt(MainActivity.this,
//                executor, new BiometricPrompt.AuthenticationCallback() {
//            @Override
//            public void onAuthenticationError(int errorCode,
//                                              @NonNull CharSequence errString) {
//                super.onAuthenticationError(errorCode, errString);
//
//                Toast.makeText(getApplicationContext(),
//                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
//                        .show();
//            }
//
//            @Override
//            public void onAuthenticationSucceeded(
//                    @NonNull BiometricPrompt.AuthenticationResult result) {
//                super.onAuthenticationSucceeded(result);
//                Log.e("PASS", "SECURITY");
//                Toast.makeText(getApplicationContext(),
//                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAuthenticationFailed() {
//                super.onAuthenticationFailed();
//
//                BiometricManager biometricManager = BiometricManager.from(MainActivity.this);
//                switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
//                    case BiometricManager.BIOMETRIC_SUCCESS:
//                        Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
//                        break;
//                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
//                        Log.e("MY_APP_TAG", "No biometric features available on this device.");
//                        break;
//                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
//                        Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
//                        break;
//                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//                        // Prompts the user to create credentials that your app accepts.
//                        final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
//                        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
//                                BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL);
//                        startActivityForResult(enrollIntent, 101);
//                        break;
//                }
//
//                Toast.makeText(getApplicationContext(), "Authentication failed",
//                        Toast.LENGTH_SHORT)
//                        .show();
//            }
//        });
//
//        promptInfo =
//                new BiometricPrompt.PromptInfo.Builder()
//                        .setTitle("Biometric login for my app")
//                        .setSubtitle("Log in using your biometric credential")
//                        .setConfirmationRequired(false)
////                        .setNegativeButtonText("Cancel")
//                        .setDeviceCredentialAllowed(true)
////                        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
//                        .build();
//
//        biometricPrompt.authenticate(promptInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Log.e("PASS", "SECURITY");
                Toast.makeText(this, "pass security", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkConnectivity() {
        Log.e("Connection", "Enter");
        // here we are getting the connectivity service from connectivity manager
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);

        // Getting network Info
        // give Network Access Permission in Manifest
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        // isConnected is a boolean variable
        // here we check if network is connected or is getting connected
        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if (!isConnected) {
            // SHOW ANY ACTION YOU WANT TO SHOW
            // WHEN WE ARE NOT CONNECTED TO INTERNET/NETWORK
            Log.e("Internet", " NO NETWORK!");
// if Network is not connected we will register a network callback to  monitor network
            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder()
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .build(), connectionCallback);
            monitoringConnectivity = true;
        }
        else {
            Log.e("Internet", " HAS NETWORK!");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPause() {
        super.onPause();

        if (monitoringConnectivity) {
            final ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(connectionCallback);
            monitoringConnectivity = false;
        }
    }
}