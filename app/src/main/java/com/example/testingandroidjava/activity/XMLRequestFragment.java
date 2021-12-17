package com.example.testingandroidjava.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testingandroidjava.R;
import com.example.testingandroidjava.callback.XMLCallback;
import com.example.testingandroidjava.data.Message;
import com.example.testingandroidjava.data.WaktuSolatList;
import com.example.testingandroidjava.databinding.FragmentXmlRequestBinding;
import com.example.testingandroidjava.service.VolleyXMLService;

public class XMLRequestFragment extends Fragment {

    private FragmentXmlRequestBinding binding;

    private VolleyXMLService volleyXMLService;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        binding = FragmentXmlRequestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        volleyXMLService = new VolleyXMLService(getActivity());

        binding.buttonEighteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(XMLRequestFragment.this)
                        .navigate(R.id.action_XMLRequestFragment_to_captureEncryptDecryptPictureFragment);
            }
        });

        binding.getXml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                volleyXMLService.getWaktuSolat(new XMLCallback() {
                    @Override
                    public void onSuccess(WaktuSolatList result) {
                        Toast.makeText(getActivity(), result.waktuSolatList.get(1).title, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Message result) {
                        Toast.makeText(getActivity(), result.message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
