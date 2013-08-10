package com.cookbook.activites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.cookbook.R;
import com.example.cookbook.R.layout;
import com.example.cookbook.R.menu;
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

	private Button toNeueZutat, toAlleRezepte, toMeinSchrank;
	private List<ParseObject> todos;
	private Spinner spinner;
	private String selectedSpinner;
	
	private ArrayList<String> user = new ArrayList <String>();
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);	
		
		initParse();
		referenceUIElements();
		clickListener();
		aktuList("Alle Kategorien");
		
		spinnerListener();		
	}

	private void spinnerListener() {
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
        	public void  onItemSelected(AdapterView<?> parent, View view, int position, long id){
			
				selectedSpinner = (parent.getItemAtPosition(position)).toString();	
				aktuList(selectedSpinner);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	private void clickListener() {
		toNeueZutat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Main.this,NeueZutat.class);
				i.putStringArrayListExtra("user", user);
				startActivity(i);
			}
		});
		toAlleRezepte.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Main.this,AlleRezepte.class);
				i.putStringArrayListExtra("user", user);
				startActivity(i);
			}
		});
		toMeinSchrank.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Main.this,MeinSchrank.class);
				i.putStringArrayListExtra("user", user);
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
	}
	
	

	private void referenceUIElements() {
		toNeueZutat = (Button) findViewById (R.id.toNeueZutat);
		toAlleRezepte = (Button) findViewById (R.id.toAlleRezepte);
		toMeinSchrank = (Button) findViewById (R.id.toMeinSchrank);
		spinner = (Spinner) findViewById (R.id.sp_allerezepte_spinner);
		
		
		//user = getIntent().getExtras().getStringArrayList("user");
		if(user.size()==0){
			user.add("Hanswurst");
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
