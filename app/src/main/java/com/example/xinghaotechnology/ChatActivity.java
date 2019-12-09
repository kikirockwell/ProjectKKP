package com.example.xinghaotechnology;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private ImageButton btnKirimPesan;
    private EditText etPesan;
    private TextView tvPercakapan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        btnKirimPesan = findViewById(R.id.btnKirimPesan);
        etPesan = findViewById(R.id.etPesan);
        tvPercakapan = findViewById(R.id.tvPercakapan);

        btnKirimPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("pesan",etPesan.getText().toString());
                etPesan.setText("");
            }
        });
    }
}
