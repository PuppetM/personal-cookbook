package com.cookbook.classes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.cookbook.activites.NeueZutat.DownloadImage;
import com.cookbook.asynchronImageLoader.ImageLoader;
import com.example.cookbook.R;

public class AlleRezepteSimpleArrayAdapter extends BaseAdapter {
	
	
	  	private final ArrayList<String> names;
	  	private final ArrayList<String> ids;
	  	private final ArrayList<String> kcal;
	  	private final ArrayList<String> dauer;
	  	private Activity activity;
	    private static LayoutInflater inflater=null;
	    public ImageLoader imageLoader; 
	    private Typeface font, font_bold;

	  public AlleRezepteSimpleArrayAdapter(Activity a, ArrayList<String> names, ArrayList<String> dauer,  ArrayList<String> kcal, ArrayList<String> ids, Typeface font, Typeface font_bold) {
		activity = a;
	    this.names = names;
	    this.ids = ids;
	    this.font = font;
	    this.font_bold = font_bold;
	    this.kcal = kcal;
	    this.dauer = dauer;

	    inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader (activity.getApplicationContext());
        
	  	}
	  
	    public int getCount() {
	        return names.size();
	    }
	  
	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {	    	
	    	
	    	View vi=convertView;
	        if(convertView==null){
	            vi = inflater.inflate(R.layout.layout_allerezepte_liste, null);
	        }
	        TextView text=(TextView)vi.findViewById(R.id.tv_alleRezepte_liste_name);
	        text.setTypeface(font_bold);	        
	        text.setText(names.get(position));
	        
	        TextView text2=(TextView)vi.findViewById(R.id.tv_alleRezepte_liste_dauer);
	        text2.setTypeface(font);
	        text2.setText(dauer.get(position)+" min");
	        
	        TextView text3=(TextView)vi.findViewById(R.id.tv_meinSchrank_dialog_main);
	        text3.setTypeface(font);
	        text3.setText(kcal.get(position)+" Kcal pro Portion");
	        
	        ImageView image=(ImageView)vi.findViewById(R.id.iv_alleRezepte_icon);
	        String helpInt = (int)Double.parseDouble(ids.get(position))+"";
	        if(helpInt.length()==4){
	        	imageLoader.DisplayImage("http://www.marions-kochbuch.de/rezept/"+helpInt+".jpg", image);
	        }else if(helpInt.length()==3){
	        	imageLoader.DisplayImage("http://www.marions-kochbuch.de/rezept/0"+helpInt+".jpg", image);
	        }else if(helpInt.length()==2){
	        	imageLoader.DisplayImage("http://www.marions-kochbuch.de/rezept/00"+helpInt+".jpg", image);
	        }else{
	        	imageLoader.DisplayImage("http://www.marions-kochbuch.de/rezept/000"+helpInt+".jpg", image);
	        }	        
	        return vi;
	    }
	
}
