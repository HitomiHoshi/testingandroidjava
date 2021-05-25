package com.example.testingandroidjava.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testingandroidjava.R;

import java.io.File;
import java.util.Objects;

public class SixteenFragment extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView, cropImage;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Uri picUri;
    //keep track of cropping intent
    final int PIC_CROP = 2;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sixteen, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.capture_image);
        cropImage = view.findViewById(R.id.crop_image);

        view.findViewById(R.id.button_sixteen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SixteenFragment.this)
                        .navigate(R.id.action_sixteenFragment_to_encryptDecryptPictureFragment);
            }
        });

        view.findViewById(R.id.button_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
//                Uri uri = FileProvider.getUriForFile(requireContext(), requireActivity().getPackageName() + ".provider", file);
//                cInt.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(cInt, 1888);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1888) {
            if (resultCode == getActivity().RESULT_OK) {
                //get the Uri for the captured image
                //File object of camera image
                File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");

                //Uri of camera image
                Uri uri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".provider", file);
//                assert data != null;
//                Log.e("DATA", data.getData().toString());
                picUri = uri;
                //carry out the crop operation
//                performCrop();

                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                saveImage(photo);
                imageView.setImageBitmap(photo);
                Bitmap resizedbitmap1=Bitmap.createBitmap(photo, 0,0,105, 120);
                cropImage.setImageBitmap(resizedbitmap1);
            }
            else  if (resultCode == getActivity().RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();

            }
        }
        //user is returning from cropping the image
        else if (requestCode == PIC_CROP) {
//get the returned data
            Bundle extras = data.getExtras();
//get the cropped bitmap
            Bitmap photo = extras.getParcelable("data");
//            assert data != null;
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//                saveImage(photo);
                imageView.setImageBitmap(photo);
        }
    }

    private void performCrop() {
        try {
//call the standard crop action intent (the user device may not support it)
//            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            Toast.makeText(getActivity(), "Whoops - your device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
        }
    }
}