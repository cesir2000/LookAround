package es.ucm.fdi.lookaround;

import android.content.ClipData;
import android.util.Log;


/*import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ItemInfo implements Serializable {
    private String name;            // Name of the establishment
    private String distance;        // Distance to the establishment
    private boolean open;           // If the establishment is open
    private String placeId;         // Place ID of the establishment
    private int priceLevel;      // Price level of the establishment;
    private double rating;          // Rating of the establishment;
    private int totalRatings;    // Total ratings of the establishment
    private String timeWalking;     // Time to get to the establishment walking
    private String timeCar;         // Time to get to the establishment by car

    public static List<ItemInfo> fromJsonResponse(String s, String latitudeOrigin, String longitudeOrigin) {
        List<ItemInfo> itemList = new ArrayList<ItemInfo>();
        JSONObject data = null;
        try {
            data = new JSONObject(s);
            JSONArray itemsArray = data.getJSONArray("results");
            for (int i = 0; i < itemsArray.length(); i++) {
                ItemInfo tmpItem = new ItemInfo();

                // Name of the place
                tmpItem.name = itemsArray.getJSONObject(i).getString("name");
                Log.d("JSONDataExtract","Name Extracted");
                /*************************************************************************************************************************
                // Get latitude and longitude for distance and time
                String longitudeDestination, latitudeDestination;

                longitudeDestination = itemsArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng");
                latitudeDestination = itemsArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat");
                Log.d("JSONDataExtract","Latitude and longitude Extracted");

                // Get distance and time to the place
                String responseDistanceTimeWalk = getDistance(latitudeOrigin, longitudeOrigin, latitudeDestination, longitudeDestination, "walking");
                String responseDistanceTimeCar = getDistance(latitudeOrigin, longitudeOrigin, latitudeDestination, longitudeDestination, "driving");


                // Walking
                JSONObject dist = new JSONObject(responseDistanceTimeWalk);
                JSONArray distArray = dist.getJSONArray("rows");
                tmpItem.distance = distArray.getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("text");
                tmpItem.timeWalking = distArray.getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getString("text");
                // By car
                dist = new JSONObject(responseDistanceTimeCar);
                distArray = dist.getJSONArray("rows");
                tmpItem.timeCar = distArray.getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").get("text").toString();
                Log.d("JSONDataExtract","Walking and driving distance Extracted");

                // Get boolean for place open or not
                if (itemsArray.getJSONObject(i).has("opening_hours")) {
                    tmpItem.open = itemsArray.getJSONObject(i).getJSONObject("opening_hours").getBoolean("open_now");
                }
                Log.d("JSONDataExtract","Open Extracted");

                // Get place ID
                tmpItem.placeId = itemsArray.getJSONObject(i).getString("place_id");
                Log.d("JSONDataExtract","PlaceID Extracted");

                // Get price level
                tmpItem.priceLevel = itemsArray.getJSONObject(i).getInt("price_level");
                Log.d("JSONDataExtract","Price level Extracted");

                // Get place rating
                tmpItem.rating = itemsArray.getJSONObject(i).getDouble("rating");
                Log.d("JSONDataExtract","Rating Extracted");

                // Get place total number of ratings
                tmpItem.totalRatings = itemsArray.getJSONObject(i).getInt("user_ratings_total");
                Log.d("JSONDataExtract","Total user ratings Extracted");
                *********************************************************************************************************************************/
                tmpItem.name = "Restaurante";
                itemList.add(tmpItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemList;
    }

    /*public static String getDistance(String latitudeOrigin, String longitudeOrigin, String latitudeDestination, String longitudeDestination, String mode) {
        final String[] responseData = new String[1];
        /*OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/distancematrix/json" +
                        "?origins="+ latitudeOrigin + "%2C"+ longitudeOrigin+
                        "&destinations=" + latitudeDestination + "%2C"+ longitudeDestination +
                        "&mode="+ mode +
                        "&language=es-ES" +
                        "&key=").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                responseData[0] = response.body().string();
            }
        });
        return responseData[0];
    }*/

    public void setName(String name) {
        this.name = name;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setTitle(boolean open) {
        this.open = open;
    }

    public void setPlaceId(String placeId) { this.placeId = placeId; }

    public void setPriceLevel(int priceLevel) { this.priceLevel = priceLevel; }

    public void setRating(double rating) { this.rating = rating; }

    public void setTotalRatings(int totalRatings) { this.totalRatings = totalRatings; }

    public String getName() { return this.name;}


}
