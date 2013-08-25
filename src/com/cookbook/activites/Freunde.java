package com.cookbook.activites;

import java.util.ArrayList;
import java.util.List;
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
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;


public class Freunde extends Activity {
	
	private ListView lv_freunde;
	private RelativeLayout freunde;
	
	private Button bt_freunde_befreunden;
	private EditText ed_freunde_freund;
	
	private FreundeSimpleArrayAdapter adapter;
	private ProgressBar pb_freunde;
	
	private Typeface font, font_bold;
	
	private Boolean isUser = false;
	private Boolean isNotFriend = false;
	private Boolean hasAlreadyFriends = false;
	private Boolean zutatVorhanden = false;
	
	private ArrayList<Zutat> zutaten;
	private ArrayList<String> friends;
	private ArrayList<String> users;
	
	private ParseUser cUser;
	private String currentUser;
	private String id;
	
	private String freund;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_freunde);	
		
		referenceUIElements();
		initParse();
		setFonts();
		
		getAllUsers();
		
		getFriends();
		
		clickListener();
		listViewListener();
	}

	//Lädt die Daten...
	private void loadData() {
		//... aller Freunde und speichert diese in dem Array zutaten ab
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
			        }	
			     }
			});
			//... des Users und speichert diese in dem Array zutaten ab
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
			        }    
			     }
			});
		}		
	}

	//ListView-Listener
	private void listViewListener() {
		lv_freunde.setOnItemLongClickListener(new OnItemLongClickListener() {
			//Bei langem Klick auf Item
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,final int position, long arg3) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(Freunde.this, R.style.AppTheme ));
			  	alertDialogBuilder.setTitle("Die Verbindung zu "+friends.get(position)+" löschen?");
			  	alertDialogBuilder.setCancelable(false);
			  	
			  	//Nein-Button
			  	alertDialogBuilder.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
			  		public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
			  	
			  //Nein-Button
			  	alertDialogBuilder.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
			  		public void onClick(DialogInterface dialog,int id) {
			  			//Feedback
						Toast toast = Toast.makeText(getApplicationContext(), "Sie sind nicht mehr mit "+friends.get(position).toString()+" befreundet!", Toast.LENGTH_SHORT);
						toast.show();
						
						//Adapteraktualisierung
						freund = friends.get(position).toString();				
						friends.remove(position);
						adapter.notifyDataSetChanged();
						
						//Löscht die User-Freunde-Beziehung aus Freunde
						ParseQuery<ParseObject> query = ParseQuery.getQuery("Freunde");
						query.whereContains("User", currentUser);
						query.whereContains("Freund", freund);
						query.findInBackground(new FindCallback<ParseObject>() {
						    public void done(List<ParseObject> scoreList, ParseException e) {
						        if (e == null && scoreList.size()!=0) {
						        	scoreList.get(0).deleteInBackground();
						        }					
								removeUserFromDatabase(freund);						
						    }					
						});
			  			dialog.cancel();
					}
				});
			  	AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				return true;
			}
        }); 		
	}
	
	//Löscht die Daten des Freundes, der gelöscht wird aus der gemeinsamen Datenbank
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
		        	deleteOldDatabase();
		        }
		    }			
		});				
	}

	//Klick-Listener beim Button "Befreunden"
	private void clickListener() {
		bt_freunde_befreunden.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isUser = false;
				isNotFriend = false;
				
				//Überprüft, ob EditText-Field leer ist
				if(ed_freunde_freund.getText().toString().length()!=0){
					//Überprüft, ob der User exisitiert
					for(int i = 0; i < users.size(); i++){
						if(ed_freunde_freund.getText().toString().equals(users.get(i))){
							if(ed_freunde_freund.getText().toString().equals(currentUser)){								
							}else{
								isUser = true;
							}
						}
					}
					//Überprüft, ob der User und der Freund bereits befreundet sind
					for(int i = 0; i < friends.size(); i++){
						if(ed_freunde_freund.getText().toString().equals(friends.get(i))){
							isNotFriend = true;
						}
					}
					
					if(isUser&&isNotFriend==false){
						//Neuer Freund
						createUserConnection(ed_freunde_freund.getText().toString());						
						Toast toast = Toast.makeText(getApplicationContext(), ed_freunde_freund.getText().toString()+" wurde hinzugefügt!", Toast.LENGTH_SHORT);
						toast.show();
						ed_freunde_freund.setText(null);
					}else if(isUser==false){
						//Freund existiert nicht
						Toast toast = Toast.makeText(getApplicationContext(), "Der User existiert nicht!", Toast.LENGTH_SHORT);
						toast.show();
					}else{
						//Bereits befreundet
						Toast toast = Toast.makeText(getApplicationContext(), "Sie sind bereits mit dem User befreundet!", Toast.LENGTH_SHORT);
						toast.show();
					}
				}else{
					//Keine Daten
					Toast toast = Toast.makeText(getApplicationContext(), "Bitte füllen Sie alle Daten aus.", Toast.LENGTH_SHORT);
					toast.show();
				}
			}			
		});
	}

	//Alle Benutzer von der App werden im users-Array abgespeichert
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
	
	//Überprüft, ob der User Freunde hat und speichert diese in dem Array friends ab
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
		        //Lädt die Daten
		        loadData();
		        pb_freunde.setVisibility(View.INVISIBLE);
		        adapter.notifyDataSetChanged();
		        setBackground();
		    }
		});	
	}
	
	//Setzt den richtigen Hintergrund
	private void setBackground(){
		if(friends.size()<1){
			freunde.setBackgroundResource(R.drawable.bg_nofriends);
	    }else{
	    	freunde.setBackgroundResource(R.drawable.bg);
	    }	
	}
	
	//Erstellt die Beziehung zwischen dem neuen Freund und dem Usern
	private void createUserConnection(String freund) {
		//Wenn noch keine ID vorhanden ist, wird ein Hash erstellt
		if(friends.size()==0){
			id = (freund.hashCode()+"CookBook"+currentUser.hashCode()).hashCode()+"";
		}
		//Neuer Eintrag in der Datenbank Freund
		ParseObject data = new ParseObject("Freunde");
		data.put("User", currentUser);
		data.put("Freund", freund);
		data.put("ID", id);
		data.saveInBackground();
		//Aktualisert ListView
		friends.add(freund);
		getFriendsData(freund);
		adapter.notifyDataSetChanged();
		setBackground();
	}
	
	//Zutaten des neuen Freundes werden besorgt und zu den neuen im zutaten-Array hinzugefügt
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
		        	deleteOldDatabase();
		        } 		    
		     }
		});		
	}
	
	//Löscht alte Datenbank
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

	//Erstellt neue Datenbank
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
	
	//Initialisiert Daten
	private void referenceUIElements() {
		ed_freunde_freund = (EditText) findViewById (R.id.ed_freunde_freund);
		lv_freunde = (ListView) findViewById (R.id.lv_freunde);
		bt_freunde_befreunden = (Button) findViewById (R.id.bt_freunde_befreunden);	
		freunde = (RelativeLayout) findViewById(R.id.layout_freunde);
		pb_freunde = (ProgressBar) findViewById (R.id.pb_freunde);		
		pb_freunde.setProgress(5);
		zutaten = new ArrayList<Zutat>();
		users = new ArrayList<String>();
		friends = new ArrayList<String>();
	}
	
	//Setzt Font
	private void setFonts() {
		font_bold = Typeface.createFromAsset(getAssets(), "fonts/font_bold.ttf");
		font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
		ed_freunde_freund.setTypeface(font);
		bt_freunde_befreunden.setTypeface(font_bold);
		adapter = new FreundeSimpleArrayAdapter(this, friends, font_bold);
		lv_freunde.setAdapter(adapter);
	}

	//Initialisiert Parse
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
	
	