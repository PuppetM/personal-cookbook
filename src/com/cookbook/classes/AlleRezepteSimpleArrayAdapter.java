package com.cookbook.classes;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
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

	  public AlleRezepteSimpleArrayAdapter(Activity activity, ArrayList<String> names, ArrayList<String> dauer,  ArrayList<String> kcal, ArrayList<String> ids, Typeface font, Typeface font_bold) {
		this.activity = activity;
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
	        TextView tv_alleRezepte_liste_name=(TextView)vi.findViewById(R.id.tv_alleRezepte_liste_name);
	        tv_alleRezepte_liste_name.setTypeface(font_bold);	        
	        tv_alleRezepte_liste_name.setText(names.get(position));
	        
	        TextView tv_alleRezepte_liste_dauer=(TextView)vi.findViewById(R.id.tv_alleRezepte_liste_dauer);
	        tv_alleRezepte_liste_dauer.setTypeface(font);
	        tv_alleRezepte_liste_dauer.setText(dauer.get(position)+" min");
	        
	        TextView tv_alleRezepte_liste_kcal=(TextView)vi.findViewById(R.id.tv_alleRezepte_liste_kcal);
	        tv_alleRezepte_liste_kcal.setTypeface(font);
	        tv_alleRezepte_liste_kcal.setText(kcal.get(position)+" Kcal pro Portion");
	        
	        ImageView iv_alleRezepte_liste_icon=(ImageView)vi.findViewById(R.id.iv_alleRezepte_liste_icon);
	        String helpInt = (int)Double.parseDouble(ids.get(position))+"";
	        if(helpInt.length()==4){
	        	imageLoader.DisplayImage("http://www.marions-kochbuch.de/rezept/"+helpInt+".jpg", iv_alleRezepte_liste_icon);
	        }else if(helpInt.length()==3){
	        	imageLoader.DisplayImage("http://www.marions-kochbuch.de/rezept/0"+helpInt+".jpg", iv_alleRezepte_liste_icon);
	        }else if(helpInt.length()==2){
	        	imageLoader.DisplayImage("http://www.marions-kochbuch.de/rezept/00"+helpInt+".jpg", iv_alleRezepte_liste_icon);
	        }else{
	        	imageLoader.DisplayImage("http://www.marions-kochbuch.de/rezept/000"+helpInt+".jpg", iv_alleRezepte_liste_icon);
	        }	        
	        return vi;
	    }
	
}
