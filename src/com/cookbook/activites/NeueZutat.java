package com.cookbook.activites;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.cookbook.classes.Zutat;
import com.cookbook.scanner.IntentIntegrator;
import com.cookbook.scanner.IntentResult;
import com.example.cookbook.R;
import com.example.cookbook.R.layout;
import com.example.cookbook.R.menu;
import com.parse.FindCallback;
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
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTabHost;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

public class NeueZutat extends Activity {

	private static final String[] Zutaten = new String[] {"Langustenschwaenze","Pilzeinweichwasser","Spargelabschnitte","Walnuesse","Weissweinessig","Fruehlingsrollenteig","Limettenblaetter","Mayonnaise","Wasser, kalt","Pecorino","Krakauer","Koriander","Bio Zitrone","Hirse","Salsa espanola","Chorizo","Hamburger-Broetchen","Kalbshaxenscheiben","frische Kraeuter","Maismehl","Schinkenkrakauer","Schafskaese","Salatgurke","Cashew-Nuesse","Chilischoten, eingelegt","Glasnudeln","Schweinefilet","Johannisbeersaft, schwarz","Suppengruen","Garnelen","Curry, rot","Kroketten","Ananas, Dose","Sesamoel","Gemuesebruehe","Schinkenspeck","Weisskohl","Putenschnitzel","Schweinelachssteak","Baguette","Kalbsfond","Zucker","Staerkemehl","Champignons","Wiener Wuerstchen","Tagliatelle","Fleur de Sel","Fleischtomaten","Pinienkerne","Senfkoerner","Maiskeimoel","Grana Padano","Schalotten","Tomate","Rotweinessig","Feta","Knoblauchzehen","Creme Fraiche","Chilisauce","Speck, durchwachsen","Forellen","Bacon, in Streifen","Frischkaese","Erbsen","China-Sauce, suess-sauer","Fischfond","Ciabatta","Nudeln","Ravioli, Spinat","Sambal Oelek","Basmati Reis","Kraeuterseitlinge","Miesmuscheln","Limonensaft","Butter","Thymian","Putenmedaillon","Butterschmalz","Kohlrabi","Knoblauch","Lachs, geraeuchert","Paprika, gruen","Rumpsteak","Oel","Pflaumenwein","Oliven, gruen","Rosmarinzweige","Markknochen","Kokosraspel","Pfeffer, Salz","Schweinegulasch","Chinakohl","Lorbeerblatt","Nudeln, bunte","Calamaris","Kartoffeln","Balsamico-Essig","Fleischbruehe","Parma","Schnittlauch","Porree","Eiweiss","Gouda, gerieben","Lauch","Wok-Nudeln","Gemuesemix","Oliven mit Paprika","Rinderhuftsteak","Shrimps","Paprika","Tzatziki","Zuckercouleur","Chilipulver","Spitzpaprika, gelb","Curry","Sojasauce, suess","Mett","Rapsoel","Gewuerzgurken","Pfefferkoerner, rot","Ingwer","Tomatenpaprika, rot","Rindergulasch","Roquefortkaese","Hot Dog Broetchen","Creme Fraiche Kraeuter","franzoesischer Weichkaese","Orange","Schmand","Senf","Bucatini","Apfelkompott","Haehnchenbrustfilet","Gouda, mittelalt","Spaetzle geschabt","Basilikum","Muscheln","Schweinerollbraten","Kasseler Kotelett","Schweineschnitzel","Wasser, heiss","Safran","Mandarinen","Gyros","Ananas","Emmentaler","Tung Koo","Paprikamark","Chinesische Chilisauce","Eier","Champignons, braun","Sojabohnen","Rotwein","Gewuerzgurkenwasser","Peperoncino","Remoulade","Suppengemuese","Macaroni","Ricotta","Rosmarin","Wasser","Blaetterteig","Margarine","Hefe","Scampi","Sonnenblumenkerne","Bandnudeln","Lasagne-Blaetter","Moehren","Muskat","Rinderfilet","Schmelzkaese","Sake","Spargel","Basilikum, getrocknet","Schollenfilet","Zucchini","Hackfleisch","Gabelspaghetti","Ananassaft","Spaetzle","Schinkenwuerfel","Tomatenpueree","Fett","Bacon","Hoernchennudeln","Joghurt","Zitronenscheiben","Wurzeln","Fruehstuecksspeck","Blumenkohl","Sojasprossen, frisch","Kapern","Tortellini","Saucenbinder","Risotto Reis","Bismarckhering","Rucola Salat","Entenbrust","Eiswasser","Sojaoel","Kidneybohnen","Minutensteak","Fruehlingszwiebeln","Pastasauce Napoli","Quark","Lauchzwiebeln","Liebstoeckel","Weisswein","Schweineschmalz","Meeresfruechte","Tomatenmark","Boursin","Sardellenfilets","Putenfilet","Honig","Orangensaft","Parmesan","Selleriesalz","Pfeffer aus der Muehle","Gouda","Kresse","Estragon","Chilischote, getrocknet","Tomaten","Kalbsgulasch","Krabben","Edamer","Schinkenkrustenbraten","Dill","Broccoli","Cannelloni","Dickmilch","Weisse Bohnen","Entrecôte Steak","Milch","Mandeln","saure Sahne","Prosecco","Cocktailsauce","Bruehe, instant","Kartoffeln, mehligkochend","Karotten","Cocktailtomaten","Kokosmilch","Cognac","Rinderschmorbraten","Schmetterlingssteak","Kurkuma","Oliven, schwarz","Austernsauce","Bergkaese","Perlzwiebeln","Olivenoel","Putensteaks","Mexiko Gemuesepfanne","Orangenschale","Zuckererbsenschoten","Pfefferkoerner gruen","Roastbeef Braten","Toast","Spaghetti","Speisestaerke","Pak Choi","Paprika, rot","Eisbergsalat","Krebspaste","Pommes frites","Ingwerpulver","Kalbsschnitzel","Erdnussoel","Roastbeef","Paprikapulver","Chiliflocken","Cappelletti","Rinderhack","Mischpilze, getrocknet","Limonenscheibe","Fuenf-Gewuerz","Aubergine","Peperoni, eingelegt","Schweinesteak","Thymianzweige","Fischsauce","Mandeln, gehackt","Steinpilze, getrocknet","Ketchup","Gorgonzola","Nackensteak","Huhn","Lachs","Cayennepfeffer","Fleischsalat","Hoehlenkaese","Preiselbeergelee","Zitronensaft","Schweinerouladen","Sojasauce","Mailaender Salami","Speck, weiss","Staudensellerie","Raclette Kaese","Rohrzucker, braun","Quittengelee","Schweinenacken","Madeirawein","Koriander, frisch","Majoran","Wacholderbeeren","gekochter Schinken","Hummer-Paste","Salbei","Petersilienwurzel","Tintenfisch","Zitronenschale","Salz","Reis","Venusmuscheln","Cabanossi","Zitronengras, frisch","Pfifferlinge","Pfefferkoerner, schwarz","Chilischote","Mais","Preiselbeeren","Petersilie","Romanesco","Limone","Haehnchenkeulen","Sonnenblumenoel","Hohe Rippe","Zwiebel, weiss","Frischkaese mit Kraeutern","Bio Ei","Muskatnuss","Sahne","Parmesan, frisch","Sellerie","Roestzwiebeln","Ungarische Salami","Tabasco","Paprika, gelb","Petersilie, glatt","Eigelb","Tofu","Ajwar","Scampis","Cherrytomaten","Zwiebeln","Pfeffer, weiss","Bier","Tomatenwuerfel","Bruehe","Kartoffeln, festkochend","Erbsen, frisch","Eiernudeln, Mie","Huehnerbruehe","Farfalle","Karbonade","Schweinebacke geraeuchert","Artischockenherzen","Schweineschulter","Gnocchi","Mehl","Gruene Bohnen","Baby-Moehren","getrocknete Tomaten","Gemuesezwiebel","Zwiebeln, rot","Jaegersauce, instant","Fettuccine","Fenchel","Karpfen","Creme légère","Bambussprossen","Tomaten, Dose","Muschelnudeln","Oregano","Seelachsfilet","Penne","Worcester Sauce","Kraeuter der Provence","Haselnuesse, gehackt","Artischocken","Thunfisch","Spinat","Krautsalat","Fladenbrot","Kotelett","Mozzarella","Paniermehl","Mascarpone"};
	private AutoCompleteTextView name;
	private EditText einheit;
	private Spinner spinner;
	private Button insert,scan;
	private ProgressBar pb;
	private String barcode, nameEAN, hersteller, ean;
	private Boolean vorhanden = false;
	ImageView iv_zutat_picture;
	
	private ArrayList<Zutat> zutaten;

	ParseUser cUser;
	String currentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_neuezutat);		
		referenceUIElements();
		initParse();
		getUserData();
		onClickListener();
		textChangedListener();		
	}

	private void getUserData(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
		query.whereEqualTo("Username", currentUser);
		query.orderByAscending("Zutat");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		        	for(int i = 0; i < scoreList.size(); i++){					        		
		        		Zutat newZutat = new Zutat (scoreList.get(i).getString("Zutat"),scoreList.get(i).getDouble("Menge"),scoreList.get(i).getString("Masseinheit"));
		        		zutaten.add(newZutat);
		        	}
		        } else {

		        }
		    }
		});
	}
	
	private void onClickListener() {
		insert.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vorhanden = false;
				if(name.getText().toString().length()>0&&einheit.getText().toString().length()>0){
					for(int m = 0; m < zutaten.size(); m++){
	        			if(zutaten.get(m).getName().equals(name.getText().toString())&&zutaten.get(m).getEinheit().equals(spinner.getSelectedItem().toString())){
	        				vorhanden = true;
	        				zutaten.get(m).addMenge( Double.parseDouble(einheit.getText().toString()));
	        			}
	        		}
	        		if(!vorhanden){
	        			Zutat newZutat = new Zutat (name.getText().toString(), Double.parseDouble(einheit.getText().toString()), spinner.getSelectedItem().toString());
	        			zutaten.add(newZutat);
	        		}
	        		deleteOldDatabase();
				}
			}
		});	
		
		scan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addObject();				
			}
		});	
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
		Intent i = new Intent(NeueZutat.this,Main.class);
		startActivity(i);
	}
	
    public void addObject (){
    	IntentIntegrator integrator = new IntentIntegrator(this);
    	integrator.initiateScan();
    }

	private void getEinheit() {		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Zutaten");
		query.whereContains("Zutat", name.getText().toString());
		query.setLimit(1000);
		query.findInBackground(new FindCallback<ParseObject>() {        		   
			@Override
			public void done(List<ParseObject> arg0, ParseException e) {
				if (arg0.size()!=0) {	
	    			String get = "";
					for(int i = 0; i < arg0.size(); i++){
						if(arg0.get(i).getString("Masseinheit").equals("kg")||arg0.get(i).getString("Masseinheit").equals("g")||arg0.get(i).getString("Masseinheit").equals("ml")||arg0.get(i).getString("Masseinheit").equals("L")||arg0.get(i).getString("Masseinheit").equals("Stück")||arg0.get(i).getString("Masseinheit").equals("Dose")){
							get = arg0.get(i).getString("Masseinheit");
						}
					} 					
	    			if(get.equals("kg")){
						spinner.setSelection(0);
					}else if(get.equals("g")){
						spinner.setSelection(1);
					}else if(get.equals("ml")){
						spinner.setSelection(2);
					}else if(get.equals("L")){
						spinner.setSelection(3);
					}else if(get.equals("Stück")){
						spinner.setSelection(4);
					}else if(get.equals("Dose")){
						spinner.setSelection(5);
					}    				
		        }						
			}
		});
	}

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	  if (scanResult != null) {
	    	    barcode = scanResult.getContents(); 
	    	    if(barcode!=""&&barcode!=null){
		  	  	    pb.setVisibility(View.VISIBLE);
		  	  	    new RequestTask().execute("http://openean.kaufkauf.net/?ean="+barcode+"&cmd=query&queryid=290247937");
	    	    }
    	  }
      }
	
	private void referenceUIElements() {
		spinner = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, Zutaten);
		name = (AutoCompleteTextView) findViewById(R.id.zutat);
		iv_zutat_picture = (ImageView) findViewById (R.id.iv_zutat_picture);
		name.setAdapter(adapter);
		insert = (Button) findViewById (R.id.zutatHinzufuegenButton);
		einheit = (EditText) findViewById (R.id.einheit);
		scan = (Button) findViewById (R.id.scanButton);
		//pb = (ProgressBar) findViewById (R.id.progressBar1);
		//pb.setVisibility(View.INVISIBLE);
		iv_zutat_picture.setVisibility(View.INVISIBLE);
		zutaten = new ArrayList<Zutat>();
		
	}
	
	private void textChangedListener(){		
		name.setOnFocusChangeListener(new OnFocusChangeListener() {          

	        public void onFocusChange(View v, boolean hasFocus) {
	            if(!hasFocus&&name.getText().toString().length()!=0){
	            	getEinheit();
	            	setPicture();
	            }
	        }
	    });
	}
	
	@SuppressWarnings("deprecation")
	private void setPicture(){
		new DownloadImage().execute("http://www.marions-kochbuch.de/index-bilder/"+URLEncoder.encode(name.getText().toString().toLowerCase())+".jpg");
	}	
	
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
		return true;
	}
	
	class RequestTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
            	Log.e("Test","Fehler");
            } catch (IOException e) {
            	Log.e("Test","Fehler");
            }
            return responseString;
        }
        
        protected void onProgressUpdate(Integer... progress){
			pb.setProgress(progress[0]);
		}

        @Override
        protected void onPostExecute(String result) {
        	InputStream is = new ByteArrayInputStream(result.getBytes());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            int i = 1;
            
            ean = barcode;
            
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {               	
                	
                	if(line.contains("error=1")){ 
                		Toast toast = Toast.makeText(getApplicationContext(), "Artikel wurde nicht erkannt - bitte manuell eingeben", Toast.LENGTH_LONG);
        				toast.show();
                		break;
                    }
                	if(line.contains("name=")&&line.indexOf("detailname=")==-1&&i<10){           		
                		nameEAN = line.substring(5);
                		nameEAN = editName(nameEAN);
                    	name.setText(nameEAN);
                    	
                	}
                	if(line.contains("detailname=")&&nameEAN.length()<1){           		
                		nameEAN = line.substring(11);
                		nameEAN = editName(nameEAN);
                    	name.setText(nameEAN );
                    	                 
                	}
                	i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            pb.setVisibility(View.GONE);
        }
    }
	
    private String editName(String result){
    	if(result.indexOf(',')!=-1){
    		result = result.substring(0, result.indexOf(','));
    	}
    	if(result.indexOf('.')!=-1){
    		result = result.substring(0, result.indexOf('.'));
    	}
    	if(result.indexOf(':')!=-1){
    		result = result.substring(0, result.indexOf(':'));
    	}
    	if(result.indexOf(';')!=-1){
    		result = result.substring(0, result.indexOf(';'));
    	}
    	if(result.indexOf('-')!=-1){
    		result = result.substring(0, result.indexOf('-'));
    	}    	
    	return result;
    }
    
    private void setImage(Drawable drawable){
    	iv_zutat_picture.setImageDrawable(drawable);
    }

	public class DownloadImage extends AsyncTask<String, Integer, Drawable> {
	
	    @Override
	    protected Drawable doInBackground(String... arg0) {
	        return downloadImage(arg0[0]);
	    }
	
	    protected void onPostExecute(Drawable image){
	    	iv_zutat_picture.setVisibility(View.VISIBLE);
	        setImage(image);
	    }
	
	    private Drawable downloadImage(String _url){
	        URL url;        
	        BufferedOutputStream out;
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
