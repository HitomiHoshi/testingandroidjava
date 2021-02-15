package com.example.testingandroidjava.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testingandroidjava.FirstFragment;
import com.example.testingandroidjava.R;
import com.example.testingandroidjava.data.User;

import java.util.ArrayList;
import java.util.List;

public class ThirteenFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner numberSpinner, customSpinner;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<User> userAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thirteen, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        numberSpinner = view.findViewById(R.id.spinner_number);
        customSpinner = view.findViewById(R.id.custom_spinner);

        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.numbers, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numberSpinner.setAdapter(adapter);
        numberSpinner.setOnItemSelectedListener(this);

        view.findViewById(R.id.button_13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirteenFragment.this)
                        .navigate(R.id.action_thirteenFragment_to_fourteenFragment);
            }
        });

        List<User> userList = new ArrayList<>();
        User user1 = new User("Jim", 20, "jim@gmail.com");
        userList.add(user1);
        User user2 = new User("John", 23, "john@gmail.com");
        userList.add(user2);
        User user3 = new User("Jenny", 25, "jenny@gmail.com");
        userList.add(user3);

        userAdapter = new ArrayAdapter<User>(getActivity(), android.R.layout.simple_spinner_item, userList);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customSpinner.setAdapter(userAdapter);
        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getSelectedItem();
                displayUserData(user);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void displayUserData(User user) {
        String name = user.getName();
        int age = user.getAge();
        String mail = user.getMail();
        String userData = "Name: " + name + "\nAge: " + age + "\nMail: " + mail;
        Toast.makeText(getActivity(), userData, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}