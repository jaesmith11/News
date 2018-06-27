package com.example.android.news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by James on 6/26/18.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private static final String LOG_TAG = ArticleLoader.class.getName();

    private String url;

    public ArticleLoader(Context context, String url){
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if (url == null){
            return null;
        }

        List<Article> articles = QueryUtils.fetchArticleData(url);
        return articles;
    }

}
