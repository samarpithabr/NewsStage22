package com.example.shara.newsstage2;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final int News_Loader_id = 1;
    /**
     * Url of what need to be accesed
     */
    private static final String News_URL ="https://content.guardianapis.com/search";
            //"https://content.guardianapis.com/search?api-key=adad3c5b-1616-47b8-8e7e-a03b2ab8e819&show-tags=contributor";
    private String TAG = MainActivity.class.getSimpleName();
    ArrayList<HashMap<String, String>> newsList;
    private NewsAdapter newsAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsAdapter = new NewsAdapter(this, new ArrayList<News>());
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        ListView newsListView = (ListView) findViewById(R.id.list);
        newsListView.setAdapter(newsAdapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current article that was clicked on
                News currentArticle = newsAdapter.getItem(position);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri NewsUri = Uri.parse(currentArticle.getWebUrl());
                // Create a new intent to view the article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, NewsUri);
                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        //To check the Internet Connection
        ConnectivityManager cm = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        //If Internet connection is there the list of articles
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(News_Loader_id, null, MainActivity.this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.noi);
        }
    }



        @Override
        // onCreateLoader instantiates and returns a new Loader for the given ID
        public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

            // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
            String mindate = sharedPrefs.getString(
                    getString(R.string.settings_min_date_key),
                    getString(R.string.settings_min_date_default));

            String orderBy  = sharedPrefs.getString(
                    getString(R.string.settings_order_by_key),
                    getString(R.string.settings_order_by_default)
            );

            // parse breaks apart the URI string that's passed into its parameter
            Uri baseUri = Uri.parse(News_URL);

            // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
            Uri.Builder uriBuilder = baseUri.buildUpon();

            // Append query parameter and its value. For example, the `format=geojson`
            uriBuilder.appendQueryParameter("api-key", "adad3c5b-1616-47b8-8e7e-a03b2ab8e819");
            uriBuilder.appendQueryParameter("show-tags", "contributor");
            uriBuilder.appendQueryParameter("minmag", mindate);
            uriBuilder.appendQueryParameter("orderby", "time");

            // Return the completed uri `http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=10&minmag=minMagnitude&orderby=time
            return new NewsLoader(this, uriBuilder.toString());

        }



    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsArrayList) {


        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        newsAdapter.clear();
        if (newsArrayList != null && !newsArrayList.isEmpty())
            newsAdapter.addAll(newsArrayList);
        else
            mEmptyStateTextView.setText(R.string.noarticles);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


