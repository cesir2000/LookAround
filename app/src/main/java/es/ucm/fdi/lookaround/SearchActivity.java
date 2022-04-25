package es.ucm.fdi.lookaround;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemsResultListAdapter itemsResultListAdapter;
    private EditText editTextTextPlaceName;
    private ArrayList<ItemInfo> itemsList = new ArrayList<ItemInfo>();;
    private String latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        editTextTextPlaceName = findViewById(R.id.editTextTextPlaceName);
        recyclerView = (RecyclerView) findViewById(R.id.searchRecyclerView);
        itemsResultListAdapter = new ItemsResultListAdapter(this, itemsList);
        itemsResultListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(itemsResultListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                        "query=" + placeName +
                        "&location=" + latitude + "%2C" + longitude +
                        "&radius=1500" +
                        "&key=").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                itemsList = (ArrayList<ItemInfo>) ItemInfo.fromJsonResponse(responseData, latitude, longitude);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        itemsResultListAdapter.notifyDataSetChanged();

                    }
                });

            }
        });
    }


}