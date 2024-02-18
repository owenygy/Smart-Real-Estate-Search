package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public ListView details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        details = findViewById(R.id.detailsList);

        Intent intent = getIntent();
        String detail = intent.getStringExtra("details");
        assert detail != null;
        String[] separate = detail.split("\\|");
        String rent = "Weekly Price: " + separate[0];
        String bedroom = "Number of Bedroom: " + separate[1];
        String location = "Area: " + separate[2];
        String garage = "Number of Garage: " + separate[3];
        String highlights = "Highlights: " + separate[4];

        List<String> listDetails = new ArrayList<>();
        listDetails.add(rent);
        listDetails.add(bedroom);
        listDetails.add(location);
        listDetails.add(garage);
        listDetails.add(highlights);

        Button favourite = findViewById(R.id.likeButton2);
        favourite.setOnClickListener(View -> {
            Intent intent_before = getIntent();
            SelectActivity.User user = (SelectActivity.User) intent_before.getExtras().getSerializable("User");
            for (int s = 0; s < SelectActivity.users.size(); s++) {
                if (SelectActivity.users.get(s).username.equals(user.username)) {
                    SelectActivity.User user1 = SelectActivity.users.get(s);
                    ArrayList<String> result = new ArrayList<>();
                    result.addAll(user1.like);
                    result.add(detail);
                    SelectActivity.User finalResult = new SelectActivity.User(user1.username, user1.password, user1.history, result);
                    SelectActivity.users.set(s, finalResult);
                }
            }
        });

        ArrayAdapter<String> estateDetail = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listDetails);
        details.setAdapter(estateDetail);
    }
}