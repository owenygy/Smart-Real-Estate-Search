package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.internal.Details;
import com.example.myapplication.internal.DetailsDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button history = findViewById(R.id.historyButton);

        history.setOnClickListener(v -> {
            Intent intent_before = getIntent();
            SelectActivity.User user = (SelectActivity.User) intent_before.getExtras().getSerializable("User");
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra("User", user);
            startActivity(intent);
        });

        Button like = findViewById(R.id.likeButton_);
        like.setOnClickListener(v -> {
            Intent intent_before = getIntent();
            SelectActivity.User user = (SelectActivity.User) intent_before.getExtras().getSerializable("User");
            Intent intent = new Intent(MainActivity.this, LikeActivity.class);
            intent.putExtra("User", user);
            startActivity(intent);
        });

        ArrayList<String> array = new ArrayList<>();
        ListView list = findViewById(R.id.history);
        Button button = findViewById(R.id.supportButton);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array);

        EditText searchEditText = findViewById(R.id.searchText);
        Button mainGo = findViewById(R.id.searchBtn);
        mainGo.setOnClickListener(view -> {
            array.add(searchEditText.getText().toString());
            list.setAdapter(adapter);

            String searchText = searchEditText.getText().toString();
            String[] conditions = searchText.split(";");
            match(conditions);
        });

        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SupportActivity.class);
            startActivity(intent);
        });

        list.setOnItemClickListener((adapterView, view, i, l) -> {
            String detail = array.get(i);
            String[] conditions = detail.split(";");
            match(conditions);
        });
    }

    /**
     * Matches specified conditions and start the ResultActivity.
     *
     * @param conditions condition to check
     */
    private void match(String[] conditions) {
        DetailsDB detailsDB = SelectActivity.getDetailsDB();
        // Parse search conditions
        Set<Details> runningMatched;
        try {
            // Evaluate the first condition
            runningMatched = detailsDB.eval(conditions[0]);
            // Then take the intersection with each subsequent condition
            for (int i = 1; i < conditions.length; i++) {
                runningMatched.retainAll(detailsDB.eval(conditions[i]));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Incorrect search condition", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert set into ArrayList so that we can run selectSort()
        ArrayList<Details> finalMatched = new ArrayList<>(runningMatched);
        selectSort(finalMatched); // modifies the ArrayList in-place

        // As string list
        ArrayList<String> resultList = new ArrayList<String>() {{
            for (Details d : finalMatched) {
                add(d.toString());
            }
        }};
        // Pass the result to ResultActivity
        Intent intent_before = getIntent();
        SelectActivity.User user = (SelectActivity.User) intent_before.getExtras().getSerializable("User");
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putStringArrayListExtra("data", resultList);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    public static void selectSort(List<Details> details) {
        int number = details.size();
        for (int i = 0; i < number - 1; i++) {
            int min = i;
            for (int j = i + 1; j < number; j++) {
                if (details.get(min).rent > details.get(j).rent) min = j;
            }
            Details temp = details.get(i);
            details.set(i, details.get(min));
            details.set(min, temp);
        }
    }


}