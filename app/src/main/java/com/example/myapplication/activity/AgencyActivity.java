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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AgencyActivity extends AppCompatActivity {
    ArrayList<String> agencyList;
    ArrayAdapter<String> adapter;
    Button addButton;
    Button deleteButton;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency);

        Intent intent = getIntent();
        Objects.requireNonNull(intent.getExtras()).getSerializable("User");

        lv = findViewById(R.id.history_listview);
        EditText idText = findViewById(R.id.inputID);
        EditText rentText = findViewById(R.id.inputRent);
        EditText bedroomText = findViewById(R.id.inputBedroom);
        EditText locationText = findViewById(R.id.inputLocation);
        EditText garageText = findViewById(R.id.inputGarage);
        EditText highlightsText = findViewById(R.id.inputHighlights);
        addButton = findViewById(R.id.add);
        deleteButton = findViewById(R.id.delete__button);

        agencyList = new ArrayList<>();
        ArrayList<String> listDetails = new ArrayList<>();
        adapter = new ArrayAdapter<>(AgencyActivity.this, android.R.layout.simple_list_item_1, listDetails);
        lv.setAdapter(adapter);

        addButton.setOnClickListener(view -> {
            // DB operation
            DetailsDB db = SelectActivity.getDetailsDB();
            int id = Integer.parseInt(idText.getText().toString());
            int rent = Integer.parseInt(rentText.getText().toString());
            int bedroom = Integer.parseInt(bedroomText.getText().toString());
            String location = locationText.getText().toString();
            int garage = Integer.parseInt(garageText.getText().toString());
            String highlights = highlightsText.getText().toString();
            Details details = new Details(id, rent, bedroom, location, garage, highlights);
            db.add(details);

            // ListView operation
            // Author: Guanyu Yi
            agencyList.clear();
            if (idText.getText().toString().equals("") ||
                    rentText.getText().toString().equals("") ||
                    bedroomText.getText().toString().equals("") ||
                    locationText.getText().toString().equals("") ||
                    garageText.getText().toString().equals("") ||
                    highlightsText.getText().toString().equals("")) {
                Toast.makeText(AgencyActivity.this, "Incomplete form!", Toast.LENGTH_SHORT).show();
            } else {
                agencyList.add("ID: " + idText.getText().toString() + " | ");
                agencyList.add("Rent: " + rentText.getText().toString() + " | ");
                agencyList.add("Bedroom: " + bedroomText.getText().toString() + " | ");
                agencyList.add("Location: " + locationText.getText().toString() + " | ");
                agencyList.add("Garage: " + garageText.getText().toString() + " | ");
                agencyList.add("Highlights: " + highlightsText.getText().toString());
                StringBuilder sb = new StringBuilder();
                for (String s : agencyList) {
                    sb.append(s);
                }
                listDetails.add(sb.toString());
                idText.setText("");
                rentText.setText("");
                bedroomText.setText("");
                locationText.setText("");
                garageText.setText("");
                highlightsText.setText("");
                adapter.notifyDataSetChanged();
            }
        });

        deleteButton.setOnClickListener(view -> {
            // DB operation
            DetailsDB db = SelectActivity.getDetailsDB();
            int id = Integer.parseInt(idText.getText().toString());
            int rent = Integer.parseInt(rentText.getText().toString());
            int bedroom = Integer.parseInt(bedroomText.getText().toString());
            String location = locationText.getText().toString();
            int garage = Integer.parseInt(garageText.getText().toString());
            String highlights = highlightsText.getText().toString();
            Details details = new Details(id, rent, bedroom, location, garage, highlights);
            try {
                db.delete(details);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // ListView operation
            // Author: Guanyu Yi
            for (int i = 0; i < listDetails.size(); i++) {
                if (idText.getText().toString().equals("") ||
                        rentText.getText().toString().equals("") ||
                        bedroomText.getText().toString().equals("") ||
                        locationText.getText().toString().equals("") ||
                        garageText.getText().toString().equals("") ||
                        highlightsText.getText().toString().equals("")) {
                    Toast.makeText(AgencyActivity.this, "Incomplete form!", Toast.LENGTH_SHORT).show();
                    break;
                }
                String byID = "ID: " + idText.getText().toString() + " ";
                String byRent = " Rent: " + rentText.getText().toString() + " ";
                String byBedroom = " Bedroom: " + bedroomText.getText().toString() + " ";
                String byLocation = " Location: " + locationText.getText().toString() + " ";
                String byGarage = " Garage: " + garageText.getText().toString() + " ";
                String byHighlights = " Highlights: " + highlightsText.getText().toString();
                if (listDetails.get(i).split("\\|")[0].equals(byID) &&
                        listDetails.get(i).split("\\|")[1].equals(byRent) &&
                        listDetails.get(i).split("\\|")[2].equals(byBedroom) &&
                        listDetails.get(i).split("\\|")[3].equals(byLocation) &&
                        listDetails.get(i).split("\\|")[4].equals(byGarage) &&
                        listDetails.get(i).split("\\|")[5].equals(byHighlights)) {
                    listDetails.remove(i);
                    idText.setText("");
                    rentText.setText("");
                    bedroomText.setText("");
                    locationText.setText("");
                    garageText.setText("");
                    highlightsText.setText("");
                    Toast.makeText(AgencyActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    break;
                } else {
                    idText.setText("");
                    rentText.setText("");
                    bedroomText.setText("");
                    locationText.setText("");
                    garageText.setText("");
                    highlightsText.setText("");
                    Toast.makeText(AgencyActivity.this, "No match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}