package es.ucm.fdi.lookaround;

import android.content.Context;
import android.media.Image;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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
        holder.setName("name");
        //holder.setImage(image);
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
        private TextView imageView;
        private TextView categoryView;


        public ItemViewHolder(View itemView, CategoriesResultListAdapter adapter) {
            super(itemView);
            this.categoryView = itemView.findViewById(R.id.textViewTitleContent);
            this.mAdapter = adapter;
        }

        public void setName(String title) {
            categoryView.setText(title);
        }

        public void setImage(int authors) {
            imageView.setText(authors);
        }

    }
}
