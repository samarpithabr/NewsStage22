package com.example.shara.newsstage2;

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
    private static final String News_URL = "https://content.guardianapis.com/search?api-key=adad3c5b-1616-47b8-8e7e-a03b2ab8e819&show-tags=contributor";
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
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, News_URL);
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
}


