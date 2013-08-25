package com.cookbook.activites;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cookbook.classes.Zutat;
import com.example.cookbook.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.os.AsyncTask;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Rezept extends Activity {
	
	private TextView tv_rezept_name, tv_rezept_beschreibung;
	private Button bt_rezept_gekocht;
	private ProgressBar pb_rezept;
	private ImageView iv_rezept_icon;
		
	private ArrayList<Zutat> zutaten;
	private ArrayList<Zutat> ownZutaten;
	private ArrayList<String> friends;
	
	private String name, beschreibung, friendId, currentUser;
	private int id;
	
	private Typeface font, font_bold;
	
	private ParseUser cUser;
	
	private Boolean done = false;	
	private Boolean userHasFriends;
	
	private View linearLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_rezept);		
		
		initParse();
		referenceUIElements();
		setFonts();				
		
		getId();
		
		hasFriends();
		clickListener();		
	}
	
	//Es wird überprüft, ob der User Freunde hat	
	private void hasFriends(){
		ownZutaten.clear();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Freunde");
		query.whereContains("User", currentUser);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null && scoreList.size()!=0) {	
		    		friends.clear();
		    		friends.add(currentUser);
		        	for(int i = 0; i < scoreList.size(); i++){
		        		friendId = scoreList.get(0).getString("ID");
		        		friends.add(scoreList.get(i).getString("Freund"));
		        	}
		        	userHasFriends = true;
		        	getData();
		        } else {
		        	userHasFriends = false;
		        	getData();
		        }
		    }
		});
	}
	
	//Die Daten des Users werden aus der Datenbank geholt...
	private void getData() {
		//... wenn der User mit Frenden kocht und im Array ownZutaten abgespeichert
		if(userHasFriends){
			ParseQuery<ParseObject> query = ParseQuery.getQuery("TogetherUserData");
			query.whereContains("Username", friendId);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> scoreList, ParseException e) {
			    	ownZutaten.clear();
			    	if (e == null && scoreList.size()!=0) {	
			        	for(int i = 0; i < scoreList.size(); i++){
			        		Zutat newZutat = new Zutat (scoreList.get(i).getString("Zutat"),scoreList.get(i).getDouble("Menge"),scoreList.get(i).getString("Masseinheit"));
			        		ownZutaten.add(newZutat);
			        	}
			        }
			    	done = true;
			    }			    
			});
			//... wenn der User álleine kocht und im Array ownZutaten abgespeichert
		}else{
			ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
			query.whereContains("Username", currentUser);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> scoreList, ParseException e) {
			    	ownZutaten.clear();
			    	if (e == null && scoreList.size()!=0) {	
			        	for(int i = 0; i < scoreList.size(); i++){
			        		Zutat newZutat = new Zutat (scoreList.get(i).getString("Zutat"),scoreList.get(i).getDouble("Menge"),scoreList.get(i).getString("Masseinheit"));
		        			ownZutaten.add(newZutat);
			        	}
			        }
			    	done = true;
			    }
			});
		}
	}
	
	//Die Beschreibung wird aus der Datenbank geholt
	private void setBeschreibung() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Beschreibung");
		query.whereEqualTo("ID", id);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		        	beschreibung = scoreList.get(0).getString("Beschreibung");
		        	tv_rezept_beschreibung.setText(beschreibung);
		        }
		    }
		});
		
	}

	//Die ID des Gericht wird aus der Datenbank geholt
	private void getId() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Gericht");
		query.whereEqualTo("Name", name);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		        	id = (int)(Double.parseDouble(scoreList.get(0).getNumber("ID").toString()));
		        	setBeschreibung();
		        	setPicture();
		        	getZutaten();
		        }
		    }
		});			
	}
	
	//Die Zutaten des Rezept werden aus der Datenbank heruntergeladen und in dem Objekt-Array zutaten gespeichert
	private void getZutaten(){
		if(done){
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Zutaten");
			query.whereEqualTo("ID", id);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> scoreList, ParseException e) {
			        if (e == null) {
			        	for(int i = 0; i < scoreList.size(); i++){
		        			Zutat newZutat = new Zutat (scoreList.get(i).getString("Zutat"),scoreList.get(i).getDouble("Menge"),scoreList.get(i).getString("Masseinheit"));
			        		zutaten.add(newZutat);
			        	}
			        }
			        setZutatenTextView();
			    }
			});	
		}else{
			getId();
		}
	}
	
	//Die Zutaten-List-View wird erstelllt
	private void setZutatenTextView() {
		for(int i = 0; i < zutaten.size(); i++){
			//New Child mit Layout
			View child = getLayoutInflater().inflate(R.layout.layout_rezept_liste, null);
			
			//Initialisierung
			TextView tv_rezepte_liste_einheit = (TextView) child.findViewById (R.id.tv_rezepte_liste_einheit);
			tv_rezepte_liste_einheit.setTypeface(font);	
        	TextView tv_rezepte_liste_menge = (TextView) child.findViewById (R.id.tv_rezepte_liste_menge);
        	tv_rezepte_liste_menge.setTypeface(font);	
        	TextView tv_rezepte_liste_name = (TextView) child.findViewById (R.id.tv_rezepte_liste_name);
        	tv_rezepte_liste_name.setTypeface(font);	
        	tv_rezepte_liste_menge.setText(zutaten.get(i).getMenge()+"");
			tv_rezepte_liste_einheit.setText(zutaten.get(i).getEinheit());
			tv_rezepte_liste_name.setText(zutaten.get(i).getName());
        	tv_rezepte_liste_einheit.setTextColor(Color.RED);	
        	tv_rezepte_liste_menge.setTextColor(Color.RED);	
        	tv_rezepte_liste_name.setTextColor(Color.RED);	
			
        	//Überprüft, ob Zutaten in der Datenbank vorhanden sind
			for(int m = 0; m < ownZutaten.size(); m++){
				//Zutaten sind vorhanden, reichen aber nicht aus
				if(zutaten.get(i).getName().equals(ownZutaten.get(m).getName())&&zutaten.get(i).getMenge()<=ownZutaten.get(m).getMenge()){
					tv_rezepte_liste_einheit.setText(zutaten.get(i).getEinheit());
					tv_rezepte_liste_menge.setText(zutaten.get(i).getMenge()+"");
					tv_rezepte_liste_name.setText(zutaten.get(i).getName());
					tv_rezepte_liste_einheit.setTextColor(Color.GREEN);	
		        	tv_rezepte_liste_menge.setTextColor(Color.GREEN);	
		        	tv_rezepte_liste_name.setTextColor(Color.GREEN);	
				//Zutaten sind vorhanden, reichen aus
				}else if(zutaten.get(i).getName().equals(ownZutaten.get(m).getName())&&zutaten.get(i).getMenge()>ownZutaten.get(m).getMenge()){
					tv_rezepte_liste_menge.setText((zutaten.get(i).getMenge()-ownZutaten.get(m).getMenge())+"");
					tv_rezepte_liste_einheit.setText(zutaten.get(i).getEinheit());
					tv_rezepte_liste_name.setText(zutaten.get(i).getName() + " fehlen noch!");
					tv_rezepte_liste_einheit.setTextColor(Color.RED);	
		        	tv_rezepte_liste_menge.setTextColor(Color.RED);	
		        	tv_rezepte_liste_name.setTextColor(Color.RED);						
				}
			}        
			//Child wird Layout hinzugefügt
	        ((LinearLayout) linearLayout).addView(child);	        
		}	
		tv_rezept_name.setVisibility(View.VISIBLE);
		iv_rezept_icon.setVisibility(View.VISIBLE);
		tv_rezept_beschreibung.setVisibility(View.VISIBLE);
		pb_rezept.setVisibility(View.INVISIBLE);
		bt_rezept_gekocht.setVisibility(View.VISIBLE);
		
	}

	//Die richtige Adresse des Pics wird heruntergeladen
	private void setPicture(){
		if(id<10){
			new DownloadImage().execute("http://www.marions-kochbuch.de/rezept/000"+id+".jpg");
		}else if(id<100){
			new DownloadImage().execute("http://www.marions-kochbuch.de/rezept/00"+id+".jpg");
		}else if(id<1000){
			new DownloadImage().execute("http://www.marions-kochbuch.de/rezept/0"+id+".jpg");
		}else{
			new DownloadImage().execute("http://www.marions-kochbuch.de/rezept/"+id+".jpg");
		}		
	}
	
	//Die Fonts werden gesetzt
	private void setFonts() {
		font_bold = Typeface.createFromAsset(getAssets(), "fonts/font_bold.ttf");
		font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
		tv_rezept_name.setTypeface(font_bold);
		tv_rezept_beschreibung.setTypeface(font);
		bt_rezept_gekocht.setTypeface(font_bold);
	}
	
	//Wenn der User auf den Button "Fertig gekocht" klickt
	private void clickListener() {
		bt_rezept_gekocht.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//User kocht mit Freunden -> User wird auf neues Intent weitergeleitet
				if(userHasFriends){
					
					
					///HIER KOMMT NOCH DEIN CODE REIN!!!! (Dein Acitivity, wo die Daten abgezogen werden)
					
					//Intent i = new Intent(Rezept.this,...
					//i.putExtra("ID", id);
					//startActivity(i);
					
					
					
				//Wenn der User alleine kocht, werden die Daten des Rezepts von der Datenbank abgezogen
				}else{
					Log.e("TAG", "Got here");
					for(int i = 0; i < zutaten.size(); i++){
						for(int m = 0; m < ownZutaten.size(); m++){
							if(zutaten.get(i).getName().equals(ownZutaten.get(m).getName())&&zutaten.get(i).getEinheit().equals(ownZutaten.get(m).getEinheit())){
								ownZutaten.get(m).delMenge(zutaten.get(i).getMenge());
								if(ownZutaten.get(m).getMenge()<=0){
									ownZutaten.remove(m);
								}
							}
						}
					}
					deleteOldDatabase();
				}
			}
		});
	}
	
	//Alte Datenbank des Users wird gelöscht
	private void deleteOldDatabase(){
		Log.e("TAG", "Got here");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
		query.whereContains("Username", currentUser);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null && scoreList.size()!=0) {		
		        	for(int i = 0; i < scoreList.size(); i++){
		        		scoreList.get(i).deleteInBackground();
		        	}
		        } else {
		       
		        }	
		        createNewDatabase();			        
		     }
		});	
	}

	//Neue Datenbank des Users wird erstellt und anschließend auf Main weitergeleitet
	private void createNewDatabase(){
		for(int i = 0; i < ownZutaten.size(); i++){
			ParseObject data = new ParseObject("UserData");
			data.put("Username", currentUser);
			data.put("Masseinheit", ownZutaten.get(i).getEinheit());
			data.put("Menge", ownZutaten.get(i).getMenge());
			data.put("Zutat", ownZutaten.get(i).getName());
			data.saveInBackground();
		}	
		newIntent();
	}
	
	//Weiterleitung auf Main
	private void newIntent(){
		Intent i = new Intent(Rezept.this,Main.class);
		startActivity(i);
	}

	//Initialisierung
	private void referenceUIElements() {	
		linearLayout = (LinearLayout)findViewById(R.id.linare_layout_rezepte_zutaten);
		tv_rezept_name = (TextView) findViewById (R.id.tv_rezept_name);
		tv_rezept_beschreibung = (TextView) findViewById (R.id.tv_rezept_beschreibung);
		iv_rezept_icon = (ImageView) findViewById (R.id.iv_rezept_icon);
		pb_rezept = (ProgressBar) findViewById (R.id.pb_rezept);
		bt_rezept_gekocht = (Button) findViewById (R.id.bt_rezept_gekocht);
		tv_rezept_name.setVisibility(View.INVISIBLE);
		iv_rezept_icon.setVisibility(View.INVISIBLE);
		tv_rezept_beschreibung.setVisibility(View.INVISIBLE);
		bt_rezept_gekocht.setVisibility(View.INVISIBLE);
		pb_rezept.setProgress(5);		
		name = getIntent().getExtras().getString("Name");
		tv_rezept_name.setText(name);
		zutaten = new ArrayList<Zutat>();
		ownZutaten = new ArrayList<Zutat>();
		friends = new ArrayList<String>();
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
	
	//Bild wird heruntergeladen
    private void setImage(Drawable drawable){
    	iv_rezept_icon.setImageDrawable(drawable);
    }
	
	public class DownloadImage extends AsyncTask<String, Integer, Drawable> {
		
	    @Override
	    protected Drawable doInBackground(String... arg0) {
	        return downloadImage(arg0[0]);
	    }
	
	    protected void onPostExecute(Drawable image){
	        setImage(image);
	    }
	
	    @SuppressWarnings("deprecation")
		private Drawable downloadImage(String _url){
	        URL url;        
	        InputStream in;
	        BufferedInputStream buf;
	        
	        try {
	            url = new URL(_url);
	            in = url.openStream();
	            buf = new BufferedInputStream(in);
	
	            Bitmap bMap = BitmapFactory.decodeStream(buf);
	            if (in != null) {
	                in.close();
	            }
	            if (buf != null) {
	                buf.close();
	            }
	
	            return new BitmapDrawable(bMap);
	
	        } catch (Exception e) {
	            Log.e("Error reading file", e.toString());
	        }
	
	        return null;
	    }
	
	}

}
