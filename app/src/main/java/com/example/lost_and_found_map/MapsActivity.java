package com.example.lost_and_found_map;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lost_and_found_map.databinding.ActivityMapsBinding;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LostFoundDAO lostFoundDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lostFoundDAO = new LostFoundDAO(this);
        lostFoundDAO.open();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lostFoundDAO.close();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        retrieveLostFoundItems();
    }

    private void retrieveLostFoundItems() {
        List<LostFoundItem> lostFoundItems = lostFoundDAO.getAllItems();

        for (LostFoundItem item : lostFoundItems) {
            LatLng location = new LatLng(item.getLatitude(), item.getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title(item.getName()));
        }

        if (!lostFoundItems.isEmpty()) {
            LatLng firstItemLocation = new LatLng(lostFoundItems.get(0).getLatitude(), lostFoundItems.get(0).getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstItemLocation, 10));
        }
    }
}
