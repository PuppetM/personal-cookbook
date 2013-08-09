package com.cookbook.classes;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookbook.R;

public class CustomAdapter extends ArrayAdapter<Zutat>{
    private ArrayList<Zutat> entries;
    private Activity activity;
    
 

 
    public CustomAdapter(Activity a, int textViewResourceId, ArrayList<Zutat> entries) {
    	super(a, textViewResourceId, entries);
        this.entries = entries;
        this.activity = a;
	}

	public class ViewHolder{
        public TextView tv_zutat_name;
        public TextView tv_zutat_menge;
        public TextView tv_zutat_einheit;
        public ImageView iv_zutat_picture;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.layout_meinschrank_liste, null);
            holder = new ViewHolder();
            holder.tv_zutat_name = (TextView) v.findViewById(R.id.tv_zutat_name);
            holder.tv_zutat_menge = (TextView) v.findViewById(R.id.tv_zutat_menge);
            holder.tv_zutat_einheit = (TextView) v.findViewById(R.id.tv_zutat_einheit);
            holder.iv_zutat_picture = (ImageView) v.findViewById(R.id.iv_zutat_picture);
            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();
 
        final Zutat custom = entries.get(position);
        if (custom != null) {
            holder.tv_zutat_name.setText(custom.getName());
            holder.tv_zutat_menge.setText(custom.getMenge().toString());
            holder.tv_zutat_einheit.setText(custom.getEinheit());
            //holder.iv_zutat_picture.setImageBitmap();
            
        }
        return v;
    }
    


 
}
