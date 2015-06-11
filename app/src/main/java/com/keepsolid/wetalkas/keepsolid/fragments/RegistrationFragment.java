package com.keepsolid.wetalkas.keepsolid.fragments;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.keepsolid.wetalkas.keepsolid.R;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomFragmentManager;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomPreferenceManager;

import java.util.ArrayList;
import java.util.List;


public class RegistrationFragment extends Fragment {


    EditText etRegEmail;
    EditText etRegPassword;
    EditText etRegRetryPassword;

    Button btRegSignUp;


    CustomPreferenceManager preferenceManager;

    CustomFragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        preferenceManager = CustomPreferenceManager.getInstance();

        fragmentManager = CustomFragmentManager.getInstance();

        setUpUI(rootView);


        // Inflate the layout for this fragment
        return rootView;
    }


    public void setUpUI(View rootView) {

        etRegEmail = (EditText)rootView.findViewById(R.id.etRegEmail);
        etRegPassword = (EditText)rootView.findViewById(R.id.etRegPassword);
        etRegRetryPassword = (EditText)rootView.findViewById(R.id.etRegRetryPassword);


        btRegSignUp = (Button)rootView.findViewById(R.id.btRegSignUp);



        btRegSignUp.setOnClickListener(onSignUpClick);


        etRegEmail.setText(preferenceManager.getString("saved_login_for_registration"));





    }

    View.OnClickListener onSignUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (etRegEmail.getText().toString().equals("") || etRegEmail.getText().toString().equals(" ")) {
                Toast.makeText(getActivity(), "Enter your login", Toast.LENGTH_SHORT).show();
            } else if (etRegPassword.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Enter your password", Toast.LENGTH_SHORT).show();
            } else if (etRegRetryPassword.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Retry your password", Toast.LENGTH_SHORT).show();
            } else if (!etRegPassword.getText().toString().equals(etRegRetryPassword.getText().toString())) {
                Toast.makeText(getActivity(), "Retry your password correct", Toast.LENGTH_SHORT).show();

            } else {
                preferenceManager.putString(etRegEmail.getText().toString(), etRegPassword.getText().toString());

                fragmentManager.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                AuthorisationFragment authorisationFragment = new AuthorisationFragment();
                fragmentManager.setFragment(R.id.container, authorisationFragment, false);



            }

        }
    };



}
