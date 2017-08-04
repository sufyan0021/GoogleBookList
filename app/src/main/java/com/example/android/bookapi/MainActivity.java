package com.example.android.bookapi;

import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks <List<Book_Data>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private BookAdapter mAdapter;
    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;
    private static final int Book_LOADER_ID = 1;
    private static final String GBooks_REQUEST_URL ="https://www.googleapis.com/books/v1/volumes?q=";
    ArrayList<Book_Data> bookList=new ArrayList<Book_Data>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bv=(Button)findViewById(R.id.button);
        bv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoaderManager().restartLoader(Book_LOADER_ID,null,MainActivity.this);
                mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
                ConnectivityManager cm =
                        (ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(isConnected){
                    getLoaderManager().initLoader(Book_LOADER_ID,null,MainActivity.this);
                    Log.v("initLoader","LoaderInitialized");
                }
                else{
                    findViewById(R.id.prog_bar).setVisibility(View.GONE);
                    mEmptyStateTextView.setText("Check Internet Connection");
                }

                // Lookup the recyclerview in activity layout
                RecyclerView rvBooks = (RecyclerView) findViewById(R.id.rvBooks);

                mAdapter=new BookAdapter(MainActivity.this,bookList);

                rvBooks.setAdapter(mAdapter);

                rvBooks.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
        });

    }

    @Override
    public Loader<List<Book_Data>> onCreateLoader(int id, Bundle args) {
        EditText m=(EditText)findViewById(R.id.editText);
        String z=m.getText().toString();
        //String z="harry";
       return new BookLoader(this,GBooks_REQUEST_URL+z);
    }

    @Override
    public void onLoadFinished(Loader<List<Book_Data>> loader, List<Book_Data> data) {
        // Clear the adapter of previous earthquake data
        bookList.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            Log.v("Parsed","results");
            bookList.addAll(data);
            mAdapter.notifyDataSetChanged();
            findViewById(R.id.prog_bar).setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book_Data>> loader) {
      bookList.clear();
        mAdapter.notifyDataSetChanged();
    }

/*    public static List<Book_Data> extractBooks(String booklistJSON) {

        Log.v("extractEarthquakes","JsonResponse extracted");

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(booklistJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Book_Data> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject baseJsonResponse=new JSONObject(booklistJSON);
            JSONArray ab=baseJsonResponse.getJSONArray("items");

            for(int i=0;i<ab.length();i++)
            {
                JSONObject c=ab.getJSONObject(i);
                JSONObject d=c.getJSONObject("volumeInfo");
                JSONObject im=d.getJSONObject("imageLinks");
                Uri imglink=Uri.parse(im.getString("smallThumbnail"));
                //int imglink=R.mipmap.ic_launcher;
                JSONArray p=d.getJSONArray("authors");
                String author=p.toString();
                String tle=d.getString("title");
                JSONObject s=c.getJSONObject("saleInfo");
                JSONObject cp=s.getJSONObject("listPrice");
                String rprice=cp.getString("amount");
                String prlink=s.getString("buyLink");
                Log.v("stage1","finished parsing"+imglink);
                Book_Data book=new Book_Data(imglink,tle,author,rprice,prlink);
                Log.v("stage1","finished parsing"+imglink);
                books.add(book);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return books;
    }*/
}
