package com.example.android.bookapi;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by sufya on 03-08-2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Book_Data>> {

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link BookLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v("onStartLoading","Data Loading started");
    }

    /**
     * This is on a background thread.
     */

    @Override
    public List<Book_Data> loadInBackground() {
        Log.v("loadInBackground","Data Request in background thread");

        if (mUrl == null) {
            return null;
        }

        // Perform the HTTP request for earthquake data and process the response.
        List<Book_Data> books = QueryUtils.fetchBooksData(mUrl);
        return books;
    }
}
