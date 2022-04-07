package es.ucm.fdi.lookaround;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoriesResultListAdapter extends RecyclerView.Adapter<CategoriesResultListAdapter.ItemViewHolder> {

    private LayoutInflater mInflater;
    private List<Pair<String, Integer>> categoryList;
    private Map<String, String> searchNames;
    private String latitude;
    private String longitude;

    public CategoriesResultListAdapter(Context context, ArrayList<Pair<String, Integer>> itemList, Map<String, String> searchNames, String latitude, String longitude) {
        mInflater = LayoutInflater.from(context);
        this.categoryList = itemList;
        this.searchNames = searchNames;
        this.latitude = latitude;
        this.longitude = longitude;
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
        //holder.setImage(categoryList.get(position).second);
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
        private TextView imageView;
        private TextView categoryView;
        private TextView distanceView;
        private List<ItemInfo> itemsList;


        public ItemViewHolder(View itemView, CategoriesResultListAdapter adapter) {
            super(itemView);
            this.categoryView = itemView.findViewById(R.id.textViewTitleContent);
            this.mAdapter = adapter;

            // Once one card of the categories is clicked, the searh by category is done and new activity is started with the data received
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    distanceView = v.findViewById(R.id.distanceText);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                                    "keyword=" + searchNames.get(categoryView.getText().toString()) +
                                    "&location=" + latitude + "%2C" + longitude +
                                    "&type=" + searchNames.get(categoryView.getText().toString()) +
                                    "&rankby=distance"+
                                    "&key=AIzaSyDTOWVjYUuUg1RESfwrVCET2xGWL92rug4").build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {

                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            String responseData = response.body().string();
                            itemsList = ItemInfo.fromJsonResponse(responseData, latitude, longitude);
                        }
                    });

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

        public void setImage(int authors) {
            imageView.setText(authors);
        }

    }
}
