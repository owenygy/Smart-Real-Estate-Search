package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ArrayList<SelectActivity.User> Ausers = new ArrayList<>();


        Button button = findViewById(R.id.login);
        button.setOnClickListener(v -> {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(getAssets().open("LoginDetails.csv"), StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",");
                    if (tokens.length == 2) {
                        SelectActivity.User user = new SelectActivity.User(tokens[0], tokens[1], new ArrayList<>(), new ArrayList<>());
                        Ausers.add(user);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                int flag = 0;
                int finalFlag = 0;
                for (SelectActivity.User user : Ausers) {
                    EditText username1 = findViewById(R.id.userName);
                    EditText password1 = findViewById(R.id.Password);
                    String username = username1.getText().toString();
                    String password = password1.getText().toString();
                    if (user.username.equals(username) && user.password.equals(password)) {
                        Intent intent = new Intent(LoginActivity.this, AgencyActivity.class);
                        intent.putExtra("User", user);
                        startActivity(intent);
                        finalFlag = 1;

                    } else if (user.username.equals(username)) {
                        flag = 1;
                    }
                }
                if (finalFlag == 0) {
                    if (flag == 0) {
                        Toast.makeText(getApplicationContext(), "Invalid user", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                }
            } finally {
                System.out.println("finish");
            }

        });

    }
}