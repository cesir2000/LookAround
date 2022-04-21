package es.ucm.fdi.lookaround;

import android.content.Context;
import android.content.Intent;
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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ItemsResultListAdapter mAdapter;
        private TextView titleView;
        private TextView distanceview;
        private TextView timeView;

        public ItemViewHolder(View itemView, ItemsResultListAdapter adapter) {
            super(itemView);
            this.titleView = itemView.findViewById(R.id.textViewTitleContentPlace);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void setName(String name) {
            titleView.setText(name);
        }

        public void setDistance(String distance) { distanceview.setText(distance); }

        public void setTime(String time) { timeView.setText(time); }

    }

}
