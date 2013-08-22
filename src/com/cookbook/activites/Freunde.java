package com.cookbook.activites;

import java.util.ArrayList;
import java.util.List;

import com.cookbook.classes.CustomAdapter;
import com.cookbook.classes.FreundeSimpleArrayAdapter;
import com.cookbook.classes.Zutat;
import com.example.cookbook.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;


public class Freunde extends Activity {

	ParseUser cUser;
	String currentUser;
	String TAG = "Error";
	ListView lv_freunde;
	Button bt_freunde_befreunden;
	EditText ed_freunde_freund;
	FreundeSimpleArrayAdapter adapter;
	ProgressBar pb_freunde;
	
	RelativeLayout freunde;
	
	String freund;
	
	private ArrayList<Zutat> zutaten;
	
	Boolean isUser = false;
	Boolean isNotFriend = false;
	Boolean hasAlreadyFriends = false;
	Boolean zutatVorhanden;
	
	String id;
	
	private ArrayList<String> friends = new ArrayList<String>();
	
	private ArrayList<String> users;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_freunde);	
		
		referenceUIElements();
		initParse();
		
		getAllUsers();
		
		getFriends();
		
		clickListener();
		listViewListener();
	}

	private void getZutaten() {
		if(hasAlreadyFriends){
			ParseQuery<ParseObject> query = ParseQuery.getQuery("TogetherUserData");
			query.whereContains("Username", id);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> scoreList, ParseException e) {
			        if (e == null && scoreList.size()!=0) {		
			        	zutaten.clear();
			        	for(int i = 0; i < scoreList.size(); i++){
			        		Zutat newZutat = new Zutat (scoreList.get(i).getString("Zutat"),scoreList.get(i).getDouble("Menge"),scoreList.get(i).getString("Masseinheit"));
			        		zutaten.add(newZutat);
			        	}	
			        } else {
			        }	
			     }
			});
		}else{
			ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
			query.whereContains("Username", currentUser);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> scoreList, ParseException e) {
			        if (e == null && scoreList.size()!=0) {		
			        	zutaten.clear();
			        	for(int i = 0; i < scoreList.size(); i++){
			        		Zutat newZutat = new Zutat (scoreList.get(i).getString("Zutat"),scoreList.get(i).getDouble("Menge"),scoreList.get(i).getString("Masseinheit"));
			        		zutaten.add(newZutat);
			        	}	
			        } else {
			        }	    
			     }
			});
		}		
	}

	private void listViewListener() {
		lv_freunde.setOnItemLongClickListener(new OnItemLongClickListener() {
			
				
			
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,final int arg2, long arg3) {
			
				Toast toast = Toast.makeText(getApplicationContext(), "Sie sind nicht mehr mit "+friends.get(arg2).toString()+" befreundet!", Toast.LENGTH_SHORT);
				toast.show();
				
				freund = friends.get(arg2).toString();
				
				friends.remove(arg2);
				adapter.notifyDataSetChanged();
			
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Freunde");
				query.whereContains("User", currentUser);
				query.whereContains("Freund", freund);
				query.findInBackground(new FindCallback<ParseObject>() {
				    public void done(List<ParseObject> scoreList, ParseException e) {
				        if (e == null && scoreList.size()!=0) {
				        	scoreList.get(0).deleteInBackground();
				        } else {

				        }					
						removeUserFromDatabase(freund);						
				    }					
				});
				return true;
			}
        }); 		
	}
	
	private void removeUserFromDatabase(String freund) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
		query.whereContains("Username", freund);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null && scoreList.size()!=0) {
		        	for(int i = 0; i < scoreList.size(); i++){
		        		for(int m = 0; m < zutaten.size(); m++){
		        			if(zutaten.get(m).getName().equals(scoreList.get(i).getString("Zutat"))&&zutaten.get(m).getEinheit().equals(scoreList.get(i).getString("Masseinheit"))){
		        				zutaten.get(m).delMenge(scoreList.get(i).getDouble("Menge"));
		        			}
		        		}
		        	}
		        	for(int i = 0; i < zutaten.size(); i++){
		        		if(zutaten.get(i).getMenge()==0){
		        			zutaten.remove(i);
		        		}
		        	}
		        	Log.e("TAG", zutaten.size()+"");
		        	deleteOldDatabase();
		        } else {

		        }
		    }			
		});				
	}

	private void clickListener() {
		bt_freunde_befreunden.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isUser = false;
				isNotFriend = false;				
				if(ed_freunde_freund.getText().toString().length()!=0){
					for(int i = 0; i < users.size(); i++){
						if(ed_freunde_freund.getText().toString().equals(users.get(i))){
							if(ed_freunde_freund.getText().toString().equals(currentUser)){								
							}else{
								isUser = true;
							}
						}
					}
					for(int i = 0; i < friends.size(); i++){
						if(ed_freunde_freund.getText().toString().equals(friends.get(i))){
							isNotFriend = true;
						}
					}
					if(isUser&&isNotFriend==false){
						createUserConnection(ed_freunde_freund.getText().toString());						
						Toast toast = Toast.makeText(getApplicationContext(), ed_freunde_freund.getText().toString()+" wurde hinzugefügt!", Toast.LENGTH_SHORT);
						toast.show();
						ed_freunde_freund.setText(null);
					}else if(isUser==false){
						Toast toast = Toast.makeText(getApplicationContext(), "Der User existiert nicht!", Toast.LENGTH_SHORT);
						toast.show();
					}else{
						Toast toast = Toast.makeText(getApplicationContext(), "Sie sind bereits mit dem User befreundet!", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			}			
		});
	}

	//Alle Benutzer
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
	
	//Alle Freunde
	private void getFriends(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Freunde");
		query.whereContains("User", currentUser);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null && scoreList.size()!=0) {		
		        	for(int i = 0; i < scoreList.size(); i++){
		        		id = scoreList.get(0).getString("ID");
		        		friends.add(scoreList.get(i).getString("Freund"));
		        		hasAlreadyFriends = true;
		        	}		        	
		        } else {
		        	hasAlreadyFriends = false;
		        }
		        getZutaten();
		        pb_freunde.setVisibility(View.INVISIBLE);
		        adapter.notifyDataSetChanged();
		        setBackground();
		    }
		});
		
		
		
	}
	
	private void setBackground(){
		if(friends.size()<1){
	    	   freunde.setBackgroundResource(R.drawable.freundebg);
	    }else{
	    	   freunde.setBackgroundResource(R.drawable.bg);
	    }	
	}
	
	private void createUserConnection(String freund) {
		if(friends.size()==0){
			id = freund.hashCode()+"";
		}
		ParseObject data = new ParseObject("Freunde");
		data.put("User", currentUser);
		data.put("Freund", freund);
		data.put("ID", id);
		data.saveInBackground();
		friends.add(freund);
		getFriendsData(freund);
		adapter.notifyDataSetChanged();
		setBackground();
	}
	
	private void getFriendsData(String freund) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
		query.whereContains("Username", freund);
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
		        	Log.e("TAG", zutaten.size()+"");
		        	deleteOldDatabase();
		        } else {
		        }			    
		     }
		});		
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
	}
	
	private void referenceUIElements() {
		ed_freunde_freund = (EditText) findViewById (R.id.ed_freunde_freund);
		lv_freunde = (ListView) findViewById (R.id.lv_freunde);
		bt_freunde_befreunden = (Button) findViewById (R.id.bt_freunde_befreunden);	
		pb_freunde = (ProgressBar) findViewById (R.id.pb_freunde);
		freunde = (RelativeLayout) findViewById(R.id.layout_freunde);
		pb_freunde.setProgress(5);
		zutaten = new ArrayList<Zutat>();
		users = new ArrayList<String>();
		friends = new ArrayList<String>();
		adapter = new FreundeSimpleArrayAdapter(this, friends);
		lv_freunde.setAdapter(adapter);
	}

	private void initParse() {
		Parse.initialize(this, "PXJakVYimXSoEUbQvyiNRIB3LzCbP0FEqFOM7NZD", "ms0stwKSjkAcbhuBFs3LOt0Qmjt50UZ3buElHYGm");
		ParseAnalytics.trackAppOpened(getIntent());	
		ParseUser.enableAutomaticUser();
		cUser = ParseUser.getCurrentUser();
		currentUser = cUser.get("username").toString();		
	}
}
	
	