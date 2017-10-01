package com.example.nathan.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
    private static final String ACTIVITY_NAME = "LoginActivity";
    private static final String DEFAULT_EMAIL = "email@domain.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final SharedPreferences sharedPreferences = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);

        final EditText txtEmail = (EditText) findViewById(R.id.txtEmail);

        String emailString = sharedPreferences.getString("DefaultEmail", DEFAULT_EMAIL);
        txtEmail.setText(emailString);

        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = txtEmail.getEditableText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("DefaultEmail", enteredEmail);
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart(){
        Log.i(ACTIVITY_NAME, "In onStart()");
        super.onStart();
    }

    @Override
    protected void onResume(){
        Log.i(ACTIVITY_NAME, "In onResume()");
        super.onResume();
    }

    @Override
    protected void onPause(){
        Log.i(ACTIVITY_NAME, "In onPause()");
        super.onPause();
    }

    @Override
    protected void onStop(){
        Log.i(ACTIVITY_NAME, "In onStop()");
        super.onStop();
    }

    @Override
    public void finish() {
        super.finish();
        Log.i(ACTIVITY_NAME, "In onFinish()");
        this.onDestroy();
    }

    @Override
    protected void onDestroy(){
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
    }
}