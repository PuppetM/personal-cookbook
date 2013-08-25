package com.cookbook.classes;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cookbook.asynchronImageLoader.ImageLoader;
import com.example.cookbook.R;

public class CustomAdapter extends BaseAdapter{
    
  	private final ArrayList<Zutat> entries;
  	private Activity activity;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    private Typeface font, font_bold;

 
    public CustomAdapter(Activity a, ArrayList<Zutat> entries, Typeface font, Typeface font_bold) {
		activity = a;
	    this.entries = entries;
	    inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader (activity.getApplicationContext());  
        this.font = font;
        this.font_bold = font_bold;
	  }

	@Override
	public int getCount() {
		return entries.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	 @SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {	    	
	    	
	    	View vi=convertView;
	        if(convertView==null){
	            vi = inflater.inflate(R.layout.layout_meinschrank_liste, null);
	        }
	        
	        ImageView iv_zutat_picture =(ImageView)vi.findViewById(R.id.iv_mein_schrank_liste_icon);
	        TextView tv_zutat_einheit =(TextView)vi.findViewById(R.id.tv_mein_schrank_liste_einheit);
	        TextView tv_zutat_menge =(TextView)vi.findViewById(R.id.tv_mein_schrank_liste_menge);
	        TextView tv_zutat_name =(TextView)vi.findViewById(R.id.tv_mein_schrank_liste_name);
	        
	        tv_zutat_einheit.setTypeface(font);	        
	        tv_zutat_menge.setTypeface(font);	        
	        tv_zutat_name.setTypeface(font_bold);
	        
	        tv_zutat_name.setText(entries.get(position).getName());
	        tv_zutat_menge.setText(entries.get(position).getMenge().toString());
	        tv_zutat_einheit.setText(entries.get(position).getEinheit());
	        
	        imageLoader.DisplayImage("http://www.marions-kochbuch.de/index-bilder/"+URLEncoder.encode(entries.get(position).getName().toLowerCase())+".jpg", iv_zutat_picture);
        
	        return vi;
	   }
    


 
}
