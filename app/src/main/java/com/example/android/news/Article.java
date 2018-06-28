package com.example.android.news;

/**
 * Created by James on 6/26/18.
 */

public class Article {

    private String title;
    private String author;
    private String section;
    private String datePublished;
    private String url;

    public Article(String title, String section, String datePublished, String url, String author) {
        this.title = title;
        this.section = section;
        this.datePublished = datePublished;
        this.url = url;
        this.author = author;

    }

    public String getAuthor() {
        return author;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getSection() {
        return section;
    }
}
