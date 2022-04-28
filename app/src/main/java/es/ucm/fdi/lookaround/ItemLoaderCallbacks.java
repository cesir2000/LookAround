package es.ucm.fdi.lookaround;

import android.content.Context;
import android.os.Bundle;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.List;


public class ItemLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<ItemInfo>> {

    private Context context;

    public ItemLoaderCallbacks(Context context) {
        this.context = context;
    }

    @Override
    public Loader<List<ItemInfo>> onCreateLoader(int id, Bundle args) {
        return new ItemLoader(this.context, args.getString("queryString"), args.getString("printType"));
    }

    @Override
    public void onLoadFinished(Loader<List<ItemInfo>> loader, List<ItemInfo> data) {
        
    }

    @Override
    public void onLoaderReset(Loader<List<ItemInfo>> loader) {

    }


}
