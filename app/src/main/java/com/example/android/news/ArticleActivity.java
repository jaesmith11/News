package com.example.android.news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/artanddesign?&show-tags=contributor&api-key=4802be1e-6ddf-4d29-9655-4dcd853a7f62";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_listview);

        ListView articleListView = (ListView) findViewById(R.id.list);
        adapter = new ArticleAdapter(this, new ArrayList<Article>());
        Log.i(LOG_TAG, "ArticleLoader is being created");
        articleListView.setAdapter(adapter);

        emptyStateTextView = (TextView)findViewById(R.id.empty_view);
        articleListView.setEmptyView(emptyStateTextView);

        pBar = (ProgressBar) findViewById(R.id.loading_indicator);

        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article currentArticle = adapter.getItem(position);

                Uri articleUri = Uri.parse(currentArticle.getUrl());

                Intent webIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                startActivity(webIntent);
            }
        });
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        }else{
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            emptyStateTextView.setText("No Internet Connection.");
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args){
        return new ArticleLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data){
        emptyStateTextView.setText("No Articles Found.");
        emptyStateTextView.setVisibility(View.GONE);
        pBar.setVisibility(View.GONE);

        adapter.clear();

        if (data != null && !data.isEmpty()){
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader){
        adapter.clear();
    }
}
