package com.example.testingandroidjava.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.testingandroidjava.FirstFragment;
import com.example.testingandroidjava.R;
import com.example.testingandroidjava.adapter.SwipeAdapter;
import com.example.testingandroidjava.data.ControllerSensorSensor;
import com.example.testingandroidjava.databinding.FragmentFirstBinding;
import com.example.testingandroidjava.databinding.TestingSwipeLayoutBinding;

import java.util.ArrayList;

public class TestingSwipeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener  {

    private TestingSwipeLayoutBinding binding;

    httpRequestService httpService;
    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;
    TextView usernameText;

    ArrayList<ControllerSensorSensor> controllerSensorList;
    SwipeAdapter swipeAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        binding = TestingSwipeLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        httpService = new httpRequestService(getActivity());

        swipeRefreshLayout = binding.testingSwipeRefresh;
        usernameText = binding.usernameTesting;
        recyclerView = binding.swipeList;

        swipeRefreshLayout.setOnRefreshListener(this);

        controllerSensorList = new ArrayList<>();
        recyclerView.setAdapter(swipeAdapter);

//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }

    @Override
    public void onRefresh() {

    }
}