package es.ucm.fdi.lookaround;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

public class PopUpWindow extends ItemListActivity {
    //PopupWindow display method

    public void showPopupWindow(final View view, View parentView) {
        //Create a View object yourself through inflater
        parentView.setAlpha(0.3f);
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pop_up_layout, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        Button buttonFilter = popupView.findViewById(R.id.button);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFilters(v);
                parentView.setAlpha(1f);
                popupWindow.dismiss();
            }
        });



        //Handler for clicking on the inactive zone of the window

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                parentView.setAlpha(1f);
                popupWindow.dismiss();
                return true;
            }
        });
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
