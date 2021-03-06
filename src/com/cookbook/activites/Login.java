package com.cookbook.activites;

import com.example.cookbook.R;
import com.cookbook.classes.*;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

	private TextView tv_login_benutzer, tv_login_passwort;
	private Button bt_login_login, bt_login_register;
	private EditText ed_login_benutzer, ed_login_passwort;
	private ProgressBar pb_login;
	
	private Typeface font, font_bold;
	private ConnectionDetector cd;
	
	private Boolean isInternetPresent = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);	
		
		initParse();
		referenceUIElements();
		setFonts();		
		currentUser();		
		onClickListener();		
	}
	
	//Setzt Fonts
	private void setFonts() {
		font_bold = Typeface.createFromAsset(getAssets(), "fonts/font_bold.ttf");
		font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
		tv_login_benutzer.setTypeface(font_bold);
		tv_login_passwort.setTypeface(font_bold);
		bt_login_login.setTypeface(font_bold);
		bt_login_register.setTypeface(font_bold);
		ed_login_benutzer.setTypeface(font);
		ed_login_passwort.setTypeface(font);	
	}
	
	//Wenn eingeloggt, wird User direkt weitergeleitet
	private void currentUser() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			
		} else {
			Intent i = new Intent(Login.this,Main.class);
			startActivity(i);
		}		
	}
	
	//KlickListener von "Login" und "Registrierung"	
	private void onClickListener() {
		//"Login"
		bt_login_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//�berpr�ft auf Internetverbindung
				isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                	pb_login.setVisibility(View.VISIBLE);
                	loginParse();
                } else {
                	Toast toast = Toast.makeText(getApplicationContext(), "Keine Internetverbindung!", Toast.LENGTH_LONG);
					toast.show();
                }
			}
		});
		//"Registrierung"
		bt_login_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                	Intent i = new Intent(Login.this,Register.class);
    				startActivity(i);
                } else {
                	Toast toast = Toast.makeText(getApplicationContext(), "Keine Internetverbindung!", Toast.LENGTH_LONG);
					toast.show();
                }
			}
		});
	}

	//Loggt den User auf parse.com ein
	private void loginParse() {
		ParseUser.logInInBackground(ed_login_benutzer.getText().toString(), ed_login_passwort.getText().toString(), new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			    	Intent i = new Intent(Login.this,Main.class);
					startActivity(i);
			    } else {
			    	pb_login.setVisibility(View.INVISIBLE);
			    	Toast toast = Toast.makeText(getApplicationContext(), "Login fehlgeschlagen!", Toast.LENGTH_SHORT);
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
		tv_login_benutzer = (TextView) findViewById (R.id.tv_login_benutzer);
		tv_login_passwort= (TextView) findViewById (R.id.tv_login_passwort);
		bt_login_login= (Button) findViewById (R.id.bt_login_login);
		bt_login_register= (Button) findViewById (R.id.bt_login_register);
		ed_login_benutzer= (EditText) findViewById (R.id.ed_login_benutzer);
		ed_login_passwort= (EditText) findViewById (R.id.ed_login_passwort);
		pb_login = (ProgressBar) findViewById (R.id.pb_login);
		pb_login.setVisibility(View.INVISIBLE);
		pb_login.setProgress(5);
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
