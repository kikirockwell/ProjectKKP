package com.example.xinghaotechnology;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
                    new MessageTask().execute(getID(), edittext_chatbox.getText().toString());
                }
            }
        });
    }

    private void setChat(String message) {
        UserMessage list = new UserMessage();
        User user = new User();

        user.setMe(true);

        list.setSender(user);
        list.setMessage(message);

        messageList.add(list);
        mMessageAdapter.notifyDataSetChanged();
        edittext_chatbox.setText("");
    }

    private class MessageTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection urlConnection = null;

            try {
                String urlParameters = "id_cust=" + params[0] + "&msg=" + params[1];
                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

                URL url = new URL("https://kkpapi.herokuapp.com/message");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("User-Agent", "Java client");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                try (
                        DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())) {

                    wr.write(postData);
                }

                StringBuilder content;

                try (
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(urlConnection.getInputStream()))) {

                    String line;
                    content = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        content.append(line);
                        content.append(System.lineSeparator());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setChat(edittext_chatbox.getText().toString());
        }
    }

    private String getID() {
        SharedPreferences prefs = getSharedPreferences("ID", MODE_PRIVATE);
        return prefs.getString("id", "");
    }
}
