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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MeinSchrank extends Activity {


	private ArrayList<Zutat> zutaten;
	
	ArrayList<String> zutaten_name;
	ArrayList<String> zutaten_einheit;
	ArrayList<Double> zutaten_menge;
	
	RelativeLayout meinSchrank;
	
	ListView lv_mein_schrank;
	private CustomAdapter adapter;
	Button bu_datenbank_verknüpfen, bt_meinSchrank_neueZutat;
	Dialog dialog;
	
	ProgressBar pb_meinSchrank;
	
	EditText et_meinschrank_dialog;
	Button bt_meinschrank_dialog;
	TextView tv_meinschrank_dialog;
	ParseUser cUser;
	String currentUser, friend;
	String id;
	final Context context = this;
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
		
		hasFriends();		
		
		clickListener();
		
		listListner();
	}
	
	private void listListner() {
		lv_mein_schrank.setOnItemLongClickListener(new OnItemLongClickListener()  {
			  public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long arg3) {		 			  
				  ContextThemeWrapper ctw = new ContextThemeWrapper( MeinSchrank.this, R.style.AppTheme );
				  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
						alertDialogBuilder.setTitle(zutaten.get(position).getName()+" löschen?");
						alertDialogBuilder
							.setCancelable(false)
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							})
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									if(userHasFriends){
										Toast toast = Toast.makeText(context, "Daten können nur gelöscht werden, wenn keine Verbindung zu anderen Datenbanken besteht!", Toast.LENGTH_LONG);
										toast.show();										
									}else{
										String helpString = zutaten.get(position).getName();
										zutaten.remove(position);
										adapter.notifyDataSetChanged();
										
										ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
										query.whereContains("Username", currentUser);
										query.whereContains("Zutat", helpString);
										query.findInBackground(new FindCallback<ParseObject>() {
										    public void done(List<ParseObject> scoreList, ParseException e) {
										    	if (e == null && scoreList.size()!=0) {	
										    		scoreList.get(0).deleteInBackground();										    		
										        } else {
										        	Log.e("TAG", "failed");
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
		lv_mein_schrank.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
				if(userHasFriends){
					Toast toast = Toast.makeText(context, "Daten können nur geändert werden, wenn keine Verbindung zu anderen Datenbanken besteht!", Toast.LENGTH_LONG);
					toast.show();										
				}else{
					
					dialog = new Dialog(MeinSchrank.this, R.style.ThemeDialogCustom) ;		
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(34));
					dialog.setContentView(R.layout.layout_meinschrank_dialog);	
					
					final EditText ed_meinschrank_dialog_artikel = (EditText) dialog.findViewById (R.id.ed_meinschrank_dialog_artikel);
					final EditText ed_meinSchrank_Dialog_Menge = (EditText) dialog.findViewById (R.id.ed_neueZutat_Menge);
					final Spinner sp_meinSchrank_Dialog_Spinner = (Spinner) dialog.findViewById (R.id.sp_meinSchrank_Dialog_Spinner);
					final Button bt_meinSchrank_dialog_speichern = (Button) dialog.findViewById (R.id.bt_meinSchrank_dialog_speichern);
					
					ed_meinschrank_dialog_artikel.setText(zutaten.get(arg2).getName());
					ed_meinSchrank_Dialog_Menge.setText(zutaten.get(arg2).getMenge().toString());
					String get = zutaten.get(arg2).getEinheit();				
					
					if(get.equals("kg")){
						sp_meinSchrank_Dialog_Spinner.setSelection(0);
					}else if(get.equals("g")){
						sp_meinSchrank_Dialog_Spinner.setSelection(1);
					}else if(get.equals("ml")){
						sp_meinSchrank_Dialog_Spinner.setSelection(2);
					}else if(get.equals("L")){
						sp_meinSchrank_Dialog_Spinner.setSelection(3);
					}else if(get.equals("Stück")){
						sp_meinSchrank_Dialog_Spinner.setSelection(4);
					}else if(get.equals("Dose")){
						sp_meinSchrank_Dialog_Spinner.setSelection(5);
					}				
					
					bt_meinSchrank_dialog_speichern.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							zutaten.remove(arg2);					
							Zutat newZutat = new Zutat (ed_meinschrank_dialog_artikel.getText().toString(),Double.parseDouble(ed_meinSchrank_Dialog_Menge.getText().toString()),sp_meinSchrank_Dialog_Spinner.getSelectedItem().toString());
			    			zutaten.add(newZutat);
			    			adapter.notifyDataSetChanged();	
			    			dialog.cancel();
			    			deleteOldDatabase();		    			
						}
					});					
	    			
				 	dialog.show();
				 	
				}
			}			
		});
	}

	public void onResume(){
		super.onResume();
		zutaten.clear();
		adapter.notifyDataSetChanged();	
		pb_meinSchrank.setVisibility(View.VISIBLE);
		hasFriends();	
	}
	
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

	private void akuList() {
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
			        	meinSchrank.setBackgroundResource(R.drawable.meinschrankbg);
			        }
			    	pb_meinSchrank.setVisibility(View.INVISIBLE);
			        adapter.notifyDataSetChanged();
			    }
			    
			});
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
			        	meinSchrank.setBackgroundResource(R.drawable.meinschrankbg);
			        }
			    	pb_meinSchrank.setVisibility(View.INVISIBLE);
			        adapter.notifyDataSetChanged();
			    }
			});
		}
	}
	
	private void clickListener() {
		bu_datenbank_verknüpfen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				Intent i = new Intent(MeinSchrank.this,Freunde.class);
				startActivity(i);				
			}
		});
		bt_meinSchrank_neueZutat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				Intent i = new Intent(MeinSchrank.this,NeueZutat.class);
				startActivity(i);				
			}
		});
	}
	
	private void hasFriends(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Freunde");
		query.whereContains("User", currentUser);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null && scoreList.size()!=0) {	
		        	Log.e("Error", "User has Friends");
		        	userHasFriends = true;
		        	id = scoreList.get(0).getString("ID");
		        	akuList();
		        } else {
		        	userHasFriends = false;
		        	Log.e("Error", "User has no Friends");
		        	akuList();
		        	
		        }
		    }
		});
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
		lv_mein_schrank = (ListView) findViewById(R.id.lv_mein_schrank);
		
		adapter = new CustomAdapter(this,zutaten);
		
		meinSchrank = (RelativeLayout) findViewById(R.id.meinSchrank);
		lv_mein_schrank.setAdapter(adapter);
		bu_datenbank_verknüpfen = (Button) findViewById(R.id.neueDatenbank);
		bt_meinSchrank_neueZutat = (Button) findViewById(R.id.bt_meinSchrank_neueZutat);
		zutaten_name = new ArrayList<String>();
		zutaten_einheit = new ArrayList<String>();
		zutaten_menge = new ArrayList<Double>();
		pb_meinSchrank = (ProgressBar) findViewById (R.id.pb_meinSchrank);
		pb_meinSchrank.setProgress(5);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
}
