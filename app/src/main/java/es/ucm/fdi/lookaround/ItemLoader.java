package es.ucm.fdi.lookaround;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ItemLoader extends AsyncTaskLoader<List<ItemInfo>> {

    private String queryString;
    private String printType;

    final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    final String QUERY_PARAM = "q";
    final String MAX_RESULTS = "maxResults";
    final String PRINT_TYPE = "printType";

    public ItemLoader(Context context, String queryString, String printType) {
        super(context);
        this.printType = printType;
        this.queryString = queryString;
    }

    @Override
    public List<ItemInfo> loadInBackground() {
        if (queryString.equals(" ")) {
            ArrayList<ItemInfo> tmp = new ArrayList<>();
            return tmp;
        }
        String data = "";
        Uri builtURI = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, queryString)
                .appendQueryParameter(MAX_RESULTS, "40")
                .appendQueryParameter(PRINT_TYPE, printType)
                .build();
        URL requestURL = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        String contentAsString = "";
        try {
            requestURL = new URL(builtURI.toString());
            conn = (HttpURLConnection) requestURL.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            contentAsString = convertIsToString(is, 50000);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("JSONResult", contentAsString);

        //return ItemInfo.fromJsonResponse(contentAsString);
        return null;
    }

    public String convertIsToString(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        int nextCharacter; // read() returns an int, we cast it to char later
        String responseData = "";
        while (true) { // Infinite loop, can only be stopped by a "break" statement
            nextCharacter = reader.read(); // read() without parameters returns one character
            if (nextCharacter == -1) // A return value of -1 means that we reached the end
                break;
            responseData += (char) nextCharacter; // The += operator appends the character to the end of the string
        }
        return responseData;

    }

    public void onStartLoading() {
        forceLoad();
    }
}
