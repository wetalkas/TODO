package com.keepsolid.wetalkas.keepsolid.fragments;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.keepsolid.wetalkas.keepsolid.R;

import com.keepsolid.wetalkas.keepsolid.activities.MainActivity;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomFragmentManager;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomPreferenceManager;


public class AuthorisationFragment extends Fragment {




    EditText etAuthEmail;
    EditText etAuthPassword;

    TextInputLayout tilAuthEmail;
    TextInputLayout tilAuthPassword;

    Button btAuthLogIn;
    Button btAuthSignUp;

    CheckBox cbAuthRememberMe;

    MainActivity activity;

    CustomPreferenceManager preferenceManager;

    CustomFragmentManager customFragmentManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_authorisation, container, false);

        if (getActivity() != null) {
            activity = (MainActivity) getActivity();
        }

        setUpUI(rootView);


        preferenceManager = CustomPreferenceManager.getInstance();



        customFragmentManager = CustomFragmentManager.getInstance();





        return rootView;
    }



    public void setUpUI(View rootView) {


        tilAuthEmail = (TextInputLayout)rootView.findViewById(R.id.tilAuthEmail);
        etAuthEmail = tilAuthEmail.getEditText();

        tilAuthPassword = (TextInputLayout)rootView.findViewById(R.id.tilAuthPassword);
        etAuthPassword = tilAuthPassword.getEditText();

        tilAuthEmail.setHint(activity.getResources().getString(R.string.email));
        tilAuthPassword.setHint(activity.getResources().getString(R.string.password));

        btAuthLogIn = (Button)rootView.findViewById(R.id.btAuthLogIn);
        btAuthSignUp = (Button)rootView.findViewById(R.id.btAuthSignUp);

        cbAuthRememberMe = (CheckBox) rootView.findViewById(R.id.cbAuthRememberMe);

        etAuthEmail.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);



        btAuthLogIn.setOnClickListener(onLogInClick);
        btAuthSignUp.setOnClickListener(onSignUpClick);
    }




    View.OnClickListener onLogInClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (etAuthEmail.getText().toString().equals("") || etAuthEmail.getText().toString().equals(" ")) {
                Toast.makeText(getActivity(), "Enter your login", Toast.LENGTH_SHORT).show();
            } else if (etAuthPassword.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Enter your password", Toast.LENGTH_SHORT).show();
            } else {

                String currentLogin = etAuthEmail.getText().toString();

                String currentPasswordPref = preferenceManager.getString(currentLogin);

                if (!currentPasswordPref.isEmpty() && currentPasswordPref.equals(etAuthPassword.getText().toString())) {


                    preferenceManager.putBoolean("remembered", cbAuthRememberMe.isChecked());


                    preferenceManager.putString("current_login", currentLogin);
                    customFragmentManager.setFragment(R.id.container, activity.getTasksFragment(), false);
                } else {
                    Toast.makeText(getActivity(), "Wrong login or password", Toast.LENGTH_LONG).show();
                }


            }

        }
    };


    View.OnClickListener onSignUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            preferenceManager.putString("saved_login_for_registration", etAuthEmail.getText().toString());



            RegistrationFragment registrationFragment = new RegistrationFragment();

            customFragmentManager.setFragment(R.id.container, registrationFragment, true);




            /*FragmentTransaction transaction = getFragmentManager().beginTransaction();

            RegistrationFragment registrationFragment = new RegistrationFragment();

            transaction.replace(R.id.container, registrationFragment);

            transaction.addToBackStack(null);

            transaction.commit();*/



        }
    };





}
