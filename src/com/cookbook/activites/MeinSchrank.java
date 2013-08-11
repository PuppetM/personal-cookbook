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
		getFriends();
		
		clickListener();	
		getAllUsers();
	}

	private void clickListener() {
		bu_datenbank_verknüpfen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = new Dialog(MeinSchrank.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth) ;		
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
				dialog.setContentView(R.layout.layout_meinschrank_dialog);	
				
				et_meinschrank_dialog = (EditText) dialog.findViewById (R.id.et_meinschrank_dialog);
				bt_meinschrank_dialog = (Button) dialog.findViewById (R.id.bt_meinschrank_dialog);
				tv_meinschrank_dialog = (TextView) dialog.findViewById (R.id.tv_meinschrank_dialog);
									
				bt_meinschrank_dialog.setOnClickListener(new OnClickListener() {		
		        	public void onClick(View v) {	        			
		        		if(et_meinschrank_dialog.getText().toString().length()!=0){	        		
			        		for(int i = 0; i < users.size(); i++){
			        			if(users.get(i).equals(et_meinschrank_dialog.getText().toString())&&et_meinschrank_dialog.getText().toString().equals(currentUser)==false){
			        				getUser = true;			        				
			        			}
			        		}
			        		
			        		if(getUser){
			        			friend = et_meinschrank_dialog.getText().toString();
			        			for(int i = 0; i < friends.size(); i++){
			        				if(friends.get(i).equals(friend)){
			        					alreadyFriend = true;
			        				}
			        			}
			        			if(alreadyFriend){
			        				Toast toast = Toast.makeText(getApplicationContext(), "Mit "+friend+" bereits verknüpft!", Toast.LENGTH_SHORT);
									toast.show();	
			        			}else{
				        			createUserConnection(friend);
					        		zutaten.clear();
						        	aktuList();
						        	dialog.dismiss();
						        	Toast toast = Toast.makeText(getApplicationContext(), "Datenbank mit User "+et_meinschrank_dialog.getText().toString()+" verknüpft", Toast.LENGTH_SHORT);
									toast.show();
			        			}
			        		}else{
				        		Toast toast = Toast.makeText(getApplicationContext(), "Username nicht vorhanden!", Toast.LENGTH_SHORT);
								toast.show();	
			        		}
		        		}
					}

						
			 	});			 	
			 	dialog.show();				
				
			}
		});
	}
	
	private void getAllUsers() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		            for(int i = 0; i < scoreList.size(); i++){
		            	String name = scoreList.get(i).get("username").toString();
		    			users.add(name);
		            }
		        } else {
		        }
		    }
		});						
	}	
	
	private void createUserConnection(String freund) {
		ParseObject data = new ParseObject("Freunde");
		data.put("User", currentUser);
		data.put("Freund", freund);
		data.saveInBackground();
		
		ParseObject data2 = new ParseObject("Freunde");
		data2.put("User", freund);
		data2.put("Freund", currentUser);
		data2.saveInBackground();
		
		getFriends();
	}	
	
	private void getFriends(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Freunde");
		query.whereContains("User", currentUser);
		query.orderByAscending("updatedAt");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null && scoreList.size()!=0) {
		        	userHasFriends=true;
		        		        	
		        	for(int i = 0; i < scoreList.size(); i++){
		        		friends.add(scoreList.get(i).getString("Freund"));
		        	}
		        	id = scoreList.get(0).getObjectId();
		        	generateTogetherDB(scoreList.get(0).getObjectId());
		        	aktuList();	
		        } else {
		        	aktuList();
		        }
		    }
		});
	}
	
	private void generateTogetherDB(final String id) {
		Runnable myJob = new MyDemoRunner(id);
		Thread myThread = new Thread(myJob);
		myThread.start();
	}

	private void aktuList(){
		if(userHasFriends){
			ParseQuery<ParseObject> query = ParseQuery.getQuery("TogetherUserData");
			query.whereContains("Username", id);
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
		}else{
			ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
			query.whereContains("Username", currentUser);
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
	
	class MyDemoRunner implements Runnable {
		
		String userId;
		
		public MyDemoRunner(String id) {
			this.userId = id;
		}

		public void run() {
			try {
				workVeryLong();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void workVeryLong() throws InterruptedException {
			friends.add(currentUser);
			//Freunde
			for(int i = 0; i < friends.size(); i++){
				Thread.sleep(1200);	
				final int iFriend = i;
				//Get together Data
				ParseQuery<ParseObject> query = ParseQuery.getQuery("TogetherUserData");
				query.whereContains("Username", userId);
				query.findInBackground(new FindCallback<ParseObject>() {
				    public void done(final List<ParseObject> togetherScoreList, ParseException e) {	
				    	//If done: Get Userdata
				    	ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
						query.whereContains("Username", friends.get(iFriend));
						query.findInBackground(new FindCallback<ParseObject>() {
						    public void done(final List<ParseObject> userScoreList, ParseException e) {	    	
						    	//Saves Userdata in Array
						    	zutaten_name.clear();
						    	zutaten_einheit.clear();
						    	zutaten_menge.clear();
						    	for(int m = 0; m < userScoreList.size(); m++){
					            	String name = userScoreList.get(m).get("Zutat").toString();
					            	String einheit = userScoreList.get(m).get("Masseinheit").toString();
					            	Double menge = Double.parseDouble(userScoreList.get(m).get("Menge").toString());	
					            	zutaten_name.add(name);
					            	zutaten_einheit.add(einheit);
					            	zutaten_menge.add(menge);
						    	}
						    	for(int m = 0; m < zutaten_name.size(); m++){			            	
						    		vorhanden = false;
						    		for(int k = 0; k < togetherScoreList.size(); k++){			            	
						    			if(zutaten_name.get(m).equals(togetherScoreList.get(k).getString("Zutat"))){
						    				vorhanden = true;
						    			}
							    	}
						    		if(!vorhanden){
							    		ParseObject data = new ParseObject("TogetherUserData");
										data.put("Masseinheit", zutaten_einheit.get(m));
										data.put("Zutat", zutaten_name.get(m));
										data.put("Username", userId);
										data.put("Menge", zutaten_menge.get(m));
										data.saveInBackground();
											
							    	}
						    	}
					    						    				    	
						    }
						});		    				    	
				    }
				});
			}			
			
		}
    }
	
}
