package es.ucm.fdi.lookaround;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/*import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;*/

import java.util.ArrayList;

public class ItemsResultListAdapter extends RecyclerView.Adapter<ItemsResultListAdapter.ItemViewHolder>{
    private LayoutInflater mInflater;
    private ArrayList<ItemInfo> items;

    public ItemsResultListAdapter(Context context, ArrayList<ItemInfo> items) {
        mInflater = LayoutInflater.from(context);
        this.items = items;
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
        holder.setDistance(items.get(position).getDistance());
        holder.setRating(items.get(position).getRating(), items.get(position).getTotalRatings());
        holder.setOpen(items.get(position).getOpen());
        holder.setTimeCar(items.get(position).getTimeCar());
        holder.setTimeWalking(items.get(position).getTimeWalking());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ItemsResultListAdapter mAdapter;
        private TextView titleView;
        private TextView distanceView;
        private TextView timeCarView;
        private TextView timeWalkingView;
        private TextView ratingView;
        private TextView openView;

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
                public void onClick(View v) {}
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

    }

}
