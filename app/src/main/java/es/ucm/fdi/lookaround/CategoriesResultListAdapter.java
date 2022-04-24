package es.ucm.fdi.lookaround;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/*import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;*/

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private TextView distance;

    public CategoriesResultListAdapter(Context context, ArrayList<Pair<String, Integer>> itemList, Map<String, String> searchNames, String latitude, String longitude, TextView distance) {
        mInflater = LayoutInflater.from(context);
        this.categoryList = itemList;
        this.searchNames = searchNames;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate an item view.
        View mItemView = mInflater.inflate(R.layout.category_item, parent, false);
        return new ItemViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String name = categoryList.get(position).first;
        int image = categoryList.get(position).second;
        holder.setName(categoryList.get(position).first);
        holder.setImage(categoryList.get(position).second);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setBooksData(ArrayList<Pair<String, Integer>> data) {
        this.categoryList = data;
    }

    public void setLocation(String latitude, String longitude) {this.longitude = longitude; this.latitude = latitude;}

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private CategoriesResultListAdapter mAdapter;
        private ImageView imageView;
        private TextView categoryView;
        private List<ItemInfo> itemsList;


        public ItemViewHolder(View itemView, CategoriesResultListAdapter adapter) {
            super(itemView);
            this.imageView=itemView.findViewById(R.id.imageViewSVGContent);
            this.categoryView = itemView.findViewById(R.id.textViewTitleContent);
            this.mAdapter = adapter;

            // Once one card of the categories is clicked, the searh by category is done and new activity is started with the data received
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String distanceText;
                    if (!distance.getText().toString().equals("")) {
                        distanceText = Double.parseDouble(distance.getText().toString())*1000+"";
                    }
                    else {
                        distanceText = "10000";  // Default distance set to 10km
                    }

                    //https://developers.google.com/maps/documentation/places/web-service/search-nearby
                    //https://console.cloud.google.com/projectselector2/apis/dashboard?pli=1&supportedpurview=project api create account

                    CountDownLatch countDownLatch = new CountDownLatch(1);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                                    "&location=" + latitude + "%2C" + longitude +
                                    "&radius="+distanceText+
                                    "&type=" + searchNames.get(categoryView.getText().toString()) +
                                    "&key=AIzaSyD7zEUdA01mZPjRmufqJj5PzdtzZuudwxg").build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            itemsList = ItemInfo.fromJsonResponse(responseData, latitude, longitude);
                            countDownLatch.countDown();
                        }

                    });

                    /*itemsList = new ArrayList<ItemInfo>();
                    ItemInfo tmpItem = new ItemInfo();
                    tmpItem.setName("Restaurante");
                    tmpItem.setDistance("12");
                    tmpItem.setRating(4.3);
                    tmpItem.setTotalRatings(43);
                    tmpItem.setOpen(true);
                    tmpItem.setTimeCar("2");
                    tmpItem.setTimeWalking("10");
                    itemsList.add(tmpItem);
                    ItemInfo tmpItem2 = new ItemInfo();
                    tmpItem2.setName("Restaurante El Pepe");
                    tmpItem2.setDistance("500");
                    tmpItem2.setRating(3.4);
                    tmpItem2.setTotalRatings(1120);
                    tmpItem2.setOpen(false);
                    tmpItem2.setTimeCar("5");
                    tmpItem2.setTimeWalking("15");
                    itemsList.add(tmpItem2);
                    ItemInfo tmpItem3 = new ItemInfo();
                    tmpItem3.setName("Lugar muy lejano");
                    tmpItem3.setDistance("340");
                    tmpItem3.setRating(1.1);
                    tmpItem3.setTotalRatings(120);
                    tmpItem3.setOpen(true);
                    tmpItem3.setTimeCar("30");
                    tmpItem3.setTimeWalking("1");
                    itemsList.add(tmpItem3);
                  */

                    // Wait for the request to finish
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intentMain = new Intent(v.getContext(),
                            ItemListActivity.class);
                    intentMain.putExtra("itemsList", (Serializable) itemsList);
                    v.getContext().startActivity(intentMain);
                    Log.i("Content ", " Results Layout ");
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
