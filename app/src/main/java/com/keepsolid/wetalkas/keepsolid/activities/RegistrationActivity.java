package com.keepsolid.wetalkas.keepsolid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.keepsolid.wetalkas.keepsolid.sdk.CustomPreferenceManager;
import com.keepsolid.wetalkas.keepsolid.R;

public class RegistrationActivity extends Activity {



    EditText etRegEmail;
    EditText etRegPassword;
    EditText etRegRetryPassword;

    Button btRegSignUp;


    CustomPreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        preferenceManager = CustomPreferenceManager.getInstance();

        setUpUI();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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

        etRegEmail = (EditText)findViewById(R.id.etRegEmail);
        etRegPassword = (EditText)findViewById(R.id.etRegPassword);
        etRegRetryPassword = (EditText)findViewById(R.id.etRegRetryPassword);


        btRegSignUp = (Button)findViewById(R.id.btRegSignUp);



        btRegSignUp.setOnClickListener(onSignUpClick);


        etRegEmail.setText(preferenceManager.getString("Login"));
        etRegPassword.setText(preferenceManager.getString("Password"));




    }

    View.OnClickListener onSignUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (etRegEmail.getText().toString().equals("") || etRegEmail.getText().toString().equals(" ")) {
                Toast.makeText(RegistrationActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
            } else if (etRegPassword.getText().toString().equals("")) {
                Toast.makeText(RegistrationActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
            } else if (etRegRetryPassword.getText().toString().equals("")) {
                Toast.makeText(RegistrationActivity.this, "Retry your password", Toast.LENGTH_SHORT).show();
            } else if (!etRegPassword.getText().toString().equals(etRegRetryPassword.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Retry your password correct", Toast.LENGTH_SHORT).show();

            }

        }
    };

}
