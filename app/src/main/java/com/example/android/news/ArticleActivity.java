package com.example.android.news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String LOG_TAG = ArticleActivity.class.getName();
    private static final int ARTICLE_LOADER_ID = 1;

    private ArticleAdapter adapter;
    private TextView emptyStateTextView;
    private ProgressBar pBar;
    private SwipeRefreshLayout swipeRefresh;

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/artanddesign?&page-size=50&show-tags=contributor&api-key=4802be1e-6ddf-4d29-9655-4dcd853a7f62";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_listview);

        ListView articleListView = (ListView) findViewById(R.id.list);
        adapter = new ArticleAdapter(this, new ArrayList<Article>());
        Log.i(LOG_TAG, "ArticleLoader is being created");
        articleListView.setAdapter(adapter);

        //Find the TextView in the article_listview.xml file that will provide
        //feedback to user if there's no internet or articles
        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        articleListView.setEmptyView(emptyStateTextView);

        //Create a progress bar to indicate loading progress
        pBar = (ProgressBar) findViewById(R.id.loading_indicator);

        //Find the SwipeRefreshLayout to allow user to dynamically check for article updates
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                        // This method performs the actual data-refresh operation.
                        updateInfo();
                    }
                }
        );

        //Get a reference to the ConnectivityManager to determine if the network is active
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        //Set a ClickListener on the ListView so the user can open the article in their browser
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article currentArticle = adapter.getItem(position);
                Uri articleUri = Uri.parse(currentArticle.getUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                startActivity(webIntent);
            }
        });

        //Check if the network is active before loading articles
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            //Get a reference to the LoaderManager to interact with loader
            LoaderManager loaderManager = getLoaderManager();
            //Initialize the loader. Pass in this activity because this activity implements the
            //LoaderCallbacks interface
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            //Update empty state with no connection error message
            emptyStateTextView.setText("No Internet Connection.");
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        emptyStateTextView.setText("No Articles Found.");
        emptyStateTextView.setVisibility(View.GONE);
        pBar.setVisibility(View.GONE);
        //Clear the adapter of previous article data
        adapter.clear();
        //No longer need to swiperefresh
        swipeRefresh.setRefreshing(false);
        //If there is a valid list of articles, add them to the adapters dataset
        //This triggers the ListView to update
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        adapter.clear();
    }

    //Helper method to OnRefresh that restarts the loader and updates article data
    private void updateInfo() {
        getLoaderManager().restartLoader(ARTICLE_LOADER_ID, null, this);
    }

}
