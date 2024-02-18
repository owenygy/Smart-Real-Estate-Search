package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class CLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_login);

        Button button = findViewById(R.id.cusLogin);
        button.setOnClickListener(v -> {
            try {
                // Initialize I/O selector for this activity
                // Get the input stream from the I/O selector
                int flag = 0;
                int finalFlag = 0;
                for (SelectActivity.User user : SelectActivity.users) {
                    EditText username1 = findViewById(R.id.cusName);
                    EditText password1 = findViewById(R.id.cusPassword);
                    String username = username1.getText().toString();
                    String password = password1.getText().toString();
                    if (user.username.equals(username) && user.password.equals(password)) {
                        Intent intent = new Intent(CLoginActivity.this, MainActivity.class);
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