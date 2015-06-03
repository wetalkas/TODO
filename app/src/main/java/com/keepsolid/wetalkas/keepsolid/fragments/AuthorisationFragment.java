package com.keepsolid.wetalkas.keepsolid.fragments;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.keepsolid.wetalkas.keepsolid.R;

import com.keepsolid.wetalkas.keepsolid.sdk.CustomFragmentManager;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomPreferenceManager;


public class AuthorisationFragment extends Fragment {




    EditText etAuthEmail;
    EditText etAuthPassword;

    Button btAuthLogIn;
    Button btAuthSignUp;
    Button btOpenScrollView;

    CustomPreferenceManager preferenceManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_authorisation, container, false);


        SearchView sv = (SearchView)rootView.findViewById(R.id.searchView);


        setUpUI(rootView);


        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        preferenceManager = CustomPreferenceManager.getInstance();



        //((ActionBarActivity)getActivity()).setSupportActionBar();




    }







    public void setUpUI(View rootView) {

        etAuthEmail = (EditText)rootView.findViewById(R.id.etAuthEmail);
        etAuthPassword = (EditText)rootView.findViewById(R.id.etAuthPassword);

        btAuthLogIn = (Button)rootView.findViewById(R.id.btAuthLogIn);
        btAuthSignUp = (Button)rootView.findViewById(R.id.btAuthSignUp);
        btOpenScrollView = (Button)rootView.findViewById(R.id.btOpenScrollView);




        btAuthLogIn.setOnClickListener(onLogInClick);
        btAuthSignUp.setOnClickListener(onSignUpClick);
        btOpenScrollView.setOnClickListener(onScrollActivityClick);
    }




    View.OnClickListener onLogInClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (etAuthEmail.getText().toString().equals("") || etAuthEmail.getText().toString().equals(" ")) {
                Toast.makeText(getActivity(), "Enter your email", Toast.LENGTH_SHORT).show();
            } else if (etAuthPassword.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Enter your password", Toast.LENGTH_SHORT).show();
            } else {

                preferenceManager.putString("Login", etAuthEmail.getText().toString());
                preferenceManager.putString("Password", etAuthPassword.getText().toString());


            }

        }
    };


    View.OnClickListener onSignUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            preferenceManager.putString("Login", etAuthEmail.getText().toString());
            preferenceManager.putString("Password", etAuthPassword.getText().toString());


            CustomFragmentManager customFragmentManager = CustomFragmentManager.getInstance();

            RegistrationFragment registrationFragment = new RegistrationFragment();

            customFragmentManager.setFragment(R.id.container, registrationFragment, true);




            /*FragmentTransaction transaction = getFragmentManager().beginTransaction();

            RegistrationFragment registrationFragment = new RegistrationFragment();

            transaction.replace(R.id.container, registrationFragment);

            transaction.addToBackStack(null);

            transaction.commit();*/



        }
    };

    View.OnClickListener onScrollActivityClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            /*Intent intent = new Intent();
            intent.setClass(getActivity(), ScrollActivity.class);
            startActivity(intent);*/

        }
    };



}
