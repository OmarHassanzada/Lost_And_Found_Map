package com.example.lost_and_found_map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class CreateAdvertActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private double latitude;
    private double longitude;

    private RadioGroup radioGroupPostType;
    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextDescription;
    private EditText editTextDate;
    //private EditText editTextLocation;
    private Button btnSave;
    private Button btnGetCurrentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng currentLocation;
    private LostFoundDAO lostFoundDAO;
    AutocompleteSupportFragment AutoCompleteLocation;//this is the fragment to get the auto completed address

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        // Initialize Places API with your API key
        String apiKey = "";//API key taken off due to security reasons as i was unable to restrict it
        Places.initialize(getApplicationContext(), apiKey);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        lostFoundDAO = new LostFoundDAO(this);
        lostFoundDAO.open();

        radioGroupPostType = findViewById(R.id.radioGroupPostType);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.editTextDate);
        //editTextLocation = findViewById(R.id.editTextLocation);
        btnSave = findViewById(R.id.btnSave);
        btnGetCurrentLocation = findViewById(R.id.btnGetCurrentLocation);

        btnSave.setOnClickListener(v -> saveItem());
        btnGetCurrentLocation.setOnClickListener(v -> retrieveCurrentLocation());
        AutoCompleteLocation = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);



        // Set up AutocompleteSupportFragment
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("AU"); // Replace with your country code (e.g., "US")
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                String placeName = place.getName();
                //we define atlng to retireive longitude and latitude
                LatLng placeLatLng = place.getLatLng();

                if (placeName != null && placeLatLng != null) {
                    //editTextLocation.setText(placeName);
                    currentLocation = placeLatLng;

                    // Retrieve latitude and longitude from the selected place
                    latitude = placeLatLng.latitude;
                    longitude = placeLatLng.longitude;

                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("PlaceAutocomplete", "An error occurred: " + status);
            }
        });


    }

    private void retrieveCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Geocoder geocoder = new Geocoder(CreateAdvertActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                String address = addresses.get(0).getAddressLine(0);
                                AutoCompleteLocation.setText(address);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            retrieveCurrentLocation();
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    // ...

    private void saveItem() {
        // Getting all input variables
        int selectedPostTypeId = radioGroupPostType.getCheckedRadioButtonId();
        String postType = ((RadioButton) findViewById(selectedPostTypeId)).getText().toString();
        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String description = editTextDescription.getText().toString();
        String date = editTextDate.getText().toString();
        String location = AutoCompleteLocation.toString();


        if (currentLocation != null) {
            latitude = currentLocation.latitude;
            longitude = currentLocation.longitude;
        }

        //new item object to insert into the database
        long itemId = lostFoundDAO.insertItem(postType, name, phone, description, date, location, latitude, longitude);

        // Item is saved successfully, toast is shown
        if (itemId != -1) {
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Item Saved Successfully!", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
        // Item has failed to save, and then a toast is shown
        else {
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Failed to Save Item!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        lostFoundDAO.close();
        super.onDestroy();
    }
}
