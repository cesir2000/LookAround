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

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final int PERMISSION_ID = 44;
    private CategoriesResultListAdapter categoriesAdapter;
    private RecyclerView recyclerView;
    private String[] cardNames = {"Favorites","Restaurantes", "Museos", "Parques", "Bares", "Monumentos", "Hoteles", "Divisas", "Aeropuertos", "Atracciones", "Autobuses", "Rental", "Taxi"};
    private String[] searchNames = {"favorites","restaurant", "museum", "park", "bar", "tourist_attraction", "lodging", "atm", "airport", "amusement_park", "bus_station", "car_rental", "taxi_stand"};
    String latitude;
    String longitude;
    private int[] images = {R.drawable.ic_heart_filled_svgrepo_com, R.drawable.ic_restaurant_svg, R.drawable.ic_museum_svgrepo_com, R.drawable.ic_public_park_svgrepo_com, R.drawable.ic_beer_svgrepo_com,
            R.drawable.ic_monument_14_svgrepo_com, R.drawable.ic_hotel_svgrepo_com, R.drawable.ic_dollar_euro_money_exchange_svgrepo_com, R.drawable.ic_plane_svgrepo_com,
            R.drawable.ic_amusement_park_svgrepo_com, R.drawable.ic_bus_svgrepo_com, R.drawable.ic_car_svgrepo_com, R.drawable.ic_taxi_svgrepo_com};
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private TextView distanceText;
    private int distance;
    private SeekBar distanceBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(1000)
                .setNumUpdates(1);

        getLocation(); // Method to get location of the user
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


        // Get user last known location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        Log.d("MainActivityLog","End of onCreate()");
    }

    public void setLocation(String latitude, String longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        categoriesAdapter.setLocation(latitude, longitude);
        categoriesAdapter.notifyDataSetChanged();
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        // check if permissions are given
        if (checkPermissions()) {
            Log.d("PermissionsLog","Permissions are given");
            // check if location is enabled
            if (isLocationEnabled()) {
                Log.d("LocationLog","Location is enabled");

                LocationCallback mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult == null) {
                            Log.d("LocationLog", "There was an error getting the current location");
                            return;
                        }
                        for (Location location : locationResult.getLocations()) {
                            if (location != null) {
                                setLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                                Log.d("LocationLog", location.toString());
                            }
                        }
                    }
                };

                LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);

            } else {
                Log.d("LocationLog","Location is not enabled");
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            Log.d("PermissionsLog","Asking for permissions");
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = String.valueOf(mLastLocation.getLatitude());
            longitude = String.valueOf(mLastLocation.getLongitude());
        }
    };

    // Method to check if the permissions are on
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Method to see if the location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
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

    public void showPlaces(View view) {
        /*Get the place the user has pushed (restaurante, bar, museo, etc.)*/

        /*Start the activity. Create a new instance of the activity and create a new instance of an intent*/
        ItemListActivity calculatorResultActivity = new ItemListActivity();
        Intent it = new Intent(this, ItemListActivity.class);
        //it.putExtra("type", type_place);
        startActivity(it);

    }

    public void onHomeButtonClick(View view){
        /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivity(intent);*/
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

    @Override
    public void onLocationChanged(@NonNull Location location) {
        getLocation();
    }
}