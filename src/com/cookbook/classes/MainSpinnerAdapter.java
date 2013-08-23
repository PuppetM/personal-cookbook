package com.cookbook.classes;

import com.example.cookbook.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainSpinnerAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private String[] Categories;
    private Typeface font;
    private int layout;
    private int text;

    public MainSpinnerAdapter(Activity con, String[] Categories, Typeface font, int layout, int text) {
        // TODO Auto-generated constructor stub
        mInflater = LayoutInflater.from(con);
        this.Categories = Categories;
        this.font = font;
        this.layout = layout;
        this.text = text;
    }

	@Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Categories.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ListContent holder;
        View v = convertView;
        if (v == null) {
            v = mInflater.inflate(layout, null);
            holder = new ListContent();
            holder.name = (TextView) v.findViewById(text);
            v.setTag(holder);
        } else {
            holder = (ListContent) v.getTag();
        }

        holder.name.setTypeface(font);
        holder.name.setText("" + Categories[position]);

        return v;
    }
    
    static class ListContent {
    	TextView name;
    }

}

