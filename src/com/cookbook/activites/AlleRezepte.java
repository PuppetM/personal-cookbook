package com.cookbook.activites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentTabHost;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

public class AlleRezepte extends Activity {

	EditText ed_allerezepte_search;
	Spinner sp_allerezepte_spinner;
	ListView lv_allerezepte;
	
	ProgressBar pb_allerezepte;
	String helpName;
	
	private ArrayList<String> names;
	
	String currentUser;
	ParseUser cUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_allerezepte);		
		
		initParse();
		referenceUIElements();
		
		aktuList("Alle Kategorien",ed_allerezepte_search.getText().toString());
		
		spinnerListener(ed_allerezepte_search.getText().toString());
		editTextChangeListener();
		
		addListViewListener();
		
	}

	private void addListViewListener() {
		lv_allerezepte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {				 			  
					Intent i = new Intent(AlleRezepte.this, Rezept.class);
					i.putExtra("Name", names.get(position));
					startActivity(i);				   
			  }		
		});
		
	}

	private void editTextChangeListener() {
		ed_allerezepte_search.addTextChangedListener(new TextWatcher(){
			@Override
	        public void onTextChanged(CharSequence s, int start, int before, int count){			
	        }
			@Override
			public void afterTextChanged(Editable s) {
				aktuList(sp_allerezepte_spinner.getSelectedItem().toString(),s.toString());
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
	    }); 		
	}

	private void spinnerListener(final String name) {
		sp_allerezepte_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
        	public void  onItemSelected(AdapterView<?> parent, View view, int position, long id){			
				String selected_item = (parent.getItemAtPosition(position)).toString();	
				aktuList(selected_item, ed_allerezepte_search.getText().toString());				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {		
			}
		});		
	}

	private void aktuList(final String cat, final String name) {
		ParseQueryAdapter<ParseObject> adapter =  new ParseQueryAdapter<ParseObject>(this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			public ParseQuery<ParseObject> create() {	   
				List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();			
				if(name.length()>0){
					String[] arr = name.split(" ");    
					for ( String ss : arr) {
						if(ss.charAt(0)>='A'&&ss.charAt(0)<='Z'){
							helpName = ss.toLowerCase();
						}else{
							char helpChar = ss.charAt(0);
							helpName = (helpChar+"").toUpperCase()+ss.substring(1);
						}
						ParseQuery<ParseObject> parseQueryName = new ParseQuery<ParseObject>("Gericht");
						parseQueryName.whereContains("Name", ss);
						queries.add(parseQueryName);
						ParseQuery<ParseObject> helpParseQueryName = new ParseQuery<ParseObject>("Gericht");
						helpParseQueryName.whereContains("Name", helpName);
						queries.add(helpParseQueryName);
					 }
				}else{
					ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Gericht");	
					queries.add(query);
				}				
				ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);				
				if(cat.equals("Alle Kategorien")){					
				}else{
					mainQuery.whereContains("Kategorie", cat);
				}
				
				try {
					names.clear();
					List<ParseObject> results = mainQuery.find();
					for(int i = 0; i < results.size(); i++){
						  String name = results.get(i).getString("Name");
						  names.add(name);
					 }
				} catch (ParseException e) {
					e.printStackTrace();
				}

				return mainQuery;
			}
			
		});
		adapter.setTextKey("Name");		
		lv_allerezepte.setAdapter(adapter);		
	}

	private void referenceUIElements() {
		ed_allerezepte_search = (EditText) findViewById (R.id.ed_allerezepte_search);
		sp_allerezepte_spinner = (Spinner) findViewById (R.id.sp_allerezepte_spinner);
		lv_allerezepte = (ListView) findViewById (R.id.lv_allerezepte);	
		names = new ArrayList <String>();
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

}
