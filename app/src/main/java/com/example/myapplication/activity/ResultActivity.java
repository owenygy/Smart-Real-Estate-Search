package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.internal.Details;
import com.example.myapplication.internal.DetailsDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.example.myapplication.activity.MainActivity.selectSort;

public class ResultActivity extends AppCompatActivity {
    Button homeButton;
    private List<ADs> aDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        readAD();
        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, SelectActivity.class);
                startActivity(intent);
            }
        });

        String ad1 = aDs.get(0).getAdvertisement();
        String ad2 = aDs.get(1).getAdvertisement();

        Intent intent = getIntent();
        final ArrayList<String> array = intent.getStringArrayListExtra("data");
        assert array != null;
        array.add(0, ad1);
        array.add(0, ad2);



        ListView listView = findViewById(R.id.resultList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Objects.requireNonNull(array));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
//            String detail = array.get(i);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)); // the encoding is optional
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,StandardCharsets.UTF_8));
//            try {
//                String line = reader.readLine();
//                while (line!=null){
//                    String[] tokens = line.split(",");
//                    if (tokens[0].equals(user.username)){
//                        writer.write(line+" "+detail);
//                        writer.flush();
//                        writer.close();
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            Intent intent_before = getIntent();
            SelectActivity.User user = (SelectActivity.User) intent_before.getExtras().getSerializable("User");
            for (int s = 0; s < SelectActivity.users.size(); s++) {
                if (SelectActivity.users.get(s).username.equals(user.username)) {
                    SelectActivity.User user1 = SelectActivity.users.get(s);
                    ArrayList<String> result = new ArrayList<>();
                    result.addAll(user1.history);
                    result.add(array.get(i));
                    SelectActivity.User finalResult = new SelectActivity.User(user1.username, user1.password, result, user1.like);
                    SelectActivity.users.set(s, finalResult);
                }
            }

            Intent intent1 = new Intent(ResultActivity.this, DetailActivity.class);
            if (i == 0 || i == 1) {
                Toast.makeText(ResultActivity.this, "Call 0412345678 to learn more", Toast.LENGTH_LONG).show();
            } else {
                intent1.putExtra("User", user);
                intent1.putExtra("details", array.get(i));
                startActivity(intent1);
            }
        });
        ArrayList<String> arrayList = new ArrayList<>();
        EditText editText = findViewById(R.id.resultEdit);
        Button button = findViewById(R.id.resultButton);

        final ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);

        button.setOnClickListener(view -> {
            array.add(editText.getText().toString());
            listView.setAdapter(adapter);

            String searchText = editText.getText().toString();
            String[] conditions = searchText.split(";");
            match(conditions);
        });

    }

    public class ADs {
        private String advertisement;

        public String getAdvertisement(){
            return advertisement;
        }

        public void setAdvertisement(String advertisement){
            this.advertisement = advertisement;
        }
    }


    private void readAD() {
        InputStream is = getResources().openRawResource(R.raw.ad);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line = "";
        try {
            while ((line = reader.readLine())!=null) {
                ADs ad = new ADs();
                ad.setAdvertisement(line);
                aDs.add(ad);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        Intent intent = new Intent(ResultActivity.this, ResultActivity.class);
        intent.putStringArrayListExtra("data", resultList);
        intent.putExtra("User", user);
        startActivity(intent);
    }
}