package es.ucm.fdi.lookaround;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private CategoriesResultListAdapter categoriesAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<Pair<String, Integer>> categories = createCategories();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        categoriesAdapter = new CategoriesResultListAdapter(this, categories);
        categoriesAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(categoriesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<Pair<String, Integer>> createCategories() {
        ArrayList<Pair<String, Integer>> names = new ArrayList<Pair<String, Integer>>(Arrays.asList(new Pair<String, Integer>("Restaurantes",R.drawable.ic_restaurant_svg),
                new Pair<String, Integer>("Museos",R.drawable.ic_restaurant_svg),
                new Pair<String, Integer>("Parques",R.drawable.ic_restaurant_svg)
                ));
        return names;
    }
}