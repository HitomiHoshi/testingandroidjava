package com.example.testingandroidjava.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testingandroidjava.FirstFragment;
import com.example.testingandroidjava.R;
import com.example.testingandroidjava.helper.ImageEncryptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.crypto.NoSuchPaddingException;

public class EncryptDecryptPictureFragment extends Fragment {

    static final String FILE_NAME_DECRYPT = "motivate_quotes_decrypt";
    static final String FILE_NAME_ENCRYPT = "motivate_quotes_encrypt.png";
    Button encryptButton, decryptButton;
    ImageView decryptImageView;

    File myDir;
    String my_key = "j8ub7KMSFx13WsWK";
    String my_spec_key = "XfE2DetErXT9U7Ni";
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_encrypt_decrypt_picture, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        encryptButton = view.findViewById(R.id.encrypt_button);
        decryptButton = view.findViewById(R.id.decrypt_button);
        decryptImageView = view.findViewById(R.id.decrypt_image);

        myDir = new File(requireActivity().getFilesDir() + "/saved_images");
//        myDir = new File(Environment.getExternalStorageDirectory() + "/saved_images");

        view.findViewById(R.id.button_seventeen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EncryptDecryptPictureFragment.this)
                        .navigate(R.id.action_encryptDecryptPictureFragment_to_sixthFragment);
            }
        });

        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                convert drawable to bitmap

                Drawable drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.motivate_desktop);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                assert bitmapDrawable != null;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                InputStream inputStream = new ByteArrayInputStream(stream.toByteArray());

//                create file
                File outputFileEncrypt = new File(requireActivity().getFilesDir(),FILE_NAME_ENCRYPT);
                try {
                    ImageEncryptor.encryptToFile(my_key, my_spec_key, inputStream, new FileOutputStream(outputFileEncrypt));

                    Log.e("DIR", String.valueOf(requireActivity().getFilesDir()));
                    Toast.makeText(requireActivity(), "Encrypted", Toast.LENGTH_SHORT).show();

                } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputFileDecrypt = new File(requireActivity().getFilesDir(), FILE_NAME_DECRYPT);
                File encryptFile = new File(requireActivity().getFilesDir(),FILE_NAME_ENCRYPT);

                try{
                    ImageEncryptor.decryptToFile(my_key, my_spec_key, new FileInputStream(encryptFile), new FileOutputStream(outputFileDecrypt));
                    decryptImageView.setImageURI(Uri.fromFile(outputFileDecrypt));
                    outputFileDecrypt.delete();

                    Toast.makeText(requireActivity(), "Decrypted", Toast.LENGTH_SHORT).show();

                } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}