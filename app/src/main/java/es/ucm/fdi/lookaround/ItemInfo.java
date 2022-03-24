package es.ucm.fdi.lookaround;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ItemInfo {
    private String title;
    private String authors;
    private URL infoLink;

    public static List<ItemInfo> fromJsonResponse(String s){
        List<ItemInfo> bookList = new ArrayList<ItemInfo>();
        JSONObject data = null;
        try {
            data = new JSONObject(s);
            JSONArray booksArray = data.getJSONArray("items");
            for (int i = 0; i < 40; i++) {
                ItemInfo tmpBook = new ItemInfo();

                if (booksArray.getJSONObject(i).getJSONObject("volumeInfo").has("title")) {
                    tmpBook.setTitle(booksArray.getJSONObject(i).getJSONObject("volumeInfo").get("title").toString());
                }
                else {
                    tmpBook.setTitle("No title");
                }

                if(booksArray.getJSONObject(i).getJSONObject("volumeInfo").has("authors")) {
                    JSONArray autores = booksArray.getJSONObject(i).getJSONObject("volumeInfo").getJSONArray("authors");
                    String autoresString = "";
                    for (int j = 0; i < autores.length()-1; i++) {
                        autoresString += autores.getString(i) + ", ";
                    }
                    autoresString += autores.getString(autores.length()-1);
                    tmpBook.setAuthors(autoresString);
                }
                else {
                    tmpBook.setAuthors("Unknown author");
                }

                try {
                    tmpBook.setURL(new URL(booksArray.getJSONObject(i).getJSONObject("volumeInfo").get("infoLink").toString()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Log.d("JSONBookProcessed", ""+i);
                Log.d("URLofBook", booksArray.getJSONObject(i).getJSONObject("volumeInfo").get("infoLink").toString());
                bookList.add(tmpBook);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public void setURL(URL url) {
        this.infoLink = url;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getLink() {
        return infoLink.toString();
    }



}
