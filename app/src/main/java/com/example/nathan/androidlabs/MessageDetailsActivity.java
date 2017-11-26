package com.example.nathan.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MessageDetailsActivity extends Activity {

    private Button btnDeleteMessage;
    private Bundle msgDetails;
    private ChatWindow.MessageFragment loadedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null){

            msgDetails = extras.getBundle("msgDetails");
            ChatWindow.MessageFragment loadedFragment = new ChatWindow.MessageFragment();
            loadedFragment.setArguments(msgDetails);

            getFragmentManager().beginTransaction()
                    .add(R.id.flMsgDetailsPhone, loadedFragment).commit();
        }
    }

    public void deleteMessage(long id, int position){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", id);
        resultIntent.putExtra("position", position);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
