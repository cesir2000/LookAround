package es.ucm.fdi.lookaround;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    private ArrayList<ItemInfo> itemsList;
    private ItemsResultListAdapter itemsAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        itemsList = (ArrayList<ItemInfo>) getIntent().getSerializableExtra("itemsList");

        // Recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewItems);
        itemsAdapter = new ItemsResultListAdapter(this, itemsList);
        itemsAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}