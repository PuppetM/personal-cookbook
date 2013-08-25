package com.cookbook.classes;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


//Spinner-Adapter
public class MainSpinnerAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private String[] Categories;
    private Typeface font;
    private int layout;
    private int text;

    public MainSpinnerAdapter(Activity con, String[] Categories, Typeface font, int layout, int text) {
        mInflater = LayoutInflater.from(con);
        this.Categories = Categories;
        this.font = font;
        this.layout = layout;
        this.text = text;
    }

	@Override
    public int getCount() {
        return Categories.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

