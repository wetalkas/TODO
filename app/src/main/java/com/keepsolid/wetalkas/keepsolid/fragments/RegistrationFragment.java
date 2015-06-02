package com.keepsolid.wetalkas.keepsolid.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.keepsolid.wetalkas.keepsolid.R;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomPreferenceManager;

import java.util.ArrayList;
import java.util.List;


public class RegistrationFragment extends Fragment {


    EditText etRegEmail;
    EditText etRegPassword;
    EditText etRegRetryPassword;

    Button btRegSignUp;


    CustomPreferenceManager preferenceManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }


    public void setUpUI(View rootView) {

        etRegEmail = (EditText)rootView.findViewById(R.id.etRegEmail);
        etRegPassword = (EditText)rootView.findViewById(R.id.etRegPassword);
        etRegRetryPassword = (EditText)rootView.findViewById(R.id.etRegRetryPassword);


        btRegSignUp = (Button)rootView.findViewById(R.id.btRegSignUp);



        btRegSignUp.setOnClickListener(onSignUpClick);


        etRegEmail.setText(preferenceManager.getString("Login"));
        etRegPassword.setText(preferenceManager.getString("Password"));




    }

    View.OnClickListener onSignUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (etRegEmail.getText().toString().equals("") || etRegEmail.getText().toString().equals(" ")) {
                Toast.makeText(getActivity(), "Enter your email", Toast.LENGTH_SHORT).show();
            } else if (etRegPassword.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Enter your password", Toast.LENGTH_SHORT).show();
            } else if (etRegRetryPassword.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Retry your password", Toast.LENGTH_SHORT).show();
            } else if (!etRegPassword.getText().toString().equals(etRegRetryPassword.getText().toString())) {
                Toast.makeText(getActivity(), "Retry your password correct", Toast.LENGTH_SHORT).show();

            }

        }
    };



}
