package com.keepsolid.wetalkas.keepsolid.activities;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.keepsolid.wetalkas.keepsolid.sdk.CustomPreferenceManager;
import com.keepsolid.wetalkas.keepsolid.R;


public class AuthorisationActivity extends Activity {



    EditText etAuthEmail;
    EditText etAuthPassword;

    Button btAuthLogIn;
    Button btAuthSignUp;
    Button btOpenScrollView;

    CustomPreferenceManager preferenceManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorisation);

        setUpUI();


        preferenceManager = CustomPreferenceManager.getInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_authorisation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    public void setUpUI() {

        etAuthEmail = (EditText)findViewById(R.id.etAuthEmail);
        etAuthPassword = (EditText)findViewById(R.id.etAuthPassword);

        btAuthLogIn = (Button)findViewById(R.id.btAuthLogIn);
        btAuthSignUp = (Button)findViewById(R.id.btAuthSignUp);
        btOpenScrollView = (Button)findViewById(R.id.btOpenScrollView);




        btAuthLogIn.setOnClickListener(onLogInClick);
        btAuthSignUp.setOnClickListener(onSignUpClick);
        btOpenScrollView.setOnClickListener(onScrollActivityClick);
    }




    View.OnClickListener onLogInClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (etAuthEmail.getText().toString().equals("") || etAuthEmail.getText().toString().equals(" ")) {
                Toast.makeText(AuthorisationActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
            } else if (etAuthPassword.getText().toString().equals("")) {
                Toast.makeText(AuthorisationActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
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



            Intent intent = new Intent();
            intent.setClass(AuthorisationActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener onScrollActivityClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setClass(AuthorisationActivity.this, ScrollActivity.class);
            startActivity(intent);

        }
    };
}
