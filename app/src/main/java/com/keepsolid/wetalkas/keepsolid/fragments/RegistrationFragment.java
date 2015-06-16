package com.keepsolid.wetalkas.keepsolid.fragments;


import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.keepsolid.wetalkas.keepsolid.R;
import com.keepsolid.wetalkas.keepsolid.activities.MainActivity;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomFragmentManager;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomPreferenceManager;

import java.util.ArrayList;
import java.util.List;


public class RegistrationFragment extends Fragment {


    EditText etRegEmail;
    EditText etRegPassword;
    EditText etRegRetryPassword;

    TextInputLayout tilRegEmail;
    TextInputLayout tilRegPassword;
    TextInputLayout tilRegRetryPassword;

    Button btRegSignUp;

    MainActivity activity;


    CustomPreferenceManager preferenceManager;

    CustomFragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);


        if (getActivity() != null) {
            activity = (MainActivity) getActivity();
        }

        preferenceManager = CustomPreferenceManager.getInstance();

        fragmentManager = CustomFragmentManager.getInstance();

        setUpUI(rootView);


        // Inflate the layout for this fragment
        return rootView;
    }


    public void setUpUI(View rootView) {


        tilRegEmail = (TextInputLayout)rootView.findViewById(R.id.tilRegEmail);
        tilRegEmail.setHint(activity.getResources().getString(R.string.email));
        etRegEmail = tilRegEmail.getEditText();

        tilRegPassword = (TextInputLayout)rootView.findViewById(R.id.tilRegPassword);
        tilRegPassword.setHint(activity.getResources().getString(R.string.password));
        etRegPassword = tilRegPassword.getEditText();

        tilRegRetryPassword = (TextInputLayout)rootView.findViewById(R.id.tilRegRetryPassword);
        tilRegRetryPassword.setHint(activity.getResources().getString(R.string.retry_password));
        etRegRetryPassword = tilRegRetryPassword.getEditText();


        btRegSignUp = (Button)rootView.findViewById(R.id.btRegSignUp);



        btRegSignUp.setOnClickListener(onSignUpClick);


        etRegEmail.setText(preferenceManager.getString("saved_login_for_registration"));


        etRegEmail.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);





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
