package com.cookbook.activites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cookbook.classes.ConnectionDetector;
import com.example.cookbook.R;
import com.example.cookbook.R.layout;
import com.example.cookbook.R.menu;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {

	private TextView tv_register_benutzer, tv_register_passwort;
	private Button bt_register_register;
	private EditText ed_register_benutzer, ed_register_passwort;
	private ProgressBar pb_register;
	
	private Typeface font, font_bold;
	
	private ConnectionDetector cd;	
	private Boolean isInternetPresent = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register);
		
		initParse();
		
		referenceUIElements();	
		setFonts();	
		
		onClickListener();		
	}
	
	//Setzts Fonts der Register-Activity
	private void setFonts() {
		font_bold = Typeface.createFromAsset(getAssets(), "fonts/font_bold.ttf");
		font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
		tv_register_benutzer.setTypeface(font_bold);
		tv_register_passwort.setTypeface(font_bold);
		bt_register_register.setTypeface(font_bold);
		ed_register_benutzer.setTypeface(font);
		ed_register_passwort.setTypeface(font);
	}

	//Klick-Listener des Buttons "Registrierung"
	private void onClickListener() {
		bt_register_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isInternetPresent = cd.isConnectingToInternet();
				//�berpr�ft auf Internetverbindung
                if (isInternetPresent) {
                	//�berpr�ft, ob alle Felder ausgef�llt worden sind
                	if(ed_register_benutzer.getText().toString().length()!=0&&ed_register_passwort.getText().toString().length()!=0){
						pb_register.setVisibility(View.VISIBLE);
						registerParse();
					}else{
						Toast toast = Toast.makeText(getApplicationContext(), "Bitte alle Felder ausf�llen!", Toast.LENGTH_SHORT);
						toast.show();
					}
                } else {
                	Toast toast = Toast.makeText(getApplicationContext(), "Keine Internetverbindung!", Toast.LENGTH_LONG);
					toast.show();
                }					
			}
		});
	}

	//Registriert User auf Parse.com und gibt Feedback oder Fehlermeldung
	private void registerParse() {
		ParseUser user = new ParseUser();
		user.setUsername(ed_register_benutzer.getText().toString());
		user.setPassword(ed_register_passwort.getText().toString());		
		user.signUpInBackground(new SignUpCallback() {
		  public void done(ParseException e) {
			  pb_register.setVisibility(View.INVISIBLE);
			  if (e == null) {
				Toast toast = Toast.makeText(getApplicationContext(), "Registrierung erfolgreich", Toast.LENGTH_SHORT);
				toast.show();
		    	Intent i = new Intent(Register.this,Main.class);
				startActivity(i);
			  }else {
		    	Toast toast = Toast.makeText(getApplicationContext(), "Registrierung fehlgeschlagen", Toast.LENGTH_LONG);
				toast.show();
			  }
		  }
		});
	}

	//Initalisiert Parse
	private void initParse() {
		Parse.initialize(this, "PXJakVYimXSoEUbQvyiNRIB3LzCbP0FEqFOM7NZD", "ms0stwKSjkAcbhuBFs3LOt0Qmjt50UZ3buElHYGm");
		ParseAnalytics.trackAppOpened(getIntent());	
		ParseUser.enableAutomaticUser();
	}

	private void referenceUIElements() {
		tv_register_benutzer = (TextView) findViewById (R.id.tv_register_benutzer);
		tv_register_passwort= (TextView) findViewById (R.id.tv_register_passwort);
		bt_register_register= (Button) findViewById (R.id.bt_register_register);
		ed_register_benutzer= (EditText) findViewById (R.id.ed_register_benutzer);
		ed_register_passwort= (EditText) findViewById (R.id.ed_register_passwort);	
		pb_register = (ProgressBar) findViewById (R.id.pb_register);
		pb_register.setVisibility(View.INVISIBLE);
		cd = new ConnectionDetector(getApplicationContext());	
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
