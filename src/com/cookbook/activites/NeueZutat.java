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
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.cookbook.classes.MainSpinnerAdapter;
import com.cookbook.classes.Zutat;
import com.cookbook.scanner.IntentIntegrator;
import com.cookbook.scanner.IntentResult;
import com.example.cookbook.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NeueZutat extends Activity {

	private static final String[] Zutaten = new String[] {"Langustenschwaenze","Pilzeinweichwasser","Spargelabschnitte","Walnuesse","Weissweinessig","Fruehlingsrollenteig","Limettenblaetter","Mayonnaise","Wasser, kalt","Pecorino","Krakauer","Koriander","Bio Zitrone","Hirse","Salsa espanola","Chorizo","Hamburger-Broetchen","Kalbshaxenscheiben","frische Kraeuter","Maismehl","Schinkenkrakauer","Schafskaese","Salatgurke","Cashew-Nuesse","Chilischoten, eingelegt","Glasnudeln","Schweinefilet","Johannisbeersaft, schwarz","Suppengruen","Garnelen","Curry, rot","Kroketten","Ananas, Dose","Sesamoel","Gemuesebruehe","Schinkenspeck","Weisskohl","Putenschnitzel","Schweinelachssteak","Baguette","Kalbsfond","Zucker","Staerkemehl","Champignons","Wiener Wuerstchen","Tagliatelle","Fleur de Sel","Fleischtomaten","Pinienkerne","Senfkoerner","Maiskeimoel","Grana Padano","Schalotten","Tomate","Rotweinessig","Feta","Knoblauchzehen","Creme Fraiche","Chilisauce","Speck, durchwachsen","Forellen","Bacon, in Streifen","Frischkaese","Erbsen","China-Sauce, suess-sauer","Fischfond","Ciabatta","Nudeln","Ravioli, Spinat","Sambal Oelek","Basmati Reis","Kraeuterseitlinge","Miesmuscheln","Limonensaft","Butter","Thymian","Putenmedaillon","Butterschmalz","Kohlrabi","Knoblauch","Lachs, geraeuchert","Paprika, gruen","Rumpsteak","Oel","Pflaumenwein","Oliven, gruen","Rosmarinzweige","Markknochen","Kokosraspel","Pfeffer, Salz","Schweinegulasch","Chinakohl","Lorbeerblatt","Nudeln, bunte","Calamaris","Kartoffeln","Balsamico-Essig","Fleischbruehe","Parma","Schnittlauch","Porree","Eiweiss","Gouda, gerieben","Lauch","Wok-Nudeln","Gemuesemix","Oliven mit Paprika","Rinderhuftsteak","Shrimps","Paprika","Tzatziki","Zuckercouleur","Chilipulver","Spitzpaprika, gelb","Curry","Sojasauce, suess","Mett","Rapsoel","Gewuerzgurken","Pfefferkoerner, rot","Ingwer","Tomatenpaprika, rot","Rindergulasch","Roquefortkaese","Hot Dog Broetchen","Creme Fraiche Kraeuter","franzoesischer Weichkaese","Orange","Schmand","Senf","Bucatini","Apfelkompott","Haehnchenbrustfilet","Gouda, mittelalt","Spaetzle geschabt","Basilikum","Muscheln","Schweinerollbraten","Kasseler Kotelett","Schweineschnitzel","Wasser, heiss","Safran","Mandarinen","Gyros","Ananas","Emmentaler","Tung Koo","Paprikamark","Chinesische Chilisauce","Eier","Champignons, braun","Sojabohnen","Rotwein","Gewuerzgurkenwasser","Peperoncino","Remoulade","Suppengemuese","Macaroni","Ricotta","Rosmarin","Wasser","Blaetterteig","Margarine","Hefe","Scampi","Sonnenblumenkerne","Bandnudeln","Lasagne-Blaetter","Moehren","Muskat","Rinderfilet","Schmelzkaese","Sake","Spargel","Basilikum, getrocknet","Schollenfilet","Zucchini","Hackfleisch","Gabelspaghetti","Ananassaft","Spaetzle","Schinkenwuerfel","Tomatenpueree","Fett","Bacon","Hoernchennudeln","Joghurt","Zitronenscheiben","Wurzeln","Fruehstuecksspeck","Blumenkohl","Sojasprossen, frisch","Kapern","Tortellini","Saucenbinder","Risotto Reis","Bismarckhering","Rucola Salat","Entenbrust","Eiswasser","Sojaoel","Kidneybohnen","Minutensteak","Fruehlingszwiebeln","Pastasauce Napoli","Quark","Lauchzwiebeln","Liebstoeckel","Weisswein","Schweineschmalz","Meeresfruechte","Tomatenmark","Boursin","Sardellenfilets","Putenfilet","Honig","Orangensaft","Parmesan","Selleriesalz","Pfeffer aus der Muehle","Gouda","Kresse","Estragon","Chilischote, getrocknet","Tomaten","Kalbsgulasch","Krabben","Edamer","Schinkenkrustenbraten","Dill","Broccoli","Cannelloni","Dickmilch","Weisse Bohnen","Entrecôte Steak","Milch","Mandeln","saure Sahne","Prosecco","Cocktailsauce","Bruehe, instant","Kartoffeln, mehligkochend","Karotten","Cocktailtomaten","Kokosmilch","Cognac","Rinderschmorbraten","Schmetterlingssteak","Kurkuma","Oliven, schwarz","Austernsauce","Bergkaese","Perlzwiebeln","Olivenoel","Putensteaks","Mexiko Gemuesepfanne","Orangenschale","Zuckererbsenschoten","Pfefferkoerner gruen","Roastbeef Braten","Toast","Spaghetti","Speisestaerke","Pak Choi","Paprika, rot","Eisbergsalat","Krebspaste","Pommes frites","Ingwerpulver","Kalbsschnitzel","Erdnussoel","Roastbeef","Paprikapulver","Chiliflocken","Cappelletti","Rinderhack","Mischpilze, getrocknet","Limonenscheibe","Fuenf-Gewuerz","Aubergine","Peperoni, eingelegt","Schweinesteak","Thymianzweige","Fischsauce","Mandeln, gehackt","Steinpilze, getrocknet","Ketchup","Gorgonzola","Nackensteak","Huhn","Lachs","Cayennepfeffer","Fleischsalat","Hoehlenkaese","Preiselbeergelee","Zitronensaft","Schweinerouladen","Sojasauce","Mailaender Salami","Speck, weiss","Staudensellerie","Raclette Kaese","Rohrzucker, braun","Quittengelee","Schweinenacken","Madeirawein","Koriander, frisch","Majoran","Wacholderbeeren","gekochter Schinken","Hummer-Paste","Salbei","Petersilienwurzel","Tintenfisch","Zitronenschale","Salz","Reis","Venusmuscheln","Cabanossi","Zitronengras, frisch","Pfifferlinge","Pfefferkoerner, schwarz","Chilischote","Mais","Preiselbeeren","Petersilie","Romanesco","Limone","Haehnchenkeulen","Sonnenblumenoel","Hohe Rippe","Zwiebel, weiss","Frischkaese mit Kraeutern","Bio Ei","Muskatnuss","Sahne","Parmesan, frisch","Sellerie","Roestzwiebeln","Ungarische Salami","Tabasco","Paprika, gelb","Petersilie, glatt","Eigelb","Tofu","Ajwar","Scampis","Cherrytomaten","Zwiebeln","Pfeffer, weiss","Bier","Tomatenwuerfel","Bruehe","Kartoffeln, festkochend","Erbsen, frisch","Eiernudeln, Mie","Huehnerbruehe","Farfalle","Karbonade","Schweinebacke geraeuchert","Artischockenherzen","Schweineschulter","Gnocchi","Mehl","Gruene Bohnen","Baby-Moehren","getrocknete Tomaten","Gemuesezwiebel","Zwiebeln, rot","Jaegersauce, instant","Fettuccine","Fenchel","Karpfen","Creme légère","Bambussprossen","Tomaten, Dose","Muschelnudeln","Oregano","Seelachsfilet","Penne","Worcester Sauce","Kraeuter der Provence","Haselnuesse, gehackt","Artischocken","Thunfisch","Spinat","Krautsalat","Fladenbrot","Kotelett","Mozzarella","Paniermehl","Mascarpone"};
	private static final String[] Categories = new String[] {"kg", "g", "ml", "L", "Stück", "Dose"};
	
	private Spinner sp_neue_zutat;
	private AutoCompleteTextView ed_neue_zutat_name;
	private ImageView iv_neue_zutat_icon;	
	private TextView tv_neue_zutat_header;
	private Button bt_neue_zutat_save, bt_neue_zutat_scan;
	private EditText ed_neue_zutat_menge;
	private ProgressBar pb_neue_zutat;	
	
	private Boolean vorhanden = false;
	private Typeface font_bold;
	
	private MainSpinnerAdapter msp_Cat;	
	private ArrayList<Zutat> zutaten;

	private ParseUser cUser;
	private String currentUser;
	private String barcode, nameEAN, ean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_neuezutat);		
		referenceUIElements();
		setFonts() ;
		initParse();
		getUserData();
		onClickListener();
		textChangedListener();		
	}
	
	//Font wird gesetzt
	private void setFonts() {
		font_bold = Typeface.createFromAsset(getAssets(), "fonts/font_bold.ttf");
		ed_neue_zutat_name.setTypeface(font_bold);
		bt_neue_zutat_save.setTypeface(font_bold);
		tv_neue_zutat_header.setTypeface(font_bold);
		bt_neue_zutat_scan.setTypeface(font_bold);
		ed_neue_zutat_menge.setTypeface(font_bold);
		msp_Cat = new MainSpinnerAdapter(this, Categories, font_bold, R.layout.layout_main_spinner_style, R.id.tv_main_spinner_style);
		sp_neue_zutat.setAdapter(msp_Cat);		
	}

	//Speichert die Zutaten in dem ObjektArray "zutaten" ab
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
		        }
		    }
		});
	}
	
	//Klick-Listener zu "Scan" und "Save"
	private void onClickListener() {
		bt_neue_zutat_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vorhanden = false;
				//Überprüft, ob alle Felder ausgefüllt wurden
				if(ed_neue_zutat_name.getText().toString().length()>0&&ed_neue_zutat_menge.getText().toString().length()>0){
					//Überprüft, ob das Feld Menge positiv ist
					if(Double.parseDouble(ed_neue_zutat_menge.getText().toString())>0){
						//Vergleicht die Inputs mit dem ObjektArray
						for(int m = 0; m < zutaten.size(); m++){
		        			if(zutaten.get(m).getName().equals(ed_neue_zutat_name.getText().toString())){
		        				//Zutat ist im Objektarray vorhanden
		        				vorhanden = true;
		        				if(Categories[(Integer) sp_neue_zutat.getSelectedItem()].equals("kg")||Categories[(Integer) sp_neue_zutat.getSelectedItem()].equals("L")){
		        					zutaten.get(m).addMenge(1000*Double.parseDouble(ed_neue_zutat_menge.getText().toString()));
		        				}else{
		        					zutaten.get(m).addMenge(Double.parseDouble(ed_neue_zutat_menge.getText().toString()));
		        				}
		        			}
		        		}
						//Zutat ist im Objektarray nicht vorhanden
						if(!vorhanden){
		        			if(Categories[(Integer) sp_neue_zutat.getSelectedItem()].equals("kg")||Categories[(Integer) sp_neue_zutat.getSelectedItem()].equals("L")){
		        				if(Categories[(Integer) sp_neue_zutat.getSelectedItem()].equals("kg")){
			        				Zutat newZutat = new Zutat (ed_neue_zutat_name.getText().toString(), Double.parseDouble(ed_neue_zutat_menge.getText().toString())*1000, "g");
				        			zutaten.add(newZutat);
		        				}else{
		        					Zutat newZutat = new Zutat (ed_neue_zutat_name.getText().toString(), Double.parseDouble(ed_neue_zutat_menge.getText().toString())*1000, "ml");
				        			zutaten.add(newZutat);
		        				}
	        				}else{
	        					Zutat newZutat = new Zutat (ed_neue_zutat_name.getText().toString(), Double.parseDouble(ed_neue_zutat_menge.getText().toString()), Categories[(Integer) sp_neue_zutat.getSelectedItem()]);
	    	        			zutaten.add(newZutat);
	        				}
		        		}
						//Daten werden auf Parse.com gespeichert
		        		deleteOldDatabase();
					}else{
						//Fehlermeldung, falls Menge negativ ist
						Toast toast = Toast.makeText(NeueZutat.this, "Die Menge darf nicht kleinergleich 0 sein!", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			}
		});	
		
		bt_neue_zutat_scan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addObject();				
			}
		});	
	}
	
	//Löscht alle Daten des Users
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

	//Erstellt die Daten des Users auf Daten des zutaten-Arrays
	private void createNewDatabase(){
		for(int i = 0; i < zutaten.size(); i++){
			ParseObject data = new ParseObject("UserData");
			data.put("Username", currentUser);
			data.put("Masseinheit", zutaten.get(i).getEinheit());
			data.put("Menge", zutaten.get(i).getMenge());
			data.put("Zutat", zutaten.get(i).getName());
			data.saveInBackground();
		}
		newIntent();
	}
	
	//Leitet den User mit Feedback auf Main zurück
	private void newIntent(){
		Toast toast = Toast.makeText(this, ed_neue_zutat_name.getText().toString()+" wurde hinzugefügt!", Toast.LENGTH_SHORT);
		toast.show();
		Intent i = new Intent(NeueZutat.this,Main.class);
		startActivity(i);
	}
	
    public void addObject (){
    	IntentIntegrator integrator = new IntentIntegrator(this);
    	integrator.initiateScan();
    }

    //Vergleicht die Daten des Input-Feldes mit der Datenbank und setzt den Spinner automatisch - Usability
	private void setSpinner() {		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Zutaten");
		query.whereContains("Zutat", ed_neue_zutat_name.getText().toString());
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
	    				sp_neue_zutat.setSelection(0);
					}else if(get.equals("g")){
						sp_neue_zutat.setSelection(1);
					}else if(get.equals("ml")){
						sp_neue_zutat.setSelection(2);
					}else if(get.equals("L")){
						sp_neue_zutat.setSelection(3);
					}else if(get.equals("Stück")){
						sp_neue_zutat.setSelection(4);
					}else if(get.equals("Dose")){
						sp_neue_zutat.setSelection(5);
					}    				
		        }						
			}
		});
	}

	//Sucht auf Grunde des Barcodes im Internets nach Daten
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	  if (scanResult != null) {
	    	    barcode = scanResult.getContents(); 
	    	    if(barcode!=""&&barcode!=null){
	    	    	pb_neue_zutat.setVisibility(View.VISIBLE);
		  	  	    new RequestTask().execute("http://openean.kaufkauf.net/?ean="+barcode+"&cmd=query&queryid=290247937");
	    	    }
    	  }
     }
	
	private void referenceUIElements() {		
		sp_neue_zutat = (Spinner) findViewById(R.id.sp_neue_zutat);	
		ed_neue_zutat_name = (AutoCompleteTextView) findViewById(R.id.ed_neue_zutat_name);
		iv_neue_zutat_icon = (ImageView) findViewById (R.id.iv_neue_zutat_icon);		
		tv_neue_zutat_header = (TextView) findViewById (R.id.tv_neue_zutat_header);
		bt_neue_zutat_save = (Button) findViewById (R.id.bt_neue_zutat_save);
		ed_neue_zutat_menge = (EditText) findViewById (R.id.ed_neue_zutat_menge);
		bt_neue_zutat_scan = (Button) findViewById (R.id.bt_neue_zutat_scan);
		pb_neue_zutat = (ProgressBar) findViewById (R.id.pb_neue_zutat);		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, Zutaten);	
		ed_neue_zutat_name.setAdapter(adapter);		
		pb_neue_zutat.setVisibility(View.INVISIBLE);
		zutaten = new ArrayList<Zutat>();		
	}
	
	//Wenn das EditText-Feld "ed_neue_zutat_name" keine Fokus hat, wird das Bild und der Spinner gesetzt.
	private void textChangedListener(){		
		ed_neue_zutat_name.setOnFocusChangeListener(new OnFocusChangeListener() {  
			public void onFocusChange(View v, boolean hasFocus) {
	            if(!hasFocus&&ed_neue_zutat_name.getText().toString().length()!=0){
	            	setSpinner();
	            	setPicture();
	            }
	        }
	    });
	}
	
	//Download des Pictures auf Daten des EditText-Feldes "ed_neue_zutat_name"
	@SuppressWarnings("deprecation")
	private void setPicture(){
		new DownloadImage().execute("http://www.marions-kochbuch.de/index-bilder/"+URLEncoder.encode(ed_neue_zutat_name.getText().toString().toLowerCase())+".jpg");
	}	
	
	//Initialisiert die Verbindung zu Parse
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
	
	//Barcode-Scanner-Handler
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
        	pb_neue_zutat.setProgress(progress[0]);
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
                		ed_neue_zutat_name.setText(nameEAN);
                    	
                	}
                	if(line.contains("detailname=")&&nameEAN.length()<1){           		
                		nameEAN = line.substring(11);
                		nameEAN = editName(nameEAN);
                		ed_neue_zutat_name.setText(nameEAN );
                    	                 
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
            pb_neue_zutat.setVisibility(View.GONE);
        }
    }
	
	//Bearbeitet den String
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
    
    //Setzt das Image
    private void setImage(Drawable drawable){
    	if(drawable!=null){
    		iv_neue_zutat_icon.setImageDrawable(drawable);
    	}
    }

    //Download des Images
	public class DownloadImage extends AsyncTask<String, Integer, Drawable> {
	
	    @Override
	    protected Drawable doInBackground(String... arg0) {
	        return downloadImage(arg0[0]);
	    }
	
	    protected void onPostExecute(Drawable image){
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
