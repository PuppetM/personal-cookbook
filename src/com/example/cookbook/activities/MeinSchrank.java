package com.example.cookbook.activities;

import java.util.Arrays;

import com.example.cookbook.R;
import com.example.cookbook.R.layout;
import com.example.cookbook.R.menu;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;

public class MeinSchrank extends Activity {

	String user = "Hanswurst";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_meinschrank);		
		
		initParse();
		referenceUIElements();
		clickListener();	
		aktuList();
	}

	private void clickListener() {
	
	}

	private void aktuList(){
		ParseQueryAdapter<ParseObject> adapter =  new ParseQueryAdapter<ParseObject>(this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			public ParseQuery<ParseObject> create() {	   
				ParseQuery query = new ParseQuery("UserData");	
				query.whereContains("Username", user);			
				query.orderByDescending("ID");
				return query;
			}
		});
		adapter.setTextKey("Zutat");		
		ListView listView = (ListView) findViewById(R.id.datenbankListe);
		listView.setAdapter(adapter);		
	}
	
	private void initParse() {
		Parse.initialize(this, "PXJakVYimXSoEUbQvyiNRIB3LzCbP0FEqFOM7NZD", "ms0stwKSjkAcbhuBFs3LOt0Qmjt50UZ3buElHYGm");
		ParseAnalytics.trackAppOpened(getIntent());	
		ParseUser.enableAutomaticUser();
	}

	private void referenceUIElements() {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
