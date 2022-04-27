package es.ucm.fdi.lookaround;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private ItemsResultListAdapter itemsResultListAdapter;
    private EditText editTextTextPlaceName;
    private ArrayList<ItemInfo> itemsList = new ArrayList<ItemInfo>();;
    private String latitude, longitude;
    private SeekBar distanceBar;
    private TextView distanceText;
    private String category;
    private int distance;
    private String[] cardNames = {"Restaurantes", "Museos", "Parques", "Bares", "Monumentos", "Hoteles", "Divisas"};
    private String[] searchNames = {"restaurant", "museum", "park", "bar", "tourist_attraction", "hotel", "atm"};
    private Handler handler;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        distanceText = findViewById(R.id.textViewDistance);
        distance = 0;
        distanceBar = findViewById(R.id.seekBar3);
        distanceBar.setProgress(0);
        distanceBar.setMax(50);
        distanceText.setText(0 + " Km.");
        distanceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance = progress*1000;
                distanceText.setText(progress + " Km.");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        editTextTextPlaceName = findViewById(R.id.editTextTextPlaceName);
        recyclerView = (RecyclerView) findViewById(R.id.searchRecyclerView);
        itemsResultListAdapter = new ItemsResultListAdapter(this, itemsList);
        itemsResultListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(itemsResultListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Spinner spin = findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cardNames);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(ad);
        this.progressBar = findViewById(R.id.progressBar);
        this.handler = new Handler();
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

    }

    public void onSearchPlacesButtonClick(View view){
        String placeName = editTextTextPlaceName.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        // To hide keyboard on click
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                        "keyword=" + placeName +
                        "&location=" + latitude + "%2C" + longitude +
                        "&type="+ category +
                        "&radius="+ distance +
                        "&key=AIzaSyD7zEUdA01mZPjRmufqJj5PzdtzZuudwxg").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("RequestLog", latitude+" "+longitude + " "+ distance + " " + category);
                Log.d("ResponseLog", responseData);
                itemsList = (ArrayList<ItemInfo>) ItemInfo.fromJsonResponse(responseData, latitude, longitude);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemsResultListAdapter.setList(itemsList);
                        itemsResultListAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = searchNames[position];
        Log.d("CategoryLog", category);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}