package es.ucm.fdi.lookaround;

import android.content.ClipData;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;


/*import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;*/

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    private String latitude;
    private String longitude;

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


                // Get latitude and longitude for distance and time
                String longitudeDestination, latitudeDestination;

                longitudeDestination = itemsArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng");
                latitudeDestination = itemsArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat");
                tmpItem.longitude = longitudeDestination;
                tmpItem.latitude = longitudeDestination;



                // Get distance and time to the place
                CountDownLatch countDownLatch = new CountDownLatch(1);
                String responseDistanceTimeWalk = getDistance(latitudeOrigin, longitudeOrigin, latitudeDestination, longitudeDestination, "walking",countDownLatch);
                countDownLatch = new CountDownLatch(1);
                String responseDistanceTimeCar = getDistance(latitudeOrigin, longitudeOrigin, latitudeDestination, longitudeDestination, "driving",countDownLatch);


                // Walking
                JSONObject dist = new JSONObject(responseDistanceTimeWalk);
                if (dist.has("rows")) {
                    JSONArray distArray = dist.getJSONArray("rows");
                    if (distArray.length() > 0) {
                        tmpItem.distance = distArray.getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("text");
                        tmpItem.timeWalking = distArray.getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getString("text");
                    }
                }
                // By car
                dist = new JSONObject(responseDistanceTimeCar);
                if (dist.has("rows")) {
                    JSONArray distArray = dist.getJSONArray("rows");
                    if (distArray.length() > 0) {
                        tmpItem.timeCar = distArray.getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").get("text").toString();
                    }
                }

                // Get boolean for place open or not
                if (itemsArray.getJSONObject(i).has("opening_hours")) {
                    tmpItem.open = itemsArray.getJSONObject(i).getJSONObject("opening_hours").getBoolean("open_now");
                }


                if (itemsArray.getJSONObject(i).has("place_id")) {
                    // Get place ID
                    tmpItem.placeId = itemsArray.getJSONObject(i).getString("place_id");

                }

                if (itemsArray.getJSONObject(i).has("price_level")) {
                    // Get price level
                    tmpItem.priceLevel = itemsArray.getJSONObject(i).getInt("price_level");

                }

                if (itemsArray.getJSONObject(i).has("rating")) {
                    // Get place rating
                    tmpItem.rating = itemsArray.getJSONObject(i).getDouble("rating");

                }

                if (itemsArray.getJSONObject(i).has("user_ratings_total")) {
                    // Get place total number of ratings
                    tmpItem.totalRatings = itemsArray.getJSONObject(i).getInt("user_ratings_total");

                }
                itemList.add(tmpItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("RequestLog", "Informaci??n de la API extra??da");
        Log.d("RequestLog",itemList.size()+"");
        return itemList;
    }

    public static String getDistance(String latitudeOrigin, String longitudeOrigin, String latitudeDestination, String longitudeDestination, String mode, CountDownLatch countDownLatch) {
        final String[] responseData = new String[1];
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/distancematrix/json" +
                        "?origins="+ latitudeOrigin + "%2C"+ longitudeOrigin+
                        "&destinations=" + latitudeDestination + "%2C"+ longitudeDestination +
                        "&mode="+ mode +
                        "&language=es-ES" +
                        "&key=AIzaSyD7zEUdA01mZPjRmufqJj5PzdtzZuudwxg").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                responseData[0] = response.body().string();
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseData[0];
    }


    /* Setters */

    public void setName(String name) { this.name = name; }

    public void setDistance(String distance) { this.distance = distance; }

    public void setOpen(boolean open) { this.open = open; }


    /* Setters */


    public void setPlaceId(String placeId) { this.placeId = placeId; }

    public void setPriceLevel(int priceLevel) { this.priceLevel = priceLevel; }

    public void setRating(double rating) { this.rating = rating; }

    public void setTotalRatings(int totalRatings) { this.totalRatings = totalRatings; }

    public void setTimeWalking(String timeWalking) { this.timeWalking = timeWalking; }

    public void setTimeCar(String timeCar) { this.timeCar = timeCar; }

    /* Getters */

    public String getName() { return this.name;}

    public String getDistance() { return this.distance;}

    public double getRating() { return this.rating;}

    public int getPriceLevel() { return this.priceLevel; }

    public String getPlaceId() { return this.placeId; }

    public int getTotalRatings() { return this.totalRatings; }

    public boolean getOpen() { return this.open; }

    public String getTimeWalking(){return this.timeWalking;}

    public String getTimeCar() {return this.timeCar;}

    public String getLatitude() {return this.latitude;}

    public String getLongitude() {return this.longitude;}

    // MG Testing
    public static <T extends Serializable> T stringToObjectS(String string) {
        byte[] bytes = Base64.decode(string, 0);
        T object = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            object = (T) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public static String objectToString(Serializable object) {
        String encoded = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
            encoded = new String(Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encoded;
    }
    //



}
