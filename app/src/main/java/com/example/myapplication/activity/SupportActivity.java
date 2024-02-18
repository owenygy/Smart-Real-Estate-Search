package com.example.myapplication.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        ListView listView = findViewById(R.id.supportList);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1. Select 'AS CUSTOMER' or 'AS AGENCY' to enter different mode.");
        arrayList.add("2. The key words in customer search page are 'rent', 'bedroom', 'location', 'garage' and 'highlights'.");
        arrayList.add("2.1. Use the key words by plus comparison symbols, separate by semicolon.");
        arrayList.add("2.2. Examples: (rent>=400), (bedroom=4; garage=1), (rent>=200; bedroom<3; location='city'; garage=1; highlights=near 'ANU'), etc.");
        arrayList.add("3. For the Agency mode, users can manage the estates by text the details then click add button or delete button.");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
    }
}