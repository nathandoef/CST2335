package com.example.nathan.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    public static class MessageFragment extends Fragment {

        private Activity callingActivity;
        private String msg;
        private long id;
        private int position;

        public MessageFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.message_details_fragment, container, false);

            this.msg = getArguments().getString("msg");
            this.id = getArguments().getLong("id");
            this.position = getArguments().getInt("position");

            TextView tvMsgDetails = (TextView) view.findViewById(R.id.tvMsgDetails);
            TextView tvMsgId = (TextView)view.findViewById(R.id.tvMsgId);

            tvMsgDetails.setText("Message: " + msg);
            tvMsgId.setText("ID: " + Long.toString(id));

            Button btnDeleteMessage = (Button)view.findViewById(R.id.btnDeleteMsg);

            switch(callingActivity.getLocalClassName()){
                case "ChatWindow":
                    btnDeleteMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((ChatWindow)callingActivity).deleteMessage(id, position);

                            callingActivity.getFragmentManager().beginTransaction().
                                    remove(MessageFragment.this).commit();
                        }
                    });
                    break;

                case "MessageDetailsActivity":
                    btnDeleteMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MessageDetailsActivity) callingActivity).deleteMessage(id, position);
                        }
                    });
                    break;
            }
            return view;
        }

        @Override
        public void onAttach(Context context){
            super.onAttach(context);
        }

        @Override
        public void onAttach(Activity activity){
            super.onAttach(activity);
            this.callingActivity = activity;
        }
    }

    private void deleteMessage(long id, int position){
        chatMessageList.remove(position);
        db.delete(ChatDatabaseHelper.CHAT_TABLE, ChatDatabaseHelper.KEY_ID + "=" + id, null);
        messageAdapter.notifyDataSetChanged();
    }

    private final String ACTIVITY_NAME = "ChatWindowActivity";
    private final int DELETE_REQUEST = 10;
    private boolean frameLayoutExists;

    private ListView chatWindow;
    private ChatAdapter messageAdapter;

    private EditText txtChatMsg;
    private Button btnSend;
    private ArrayList<String> chatMessageList;

    private ChatDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + ChatDatabaseHelper.CHAT_TABLE, null);

        chatMessageList = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String msg = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            chatMessageList.add(msg);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + msg);
            cursor.moveToNext();
        }

        int count = cursor.getColumnCount();
        Log.i(ACTIVITY_NAME, "Cursor's column count = " + count);

        for (int i = 0; i < count; i++){
            Log.i(ACTIVITY_NAME, "Column " + i + ": " + cursor.getColumnName(i));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        frameLayoutExists = findViewById(R.id.flMsgDetails) != null;

        chatWindow = (ListView)findViewById(R.id.chatWindow);
        chatWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String msg = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
                Bundle bundle = new Bundle();
                bundle.putLong("id", id);
                bundle.putString("msg", msg);
                bundle.putInt("position", position);

                // FrameLayout was not loaded
                // Device width is less than 600px wide (i.e. layout-sq600dp)
                if (!frameLayoutExists){
                    Intent intent = new Intent(ChatWindow.this, MessageDetailsActivity.class);
                    intent.putExtra("msgDetails", bundle);
                    startActivityForResult(intent, DELETE_REQUEST);
                }
                // Fragment loaded. Device width is more than 600px wide
                else {
                    MessageFragment messageFragment = new MessageFragment();
                    messageFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.flMsgDetails, messageFragment).commit();
                }

            }
        });

        txtChatMsg = (EditText)findViewById(R.id.txtChatMsg);
        btnSend = (Button)findViewById(R.id.btnSend);

        messageAdapter = new ChatAdapter(this);
        chatWindow.setAdapter (messageAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredText = txtChatMsg.getEditableText().toString();
                chatMessageList.add(enteredText);

                ContentValues cntValues = new ContentValues();
                cntValues.put(ChatDatabaseHelper.KEY_MESSAGE, enteredText);
                db.insert(ChatDatabaseHelper.CHAT_TABLE, "", cntValues);

                //this restarts the process of getCount()/getView()
                messageAdapter.notifyDataSetChanged();
                txtChatMsg.setText("");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == DELETE_REQUEST){
            if (resultCode == Activity.RESULT_OK){

                Bundle extras = data.getExtras();
                long id = extras.getLong("id");
                int position = extras.getInt("position");
                deleteMessage(id, position);
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        dbHelper.close();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        startActivity(new Intent(this, ChatWindow.class));
    }

    private class ChatAdapter extends ArrayAdapter<String> {

        private ChatAdapter(Context context){
            super(context, 0);
        }

        @Override
        public int getCount(){
            return chatMessageList.size();
        }

        @Override
        public String getItem(int position){
            return chatMessageList.get(position);
        }

        @Override
        public long getItemId(int position){
            if (cursor == null)
                throw new NullPointerException("ERROR: cursor is null");

            // refresh the cursors
            cursor = db.rawQuery("SELECT * FROM " + ChatDatabaseHelper.CHAT_TABLE, null);
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parentView){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result = null ;
            if(position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }
            else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView)result.findViewById(R.id.messageText);
            message.setTextSize(30);

            if (position % 2 == 0){
                message.setBackgroundColor(Color.parseColor("#CCCCCC"));    // grey
            }
            else {
                message.setBackgroundColor(Color.parseColor("#5BC236"));    // green
            }

            message.setText(getItem(position));
            return result;
        }
    }
}
