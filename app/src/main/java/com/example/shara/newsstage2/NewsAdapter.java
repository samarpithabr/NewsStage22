package com.example.shara.newsstage2;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    private static final String LOCATION_SEPARATOR1 = "T";
    private static final String LOCATION_SEPARATOR2 = "Z";

    public NewsAdapter(@NonNull Context context, @NonNull List<News> objects) {
        super(context, 0, objects);
    }

    @NonNull

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list, parent, false);
        }
        News currentNews = getItem(position);
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.sectionName);
        nameTextView.setText(currentNews.getSectionName());
        TextView dateTextview =(TextView) listItemView.findViewById(R.id.publicationDate);
        dateTextview.setText(currentNews.getNewsDate());
        TextView timeTextview =(TextView) listItemView.findViewById(R.id.publicationtime);
        timeTextview.setText(currentNews.getNewsTime());
        TextView iconView = (TextView) listItemView.findViewById(R.id.webtitl);
        iconView.setText(currentNews.getWebTitle());
        TextView iconView1 = (TextView) listItemView.findViewById(R.id.webUrl);
        iconView1.setText(currentNews.getWebUrl());
        TextView authview = (TextView) listItemView.findViewById(R.id.Author);
        authview.setText(currentNews.getAuthorName());
        return listItemView;
    }


}

