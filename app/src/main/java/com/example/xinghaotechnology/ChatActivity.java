package com.example.xinghaotechnology;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private Button button_chatbox_send;
    private EditText edittext_chatbox;
    private MessageListAdapter mMessageAdapter;
    private List<UserMessage> messageList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        button_chatbox_send = findViewById(R.id.button_chatbox_send);
        edittext_chatbox = findViewById(R.id.edittext_chatbox);

        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);

        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edittext_chatbox.getText().toString().length() > 0) {
                    setChat(edittext_chatbox.getText().toString());
                    edittext_chatbox.setText("");
                }
            }
        });
    }

    private void setChat(String message) {
        UserMessage list = new UserMessage();
        User user = new User();

        user.setMe(true);
        user.setNickname("Eno");
        user.setProfileUrl("OK");

        list.setSender(user);
        list.setCreatedAt(121212);
        list.setMessage(message);

        messageList.add(list);
        mMessageAdapter.notifyDataSetChanged();
    }
}
