package com.marius.projetandroid_v0;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainDisplayGame extends AppCompatActivity {
    TextView tv1 = null;
    TextView tv11 = null;

    TextView tv2 = null;
    TextView tv22 = null;

    TextView tv3 = null;
    TextView tv33 = null;

    TextView tv4 = null;
    TextView tv44 = null;

    TextView tv5 = null;
    TextView tv55 = null;

    Button bt1 = null;
    Button bt2 = null;
    Button bt3 = null;
    Button bt4 = null;
    Button bt5 = null;
    Button btn_home=null;

    /* fonction appelée pour effectuer la recherche le top10 des score d'un  jeu*/
    private View.OnClickListener fct_game1 = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            tv1=(TextView) findViewById(R.id.txtGame1);
            asyncGameTop10 asyncGame = new asyncGameTop10(); // Création de l'objet ASYNCHRONE
            asyncGame.execute(tv1.getText().toString()); // Exécution avec les paramètres

        }
    };

    private View.OnClickListener fct_game2 = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            tv2=(TextView) findViewById(R.id.txtGame2);
            asyncGameTop10 asyncGame = new asyncGameTop10(); // Création de l'objet ASYNCHRONE
            asyncGame.execute(tv2.getText().toString()); // Exécution avec les paramètres

        }
    };

    private View.OnClickListener fct_game3 = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            tv3=(TextView) findViewById(R.id.txtGame3);
            asyncGameTop10 asyncGame = new asyncGameTop10(); // Création de l'objet ASYNCHRONE
            asyncGame.execute(tv3.getText().toString()); // Exécution avec les paramètres

        }
    };

    private View.OnClickListener fct_game4 = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            tv4=(TextView) findViewById(R.id.txtGame4);
            asyncGameTop10 asyncGame = new asyncGameTop10(); // Création de l'objet ASYNCHRONE
            asyncGame.execute(tv4.getText().toString()); // Exécution avec les paramètres

        }
    };

    private View.OnClickListener fct_game5 = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            tv5=(TextView) findViewById(R.id.txtGame5);
            asyncGameTop10 asyncGame = new asyncGameTop10(); // Création de l'objet ASYNCHRONE
            asyncGame.execute(tv5.getText().toString()); // Exécution avec les paramètres

        }
    };

    /* fonction pour retourner au menu */
    private View.OnClickListener fct_home = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentMenu = new Intent(MainDisplayGame.this,MainMenu.class);
            startActivity(intentMenu);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_display_game);
        asyncGame asynG = new asyncGame();
        asynG.execute();

        bt1 = (Button)findViewById(R.id.btnGame1);
        bt1.setOnClickListener(fct_game1);

        bt2 = (Button)findViewById(R.id.btnGame2);
        bt2.setOnClickListener(fct_game2);

        bt3 = (Button)findViewById(R.id.btnGame3);
        bt3.setOnClickListener(fct_game3);

        bt4 = (Button)findViewById(R.id.btnGame4);
        bt4.setOnClickListener(fct_game4);

        bt5 = (Button)findViewById(R.id.btnGame5);
        bt5.setOnClickListener(fct_game5);

        btn_home = (Button)findViewById(R.id.btn_HomeDisplayGame);
        btn_home.setOnClickListener(fct_home);

    }

    /******************************************************************/
    /*                                                                */
    /*      class asynchrone                                          */
    /*                                                                */
    /******************************************************************/
    public class asyncGame extends AsyncTask<String, Void, String[]> {
        ProgressDialog progress = new ProgressDialog(MainDisplayGame.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle(MainDisplayGame.this.getString(R.string.titleBar));
            progress.setMessage(MainDisplayGame.this.getString(R.string.textBar));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
        }

        @Override
        protected  String[] doInBackground(String... data) {
            String[] rsp= new String[999999];
            int code = 0;
            try {

                // Mise en place de l'appel au RPC
                URL url = new URL("https://donjoe81.000webhostapp.com/scripts/lister_jeux.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Déclaration de la méthode pour l'envoi
                conn.setRequestMethod("GET");
                conn.connect(); // Connection au RPC

                // Récupération et vérification du code de retour
                int rspCode = conn.getResponseCode();
                if (rspCode == 200) {
                    InputStream inputStream = conn.getInputStream();
                    InputStreamReader inputSteamReader = new InputStreamReader(inputStream);
                    JsonReader jsonReader = new JsonReader(inputSteamReader);
                    jsonReader.beginObject();
                    jsonReader.nextName();
                    rsp[0]=String.valueOf(jsonReader.nextInt());

                    jsonReader.nextName();
                    jsonReader.beginArray();
                    int i=1;
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();

                        jsonReader.nextName();
                        rsp[i]=jsonReader.nextString();

                        jsonReader.nextName();
                        rsp[i+1]=jsonReader.nextString();

                       i+=2;

                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                    jsonReader.endObject();
                } else {
                    rsp=new String[1];
                    rsp[0]=String.valueOf(rspCode);
                }

                conn.disconnect();
                return rsp;

            } catch (Exception ex) {
                rsp=new String[1];
                rsp[0]=ex.getMessage();
                return rsp;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {

            if (progress.isShowing()) {
                progress.dismiss();
            }
            // Vérification du résultat
            if (!result[0].equals("0")) {

                // Déclaratoin des variables locales
                String msg;
                String title = MainDisplayGame.this.getString(R.string.errTitle);
                final boolean closeApp;

                // Mise en place du message personnalisé par erreur
                switch (result[0]) {
                    case "100":
                        msg = "ma1111";
                        break;
                    case "110":
                        msg = "ma111";
                        break;
                    case "404":
                        msg = "ma11";
                        break;
                    case "500":
                        msg = "ma1";
                        break;
                    default:
                        msg = result[1];
                        title = result[0];
                        break;
                }

                // Création et affichage de l'alertDialog
                AlertDialog err = new AlertDialog.Builder(MainDisplayGame.this).create();
                err.setTitle(title);
                err.setMessage(msg);
                err.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );
                err.show();
            } else {
                callback(result,1);
            }
        }
    }
    /******************************************************************/
    /*      fin class asynchrone                                      */
    /******************************************************************/
    public void callback(String[] result,Integer debut){
        Integer i = debut;
        tv1 = (TextView)findViewById(R.id.txtGame1);
        tv11 = (TextView)findViewById(R.id.txtGame11);

        tv2 = (TextView)findViewById(R.id.txtGame2);
        tv22 = (TextView)findViewById(R.id.txtGame22);

        tv3 = (TextView)findViewById(R.id.txtGame3);
        tv33 = (TextView)findViewById(R.id.txtGame33);

        tv4 = (TextView)findViewById(R.id.txtGame4);
        tv44 = (TextView)findViewById(R.id.txtGame44);

        tv5 = (TextView)findViewById(R.id.txtGame5);
        tv55 = (TextView)findViewById(R.id.txtGame55);

        bt1 = (Button)findViewById(R.id.btnGame1);
        bt2 = (Button)findViewById(R.id.btnGame2);
        bt3 = (Button)findViewById(R.id.btnGame3);
        bt4 = (Button)findViewById(R.id.btnGame4);
        bt5 = (Button)findViewById(R.id.btnGame5);


        if(result[i]!=null){
            tv1.setText(result[i]);
            tv11.setText(result[i+1]);
            bt1.setVisibility(View.VISIBLE);
            i+=2;
        }
        else
            bt1.setVisibility(View.INVISIBLE);

        if(result[i]!=null){
            tv2.setText(result[i]);
            tv22.setText(result[i+1]);
            bt2.setVisibility(View.VISIBLE);
            i+=2;
        }
        else
            bt2.setVisibility(View.INVISIBLE);

        if(result[i]!=null){
            tv3.setText(result[i]);
            tv33.setText(result[i+1]);
            bt3.setVisibility(View.VISIBLE);
            i+=2;
        }
        else
            bt3.setVisibility(View.INVISIBLE);

        if(result[i]!=null){
            tv4.setText(result[i]);
            tv44.setText(result[i+1]);
            bt4.setVisibility(View.VISIBLE);
            i+=2;
        }
        else
            bt4.setVisibility(View.INVISIBLE);

        if(result[i]!=null){
            tv5.setText(result[i]);
            tv55.setText(result[i+1]);
            bt5.setVisibility(View.VISIBLE);
            i+=2;
        }
        else
            bt5.setVisibility(View.INVISIBLE);

    }

    /**********************************************************************************/
    public class asyncGameTop10 extends AsyncTask<String, Void, String[]> {
        ProgressDialog progress = new ProgressDialog(MainDisplayGame.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle(MainDisplayGame.this.getString(R.string.titleBar));
            progress.setMessage(MainDisplayGame.this.getString(R.string.textBar));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
        }

        @Override
        protected String[] doInBackground(String... data) {
            String[] rsp;
            int code = 0;
            try {

                // Mise en place de l'appel au RPC
                URL url = new URL("https://donjoe81.000webhostapp.com/scripts/afficher_top.php?jeu=" + data[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Déclaration de la méthode pour l'envoi
                conn.setRequestMethod("GET");
                conn.connect(); // Connection au RPC

                // Récupération et vérification du code de retour
                int rspCode = conn.getResponseCode();
                if (rspCode == 200) {
                    rsp = new String[21];
                    InputStream inputStream = conn.getInputStream();
                    InputStreamReader inputSteamReader = new InputStreamReader(inputStream);
                    JsonReader jsonReader = new JsonReader(inputSteamReader);
                    jsonReader.beginObject();
                    jsonReader.nextName();
                    rsp[0] = String.valueOf(jsonReader.nextInt());
                    jsonReader.nextName();
                    jsonReader.beginArray();
                    int i = 1;
                    while (jsonReader.hasNext() && i < 21) {
                        jsonReader.beginObject();
                        jsonReader.nextName();
                        rsp[i] = jsonReader.nextString();
                        jsonReader.nextName();
                        rsp[i + 1] = jsonReader.nextString();
                        jsonReader.endObject();
                        i = i + 2;
                    }
                    jsonReader.endArray();
                    jsonReader.endObject();
                } else {
                    rsp = new String[1];
                    rsp[0] = String.valueOf(rspCode);
                }

                conn.disconnect();
                return rsp;

            } catch (Exception ex) {
                rsp = new String[2];
                rsp[0] = String.valueOf(code);
                rsp[1] = ex.getMessage();
                return rsp;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {

            if (progress.isShowing()) {
                progress.dismiss();
            }
            // Vérification du résultat
            if (!result[0].equals("0")) {

                // Déclaratoin des variables locales
                String msg;
                String title = MainDisplayGame.this.getString(R.string.errTitle);
                final boolean closeApp;

                // Mise en place du message personnalisé par erreur
                switch (result[0]) {
                    case "100":
                        msg = "ma1111";
                        break;
                    case "110":
                        msg = "ma111";
                        break;
                    case "404":
                        msg = "ma11";
                        break;
                    case "500":
                        msg = "ma1";
                        break;
                    default:
                        msg = result[1];
                        title = result[0];
                        break;
                }

                // Création et affichage de l'alertDialog
                AlertDialog err = new AlertDialog.Builder(MainDisplayGame.this).create();
                err.setTitle(title);
                err.setMessage(msg);
                err.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );
                err.show();
            } else {
                callback(result);
            }
        }
    }

    public void callback(String[] result) {
        TableLayout table = (TableLayout)findViewById(R.id.tableTop10Game);
        table.removeAllViewsInLayout();
        Integer i = 1, j = 1;
        Integer nbr = result.length;
        TableRow row; // création d'un élément : ligne
        TextView tv, tv1, tv2; // création des cellules
        boolean res=false;
        while (i < nbr && !res) {

            row = new TableRow(this); // création d'une nouvelle ligne

            tv1 = new TextView(this); // création cellule
            tv1.setText(result[i]); // ajout du texte
            tv1.setGravity(Gravity.CENTER); // centrage dans la cellule
            // adaptation de la largeur de colonne à l'écran :
            tv1.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            // idem 2ème cellule
            tv2 = new TextView(this);
            tv2.setText(result[i + 1]);
            tv2.setGravity(Gravity.CENTER);
            tv2.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            // idem 2ème cellule
            tv = new TextView(this);
            tv.setText(j.toString());
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            // ajout des cellules à la ligne
            row.addView(tv);
            row.addView(tv1);
            row.addView(tv2);

            // ajout de la ligne au tableau
            table.addView(row);

            i += 2;
            if(result[i]==null)
                res=true;
            j++;
        }
    }

    /*************************************************************************/
}
