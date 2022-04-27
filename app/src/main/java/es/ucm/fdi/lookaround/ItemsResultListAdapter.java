package es.ucm.fdi.lookaround;

import static es.ucm.fdi.lookaround.ItemInfo.objectToString;
import static es.ucm.fdi.lookaround.ItemInfo.stringToObjectS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private SharedPreferences sharedPreferences;

    public ItemsResultListAdapter(Context context, ArrayList<ItemInfo> items) {
        mInflater = LayoutInflater.from(context);
        this.items = items;
        this.distance = -1;
        this.timeCar = -1;
        this.timeWalking = -1;
        this.rating = -1;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public ItemsResultListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate an item view.
        View mItemView = mInflater.inflate(R.layout.result_item, parent, false);
        return new ItemsResultListAdapter.ItemViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(ItemsResultListAdapter.ItemViewHolder holder, int position) {  // For each item card inside a given category such as Restaurantes
        holder.setName(items.get(position).getName());
        holder.setDistance(items.get(position).getDistance());
        holder.setRating(items.get(position).getRating(), items.get(position).getTotalRatings());
        holder.setOpen(items.get(position).getOpen());
        holder.setLatitude(items.get(position).getLatitude());
        holder.setLongitude(items.get(position).getLongitude());
        holder.setPlaceId(items.get(position).getPlaceId());
        holder.setTimeCar(items.get(position).getTimeCar());
        holder.setTimeWalking(items.get(position).getTimeWalking());
        if(sharedPreferences.getString("MyFavoritePlace:"+items.get(position).getPlaceId(), "") == ""){
            holder.setHeart(false);
        }
        else{
            holder.setHeart(true);
        }
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

    public void setList(ArrayList<ItemInfo> itemsList) {
        this.items = itemsList;
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
        private ImageView heart;
        private boolean heartSelected;


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

            this.heart = itemView.findViewById(R.id.imageViewFavorite);
            this.heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: If heart clicked, delete from favourites, else add it to favourites
                    String placeId = items.get(getAdapterPosition()).getPlaceId();
                    //int value = sharedPreferences.getInt("FavoritesCounter", 0);
                    System.out.println("HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEREEEEEEEEEEEEEEEE 11111111111111111111111111");
                    System.out.println(getAdapterPosition());
                    if (heartSelected) {
                        heart.setImageResource(R.drawable.ic_heart_svgrepo_com);
                        sharedPreferences.edit().remove("MyFavoritePlace:"+placeId).apply();
                        //sharedPreferences.edit().putInt("FavoritesCounter", value-1).commit();
                        heartSelected = false;
                    }
                    else {
                        //sharedPreferences.edit().putString("MyFavoritePlace", objectToString( items.get(getAdapterPosition()))).commit();
                        //String favoriteId = "FavoriteId" + value;
                        String favoritePlace = "MyFavoritePlace:" + placeId;
                        System.out.println(placeId);

                        //sharedPreferences.edit().putString(favoriteId, favoritePlace).commit();
                        sharedPreferences.edit().putString(favoritePlace, objectToString( items.get(getAdapterPosition()))).commit();
                        //sharedPreferences.edit().putInt("FavoritesCounter", value+1).commit();
                        heart.setImageResource(R.drawable.ic_heart_filled_svgrepo_com);
                        heartSelected = true;
                    }


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

        public void setHeart(Boolean heartSelected) {
            this.heartSelected = heartSelected;
            if(heartSelected) {
                heart.setImageResource(R.drawable.ic_heart_filled_svgrepo_com);
            }
            else{
                heart.setImageResource(R.drawable.ic_heart_svgrepo_com);
            }
        }
    }

}
