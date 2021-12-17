package com.example.testingandroidjava.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testingandroidjava.FirstFragment;
import com.example.testingandroidjava.R;
import com.example.testingandroidjava.databinding.FragmentCaptureEncryptDecryptPictureBinding;
import com.example.testingandroidjava.databinding.FragmentFirstBinding;
import com.example.testingandroidjava.helper.ImageEncryptor;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CaptureEncryptDecryptPictureFragment extends Fragment {

    private FragmentCaptureEncryptDecryptPictureBinding binding;

    ActivityResultLauncher<Intent> activityResultCamera;

    static final String FILE_NAME_ENCRYPT = "motivate_quotes_encrypt.png";
    static final String FILE_NAME_DECRYPT = "motivate_quotes_decrypt";

    private static final int CAMERA_REQUEST = 1888;

    TextView auth;

    String my_key = "j8ub7KMSFx13WsWK";
    String my_spec_key = "XfE2DetErXT9U7Ni";

    Button camera, encrypt, decrypt;
    ImageView encryptImage, decryptImage;

    BroadcastReceiver downloadReceiver;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        binding = FragmentCaptureEncryptDecryptPictureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        camera = binding.captureCamera;
        encrypt = binding.captureEncrypt;
        decrypt = binding.captureDecrypt;
        encryptImage = binding.cameraPicture;
        decryptImage = binding.decryptPicture;
        auth = binding.authText;

        downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("download", "success ");

                File outputFileDecrypt = new File(requireActivity().getFilesDir(), FILE_NAME_DECRYPT);
                File encryptFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"" + "7.bin");

                try{
                    ImageEncryptor.decryptToFile(my_key, my_spec_key, new FileInputStream(encryptFile), new FileOutputStream(outputFileDecrypt));
                    decryptImage.setImageURI(Uri.fromFile(outputFileDecrypt));
                    outputFileDecrypt.delete();
                    encryptFile.delete();

                    Toast.makeText(requireActivity(), "Decrypted", Toast.LENGTH_SHORT).show();

                } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
                    e.printStackTrace();
                }
            }
        };

        requireActivity().registerReceiver((downloadReceiver), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        activityResultCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.e("camera", String.valueOf(result.getResultCode()));
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();

                    //get the Uri for the captured image
                    //File object of camera image
                    File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");

                    //Uri of camera image
                    Uri uri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".provider", file);

                    assert data != null;
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                imageView.setImageBitmap(photo);
                    encryptImage.setImageBitmap(photo);
                    Bitmap resizedbitmap1 = Bitmap.createBitmap(photo, 0,0,105, 120);
                    decryptImage.setImageBitmap(resizedbitmap1);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    resizedbitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    InputStream inputStream = new ByteArrayInputStream(stream.toByteArray());

//                create file
                    File outputFileEncrypt = new File(requireActivity().getFilesDir(),FILE_NAME_ENCRYPT);

                    RequestBody requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("image",outputFileEncrypt.getName(),RequestBody.create(MediaType.parse("image/*"),outputFileEncrypt))
//                        .addFormDataPart("some_key","some_value")
//                        .addFormDataPart("submit","submit")
                            .build();

                    Request request=new Request.Builder()
                            .url("http://primeguard.com.my:2010/mobile/sensor/photo/7")
                            .header("Authorization", "Bearer " + auth.getText().toString())
                            .put(requestBody)
                            .build();

                    OkHttpClient client = new OkHttpClient();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            Log.e("RESPONSE", "onResponse: " + response);
                        }
                    });

                    client.connectionPool().evictAll();

                    try {
                        ImageEncryptor.encryptToFile(my_key, my_spec_key, inputStream, new FileOutputStream(outputFileEncrypt));

                        Log.e("DIR", String.valueOf(requireActivity().getFilesDir()));
                        Toast.makeText(requireActivity(), "Encrypted", Toast.LENGTH_SHORT).show();

                    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IOException e) {
                        e.printStackTrace();
                    }
                }
                else  if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding.buttonNineteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CaptureEncryptDecryptPictureFragment.this)
                        .navigate(R.id.action_captureEncryptDecryptPictureFragment_to_swipeToRefresh);
            }
        });

        camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultCamera.launch(cInt);
            }
        });

        encrypt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://primeguard.com.my:2010/mobile/sensor/photo/7"));
                request.addRequestHeader("Authorization", "Bearer " + auth.getText().toString());
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

                request.setTitle("Download");
                request.setDescription("Downloading file...");

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + "7.bin");

                DownloadManager manager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }
        });

        decrypt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                File outputFileDecrypt = new File(requireActivity().getFilesDir(), FILE_NAME_DECRYPT);
                File encryptFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"" + "7.bin");

                try{
                    ImageEncryptor.decryptToFile(my_key, my_spec_key, new FileInputStream(encryptFile), new FileOutputStream(outputFileDecrypt));
                    decryptImage.setImageURI(Uri.fromFile(outputFileDecrypt));
                    boolean deleteDecrypt = outputFileDecrypt.delete();
                    boolean deleteEncrypt = encryptFile.delete();

                    Toast.makeText(requireActivity(), "Decrypted", Toast.LENGTH_SHORT).show();

                } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                //get the Uri for the captured image
                //File object of camera image
                File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");

                //Uri of camera image
                Uri uri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".provider", file);

                assert data != null;
                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                imageView.setImageBitmap(photo);
                encryptImage.setImageBitmap(photo);
                Bitmap resizedbitmap1 = Bitmap.createBitmap(photo, 0,0,105, 120);
                decryptImage.setImageBitmap(resizedbitmap1);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resizedbitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                InputStream inputStream = new ByteArrayInputStream(stream.toByteArray());

//                create file
                File outputFileEncrypt = new File(requireActivity().getFilesDir(),FILE_NAME_ENCRYPT);

                RequestBody requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("image",outputFileEncrypt.getName(),RequestBody.create(MediaType.parse("image/*"),outputFileEncrypt))
//                        .addFormDataPart("some_key","some_value")
//                        .addFormDataPart("submit","submit")
                        .build();

                Request request=new Request.Builder()
                        .url("http://primeguard.com.my:2010/mobile/sensor/photo/7")
                        .header("Authorization", "Bearer " + auth.getText().toString())
                        .put(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.e("RESPONSE", "onResponse: " + response);
                    }
                });

                client.connectionPool().evictAll();

                try {
                    ImageEncryptor.encryptToFile(my_key, my_spec_key, inputStream, new FileOutputStream(outputFileEncrypt));

                    Log.e("DIR", String.valueOf(requireActivity().getFilesDir()));
                    Toast.makeText(requireActivity(), "Encrypted", Toast.LENGTH_SHORT).show();

                } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IOException e) {
                    e.printStackTrace();
                }

            }
            else  if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();

            }
        }
    }
}