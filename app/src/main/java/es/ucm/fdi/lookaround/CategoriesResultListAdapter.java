package es.ucm.fdi.lookaround;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class CategoriesResultListAdapter extends RecyclerView.Adapter<CategoriesResultListAdapter.ItemViewHolder>{

    private LayoutInflater mInflater;
    private List<Pair<String, Integer>> categoryList;

    public CategoriesResultListAdapter(Context context, ArrayList<Pair<String, Integer>> itemList) {
        mInflater = LayoutInflater.from(context);
        this.categoryList = itemList;
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

    public void setBooksData(ArrayList<Pair<String, Integer>> data){
        this.categoryList = data;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private CategoriesResultListAdapter mAdapter;
        private ImageView imageView;
        private TextView categoryView;
        private TextView distanceView;


        public ItemViewHolder(View itemView, CategoriesResultListAdapter adapter) {
            super(itemView);
            this.imageView=itemView.findViewById(R.id.imageViewSVGContent);
            this.categoryView = itemView.findViewById(R.id.textViewTitleContent);
            this.mAdapter = adapter;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    RequestQueue queue = Volley.newRequestQueue(v.getContext());
                    String url ="http://www.google.com";
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Do something with response
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {}
                    });
                    queue.add(stringRequest);

                    distanceView = v.findViewById(R.id.distanceText);
                    Intent intentMain = new Intent(v.getContext(),
                            ItemListActivity.class);
                    v.getContext().startActivity(intentMain);
                    Log.i("Content "," Results Layout ");
                }
            });
        }

        public void setName(String title) {
            categoryView.setText(title);
        }

        public void setImage(int image) {
            imageView.setImageResource(image);
        }

    }
}
