package es.ucm.fdi.lookaround;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    protected ArrayList<ItemInfo> itemsList;
    protected ItemsResultListAdapter itemsAdapter;
    protected RecyclerView recyclerView;
    protected Button filterButton;

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
        filterButton = findViewById(R.id.buttonFilter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpWindow popUpWindow = new PopUpWindow();
                popUpWindow.showPopupWindow(v, findViewById(android.R.id.content));
            }
        });
    }



}