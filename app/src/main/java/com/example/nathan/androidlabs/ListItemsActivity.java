package com.example.nathan.androidlabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends Activity {
    private static final String ACTIVITY_NAME = "ListItemsActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageButton imgBtn;
    private Switch switchCntrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        imgBtn = (ImageButton)findViewById(R.id.imgBtn);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        switchCntrl = (Switch)findViewById(R.id.switchCntrl);
        switchCntrl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CharSequence text = isChecked ? getString(R.string.switch_on) : getString(R.string.switch_off);
                int duration = isChecked ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(ListItemsActivity.this , text, duration);
                toast.show();
            }
        });

        CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("Response", "My information to share");
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .show();
            }
        });
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            imgBtn.setImageBitmap(imageBitmap);
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