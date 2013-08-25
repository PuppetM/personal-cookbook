package com.cookbook.activites;

import java.util.ArrayList;
import java.util.List;
import com.cookbook.classes.CustomAdapter;
import com.cookbook.classes.MainSpinnerAdapter;
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
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MeinSchrank extends Activity {

	private static final String[] Categories = new String[] {"g", "ml", "Stück", "Dose"};
	
	private ArrayList<Zutat> zutaten;
	
	private RelativeLayout meinSchrank;
	
	private ListView lv_mein_schrank;
	private CustomAdapter adapter;
	
	private Button bt_mein_schrank_datenbank, bt_mein_schrank_neue_zutat;
	
	private Dialog dialog;
	private MainSpinnerAdapter msp_Cat;
	
	private ProgressBar pb_meinSchrank;
	
	private ParseUser cUser;
	private String currentUser;
	private String id;
	private final Context context = this;
	private Typeface font, font_bold;
	private Boolean userHasFriends = false;
	boolean vorhanden;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_meinschrank);	
		
		referenceUIElements();
		initParse();
		
		setFonts();
		setAdapter();
		
		hasFriends();			
		clickListener();	
		
		listviewClickListner();
	}
	
	//ListView-Klick-Listener	
	private void listviewClickListner() {
		//Langer Klick auf ein Objekt - Löschen-AlertDialog
		lv_mein_schrank.setOnItemLongClickListener(new OnItemLongClickListener()  {
			  public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long arg3) {	
				  	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper( MeinSchrank.this, R.style.AppTheme ));
				  	alertDialogBuilder.setTitle(zutaten.get(position).getName()+" löschen?");
				  	alertDialogBuilder.setCancelable(false);
				  	
				  	//Nein-Button
				  	alertDialogBuilder.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
				  		public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});
					
				  	//Ja-Button
					alertDialogBuilder.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							//Überprüft, ob eine Verbindung zu einem Freund besteht
							if(userHasFriends){
								Toast toast = Toast.makeText(context, "Daten können nur gelöscht werden, wenn keine Verbindung zu anderen Datenbanken besteht!", Toast.LENGTH_LONG);
								toast.show();										
							}else{
								String helpString = zutaten.get(position).getName();
								zutaten.remove(position);
								//Wenn keine Zutaten mehr -> Hintergrund wird geändert
								if(zutaten.size()==0){
									meinSchrank.setBackgroundResource(R.drawable.bg_nozutaten);
								}
								//Adapter Aktualisierung
								adapter.notifyDataSetChanged();
								
								//Löschen im Hintergrund		
								ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
								query.whereContains("Username", currentUser);
								query.whereContains("Zutat", helpString);
								query.findInBackground(new FindCallback<ParseObject>() {
									public void done(List<ParseObject> scoreList, ParseException e) {
										if (e == null && scoreList.size()!=0) {	
											scoreList.get(0).deleteInBackground();										    		
										}
									}
								});
							}
							dialog.cancel();
						}
					});
					
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
					return false;
			  }						
		});
		
		//Short-Klick-Listener
		lv_mein_schrank.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
				//Überprüft, ob eine Verbindung zu einem Freund besteht
				if(userHasFriends){
					Toast toast = Toast.makeText(context, "Daten können nur geändert werden, wenn keine Verbindung zu anderen Datenbanken besteht!", Toast.LENGTH_LONG);
					toast.show();										
				}else{	
					//Dialog-Initialisierung				
					dialog = new Dialog(MeinSchrank.this, R.style.ThemeDialogCustom) ;		
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(40));
					dialog.setContentView(R.layout.layout_meinschrank_dialog);						
					final EditText ed_mein_schrank_dialog_name = (EditText) dialog.findViewById (R.id.ed_mein_schrank_dialog_name);
					final EditText ed_mein_schrank_dialog_menge = (EditText) dialog.findViewById (R.id.ed_mein_schrank_dialog_menge);
					final Spinner sp_mein_schrank_dialog = (Spinner) dialog.findViewById (R.id.sp_mein_schrank_dialog);
					final Button bt_mein_schrank_dialog_save = (Button) dialog.findViewById (R.id.bt_mein_schrank_dialog_save);
					final TextView tv_mein_schrank_dialog_header = (TextView) dialog.findViewById (R.id.tv_mein_schrank_dialog_header);
					
					//Set Fonts
					ed_mein_schrank_dialog_name.setTypeface(font_bold);			
					ed_mein_schrank_dialog_name.setTypeface(font);
					ed_mein_schrank_dialog_menge.setTypeface(font);
					tv_mein_schrank_dialog_header.setTypeface(font_bold);
					
					//Setzt den Inhalt des Dialogs
					ed_mein_schrank_dialog_name.setText(zutaten.get(arg2).getName());
					ed_mein_schrank_dialog_menge.setText(zutaten.get(arg2).getMenge().toString());
					
					//Spinner-Adapter
					String get = zutaten.get(arg2).getEinheit();				
					msp_Cat = new MainSpinnerAdapter(MeinSchrank.this, Categories, font_bold,R.layout.layout_main_spinner_style, R.id.tv_main_spinner_style);
					sp_mein_schrank_dialog.setAdapter(msp_Cat);					
					if(get.equals("g")){
						sp_mein_schrank_dialog.setSelection(0);
					}else if(get.equals("ml")){
						sp_mein_schrank_dialog.setSelection(1);
					}else if(get.equals("Stück")){
						sp_mein_schrank_dialog.setSelection(2);
					}else if(get.equals("Dose")){
						sp_mein_schrank_dialog.setSelection(3);
					}	
					
					//Wenn auf Speichern gedrückt wird
					bt_mein_schrank_dialog_save.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(ed_mein_schrank_dialog_name.getText().toString().length()!=0&&ed_mein_schrank_dialog_menge.getText().toString().length()!=0){
								zutaten.remove(arg2);
								Log.e("","Got here");
								Zutat newZutat = new Zutat (ed_mein_schrank_dialog_name.getText().toString(),Double.parseDouble(ed_mein_schrank_dialog_menge.getText().toString()),Categories[(Integer) sp_mein_schrank_dialog.getSelectedItem()]);
				    			zutaten.add(newZutat);
				    			Log.e("","Got here2");
				    			adapter.notifyDataSetChanged();	
				    			dialog.cancel();
				    			Log.e("","Got here3");
				    			deleteOldDatabase();
				    			Toast toast = Toast.makeText(context, "Daten wurden geändert.", Toast.LENGTH_SHORT);
								toast.show();	
							}else{
								Toast toast = Toast.makeText(context, "Alle Felder müssen ausgefüllt sein.", Toast.LENGTH_LONG);
								toast.show();
							}
						}
					});		
					dialog.show();				 	
				}
			}			
		});
	}

	//Wenn der User zurückkehrt
	public void onResume(){
		super.onResume();
		if(zutaten.size()!=0){
			meinSchrank.setBackgroundResource(R.drawable.bg);
			zutaten.clear();
			adapter.notifyDataSetChanged();	
			pb_meinSchrank.setVisibility(View.VISIBLE);
			hasFriends();	
		}
	}
	
	//Setzt Fonts
	private void setFonts(){
		font_bold = Typeface.createFromAsset(getAssets(), "fonts/font_bold.ttf");
		font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
		bt_mein_schrank_datenbank.setTypeface(font_bold);
		bt_mein_schrank_neue_zutat.setTypeface(font_bold);
	}
	
	//Setzt List-View Aapter
	private void setAdapter(){
		adapter = new CustomAdapter(this,zutaten, font, font_bold);
		lv_mein_schrank.setAdapter(adapter);
	}
	
	//Löscht alte Datenbank
	private void deleteOldDatabase(){
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

	//Erstellt neue Datenbank
	private void createNewDatabase(){
		for(int i = 0; i < zutaten.size(); i++){
			ParseObject data = new ParseObject("UserData");
			data.put("Username", currentUser);
			data.put("Masseinheit", zutaten.get(i).getEinheit());
			data.put("Menge", zutaten.get(i).getMenge());
			data.put("Zutat", zutaten.get(i).getName());
			data.saveInBackground();
		}
	}

	//Lädt Daten und speichert sie in der Objket-ArrayList zutaten ab
	private void loadData() {
		//Gemeinsame Datenbank aus TogetherUserData
		if(userHasFriends){
			ParseQuery<ParseObject> query = ParseQuery.getQuery("TogetherUserData");
			query.whereContains("Username", id);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> scoreList, ParseException e) {
			    	zutaten.clear();
			    	if (e == null && scoreList.size()!=0) {	
			        	for(int i = 0; i < scoreList.size(); i++){
			        		Zutat newZutat = new Zutat (scoreList.get(i).getString("Zutat"),scoreList.get(i).getDouble("Menge"),scoreList.get(i).getString("Masseinheit"));
		        			zutaten.add(newZutat);
			        	}
			        } else {
			        	//Keine Zutaten - andere Hintergrund
			        	meinSchrank.setBackgroundResource(R.drawable.bg_nozutaten);
			        }
			    	pb_meinSchrank.setVisibility(View.INVISIBLE);
			    	adapter.notifyDataSetChanged();
			    }
			    
			});
		//User Datenbank aus UserData
		}else{
			ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
			query.whereContains("Username", currentUser);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> scoreList, ParseException e) {
			    	zutaten.clear();
			    	if (e == null && scoreList.size()!=0) {	
			        	for(int i = 0; i < scoreList.size(); i++){
			        		Zutat newZutat = new Zutat (scoreList.get(i).getString("Zutat"),scoreList.get(i).getDouble("Menge"),scoreList.get(i).getString("Masseinheit"));
		        			zutaten.add(newZutat);
			        	}			 
			        } else {
			        	//Keine Zutaten - andere Hintergrund
			        	meinSchrank.setBackgroundResource(R.drawable.bg_nozutaten);
			        }
			    	pb_meinSchrank.setVisibility(View.INVISIBLE);
			    	adapter.notifyDataSetChanged();
			    }
			});
		}
	}
	
	//Klick-Listener der Button "Zutat hinzufügen" und "Datenbank verknüpfen"
	private void clickListener() {
		bt_mein_schrank_datenbank.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				Intent i = new Intent(MeinSchrank.this,Freunde.class);
				startActivity(i);				
			}
		});
		bt_mein_schrank_neue_zutat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				Intent i = new Intent(MeinSchrank.this,NeueZutat.class);
				startActivity(i);				
			}
		});
	}
	
	//Überprüft, ob der User gespeicherte Freunde hat
	private void hasFriends(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Freunde");
		query.whereContains("User", currentUser);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null && scoreList.size()!=0) {	
		        	id = scoreList.get(0).getString("ID");
		        	userHasFriends = true;
		        } else {
		        	userHasFriends = false;		        		        	
		        }
		        loadData();	
		    }
		});
	}
	
	//Initalisiert Parse
	private void initParse() {
		Parse.initialize(this, "PXJakVYimXSoEUbQvyiNRIB3LzCbP0FEqFOM7NZD", "ms0stwKSjkAcbhuBFs3LOt0Qmjt50UZ3buElHYGm");
		ParseAnalytics.trackAppOpened(getIntent());	
		ParseUser.enableAutomaticUser();
		cUser = ParseUser.getCurrentUser();
		currentUser = cUser.get("username").toString();
	}

	private void referenceUIElements() {
		lv_mein_schrank = (ListView) findViewById(R.id.lv_mein_schrank);		
		meinSchrank = (RelativeLayout) findViewById(R.id.meinSchrank);
		bt_mein_schrank_datenbank = (Button) findViewById(R.id.bt_mein_schrank_datenbank);
		bt_mein_schrank_neue_zutat = (Button) findViewById(R.id.bt_mein_schrank_neue_zutat);
		pb_meinSchrank = (ProgressBar) findViewById (R.id.pb_meinSchrank);
		pb_meinSchrank.setProgress(5);
		zutaten = new ArrayList<Zutat>();		
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
