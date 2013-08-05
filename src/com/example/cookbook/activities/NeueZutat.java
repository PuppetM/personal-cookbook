package com.example.cookbook.activities;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import rezepte_app_scanner.IntentIntegrator;
import rezepte_app_scanner.IntentResult;

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
import android.support.v4.app.FragmentTabHost;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

public class NeueZutat extends Activity {

	private static final String[] Zutaten = new String[] {"Schokoladeneis", "Graupen", "Bratwurst", "Tete de Moine", "Piment", "Sonnenblumenoel", "Fruechtemix", "Pfeffer", "Schweinefilet", "Meerrettich", "Apricot Brandy", "Baileys", "Weissweinessig", "Minutensteaks", "Honig", "Aperol", "Bruehwuerfel", "Schokoladen Herzpralinen", "Hamburger-Broetchen", "Pfirsiche", "Brot Chips", "Gin", "Landschinken", "Peperoncino", "Rigatoni", "Moehren", "Limettenblaetter", "Tonic", "Pfeffersalami", "Teriyaki-Sauce", "Putenfilet", "Garnelen", "Frischkaese mit Kraeutern", "Ei", "Limonensaft", "Rapsoel", "Mandeln", "Peperoni", "Gouda", "Balsamico", "Limonenschale", "Pizzakaese", "Martini bianco", "Mascarpone", "Salz", "Sake", "TUC Cracker", "Tortenguss", "Sago", "Lollo bianco", "Pottasche", "Tequila", "Sahnesteif", "Ciabatta", "Forellenfilets", "Hirschmedaillons", "Feldsalat", "Lauchzwiebeln", "Fett", "Brombeeren", "Feta", "Schmand", "Noilly Prat", "Schinkenwurst", "Wassermelone", "Creme légère", "Roggenschrot", "Pernod", "Duftreis", "Vanilleeis", "Oregano", "Campari", "Krautsalat", "Vanilleschote", "Prosciutto cotto", "Schinkenschwarte", "Gefluegel Wiener", "Fischsauce", "Knack und Back Croissants", "Wuerfelzucker", "Brotpudding", "Suppengruen", "Roastbeef Braten", "Hot Pepper Sauce", "Toasties", "Lammschulter", "Haehnchenkeulen", "Drillinge Kartoffeln", "Zwiebel", "Aprikosen", "Beifuss", "Back-Oblaten", "Creme Fraiche", "Scholle", "Zitronenaroma", "Pfirsich", "Buchweizenmehl", "Apfelmus", "Erdnussoel", "Salsa Texicana", "Milch", "Lavash Brot", "Aioli Sauce", "Salatcreme", "Birnen", "Petersilie", "Pommes souffles", "Muskatnuss", "Rindfleisch in Dosen", "Passionsfrucht", "Lyoner Fleischwurst", "Buttermilch", "Kohlrabi", "Scampi", "Risotto Reis", "Lammkarree", "Walnussoel", "Kirschsaft", "Wok-Nudeln", "Ajwar", "Coca Cola", "Blue Curacao", "Vanillesirup", "schneller Rumtopf", "Lebkuchengewuerz", "Parma", "Wolfsbarsch", "Creme Pudding", "Eisbergsalat", "weisse Brause", "Haehnchen Kebab", "Cherrytomaten", "Knoblauchsauce", "Thunfisch", "Baguette", "Tanqueray", "Kochschinken", "Mangold", "Huhn", "Maronen", "Mousse au Chocolat", "Putenoberkeule", "Hefe", "Tropische Fruchtmischung", "Kokos-Milch", "Grenadinesirup", "Pecorino", "Karotten", "brauner Zucker", "Mango Chutney", "Beerenmischung", "Pfirsichhaelften", "Palmenherzen", "Holunderblueten", "Tomatendip", "Dinkelvollkornmehl", "Weisswuerste", "Pfirsichsaft", "Krakauer", "Oel", "Pflaumenkompott", "Eiswasser", "Maiskoelbchen", "Pastinaken", "Blaubeeren", "Jakobsmuscheln", "Nelkenpulver", "Rostbratwuerstchen", "Grappa", "Mairuebchen", "Apfelwein", "Kiwi gold", "Johannisbeer-Gelee", "Pinienkerne", "Cashew-Nuesse", "Nordseekrabbensalat", "Perlzwiebeln", "Huehnerbruehe", "Gyros", "Schoko-Troepfchen", "Ricotta", "Leinsamen", "Bauchspeck", "Captain Morgan Rum", "Kraeuterseitlinge", "Lummerbraten", "Spargelabschnitte", "Hagelzucker", "Sweet und Sour Sauce", "Kuemmel", "Kartoffelchips", "Babybananen", "Limone", "Karotte", "Schweinerouladen", "Hirschbraten", "Rotweinessig", "Joghurt-Sauce", "Weizenkleie", "Schokoladensauce", "Raclette Kaese", "Pastasauce Napoli", "Bohnenkraut", "Rotelle", "Meterbrot", "Chinakohl", "Karamelsirup", "Ananas Scheiben", "Kuerbis", "Entrecôte Steak", "Eiswuerfel", "Rindergulasch", "Romanesco", "Jaroma Kohl", "Lollo bionda", "Langustenschwaenze", "Weissbrot", "Kuvertuere", "Lebensmittelfarbe", "Schupfnudeln", "Croissant", "Baguettebroetchen", "Leberwurst", "Jaffa Cake", "Apfelkraut", "Selleriesalz", "Basilikum", "Schalotten", "Rinderhuftsteak", "Ananasstueck", "Cheddar", "Stockfisch", "Salatherzen", "Walnusseis", "Chorizo", "Maracujalikoer", "Kaisergemuese", "Karottensaft", "Fruehlingszwiebeln", "Chiliflocken", "getrocknete Tomaten", "Rollmoepse", "Espresso Pads", "Kristallzucker", "Gewuerzgurken", "Brotbackmischung", "Orangenschalen-Aroma", "Chianti", "Feigen", "Dashi", "Gaensekeule", "Kassler Lachsfleisch", "Lammlachs", "Malzextrakt", "Thunfischsteak", "Kapuzinerkresse", "Bourbon", "Aubergine", "Tabasco", "Haferflocken", "Fleischsalat", "Fleischwurst", "Calvados", "Jaegermeister", "Himbeergeist", "Tomatensaft", "Surimi-Sticks", "Fleur de Sel", "Backschinken", "Senfkoerner", "Provolone", "Thousand Islands Dressing", "Blaetterteigpasteten", "Pflaumen", "Limburger", "Mineralwasser", "Asti Spumante", "Champignon Scheiben", "Kalbsgulasch", "Traubenzucker", "Sonnenblumenkerne", "Schweinefiletkoepfe", "Salsa espanola", "Weinbrand", "Blumenkohl", "Macadamia Nuesse", "Schweinebraten und Sosse", "Orangenmarmelade", "Sauerkraut", "Ketchup", "Netzmelone", "Muerbeteigtoertchen", "Muscheln", "Erdnuesse", "Cornichons", "Butterkekse", "Dickmilch", "Safran", "Wurzeln", "Zwieback", "Puderzucker", "Matjesfilet", "gekochter Schinken", "Prinzessbohnen", "Schmelzkaese", "Mandelblaetter", "Kassler", "Bananenchips", "Kuerbiskerne", "Korinthen", "Apfel", "Knoblauch", "Portwein", "Fischstaebchen", "Bauchfleisch", "Tahina Sesampaste", "Schwarzbrotscheiben", "Honigmelone", "Fleischtomaten", "Cabanossi", "Cream of Coconut", "Orangenlikoer", "Brombeermarmelade", "Gelfix super", "Kokosmilch", "Hirschhornsalz", "Currysauce", "Fruchtcocktail", "Kieler Sprotten", "Kerbel", "Shrimps", "Flomen", "Roesti Ecken", "Estragonsenf", "Erdbeerlikoer", "Batida de Coco", "Fraenkische Bratwurst", "Butterkaese", "Vanillesaucenpulver", "Grapefruit", "Heringssalat", "Kalbsbratwurst", "Artischockenherzen", "Chilipulver", "Lime Juice", "Zimtstange", "Saucenbinder", "Endiviensalat", "Guinness", "Fruehstuecksspeck", "Cardamom", "Whisky", "Orangenschale", "Fruehlingsrollenteig", "Fenchel", "Bier", "Zwiebelsuppe instant", "Himbeermarmelade", "Tee", "Hummer", "Schokolinsen", "Bitter Orange", "Pain Parisienne", "Champignons Minis", "Knoblauchzehen", "Grana Padano", "Schweineschulter", "Muskat", "Jenever", "Tintenfischringe", "Hering", "Wachteln", "Sojasprossen", "Kassler Nacken", "Zartbitterschokolade", "Kartoffelknoedel", "Salatgurke", "Trockenhefe", "Rum", "Maracujasirup", "Muerbeteigboden", "Berliner Weisse", "Rote Beete Saft", "Sambuca", "Tomatenpueree", "Ente", "Schafskaese", "Pommes frites", "Radieschensprossen", "Tortilla", "Zitronengras", "Margarine", "Pfefferkoerner gruen", "Piri Piri", "Blaetterteig", "Kabeljau", "Beefsteakhack", "Ahornsirup", "Pfefferminzlikoer", "Schaelrippchen", "Lychees", "Blutorangen", "Pangasius-Filet", "Mandelstifte", "Spirelli", "Rosenwasser", "Oliven mit Paprika", "Weizen", "Maiskeimoel", "French Dressing", "Spianata", "Metaxa", "Seelachsfilet", "Porterhouse Steak", "Zitronensaeure", "Nuss-Nougat-Creme", "Cranberries", "Natron", "Waldorfsalat", "Italienische Kraeuter", "Weinblaetter", "Forellenkaviar", "Baerlauchbutter", "Schokoladenbiskuit", "Pfefferminz Schokolade", "saure Sahne", "Erdbeersaft", "Kritharaki-Nudeln", "Schinkenkrustenbraten", "Lorbeerblatt", "Bourbon-Vanille-Zucker", "Trulli", "Walnusshaelften", "Kaiserkirschen", "Gabelspaghetti", "Curry Ketchup scharf", "Rinderschmorbraten", "Speisestaerke", "Senfgurken", "Nackenkotelett", "Muensterkaese", "Thunfisch - Sushi Qualitaet", "Schollenfilet", "Burgunderschinken", "Zucker", "Silberzwiebeln", "Griechischer Joghurt", "Blockschokolade", "Rosmarinzweige", "Rinderfond", "Fleischbruehe", "Lager Bier", "Harissa", "Kluntjes  Kandiszucker", "Spekulatius", "Baby-Moehren", "Waldmeister", "Frankfurter Wuerstchen", "Pitahaya", "Karpfen", "Mailaender Salami", "Fusilli", "Sambal Oelek", "Maultaschen", "Sprossen-Mix", "Jasmintee", "Straussensteak", "Hammelfleisch", "Minutensteak", "Rotbarschfilet", "Datteln", "Ingwer", "Bananen", "Schweinesteak", "Sodawasser", "Ginger Ale", "Orangenscheibe", "Gurkensalat", "Ingwerpulver", "Fischfond", "Hoernchennudeln", "Kotelett", "Kirschwasser", "Holunderbluetensirup", "Bambussprossen", "Rotwein", "Pfirsichlikoer", "Fettuccine", "Majoran", "Karottensalat", "Vanille-Joghurt", "Dicke Rippe", "Pfeffer aus der Muehle", "Suppengemuese", "Walnuesse", "Kohlwurst", "Staudensellerie", "Weisse Bohnen", "Zitronensaft", "Creme de Cacao braun", "Paranuesse", "Toast", "Pizzateig Grundrezept", "Mozzarella", "Rouladen", "Markknochen", "Emmentaler", "Kapern", "Bergkaese", "Schokopuddingpulver", "Rote Beete", "Zucchini", "Fruechte-Tee", "Mokkabohnen", "Mais", "Weizenvollkornmehl", "Caesar Dressing", "Erdbeerlimes", "Himbeersirup", "Steinpilze", "Paprika", "Erdbeerjoghurt", "Falafel", "Clementinen", "Martini rosso", "Altbier", "Wrap Tortillas", "Muschelnudeln", "Biskuitboden", "Mars", "Kopfsalat", "Kuchenreste", "Pflaumenwein", "Focaccia", "Vanillepuddingpulver", "Lollo rosso", "Limonenscheibe", "Koernerbroetchen", "Wirsing", "Bismarckhering", "Sojabohnen", "Baerlauch", "Haehnchenbrustfilet", "Scheibletten", "Pul Biber", "Koriander", "Gaenseschmalz", "Marzipan Rohmasse", "Spinat", "Schokolade", "Pak Choi", "Papaya", "Sherry", "Senf", "Biskuit", "Lammfond", "Sekt", "Leber", "Suppenfleisch", "Rosmarin", "Mini Fladenbrot", "Gefluegelleber", "T-Bone Steak", "Erdnussbutter", "Heidelbeeren", "Bitter Lemon", "Haehnchen Unterschenkel", "Nussoel", "Nudeln", "Bresaola", "Orange-Bitter", "Romana Salat", "Physalis", "Zimt", "Wildhasenteile", "Preiselbeeren", "Scampis", "Gefluegelleberwurst", "Sojasauce", "Huettenkaese", "Sahnemeerrettich", "Gruene Bohnen", "Gelatine", "Roastbeef", "Mayonnaise", "Chilischoten", "Cappelletti", "Cantaloupe Melone", "Himbeer Rhabarber", "Paprikapulver", "Aioli", "Leberkaese", "Kraeuterschmelzkaese", "Weizenmehl", "Kirschkonfituere", "Edamer", "Slibowitz", "Serrano Schinken", "Wasser", "Marsala Wein", "Kruemelkandis", "Gravlax-Sauce", "Reisnudeln", "Rucola Salat", "Gaensebrust", "Hirse", "Tatar", "Zwiebelpulver", "Stippgruetze", "Kuerbiskernoel", "Spitzkohl", "Mettwurst", "Eisbein", "Sour Cream", "Schweinemedaillons", "Nackensteak", "Mett", "Neue Kartoffeln", "Bischofsmuetze Kuerbis", "Rotkohl", "Gelierzucker", "Mehl", "Bierbroetchen", "Spaetzle", "Limoncello", "Einlegegurken", "Traubensaft", "Kalbshaxenscheiben", "Bananenblatt", "Wildreis", "Bresso", "Eichblattsalat", "Rohrzucker", "Spitzpaprika", "Cidre", "Laugenbrezel", "Eiweiss", "Sahne", "Butter-Vanille-Aroma", "Fladenbrot", "Schmorgurken", "Sauerrahm Dressing", "Kirschpaprika", "Schichtkaese", "Tzatziki", "Tafelspitz", "Curry", "Zuckerstreusel", "Southern Comfort", "Gluehwein", "Sauce Hollandaise", "Aquavit", "Obstessig", "Kuchenglasur", "Kraeuter der Provence", "Remoulade", "Maracujasaft", "Entenbrust", "Sellerie", "Schokoladentaefelchen", "Zitrone", "Putensteaks", "Rosa Pfefferkoerner", "Backobst", "Zitronenessig", "Weisswein", "Salatdressing", "Kirschen", "Schweinebacke geraeuchert", "Pinkel", "Tuerkischer Sahnejoghurt", "Kresse", "Mini Tortenboden", "BBQ Sauce", "Kabeljaufilet", "Bratenfond", "Mandarinen", "Himbeeressig", "Lachsschinken", "Milchreis", "Hokkaido-Kuerbis", "Alkohol", "Cocktailtomaten", "Rosinen", "Wodka", "Makrele", "Gnocchi", "Lammkeule", "Trockenpflaumen", "Bucatini", "Kalbsschnitzel", "Zwetschgen", "Frischkaese", "Dinkelmehl", "Raki", "Tex Mex Sauce", "Macaroni", "Hackfleisch", "Kraeuter Joghurtsauce", "Eierlikoer", "Couscous", "Radieschen", "Camembert", "Vanillepulver", "Mandarinensaft", "Griess", "Dorsch", "Miracel Whip Balance", "Preiselbeergelee", "Lachsersatz in Oel", "Spaghetti", "Gans", "Preiselbeersirup", "Rinderhack", "Tung Koo", "Nektarine", "Miesmuscheln", "Prosecco", "Putengulasch", "Kokossirup", "Kumquats", "Pfluecksalat", "Beinscheibe", "Cachaca", "Schinken-Pfefferlinge", "Suppennudeln", "Himbeeren", "Tomaten", "franzoesischer Weichkaese", "Kaffee", "Eigelb", "Nussbiskuit", "Herzmuscheln", "Dicke Bohnen", "Lachs", "Halloumi", "Irischer Whisky", "Tomatenpaprika", "Kraeuterbutter", "Isot Paprika", "Nori-Algen-Blaetter", "Vanillezucker", "Champignons", "Wildfond", "Corned Beef", "Wakame", "Texas Mix", "Zitronensorbet", "Estragon", "Popcorn", "Ananassaft", "Pomelos", "Goetterspeise", "Champagner", "Bluetenhonig", "Eiszapfen", "Farfalle", "Meeresfruechte", "Huehnerleber", "Gemuesebruehe", "Ungarische Salami", "Speck", "Ravioli", "Jaegersauce", "Waldmeistersirup", "Rumaroma", "Kakao", "Spargel", "Waffeln", "Putengeschnetzeltes", "Cracker", "Zitronenmelisse", "Thai Curry Paste gelb", "Cornflakes", "Liebstoeckel", "Kebab", "loeslicher Espresso", "Angostura", "Heidelbeersaft", "Blutorangensaft", "Gefluegel Fleischwurst", "Mohn", "Brezen TK", "Minzezweig", "Erdbeermarmelade", "Zitronenscheiben", "Kurkuma", "Pfifferlinge", "Tomate", "Radicchio Salat", "Wan Tan Teig", "Bittermandeloel", "Putenkeule", "Fuenf-Gewuerz", "Forellenfilet", "Roestzwiebeln", "Anchovis", "Hohe Rippe", "frische Kraeuter", "Suppengemuese TK", "Roquefortkaese", "Balsamico Vinaigrette", "Knack und Back Broetchen", "Rote Gruetze", "Eiernudeln", "Selleriesalat", "Maismehl", "Arrak", "Haehnchenfluegel", "Kondensmilch", "Belegkirschen", "Sesamoel", "Schweinebraten", "Gouda Scheiben", "Apfelessig", "Pilzeinweichwasser", "Eier", "Rinderbrust", "Spaetzle geschabt", "Linsen", "Mu Err Pilze", "Pfefferkoerner", "Schweinenacken", "Bockwurst", "Wasabi Paste", "Lammhack", "Schmelzflocken", "Zuckercouleur", "Ascorbinsaeure", "American Dressing", "Spinat TK", "Tomatenmark", "Schweinerollbraten", "Barbecue Sauce", "Kefir", "Krabbensalat", "Currywurst", "Zwiebeln", "Zitronenschale", "Semmelknoedel", "Polyp", "Sesam-Samen", "Kahlua", "Korn", "Stachelbeeren", "Zigeunersauce", "Debreziner", "Gewuerzgurkenwasser", "Dijon Senf", "Tilsiter Scheiben", "Erbsen", "Tomatenwuerfel", "Crushed Ice", "Joghurt", "Schinkenspeck", "Maraschino", "Bierschinken", "Kalbsbraten", "Rote Linsen", "Weissburgunder", "Sultaninen", "Apfelringe", "Schweineschnitzel", "Erdbeeren", "Nashi", "Suppenhuhn", "Passoa", "Rumpsteak", "Sahnequark", "Tamarinde", "Schokostreusel", "Maiskolben", "Martini", "Huehnerfleisch", "Austernpilze", "Salzbrezeln", "Moselwein", "Johannisbeerblaetter", "Creme Fraiche Kraeuter", "Pistazien", "Zuckererbsenschoten", "Wildschweinmedaillons", "Akazienhonig", "Marillenlikoer", "Roggenmehl", "Mexiko Gemuesepfanne", "Selleriegruen", "Trockensauerteig", "Nelken", "Bananensaft", "Schweinelachssteak", "Schweinegulasch", "Petersilienwurzel", "Multivitaminsaft", "Burgunder", "Kraeuteressig", "Birnenschnaps", "Dill", "Cocktailkirschen", "Hoehlenkaese", "Hirschgulasch", "Espresso", "Magerquark", "Quitten", "Chinesische Chilisauce", "Brathering", "Haselnussoel", "Thunfisch naturell", "Schokoladenkuchen", "Quark", "Tortilla Chips", "Kiwi", "Doppelrahmfrischkaese", "Magermilchjoghurt", "Kaffeepulver", "Ruebenkraut", "Lammsteak", "Cranberrysaft", "Gorgonzola", "Suesskartoffeln", "Backpulver", "Brechbohnen", "Putenbrust", "Rhabarber", "Staerkemehl", "Tramezzini", "Krebspaste", "Kidneybohnen", "Kecap Manis", "Worcester Sauce", "Zitronat", "XXL Hamburger Broetchen", "Paniermehl", "Zitronen", "Kartoffelpueree", "Tagliatelle", "Vollmilchschokolade", "Pflaumenmus", "Broetchen", "Ananas", "Pumpernickel", "Glasnudeln", "Sahnepuddingpulver", "Haehnchengeschnetzeltes", "Ebly-Weizen", "Hot Dog Broetchen", "Mini-Mozzarella-Kugeln", "Beta Sweet Moehre", "Krokant", "Don Bernardo Manchego", "Okraschoten", "Dorade", "Kreuzkuemmel (Cumin)", "Kaviar", "Shiitake-Pilze", "probiotische Dessertcreme", "Conchiglioni", "Butternut-Kuerbis", "Sauerteig", "Steckrueben", "Johannisbeeren", "Greyerzer", "Schwarzbier", "Bulgaria Joghurt", "Bandnudeln", "Thymian", "Putenschnitzel", "Karbonade", "Mirabellen", "Pizzateig", "Rote Gruetze Pulver", "Avocado", "Meersalz", "Nougat", "Mozzarella Bambini", "Butterschmalz", "Gemuesezwiebel", "Oliven", "Weintrauben", "Gambas", "Hoi-Sin-Sauce", "Chicoree", "Quittengelee", "Amaretto", "Amaretti", "Weisskohl", "Jalapenos", "Paprikamark", "Lauch", "Gurke", "Pita", "Broccoli", "Zitronenjoghurt", "Palmnut Cream", "Sardellenfilets", "Mischpilze", "Cayennepfeffer", "Sauerfleisch", "Tortellini", "Penne", "Bruehe", "Energy Drink", "Sternanis", "Bueffelmozzarella", "Salami", "Bacardi Razz", "Wachsbohnensalat", "Bauernbrot Scheiben", "Cocktailsauce", "Schweinebauch", "Tilapiafilet", "Nussmischung", "Rosenkohl", "Erbsen TK", "Fotofolie", "Madeirawein", "Sardinen", "Frisee", "Orange", "Kokosraspel", "Gefluegelfond", "Zuckerruebensirup", "Mandelsplitter", "Italian Dressing", "Kroketten", "Haselnuesse", "Orangensaft", "Gemuesemix", "Fanta", "Butter", "Grapefruitsaft", "Tapiokamehl", "Fruchtjoghurt Zitrone", "Artischockenboeden", "Pecannuesse", "Mango", "Wacholderbeeren", "Reismehl", "Kokosfett", "Wachteleier", "Aranca Maracujapulver", "gruener Spargel", "Kartoffeln", "Porree", "Schinkenwuerfel", "Mettenden", "Steinbeisserfilet", "Rettich", "Aprikosenmarmelade", "Nougatschokolade", "Tofu", "Hummer-Paste", "Muesli", "Mortadella", "Puszta Salat", "Kaki", "Reis", "Forellen", "Basmati Reis", "Bagel", "Cherry Brandy", "Kalbsfond", "Wiener Wuerstchen", "Zuckersirup", "Apfelsaft", "Schnittlauch", "Salbei", "Cannelloni", "Loeffelbiskuit", "Austernsauce", "Johannisbeersaft", "Gaenseleber", "Vollkorn Spaghetti", "Pesto", "Lasagne-Blaetter", "Cognac", "Thymianzweige", "Schinken", "Ochsenschwanz", "Haferfleks", "Daenische Remoulade", "Knoblauchpulver", "Bacon", "Mirin", "Fruehstuecksfleisch", "Wasabi Pulver", "Kichererbsen", "Bierschinken Scheiben", "Fliederbeersaft", "Creme de Cassis", "Sojaoel", "Apfelkompott", "Boursin", "Mixed Pickles", "Sauermilchkaese", "Sushi-Reis", "Olivenoel", "Yufka Teigblaetter", "Kaninchen", "Putenmedaillon", "Holundersaft", "Karambole", "Fruchtsaft", "Koernerbrotscheiben", "Reisessig", "Bulgur", "Roggenbroetchen", "Artischocken", "Pfefferminzblaetter", "Ziegenfrischkaese", "Zuckererbsen", "Strudelteig", "Orangeat", "Parmesan", "Schweineschmalz", "Schwarzbrot", "Gruenkohl", "Hirschsteak", "Kalbsleber", "Anis", "Baguette Broetchen TK", "Schwarze Bohnen", "Mini Marshmallows", "Creme de Bananes", "Chilischote", "Rinderfilet", "Blutwurst", "Sherryessig", "Chilisauce", "Tilsiter", "Krabben", "Schwarzwurzeln", "Charentais Melone", "Granatapfel", "Maisgriess", "Kasseler Kotelett", "Balsamico-Essig", "Coppa"};
	private AutoCompleteTextView name;
	private EditText einheit;
	private Spinner spinner;
	private Button insert,scan;
	private ProgressBar pb;
	private String barcode, nameEAN, hersteller, ean;
	
	String user = "Hanswurst";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_neuezutat);		
		referenceUIElements();
		initParse();
		
		onClickListener();
		textChangedListener();		
	}

	private void onClickListener() {
		insert.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(name.getText().toString().length()>0&&einheit.getText().toString().length()>0){
					ParseObject data = new ParseObject("UserData");
					data.put("Masseinheit", spinner.getSelectedItem().toString());
					data.put("Zutat", name.getText().toString());
					data.put("Username", user);
					data.put("Menge", Double.parseDouble(einheit.getText().toString()));
					data.saveInBackground();
					
					Toast toast = Toast.makeText(getApplicationContext(), name.getText().toString()+" wurde(n) in der Datenbank gespeichert", Toast.LENGTH_SHORT);
					toast.show();
					
					Intent i = new Intent(NeueZutat.this,Main.class);
					startActivity(i);
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
	
    public void addObject (){
    	IntentIntegrator integrator = new IntentIntegrator(this);
    	integrator.initiateScan();
    }

	private void getEinheit() {		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Content");
		query.whereContains("Zutat", name.getText().toString());
		query.setLimit(1000);
		query.findInBackground(new FindCallback<ParseObject>() {        		   
			@Override
			public void done(List<ParseObject> arg0, ParseException e) {
				if (arg0.size()!=0) {					
					String get = arg0.get(0).getString("Masseinheit");
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
		name.setAdapter(adapter);
		insert = (Button) findViewById (R.id.zutatHinzufuegenButton);
		einheit = (EditText) findViewById (R.id.einheit);
		scan = (Button) findViewById (R.id.scanButton);
		pb = (ProgressBar) findViewById (R.id.progressBar1);
		pb.setVisibility(View.GONE);
	}
	
	private void textChangedListener(){
		name.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				getEinheit();
			}			
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){
	        }	        
	        public void onTextChanged(CharSequence s, int start, int before, int count){	            
	        }

		});
	}
	
	private void initParse() {
		Parse.initialize(this, "PXJakVYimXSoEUbQvyiNRIB3LzCbP0FEqFOM7NZD", "ms0stwKSjkAcbhuBFs3LOt0Qmjt50UZ3buElHYGm");
		ParseAnalytics.trackAppOpened(getIntent());	
		ParseUser.enableAutomaticUser();
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

}
