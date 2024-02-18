package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.internal.DetailsDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SelectActivity extends AppCompatActivity {

    private static DetailsDB detailsDB;
    public static ArrayList<User> users;

    static {
        users = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        // Initialize database for query,
        // Note that we only initialize DetailsDB once!!!
        detailsDB = DetailsDB.getInstance(getResources().openRawResource(R.raw.data));

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("CustomerLogin.csv"), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 2) {
                    SelectActivity.User user = new SelectActivity.User(tokens[0], tokens[1], new ArrayList<>(), new ArrayList<>());
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        Button buttonCLogin = findViewById(R.id.btnCLogin);
        Button buttonLogin = findViewById(R.id.btnLogin);
        Button buttonSupport = findViewById(R.id.button3);
        buttonCLogin.setOnClickListener(view -> {
            Intent intent = new Intent(SelectActivity.this, CLoginActivity.class);
            startActivity(intent);
        });
        buttonLogin.setOnClickListener(view -> {
            Intent intent = new Intent(SelectActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        buttonSupport.setOnClickListener(view -> {
            Intent intent = new Intent(SelectActivity.this, SupportActivity.class);
            startActivity(intent);
        });
    }

    public static class User implements Serializable {
        public String username;
        public String password;
        public ArrayList<String> history;
        public ArrayList<String> like;

        public User(String username, String password, ArrayList<String> history, ArrayList<String> like) {
            this.username = username;
            this.password = password;
            this.history = history;
            this.like = like;
        }
    }

    /**
     * @return singleton instance of DetailsDB
     */
    public static DetailsDB getDetailsDB() {
        return detailsDB;
    }


}