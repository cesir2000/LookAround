package es.ucm.fdi.lookaround;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = "CustomInfoWindowAdapter";
    private LayoutInflater inflater;

    public CustomInfoWindowAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    public View getInfoContents(final Marker m) {
        //Carga layout personalizado.
        View v = inflater.inflate(R.layout.infowindow_map, null);
        ((TextView)v.findViewById(R.id.textViewInfoName)).setText(m.getTitle());
        ((TextView)v.findViewById(R.id.textViewInfoClick)).setText("(Pulsa para buscar esta posición en la pantalla de búsqueda)");
        return v;
    }

    @Override
    public View getInfoWindow(Marker m) {
        return null;
    }

}
