package com.example.nathan.androidlabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import junit.framework.Test;

public class TestToolbar extends AppCompatActivity {

    private View parentLayout;
    private final String DEFAULT_SNACKBAR_TEXT = "You selected Option 1";
    private String enteredText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        // https://stackoverflow.com/questions/30978457/how-to-show-snackbar-when-activity-starts
        parentLayout = findViewById(R.id.toolBarContent);

        final Toolbar appToolbar = (Toolbar)findViewById(R.id.appToolbar);
        setSupportActionBar(appToolbar);

        final FloatingActionButton fabAdd = (FloatingActionButton)findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parentLayout, "Hello from Snackbar", Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        AlertDialog.Builder builder = null;

        switch(menuItem.getItemId()){

            case R.id.menu_opt_1:
                Log.d("Toolbar", "Option 1 selected");
                String textToDisplay = enteredText.length() == 0 ? DEFAULT_SNACKBAR_TEXT : enteredText;
                Snackbar.make(parentLayout, textToDisplay, Snackbar.LENGTH_LONG).show();
                break;

            case R.id.menu_opt_2:
                builder = new AlertDialog.Builder(TestToolbar.this);
                builder.setTitle("Do you want to go back?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                break;

            case R.id.menu_opt_3:
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout rootView = (LinearLayout)inflater.inflate(R.layout.custom_alert_dialog, null);
                final EditText etEnteredMsg = (EditText) rootView.findViewById(R.id.etEnteredMsg);

                builder = new AlertDialog.Builder(TestToolbar.this);
                // Inflate and set the layout for the dialog
                // pass null as the parent view because its going in the dialog layout
                builder.setView(rootView)
                .setPositiveButton("Show message", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enteredText = etEnteredMsg.getText().toString();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                break;

            case R.id.menu_opt_about:
                Toast.makeText(this, "Version 1.0\nBy: Nathan Doef", Toast.LENGTH_LONG)
                        .show();
                break;
        }

        if (builder != null){
            AlertDialog alert = builder.create();
            builder.show();
        }

        return true;
    }
}
