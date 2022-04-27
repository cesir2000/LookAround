package es.ucm.fdi.lookaround;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private CategoriesResultListAdapter categoriesAdapter;
    private RecyclerView recyclerView;
    private String[] cardNames = {"Restaurantes", "Museos", "Parques", "Bares", "Monumentos", "Hoteles", "Divisas", "Aeropuertos", "Atracciones", "Autobuses", "Rental", "Taxi"};
    private String[] searchNames = {"restaurant", "museum", "park", "bar", "tourist_attraction", "lodging", "atm", "airport", "amusement_park", "bus_station", "car_rental", "taxi_stand"};
    String latitude;
    String longitude;
    private int[] images = {R.drawable.ic_restaurant_svg, R.drawable.ic_museum_svgrepo_com, R.drawable.ic_public_park_svgrepo_com, R.drawable.ic_beer_svgrepo_com,
            R.drawable.ic_monument_14_svgrepo_com, R.drawable.ic_hotel_svgrepo_com, R.drawable.ic_dollar_euro_money_exchange_svgrepo_com, R.drawable.ic_plane_svgrepo_com,
            R.drawable.ic_amusement_park_svgrepo_com, R.drawable.ic_bus_svgrepo_com, R.drawable.ic_car_svgrepo_com, R.drawable.ic_taxi_svgrepo_com};
    private TextView distanceText;
    private int distance;
    private SeekBar distanceBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        setContentView(R.layout.activity_main);
        // Names for the cards
        ArrayList<Pair<String, Integer>> categories = createCategories();    // Create all category names and vector images that are going to be shown on cards
        Map<String, String> searchNames = createSearchNamesDict();          // Create a dict to map card names with search names of Google API
        distanceText = findViewById(R.id.textViewDistance);
        distanceBar = findViewById(R.id.seekBar3);
        distanceBar.setProgress(0);
        distanceBar.setMax(50);
        distanceText.setText(0 + " Km.");
        distanceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance = progress*1000;
                distanceText.setText(progress + " Km.");
                categoriesAdapter.setDistance(distance);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        categoriesAdapter = new CategoriesResultListAdapter(this, categories, searchNames, latitude, longitude, distance);
        categoriesAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(categoriesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d("MainActivityLog","End of onCreate()");
    }

    // method to create a dict for the search in google API
    private Map<String, String> createSearchNamesDict() {
        Map<String, String> dict = new HashMap<>();

        for (int i = 0; i < cardNames.length; i++) {
            dict.put(cardNames[i], searchNames[i]);
        }

        return dict;
    }

    // method to create a list of all categories with their names and images
    private ArrayList<Pair<String, Integer>> createCategories() {
        ArrayList<Pair<String, Integer>> names = new ArrayList<Pair<String, Integer>>();

        for (int i = 0; i < cardNames.length; i++) {
            names.add(new Pair<>(cardNames[i], images[i]));
        }
        return names;
    }

    public void onHomeButtonClick(View view){

    }

    public void onMapsButtonClick(View view){
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void onSearchButtonClick(View view){
        Intent intent = new Intent(this, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }

}