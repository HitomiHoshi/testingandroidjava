package com.example.testingandroidjava.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testingandroidjava.R;
import com.example.testingandroidjava.databinding.FragmentStringEncodeImageBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class StringEncodeImageFragment extends Fragment {

    private FragmentStringEncodeImageBinding binding;

    Button cameraButton, encodeImageButton, decodeImageButton;
    ImageView cameraImageView, decodeImageView;
    TextView stringText;

    String imageString;
    Bitmap photo;

    ActivityResultLauncher<Intent> activityResultCamera;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        binding = FragmentStringEncodeImageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        encodeImageButton = binding.buttonEncode;
        decodeImageButton = binding.buttonDecode;
        cameraImageView = binding.cameraView;
        stringText = binding.encodeString;
        cameraButton = binding.capture;

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
                    photo = (Bitmap) data.getExtras().get("data");
                    cameraImageView.setImageBitmap(photo);
//                    Bitmap resizedbitmap1 = Bitmap.createBitmap(photo, 0,0,105, 120);
//                    decryptImage.setImageBitmap(resizedbitmap1);
//
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    resizedbitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    InputStream inputStream = new ByteArrayInputStream(stream.toByteArray());

                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultCamera.launch(cInt);
            }
        });

        encodeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageString = StringImage(photo);
                Log.e("Image", imageString);
            }
        });

        decodeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap testing = BitmapImage(imageString);

            }
        });

        binding.button22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(StringEncodeImageFragment.this)
                        .navigate(R.id.action_stringEncodeImageFragment_to_sixthFragment);
            }
        });
    }

    public String StringImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        stringText.setText(encodeImage);

        return encodeImage;
    }

    public Bitmap BitmapImage(String testing) {

//        String donwloadImage = jsonObject.getString("image");
//        String encode = Base64.encodeToString(testing.getBytes(), Base64.DEFAULT);
//        byte[] bytes = Base64.decode(encode, Base64.DEFAULT);
//        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

//        byte[] decodeByte = Base64.decode(testing, Base64.DEFAULT);
//        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);

        // decode base64 string
        byte[] bytes = Base64.decode(testing, Base64.DEFAULT);
        // Initialize bitmap
        Bitmap decodedBitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        // set bitmap on imageView
        decodeImageView.setImageBitmap(decodedBitmap);

        return decodedBitmap;
    }
}