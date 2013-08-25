package com.cookbook.activites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


import com.cookbook.classes.MainSpinnerAdapter;
import com.cookbook.classes.Zutat;
import com.example.cookbook.R;
import com.example.cookbook.R.layout;
import com.example.cookbook.R.menu;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

	private static final String[] Categories = new String[] {"Alle Kategorien", "Fleisch", "Vegetarisch", "Pasta", "Fisch", "Gefluegel", "Salate", "Auflauf", "Suppen und Eintoepfe",};
	private Button toNeueZutat, toAlleRezepte, toMeinSchrank;
	private List<ParseObject> todos;
	private String selectedSpinner;
	Boolean sync;
	
	ArrayList<String> friends;
	private ArrayList<Zutat> zutaten;
	
	private Typeface font, font_bold;
	MainSpinnerAdapter msp;
	Boolean zutatVorhanden;
	
	
	ParseUser cUser;
	String currentUser;
	String id;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);	
		
		initParse();
		referenceUIElements();
		setFonts();
		
		hasFriends();
		
		clickListener();
		aktuList("Alle Kategorien");
			
	}

	private void setFonts() {
		font_bold = Typeface.createFromAsset(getAssets(), "fonts/font_bold.ttf");
		font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
		toNeueZutat.setTypeface(font_bold);
		toAlleRezepte.setTypeface(font_bold);
		toMeinSchrank.setTypeface(font_bold);	
		msp = new MainSpinnerAdapter(this, Categories, font_bold, R.layout.layout_main_spinner_style, R.id.tv_main_spinner_style);
		
	}

	public void onResume(){
		super.onResume();
		//hasFriends();	
	}	
	
	private void hasFriends(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Freunde");
		query.whereContains("User", currentUser);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null && scoreList.size()!=0) {	
		    		friends.clear();
		    		friends.add(currentUser);
		        	for(int i = 0; i < scoreList.size(); i++){
		        		id = scoreList.get(0).getString("ID");
		        		friends.add(scoreList.get(i).getString("Freund"));
		        	}
		        	getData();
		        } else {
		        	
		        }
		    }
		});
	}
	
	private void getData() {
		for(int o = 0; o < friends.size(); o++){
			final int k = o;
			ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
			query.whereContains("Username", friends.get(o));
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> scoreList, ParseException e) {
			        if (e == null && scoreList.size()!=0) {	
			        	for(int i = 0; i < scoreList.size(); i++){					        		
			        		zutatVorhanden = false;
			        		for(int m = 0; m < zutaten.size(); m++){
			        			if(zutaten.get(m).getName().equals(scoreList.get(i).getString("Zutat"))&&zutaten.get(m).getEinheit().equals(scoreList.get(i).getString("Masseinheit"))){
			        				zutatVorhanden = true;
			        				zutaten.get(m).addMenge(scoreList.get(i).getDouble("Menge"));
			        			}
			        		}
			        		if(!zutatVorhanden){
			        			Zutat newZutat = new Zutat (scoreList.get(i).getString("Zutat"),scoreList.get(i).getDouble("Menge"),scoreList.get(i).getString("Masseinheit"));
			        			zutaten.add(newZutat);
			        		}
			        	}
			        	if(k == friends.size()-1){
			        		deleteOldDatabase();
			        	}
			        } else {
			        }
			    }
			});															
		}
	}
	
	private void deleteOldDatabase(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("TogetherUserData");
		query.whereContains("Username", id);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null && scoreList.size()!=0) {		
		        	for(int i = 0; i < scoreList.size(); i++){
		        		scoreList.get(i).deleteInBackground();
		        	}
		        } else {
		       
		        }	
		        if(friends.size()>0){
		        	createNewDatabase();	
		        }		        
		     }
		});	
	}

	private void createNewDatabase(){
		for(int i = 0; i < zutaten.size(); i++){
			ParseObject data = new ParseObject("TogetherUserData");
			data.put("Username", id);
			data.put("Masseinheit", zutaten.get(i).getEinheit());
			data.put("Menge", zutaten.get(i).getMenge());
			data.put("Zutat", zutaten.get(i).getName());
			data.saveInBackground();
		}
		sync = false;
	}
	
	
	private void clickListener() {
		toNeueZutat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Main.this,NeueZutat.class);
				startActivity(i);
			}
		});
		toAlleRezepte.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Main.this,AlleRezepte.class);
				startActivity(i);
			}
		});
		toMeinSchrank.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Main.this,MeinSchrank.class);
				startActivity(i);
			}
		});		
	}

	private void aktuList(final String cat){
		ParseQueryAdapter<ParseObject> adapter =  new ParseQueryAdapter<ParseObject>(this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			public ParseQuery<ParseObject> create() {	   
				ParseQuery query = new ParseQuery("Gericht");				
				if(cat.equals("Alle Kategorien")){					
				}else{
					query.whereContains("Kategorie", cat);
				}				
				query.orderByDescending("ID");
				return query;

			}
		});
		adapter.setTextKey("Name");		
		ListView listView = (ListView) findViewById(R.id.rezepteDatenbankListe);
		listView.setAdapter(adapter);		
	}
	
	private void initParse() {
		Parse.initialize(this, "PXJakVYimXSoEUbQvyiNRIB3LzCbP0FEqFOM7NZD", "ms0stwKSjkAcbhuBFs3LOt0Qmjt50UZ3buElHYGm");
		ParseAnalytics.trackAppOpened(getIntent());	
		ParseUser.enableAutomaticUser();
		cUser = ParseUser.getCurrentUser();
		currentUser = cUser.get("username").toString();
	}
	
	

	private void referenceUIElements() {
		toNeueZutat = (Button) findViewById (R.id.toNeueZutat);
		toAlleRezepte = (Button) findViewById (R.id.toAlleRezepte);
		toMeinSchrank = (Button) findViewById (R.id.toMeinSchrank);
		friends = new ArrayList<String>();
		zutaten = new ArrayList<Zutat>();
		sync = true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.ic_menu);
		return true;
	}
}
	
	