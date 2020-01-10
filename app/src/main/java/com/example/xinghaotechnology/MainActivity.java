package com.example.xinghaotechnology;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    Button login;
    EditText noHp;
    static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        noHp = findViewById(R.id.nohp);
        SharedPreferences prefs = getSharedPreferences("FIREBASE", MODE_PRIVATE);
        token = prefs.getString("token", "");
        checkExisting();



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noHp.getText().toString().length() >= 12)
                    new LoginTask().execute(noHp.getText().toString(), token);
                else
                    Toast.makeText(MainActivity.this, "No. hp tidak valid", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection urlConnection = null;

            try {
                String urlParameters = "phone=" + params[0] + "&token=" + params[1];
                Log.d("paramerter", urlParameters);
                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

                URL url = new URL("https://kkpapi.herokuapp.com/login");
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
                    setDataResponse(content);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                    startActivity(new Intent(MainActivity.this, ChatActivity.class));
                    finish();
                }
            }
            return null;
        }
    }

    void setDataResponse(StringBuilder content) {
        try {
            JSONArray array = new JSONArray(content.toString());
            JSONObject data = array.getJSONObject(0);
            String id = data.getString("id");

            SharedPreferences.Editor editor = getSharedPreferences("ID", MODE_PRIVATE).edit();
            editor.putString("id", id);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getID() {
        SharedPreferences prefs = getSharedPreferences("ID", MODE_PRIVATE);
        return prefs.getString("id", "");
    }

    void checkExisting() {
        if (!getID().isEmpty() && getID() != null) {
            startActivity(new Intent(MainActivity.this, ChatActivity.class));
            finish();
        }
    }
}
