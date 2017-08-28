package com.marius.projetandroid_v0;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main2DisplayTop10 extends AppCompatActivity {
    Button btn_displayTop10 = null;
    Button btn_home = null;
    EditText edtGame = null;
    /* fonction pour retourner au menu */
    private View.OnClickListener fct_home = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentMenu = new Intent(Main2DisplayTop10.this,MainMenu.class);
            startActivity(intentMenu);
            finish();
        }
    };

    private View.OnClickListener fct_displayTop10 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            edtGame = (EditText) findViewById(R.id.edtGame);
            if(!edtGame.getText().toString().equals("")) {
                asyncTop10 aTop10 = new asyncTop10();
                aTop10.execute(edtGame.getText().toString());
            }
            else
                dialigAlertP(Main2DisplayTop10.this.getString(R.string.errTitle),"name of game is empty");

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_display_top10);
        btn_displayTop10 = (Button) findViewById(R.id.btn_top10);
        btn_displayTop10.setOnClickListener(fct_displayTop10);
        btn_home=(Button)findViewById(R.id.btn_Hometop10);
        btn_home.setOnClickListener(fct_home);
    }

    /******************************************************************/
    /*                                                                */
    /*      class asynchrone                                          */
    /*                                                                */

    /******************************************************************/
    public class asyncTop10 extends AsyncTask<String, Void, String[]> {
        ProgressDialog progress = new ProgressDialog(Main2DisplayTop10.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle(Main2DisplayTop10.this.getString(R.string.titleBar));
            progress.setMessage(Main2DisplayTop10.this.getString(R.string.textBar));
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
                    code = 1;
                    jsonReader.nextName();
                    code = 2;
                    rsp[0] = String.valueOf(jsonReader.nextInt());
                    code = 3;
                    jsonReader.nextName();
                    code = 4;
                    jsonReader.beginArray();
                    code = 5;
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
                String title = Main2DisplayTop10.this.getString(R.string.errTitle);

                // Mise en place du message personnalisé par erreur
                switch (result[0]) {
                    case "100":
                        msg = "ma1111";
                        break;
                    case "300":
                        msg = "ma111";
                        break;
                    default:
                        msg = result[1];
                        title = result[0];
                        break;
                }
                dialigAlertP(msg,title);
            } else {
                callback(result);
            }
        }
    }

    public void callback(String[] result) {
        TableLayout table = (TableLayout) findViewById(R.id.tableTop10);
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

    /* fonction boite de dialog  */
    public void dialigAlertP(String msg, String title){
        AlertDialog err = new AlertDialog.Builder(Main2DisplayTop10.this).create();
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
    }
}