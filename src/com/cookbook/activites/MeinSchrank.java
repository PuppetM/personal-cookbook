package com.cookbook.activites;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


	public ArrayList<Zutat> zutaten;
	ListView lv_mein_schrank;
	private CustomAdapter adapter;
	Button bu_datenbank_verknüpfen;
	
	private ArrayList<String> user;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_meinschrank);	
		
		referenceUIElements();
		initParse();		
		clickListener();	
		aktuList();
	}

	private void clickListener() {
		bu_datenbank_verknüpfen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				
				
			}
		});
	}

	private void aktuList(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
		query.whereContainedIn("Username", user);
		query.orderByAscending("Zutat");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		            for(int i = 0; i < scoreList.size(); i++){
		            	String name = scoreList.get(i).get("Zutat").toString();
		            	String einheit = scoreList.get(i).get("Masseinheit").toString();
		            	Double menge = Double.parseDouble(scoreList.get(i).get("Menge").toString());
		            	Zutat newZutat = new Zutat(name, menge, einheit);
		    			zutaten.add(newZutat);
		            }
		        } else {

		        }
				adapter.notifyDataSetChanged();
		    }
		});
		
	}
	
	private void initParse() {
		Parse.initialize(this, "PXJakVYimXSoEUbQvyiNRIB3LzCbP0FEqFOM7NZD", "ms0stwKSjkAcbhuBFs3LOt0Qmjt50UZ3buElHYGm");
		ParseAnalytics.trackAppOpened(getIntent());	
		ParseUser.enableAutomaticUser();
	}

	private void referenceUIElements() {
		zutaten = new ArrayList<Zutat>();
		lv_mein_schrank = (ListView) findViewById(R.id.lv_mein_schrank);
		adapter = new CustomAdapter(this,R.id.lv_mein_schrank,zutaten);
		lv_mein_schrank.setAdapter(adapter);
		bu_datenbank_verknüpfen = (Button) findViewById(R.id.neueDatenbank);
		user = new ArrayList <String>();
		user = getIntent().getExtras().getStringArrayList("user");

		

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
