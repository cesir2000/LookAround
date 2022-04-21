package es.ucm.fdi.lookaround;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import es.ucm.fdi.lookaround.databinding.ActivityMapBinding;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBinding binding;
    private SearchView searchView;
    private RadioGroup radioGroup;
    private RadioButton radioButtonLocationName;
    private RadioButton radioButtonCoordinates;
    private TextView textErrorOnSearch;
    private Marker actualMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        searchView = findViewById(R.id.idSearchView);
        radioGroup = findViewById(R.id.RadGroup);
        radioButtonCoordinates = findViewById(R.id.radioButtonCoordinates);
        radioButtonLocationName = findViewById(R.id.radioButtonLocationName);
        textErrorOnSearch = findViewById(R.id.textViewErrorOnSearch);
        radioGroup.check(radioButtonCoordinates.getId());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (checkedId == R.id.radioButtonCoordinates)
                {
                    radioButtonCoordinates.setBackgroundResource(R.drawable.button_background1);
                    radioButtonLocationName.setBackgroundResource(R.drawable.button_background2);

                }

                else if (checkedId == R.id.radioButtonLocationName)
                {
                    radioButtonCoordinates.setBackgroundResource(R.drawable.button_background2);
                    radioButtonLocationName.setBackgroundResource(R.drawable.button_background1);
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on below line we are getting the
                // location name from search view.
                // also removing the previous marker
                if (actualMarker != null) {
                    actualMarker.remove();
                }
                String location = searchView.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(MapActivity.this);
                    if (radioGroup.getCheckedRadioButtonId() == radioButtonCoordinates.getId()){

                        if (location.indexOf(",") < 0) {
                            textErrorOnSearch.setText("Coordenadas incorrectas! Revisalas!");
                            return false;
                        }
                        else {
                            double latitude, longitude;
                            try {
                                latitude = Double.parseDouble(location.substring(0, location.indexOf(",")-1));
                            } catch (NumberFormatException nfe) {
                                textErrorOnSearch.setText("Coordenadas incorrectas! Revisalas!");
                                return false;
                            }
                            try {
                                longitude = Double.parseDouble(location.substring(location.indexOf(",") + 1, location.length()));
                            } catch (NumberFormatException nfe) {
                                textErrorOnSearch.setText("Coordenadas incorrectas! Revisalas!");
                                return false;
                            }
                            Log.d("MapLog", "Latitude is " + latitude + ", Longitude is " + longitude);
                            try {
                                // on below line we are getting location from the
                                // location name and adding that location to address list.
                                addressList = geocoder.getFromLocation(latitude, longitude, 1);
                            } catch (IOException e) {
                                textErrorOnSearch.setText("Coordenadas incorrectas! Revisalas!");
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        try {
                            // on below line we are getting location from the
                            // location name and adding that location to address list.
                            addressList = geocoder.getFromLocationName(location, 1);
                        } catch (IOException e) {
                            textErrorOnSearch.setText("Nombre incorrecto! Revisalo!");
                            e.printStackTrace();
                        }
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    if(addressList != null && addressList.size() == 0){
                        textErrorOnSearch.setText("No hay resultados, intenta de nuevo!");
                    }
                    else {
                        Address address = addressList.get(0);
                        textErrorOnSearch.setText("");

                        // on below line we are creating a variable for our location
                        // where we will add our locations latitude and longitude.
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        // on below line we are adding marker to that position.
                        actualMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(location));

                        // below line is to animate camera to that position.
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        // at last we calling our map fragment to update.
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng actualLocation = new LatLng(40.45, -3.74);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(actualLocation));
    }

    public void onHomeButtonClick(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);    // To delete animation when changing screens
        startActivity(intent);
    }

    public void onMapsButtonClick(View view){
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void onSearchButtonClick(View view){
        Intent intent = new Intent(this, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}