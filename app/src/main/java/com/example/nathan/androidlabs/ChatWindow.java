package com.example.nathan.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    private ListView chatWindow;
    private ChatAdapter messageAdapter;

    private EditText txtChatMsg;
    private Button btnSend;
    private ArrayList<String> chatMessageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatMessageList = new ArrayList<>();
        chatWindow = (ListView)findViewById(R.id.chatWindow);
        txtChatMsg = (EditText)findViewById(R.id.txtChatMsg);
        btnSend = (Button)findViewById(R.id.btnSend);

        messageAdapter = new ChatAdapter(this);
        chatWindow.setAdapter (messageAdapter);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredText = txtChatMsg.getEditableText().toString();
                chatMessageList.add(enteredText);

                //this restarts the process of getCount()/getView()
                messageAdapter.notifyDataSetChanged();
                txtChatMsg.setText("");
            }
        });
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
