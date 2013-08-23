package com.cookbook.activites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {

	TextView tv_register_benutzer, tv_register_passwort;
	Button bt_register_register;
	EditText ed_register_benutzer, ed_register_passwort;
	private Typeface font, font_bold;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register);	
		
		initParse();
		referenceUIElements();	
		setFonts();
		
		currentUser();
		
		onClickListener();
		
	}
	
	private void setFonts() {
		font_bold = Typeface.createFromAsset(getAssets(), "fonts/font_bold.ttf");
		font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
		tv_register_benutzer.setTypeface(font_bold);
		tv_register_passwort.setTypeface(font_bold);
		bt_register_register.setTypeface(font_bold);
		ed_register_benutzer.setTypeface(font);
		ed_register_passwort.setTypeface(font);
	}

	private void currentUser() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
		 
		} else {
			Intent i = new Intent(Register.this,Main.class);
			startActivity(i);
		}
		
	}

	private void onClickListener() {
		bt_register_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ed_register_benutzer.getText().toString().length()!=0&&ed_register_passwort.getText().toString().length()!=0){
					registerParse();
				}
			}
		});
	}

	private void registerParse() {
		ParseUser user = new ParseUser();
		user.setUsername(ed_register_benutzer.getText().toString());
		user.setPassword(ed_register_passwort.getText().toString());
		 
		user.signUpInBackground(new SignUpCallback() {
		  public void done(ParseException e) {
		    if (e == null) {
		    	Context context = getApplicationContext();
				CharSequence text = "Registrierung erfolgreich";
				int duration = Toast.LENGTH_LONG;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
		    	Intent i = new Intent(Register.this,Main.class);
				startActivity(i);
		    } else {
		    	Context context = getApplicationContext();
				CharSequence text = "Registrierung fehlgeschlagen";
				int duration = Toast.LENGTH_LONG;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
		    }
		  }
		});
	}

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
