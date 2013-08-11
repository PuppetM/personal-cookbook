package com.cookbook.activites;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cookbook.activites.NeueZutat.DownloadImage;
import com.example.cookbook.R;
import com.example.cookbook.R.layout;
import com.example.cookbook.R.menu;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTabHost;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Rezept extends Activity {
	
	String name, beschreibung;
	int id;
	
	TextView tv_rezept_name;
	TextView tv_rezept_beschreibung;
	
	ProgressBar pb_rezept;
	
	ImageView iv_rezept_icon;
	
	ParseUser cUser;
	String currentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_rezept);		
		
		initParse();
		referenceUIElements();
		getId();		
		
	}
	
	private void setBeschreibung() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Beschreibung");
		query.whereEqualTo("ID", id);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		        	beschreibung = scoreList.get(0).getString("Beschreibung");
		        	tv_rezept_beschreibung.setText(beschreibung);
					tv_rezept_name.setVisibility(View.VISIBLE);
					iv_rezept_icon.setVisibility(View.VISIBLE);
					tv_rezept_beschreibung.setVisibility(View.VISIBLE);
					pb_rezept.setVisibility(View.INVISIBLE);
		        }
		    }
		});
		
	}

	private void getId() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Gericht");
		query.whereEqualTo("Name", name);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		        	id = scoreList.get(0).getInt("ID");
		        	setBeschreibung();
		        	setPicture();
		        }
		    }
		});
			
	}

	@SuppressWarnings("deprecation")
	private void setPicture(){
		if(id<10){
			new DownloadImage().execute("http://www.marions-kochbuch.de/rezept/000"+id+".jpg");
		}else if(id<100){
			new DownloadImage().execute("http://www.marions-kochbuch.de/rezept/00"+id+".jpg");
		}else if(id<1000){
			new DownloadImage().execute("http://www.marions-kochbuch.de/rezept/0"+id+".jpg");
		}else{
			new DownloadImage().execute("http://www.marions-kochbuch.de/rezept/"+id+".jpg");
		}
		
	}

	private void referenceUIElements() {		
		tv_rezept_name = (TextView) findViewById (R.id.tv_rezept_name);
		tv_rezept_beschreibung = (TextView) findViewById (R.id.tv_rezept_beschreibung);
		iv_rezept_icon = (ImageView) findViewById (R.id.iv_rezept_icon);
		pb_rezept = (ProgressBar) findViewById (R.id.pb_rezept);
		pb_rezept.setProgress(5);
		name = getIntent().getExtras().getString("Name");
		tv_rezept_name.setText(name);
		tv_rezept_name.setVisibility(View.INVISIBLE);
		iv_rezept_icon.setVisibility(View.INVISIBLE);
		tv_rezept_beschreibung.setVisibility(View.INVISIBLE);
	}

	private void initParse() {
		Parse.initialize(this, "PXJakVYimXSoEUbQvyiNRIB3LzCbP0FEqFOM7NZD", "ms0stwKSjkAcbhuBFs3LOt0Qmjt50UZ3buElHYGm");
		ParseAnalytics.trackAppOpened(getIntent());	
		ParseUser.enableAutomaticUser();
		cUser = ParseUser.getCurrentUser();
		currentUser = cUser.get("username").toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    private void setImage(Drawable drawable){
    	iv_rezept_icon.setImageDrawable(drawable);
    }
	
	public class DownloadImage extends AsyncTask<String, Integer, Drawable> {
		
	    @Override
	    protected Drawable doInBackground(String... arg0) {
	        return downloadImage(arg0[0]);
	    }
	
	    protected void onPostExecute(Drawable image){
	        setImage(image);
	    }
	
	    private Drawable downloadImage(String _url){
	        URL url;        
	        BufferedOutputStream out;
	        InputStream in;
	        BufferedInputStream buf;
	        
	        try {
	            url = new URL(_url);
	            in = url.openStream();
	            buf = new BufferedInputStream(in);
	
	            Bitmap bMap = BitmapFactory.decodeStream(buf);
	            if (in != null) {
	                in.close();
	            }
	            if (buf != null) {
	                buf.close();
	            }
	
	            return new BitmapDrawable(bMap);
	
	        } catch (Exception e) {
	            Log.e("Error reading file", e.toString());
	        }
	
	        return null;
	    }
	
	}

}
