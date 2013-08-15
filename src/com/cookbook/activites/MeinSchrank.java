package com.cookbook.activites;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


import com.cookbook.classes.CustomAdapter;
import com.cookbook.classes.Zutat;
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

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MeinSchrank extends Activity {


	private ArrayList<Zutat> zutaten;
	private ArrayList<String> users;
	private ArrayList<String> friends;
	ArrayList<String> zutaten_name;
	ArrayList<String> zutaten_einheit;
	ArrayList<Double> zutaten_menge;
	
	ListView lv_mein_schrank;
	private CustomAdapter adapter;
	Button bu_datenbank_verknüpfen;
	Dialog dialog;
	
	EditText et_meinschrank_dialog;
	Button bt_meinschrank_dialog;
	TextView tv_meinschrank_dialog;
	ParseUser cUser;
	String currentUser, friend;
	String id;
	
	Boolean getUser = false;
	Boolean alreadyFriend = false;
	Boolean userHasFriends = false;
	boolean vorhanden;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_meinschrank);	
		
		referenceUIElements();
		initParse();
		
		hasFriends();		
		
		clickListener();	
	}
	
	public void onResume(){
		super.onResume();
		hasFriends();	
	}


	private void akuList() {
		if(userHasFriends){
			ParseQuery<ParseObject> query = ParseQuery.getQuery("TogetherUserData");
			query.whereContains("Username", id);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> scoreList, ParseException e) {
			    	zutaten.clear();
			    	if (e == null && scoreList.size()!=0) {	
			        	for(int i = 0; i < scoreList.size(); i++){
			        		Zutat newZutat = new Zutat (scoreList.get(i).getString("Zutat"),scoreList.get(i).getDouble("Menge"),scoreList.get(i).getString("Masseinheit"));
		        			zutaten.add(newZutat);
			        	}
			        } else {
			        }
			        adapter.notifyDataSetChanged();
			    }
			});
		}else{
			ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
			query.whereContains("Username", currentUser);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> scoreList, ParseException e) {
			    	zutaten.clear();
			    	if (e == null && scoreList.size()!=0) {	
			        	for(int i = 0; i < scoreList.size(); i++){
			        		Zutat newZutat = new Zutat (scoreList.get(i).getString("Zutat"),scoreList.get(i).getDouble("Menge"),scoreList.get(i).getString("Masseinheit"));
		        			zutaten.add(newZutat);
			        	}
			        } else {
			        }
			        adapter.notifyDataSetChanged();
			    }
			});
		}
	}

	//Button - Datenbank verknüpfen
	private void clickListener() {
		bu_datenbank_verknüpfen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				Intent i = new Intent(MeinSchrank.this,Freunde.class);
				startActivity(i);				
			}
		});
	}
	
	//Alle Benutzer
	private void hasFriends(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Freunde");
		query.whereContains("User", currentUser);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null && scoreList.size()!=0) {	
		        	Log.e("Error", "User has Friends");
		        	userHasFriends = true;
		        	id = scoreList.get(0).getString("ID");
		        	akuList();
		        } else {
		        	userHasFriends = false;
		        	Log.e("Error", "User has no Friends");
		        	akuList();
		        	
		        }
		    }
		});
	}

	
	private void initParse() {
		Parse.initialize(this, "PXJakVYimXSoEUbQvyiNRIB3LzCbP0FEqFOM7NZD", "ms0stwKSjkAcbhuBFs3LOt0Qmjt50UZ3buElHYGm");
		ParseAnalytics.trackAppOpened(getIntent());	
		ParseUser.enableAutomaticUser();
		cUser = ParseUser.getCurrentUser();
		currentUser = cUser.get("username").toString();
	}

	private void referenceUIElements() {
		zutaten = new ArrayList<Zutat>();
		users = new ArrayList<String>();
		friends = new ArrayList<String>();
		lv_mein_schrank = (ListView) findViewById(R.id.lv_mein_schrank);
		adapter = new CustomAdapter(this,R.id.lv_mein_schrank,zutaten);
		lv_mein_schrank.setAdapter(adapter);
		bu_datenbank_verknüpfen = (Button) findViewById(R.id.neueDatenbank);
		zutaten_name = new ArrayList<String>();
		zutaten_einheit = new ArrayList<String>();
		zutaten_menge = new ArrayList<Double>();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
}
