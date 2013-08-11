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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

public class Login extends Activity {

	TextView tv_login_benutzer, tv_login_passwort;
	Button bt_login_login, bt_login_register;
	EditText ed_login_benutzer, ed_login_passwort;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);	
		
		initParse();
		referenceUIElements();	
		
		currentUser();
		
		onClickListener();
		
	}
	
	private void currentUser() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
		 
		} else {
			Intent i = new Intent(Login.this,Main.class);
			startActivity(i);
		}
		
	}
	private void onClickListener() {
		bt_login_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loginParse();
			}
		});
		bt_login_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Login.this,Register.class);
				startActivity(i);
			}
		});
	}

	private void loginParse() {
		ParseUser.logInInBackground(ed_login_benutzer.getText().toString(), ed_login_passwort.getText().toString(), new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			    	Intent i = new Intent(Login.this,Main.class);
					startActivity(i);
			    } else {
			    	Context context = getApplicationContext();
					CharSequence text = "Login fehlgeschlagen!";
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
		tv_login_benutzer = (TextView) findViewById (R.id.tv_login_benutzer);
		tv_login_passwort= (TextView) findViewById (R.id.tv_login_passwort);
		bt_login_login= (Button) findViewById (R.id.bt_login_login);
		bt_login_register= (Button) findViewById (R.id.bt_login_register);
		ed_login_benutzer= (EditText) findViewById (R.id.ed_login_benutzer);
		ed_login_passwort= (EditText) findViewById (R.id.ed_login_passwort);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
