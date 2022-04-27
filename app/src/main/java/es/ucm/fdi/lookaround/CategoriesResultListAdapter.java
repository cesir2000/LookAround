package es.ucm.fdi.lookaround;

import static es.ucm.fdi.lookaround.ItemInfo.stringToObjectS;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CategoriesResultListAdapter extends RecyclerView.Adapter<CategoriesResultListAdapter.ItemViewHolder> {

    private LayoutInflater mInflater;
    private List<Pair<String, Integer>> categoryList;
    private Map<String, String> searchNames;
    private String latitude;
    private String longitude;
    private int distance;
    private SharedPreferences sharedPreferences;



    public CategoriesResultListAdapter(Context context, ArrayList<Pair<String, Integer>> itemList, Map<String, String> searchNames, String latitude, String longitude, int distance) {
        mInflater = LayoutInflater.from(context);
        this.categoryList = itemList;
        this.searchNames = searchNames;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate an item view.
        View mItemView = mInflater.inflate(R.layout.category_item, parent, false);
        return new ItemViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) { // For each category card on the main screen
        String name = categoryList.get(position).first;
        int image = categoryList.get(position).second;
        holder.setName(categoryList.get(position).first);
        holder.setImage(categoryList.get(position).second);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setDistance(int distance){this.distance = distance;}

    public void setLocation(String latitude, String longitude) {this.longitude = longitude; this.latitude = latitude;}

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private Handler handler;
        private CategoriesResultListAdapter mAdapter;
        private ImageView imageView;
        private TextView categoryView;
        private List<ItemInfo> itemsList;
        private ProgressBar progressBar;


        public ItemViewHolder(View itemView, CategoriesResultListAdapter adapter) {
            super(itemView);
            this.imageView=itemView.findViewById(R.id.imageViewSVGContent);
            this.categoryView = itemView.findViewById(R.id.textViewTitleContent);
            this.progressBar = itemView.findViewById(R.id.progressBar4);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.INVISIBLE);
            this.mAdapter = adapter;
            this.handler = new Handler();


            // Once one card of the categories is clicked, the searh by category is done and new activity is started with the data received
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String distanceText;
                    String categoryClicked = categoryView.getText().toString();
                    if (!categoryClicked.equals("Favorites")){

                            new Thread() {
                                @Override
                                public void run() {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }.start();

                        //https://developers.google.com/maps/documentation/places/web-service/search-nearby
                        //https://console.cloud.google.com/projectselector2/apis/dashboard?pli=1&supportedpurview=project api create account

                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                                        "&location=" + latitude + "%2C" + longitude +
                                        "&radius=" + distance +
                                        "&type=" + searchNames.get(categoryView.getText().toString()) +
                                        "&key=AIzaSyD7zEUdA01mZPjRmufqJj5PzdtzZuudwxg").build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData = response.body().string();
                                Log.d("RequestLog", latitude + " " + longitude + " " + distance);
                                itemsList = ItemInfo.fromJsonResponse(responseData, latitude, longitude);
                                Intent intentMain = new Intent(v.getContext(),
                                        ItemListActivity.class);
                                intentMain.putExtra("itemsList", (Serializable) itemsList);
                                v.getContext().startActivity(intentMain);
                                Log.i("Content ", " Results Layout ");
                                new Thread() {
                                    @Override
                                    public void run() {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    }
                                }.start();

                        }
      
       
                    else{
                        itemsList = new ArrayList<ItemInfo>();
                        // LOADING favs from storage
                        //int favoritesCounter = sharedPreferences.getInt("FavoritesCounter", 0);
                        /*for (int i = 0; i < favoritesCounter; i++){
                            String favoriteId = "FavoriteId" + i;
                            String placeIdKey = sharedPreferences.getString(favoriteId, "");
                            String place = sharedPreferences.getString(placeIdKey, "");
                            ItemInfo obj = stringToObjectS(place);
                            if(obj != null){
                                itemsList.add(obj);
                            }
                        }*/
                        Map<String,?> keys = sharedPreferences.getAll();

                        for(Map.Entry<String,?> entry : keys.entrySet()){
                            String place = sharedPreferences.getString(entry.getKey(), "");
                            ItemInfo obj = stringToObjectS(place);
                            if(obj != null){
                                itemsList.add(obj);
                            }
                        }
                        Intent intentMain = new Intent(v.getContext(),
                                ItemListActivity.class);
                        intentMain.putExtra("itemsList", (Serializable) itemsList);
                        v.getContext().startActivity(intentMain);
                    }
                }
            });
        }

        public void setName(String name) {
            categoryView.setText(name);
        }

        public void setImage(int image) {
            imageView.setImageResource(image);
        }

    }
}
