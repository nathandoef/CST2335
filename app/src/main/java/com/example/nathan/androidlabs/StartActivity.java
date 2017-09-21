package com.example.nathan.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {
    private static final String ACTIVITY_NAME = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final Button btnStartAct = (Button)findViewById(R.id.btnStartAct);
        btnStartAct.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, 10);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data){
        if (responseCode == Activity.RESULT_OK){
            String messagePassed = data.getStringExtra("Response");
            String text = "ListItemsActivity passed: " + messagePassed;
            Toast toast = Toast.makeText(StartActivity.this , text, Toast.LENGTH_LONG);
            toast.show();
        }
        if (requestCode == 10){
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
        }
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