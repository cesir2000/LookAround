package es.ucm.fdi.lookaround;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/*import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;*/


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ItemsResultListAdapter extends RecyclerView.Adapter<ItemsResultListAdapter.ItemViewHolder>{
    private LayoutInflater mInflater;
    private ArrayList<ItemInfo> items;
    private double distance;
    private double timeCar;
    private double timeWalking;
    private double rating;


    public ItemsResultListAdapter(Context context, ArrayList<ItemInfo> items) {
        mInflater = LayoutInflater.from(context);
        this.items = items;
        this.distance = -1;
        this.timeCar = -1;
        this.timeWalking = -1;
        this.rating = -1;
    }

    @Override
    public ItemsResultListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate an item view.
        View mItemView = mInflater.inflate(R.layout.result_item, parent, false);
        return new ItemsResultListAdapter.ItemViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(ItemsResultListAdapter.ItemViewHolder holder, int position) {
        holder.setName(items.get(position).getName());
        //holder.setDistance(items.get(position).getDistance());
        holder.setRating(items.get(position).getRating(), items.get(position).getTotalRatings());
        holder.setOpen(items.get(position).getOpen());
        holder.setLatitude(items.get(position).getLatitude());
        holder.setLongitude(items.get(position).getLongitude());
        holder.setPlaceId(items.get(position).getPlaceId());
        //holder.setTimeCar(items.get(position).getTimeCar());
        //holder.setTimeWalking(items.get(position).getTimeWalking());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setFilters(double distance, double timeCar, double timeWalking, double rating){
        this.distance=distance;
        this.timeCar=timeCar;
        this.timeWalking=timeWalking;
        this.rating=rating;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ItemsResultListAdapter mAdapter;
        private TextView titleView;
        private TextView distanceView;
        private TextView timeCarView;
        private TextView timeWalkingView;
        private TextView ratingView;
        private TextView openView;
        private String place_id;
        private String latitude;
        private String longitude;


        public ItemViewHolder(View itemView, ItemsResultListAdapter adapter) {
            super(itemView);
            this.titleView = itemView.findViewById(R.id.textViewTitleContentPlace);

            this.timeCarView = itemView.findViewById(R.id.textViewCar);
            this.timeWalkingView = itemView.findViewById(R.id.textViewWalking);
            this.distanceView = itemView.findViewById(R.id.textViewDistanceContent);
            this.ratingView = itemView.findViewById(R.id.textViewRating);
            this.openView = itemView.findViewById(R.id.textViewOpen);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query="+ latitude +","+longitude+"&query_place_id="+place_id);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    v.getContext().startActivity(mapIntent);

                }
            });
        }

        public void setName(String name) {
            titleView.setText(name);
        }

        public void setDistance(String distance) { distanceView.setText(distance); }

        public void setTimeWalking(String walk_time) { timeWalkingView.setText(walk_time); }


        public void setTimeCar(String car_time) { timeCarView.setText(car_time); }
        
        public void setRating(double rating, int total_ratings) {  ratingView.setText(Double.toString(rating)+"("+total_ratings+")"); }

        public void setOpen(boolean open) {
            if(open) openView.setText("Abierto");
            else openView.setText("Cerrado");
        }



        public void setPlaceId(String place_id) {
            this.place_id = place_id;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }

}
