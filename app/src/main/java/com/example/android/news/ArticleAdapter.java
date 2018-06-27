package com.example.android.news;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by James on 6/26/18.
 */

public class ArticleAdapter extends ArrayAdapter<Article>{
    public ArticleAdapter(@NonNull ArticleActivity context, ArrayList<Article> articles){
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Article currentArticle = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_tview);
        titleTextView.setText(currentArticle.getTitle());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_tview);
        authorTextView.setText(currentArticle.getAuthor());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_tview);
//        Date dateObj = new Date(currentArticle.getDatePublished());
//        String formattedDate = formatDate(dateObj);
//        dateTextView.setText(formattedDate);

        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section_tview);
        sectionTextView.setText(currentArticle.getSection());


        return listItemView;
    }

//    private String formatDate(Date dateObj){
//        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
//        return dateFormat.format(dateObj);
//    }
}

