package com.cookbook.activites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cookbook.classes.AlleRezepteSimpleArrayAdapter;
import com.cookbook.classes.MainSpinnerAdapter;
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
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTabHost;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class AlleRezepte extends Activity {

	private static final String[] Categories = new String[] {"Alle Kategorien", "Fleisch", "Vegetarisch", "Pasta", "Fisch", "Gefluegel", "Salate", "Auflauf", "Suppen und Eintoepfe",};

	EditText ed_allerezepte_search;
	Spinner sp_allerezepte_spinner;
	ListView lv_allerezepte;
	
	ProgressBar pb_allerezepte;
	String helpName;
	
	AlleRezepteSimpleArrayAdapter adapter;
	
	private ArrayList<String> names;
	private ArrayList<String> ids;
	private ArrayList<String> dauer;
	private ArrayList<String> kcal;
	
	TextView tv_allerezepte_suchbegriff;
	
	private Typeface font, font_bold;
	MainSpinnerAdapter msp;
	
	String selectedItem = "Alle Kategorien";
	
	String currentUser;
	ParseUser cUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_allerezepte);
		
		initParse();
		referenceUIElements();
		setFonts();
		
		spinnerListener(ed_allerezepte_search.getText().toString());
		editTextChangeListener();
		
		addListViewListener();
		
	}
	
	private void setFonts() {
		font_bold = Typeface.createFromAsset(getAssets(), "fonts/font_bold.ttf");
		font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
		ed_allerezepte_search = (EditText) findViewById (R.id.ed_allerezepte_search);		
		ed_allerezepte_search.setTypeface(font);
		tv_allerezepte_suchbegriff.setTypeface(font_bold);
		msp = new MainSpinnerAdapter(this, Categories, font_bold, R.layout.layout_main_spinner_style, R.id.tv_main_spinner_style);
		sp_allerezepte_spinner.setAdapter(msp);	
		adapter = new AlleRezepteSimpleArrayAdapter(AlleRezepte.this, names, dauer, kcal, ids, font, font_bold);
		lv_allerezepte.setAdapter(adapter);			
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
				aktuList(selectedItem,s.toString());
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
				selectedItem = (Categories[position].toString());	
				aktuList(selectedItem, ed_allerezepte_search.getText().toString());				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {		
			}
		});		
	}

	private void aktuList(final String cat, String name) {					
				List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();			
				if(name.length()>0){
					if(name.contains("Ä")){
						name = name.replaceAll("Ä", "Ae");
					}
					if(name.contains("ä")){
						name = name.replaceAll("ä", "ae");
					}
					if(name.contains("Ö")){
						name = name.replaceAll("Ö", "Oe");
					}
					if(name.contains("ö")){
						name = name.replaceAll("ö", "öe");
					}
					if(name.contains("Ü")){
						name = name.replaceAll("Ü", "Ue");
					}
					if(name.contains("ü")){
						name = name.replaceAll("ü", "ue");
					}
					if(name.contains("ß")){
						name = name.replaceAll("ß", "ss");
					}
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
				mainQuery.setLimit(1000);
				if(cat.equals("Alle Kategorien")){					
				}else{
					mainQuery.whereContains("Kategorie", cat);
				}
				
				try {
					names.clear();
					ids.clear();
					List<ParseObject> results = mainQuery.find();
					for(int i = 0; i < results.size(); i++){
						  ids.add(results.get(i).getNumber("ID").toString());
						  names.add(results.get(i).getString("Name"));
						  dauer.add(((int)Double.parseDouble(results.get(i).getNumber("Zeit").toString()))+"");
						  kcal.add(((int)Double.parseDouble(results.get(i).getNumber("Kcal").toString()))+"");
					 }
				} catch (ParseException e) {
					e.printStackTrace();
				}		
		adapter.notifyDataSetChanged();
	}

	private void referenceUIElements() {		
		sp_allerezepte_spinner = (Spinner) findViewById (R.id.sp_allerezepte_spinner);
		lv_allerezepte = (ListView) findViewById (R.id.lv_alleRezepte_liste);		
		ed_allerezepte_search = (EditText) findViewById (R.id.ed_allerezepte_search);
		tv_allerezepte_suchbegriff = (TextView) findViewById (R.id.tv_allerezepte_suchbegriff);
		names = new ArrayList <String>();
		ids = new ArrayList <String>(); 
		dauer = new ArrayList <String>();
		kcal = new ArrayList <String>(); 
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
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.ic_menu);
		return true;
	}

}
