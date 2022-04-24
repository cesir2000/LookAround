package es.ucm.fdi.lookaround;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

    //Funcion que se activa cuando el usuario aplique los filtros.
    public void setFilters(View view) {
        //poner los argumentos de los filtros mediante el R.id.<nombre del textview>. Necesario modificar el result_item.xml layout
        EditText filterDistance = findViewById(R.id.editTextNumberDecimalFilter);
        EditText filterCar = findViewById(R.id.editTextNumberDecimalFilterCar);
        EditText filterRating = findViewById(R.id.editTextNumberDecimalFilterRating);
        EditText filterWalking = findViewById(R.id.editTextNumberDecimalFilterWalking);
        double distance;
        double timeCar;
        double timeWalking;
        double rating;
        if(filterDistance.getText().toString().isEmpty()) distance = -1;
        else distance = Double.parseDouble(filterDistance.getText().toString());
        if(filterCar.getText().toString().isEmpty()) timeCar = -1;
        else timeCar = Double.parseDouble(filterCar.getText().toString());
        if(filterRating.getText().toString().isEmpty()) rating = -1;
        else rating = Double.parseDouble(filterRating.getText().toString());
        if(filterWalking.getText().toString().isEmpty()) timeWalking = -1;
        else timeWalking = Double.parseDouble(filterWalking.getText().toString());

        ArrayList<ItemInfo> tmpList = new ArrayList<>();
        for(int i = 0; i < itemsList.size(); i++){
            if((distance == -1 || distance >= Double.parseDouble(itemsList.get(i).getDistance())) &&
                    (timeCar == -1 || timeCar >= Double.parseDouble(itemsList.get(i).getTimeCar()))&&
                    (timeWalking == -1 || timeWalking >= Double.parseDouble(itemsList.get(i).getTimeWalking()))&&
                    (rating == -1 || rating >= itemsList.get(i).getRating())
            )
                tmpList.add(itemsList.get(i));
        }
        itemsAdapter = new ItemsResultListAdapter(this, tmpList);
        itemsAdapter.setFilters(distance, timeCar, timeWalking, rating);
        itemsAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}