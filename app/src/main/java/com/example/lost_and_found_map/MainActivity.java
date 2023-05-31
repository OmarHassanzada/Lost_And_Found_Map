package com.example.lost_and_found_map;


import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button NewAdvert;
    Button ViewAdverts;

    Button ViewMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NewAdvert = findViewById(R.id.btnCreateAdvert);
        ViewAdverts = findViewById(R.id.btnShowItems);
        ViewMap = findViewById(R.id.btnShowOnMap);

        //Intent to create a new advert
        NewAdvert.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateAdvertActivity.class);
            startActivity(intent);
        });

        //Intent for viewing list of all adverts
        ViewAdverts.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ShowItemsActivity.class);
            startActivity(intent);
        });

        //Intent for viewing map
        ViewMap.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        });
    }
}
