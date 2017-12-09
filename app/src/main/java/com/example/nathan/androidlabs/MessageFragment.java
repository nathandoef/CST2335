package com.example.nathan.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Nathan on 2017-12-07.
 */

public class MessageFragment extends Fragment {

    private Activity callingActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        final String msg = getArguments().getString("msg");
        final long id = getArguments().getLong("id");
        final int position = getArguments().getInt("position");

        View view = inflater.inflate(R.layout.message_details_fragment, container, false);

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
