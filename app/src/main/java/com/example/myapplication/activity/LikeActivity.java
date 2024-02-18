package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;


public class LikeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        final ArrayList<String> result = new ArrayList<>();
        Intent intent_before = getIntent();
        SelectActivity.User user = (SelectActivity.User) intent_before.getExtras().getSerializable("User");
        for (int p = 0; p < SelectActivity.users.size(); p++) {
            if (SelectActivity.users.get(p).username.equals(user.username)) {
                result.addAll(SelectActivity.users.get(p).like);
            }
        }

        ListView listView = findViewById(R.id.list2);
        final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, result);
        listView.setAdapter(adapter);

    }
}