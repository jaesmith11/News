package com.example.android.news;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by James on 6/26/18.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {
    public ArticleAdapter(@NonNull ArticleActivity context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Article currentArticle = getItem(position);

        //Find and populate the TextView with the article's title
        TextView titleTextView = (TextView) convertView.findViewById(R.id.title_tview);
        titleTextView.setText(currentArticle.getTitle());

        //Find and populate the TextView with the article's author
        TextView authorTextView = (TextView) convertView.findViewById(R.id.author_tview);
        authorTextView.setText(currentArticle.getAuthor());

        //Find and populate the TextView with the article's date
        TextView dateTextView = (TextView) convertView.findViewById(R.id.date_tview);

        //https://www.mkyong.com/java/java-date-and-calendar-examples/
        //Get a reference to SimpleDateFormat to format the date
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String date = currentArticle.getDatePublished();
        try {
            //Create a Date object from the formatted SimpleDateFormat reference
            Date dateObj = sDateFormat.parse(date);
            //Create a string by formatting the Date ref's substrings (i.e. Jun 27, 2018)
            String format = dateObj.toString().substring(4, 10) + ", " + dateObj.toString().substring(23, 28);
            dateTextView.setText(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Find and populate the TextView with the article's section name
        TextView sectionTextView = (TextView) convertView.findViewById(R.id.section_tview);
        sectionTextView.setText(currentArticle.getSection());

        return convertView;
    }

}

