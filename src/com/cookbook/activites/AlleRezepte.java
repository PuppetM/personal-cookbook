package com.cookbook.activites;

import java.util.ArrayList;
import java.util.List;
import com.cookbook.classes.AlleRezepteSimpleArrayAdapter;
import com.cookbook.classes.MainSpinnerAdapter;
import com.example.cookbook.R;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class AlleRezepte extends Activity {

	private static final String[] Categories = new String[] {"Alle Kategorien", "Fleisch", "Vegetarisch", "Pasta", "Fisch", "Gefluegel", "Salate", "Auflauf", "Suppen und Eintoepfe",};

	private EditText ed_allerezepte_search;
	private Spinner sp_allerezepte_spinner;
	private ListView lv_allerezepte;	
	private TextView tv_allerezepte_suchbegriff;
	
	private MainSpinnerAdapter msp;
	private AlleRezepteSimpleArrayAdapter adapter;
	
	private ArrayList<String> names;
	private ArrayList<String> ids;
	private ArrayList<String> dauer;
	private ArrayList<String> kcal;
	
	private Typeface font, font_bold;	
	
	private String selectedItem = "Alle Kategorien";	
	private String helpName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_allerezepte);
		
		initParse();
		referenceUIElements();
		setFonts();
		setAdapter();
		
		spinnerListener(ed_allerezepte_search.getText().toString());
		editTextChangeListener();
		
		addListViewListener();		
	}
	
	//Setzt die Fonts
	private void setFonts() {
		font_bold = Typeface.createFromAsset(getAssets(), "fonts/font_bold.ttf");
		font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
		ed_allerezepte_search = (EditText) findViewById (R.id.ed_allerezepte_search);		
		ed_allerezepte_search.setTypeface(font);
		tv_allerezepte_suchbegriff.setTypeface(font_bold);
					
	}
	
	//Setzt die Spinner- und ListView-Adapter
	private void setAdapter(){
		//Spinner-Adapter
		msp = new MainSpinnerAdapter(this, Categories, font_bold, R.layout.layout_main_spinner_style, R.id.tv_main_spinner_style);
		sp_allerezepte_spinner.setAdapter(msp);	
		//ListView-Adapter
		adapter = new AlleRezepteSimpleArrayAdapter(AlleRezepte.this, names, dauer, kcal, ids, font, font_bold);
		lv_allerezepte.setAdapter(adapter);
	}
	
	//ListView-Listener
	private void addListViewListener() {
		lv_allerezepte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {				 			  
					//OnItemClick -> Neue Acitivity mit dem Rezept
				  	Intent i = new Intent(AlleRezepte.this, Rezept.class);
					i.putExtra("Name", names.get(position));
					startActivity(i);				   
			  }		
		});
		
	}

	//editText-Input-Change-Listener: Falls sich der Input �ndert
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

	//SpinnerListener: Aktualisiert ListView, falls Spinner ge�ndert wird
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

	//Editiert, falls im Input Umlaute vorhanden sind
	private String editString(String name){
		if(name.contains("�")){
			name = name.replaceAll("�", "Ae");
		}
		if(name.contains("�")){
			name = name.replaceAll("�", "ae");
		}
		if(name.contains("�")){
			name = name.replaceAll("�", "Oe");
		}
		if(name.contains("�")){
			name = name.replaceAll("�", "�e");
		}
		if(name.contains("�")){
			name = name.replaceAll("�", "Ue");
		}
		if(name.contains("�")){
			name = name.replaceAll("�", "ue");
		}
		if(name.contains("�")){
			name = name.replaceAll("�", "ss");
		}
		return name;
	}
	
	//Aktualisert den Listview
	private void aktuList(final String cat, String name) {
			//Wandelt den �bergegebenen String in zwei Strings um 
			List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();			
			if(name.length()>0){
				name = editString(name);
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
				//Query-Anfrage
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Gericht");	
				queries.add(query);
			}				
			//OR-Query
			ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);	
			mainQuery.setLimit(1000);
			if(cat.equals("Alle Kategorien")==false){
				mainQuery.whereContains("Kategorie", cat);
			}
			
			//Speichert die gewonnenen Daten in Arrays ab
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
		//Adapter-Aktualiserung	
		adapter.notifyDataSetChanged();
	}

	//Initialisierung
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

	//Initalisiert Parse
	private void initParse() {
		Parse.initialize(this, "PXJakVYimXSoEUbQvyiNRIB3LzCbP0FEqFOM7NZD", "ms0stwKSjkAcbhuBFs3LOt0Qmjt50UZ3buElHYGm");
		ParseAnalytics.trackAppOpened(getIntent());	
		ParseUser.enableAutomaticUser();	
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
