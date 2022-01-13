package com.example.testingandroidjava.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.testingandroidjava.R;
import com.example.testingandroidjava.callback.SignInCallback;
import com.example.testingandroidjava.data.Message;
import com.example.testingandroidjava.data.SignIn;
import com.example.testingandroidjava.data.SignInTFA;
import com.example.testingandroidjava.databinding.SwipeToRefreshBinding;

public class SwipeToRefresh extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeToRefreshBinding binding;
    httpRequestService httpService;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView usernameText, emailText, controllerNameText, emergencyNameText, emergencyPhoneText;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        binding = SwipeToRefreshBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        httpService = new httpRequestService(getActivity());

        usernameText = binding.usernameText;
        emailText = binding.emailText;
        controllerNameText = binding.nameControllerText;
        emergencyNameText = binding.emergencyContactNameText;
        emergencyPhoneText = binding.emergencyContactPhoneText;
        swipeRefreshLayout = binding.refreshWholeLayout;

        swipeRefreshLayout.setOnRefreshListener(this);

        binding.button20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SwipeToRefresh.this)
                        .navigate(R.id.action_swipeToRefresh_to_testingSwipeFragment);
            }
        });

        httpService.signIn("adani", "adani123", new SignInCallback() {

            @Override
            public void onSuccessTFA(SignInTFA result) {
//                Toast.makeT                                                                                                                                                                                                ext(requireActivity(), result.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(SignIn result) {
//                Toast.makeText(requireActivity(), result.message, Toast.LENGTH_SHORT).show();

                usernameText.setText(result.username);
                emailText.setText(result.email);
                controllerNameText.setText(result.userControllers.get(0).controller.name);
                emergencyNameText.setText(result.userControllers.get(0).controller.emergencyContactName);
                emergencyPhoneText.setText(result.userControllers.get(0).controller.emergencyContactPhone);
            }

            @Override
            public void onError(Message result) {
                Toast.makeText(requireActivity(), result.message, Toast.LENGTH_SHORT).show();
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    Log.e("TAG", "tekan berlakang");
                    requireActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public void onRefresh() {
        Toast.makeText(requireActivity(), "Refresh", Toast.LENGTH_SHORT).show();

        httpService.signIn("adani", "adani123", new SignInCallback() {

            @Override
            public void onSuccessTFA(SignInTFA result) {
//                Toast.makeText(requireActivity(), result.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(SignIn result) {
//                Toast.makeText(requireActivity(), result.message, Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                usernameText.setText(result.username);
                emailText.setText(result.email);
                controllerNameText.setText(result.userControllers.get(0).controller.name);
                emergencyNameText.setText(result.userControllers.get(0).controller.emergencyContactName);
                emergencyPhoneText.setText(result.userControllers.get(0).controller.emergencyContactPhone);
            }

            @Override
            public void onError(Message result) {
                Toast.makeText(requireActivity(), result.message, Toast.LENGTH_SHORT).show();
            }
        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        }, 2000);
    }
}