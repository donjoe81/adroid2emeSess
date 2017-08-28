package com.marius.projetandroid_v0;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainDisplayUsers extends AppCompatActivity {
    Button btn_home = null;
    /* fonction pour retourner au menu */
    private View.OnClickListener fct_home = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentMenu = new Intent(MainDisplayUsers.this,MainMenu.class);
            startActivity(intentMenu);
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_display_users);
        btn_home = (Button)findViewById(R.id.btn_HomeDisplayUser);
        btn_home.setOnClickListener(fct_home);
        PlayerListAsync listUser = new PlayerListAsync();
        listUser.execute();
    }


    private class PlayerListAsync extends AsyncTask<String, Void, String[]> {
        ProgressDialog progress = new ProgressDialog(MainDisplayUsers.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle(MainDisplayUsers.this.getString(R.string.titleBar));
            progress.setMessage(MainDisplayUsers.this.getString(R.string.textBar));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
        }

        @Override
        protected String[] doInBackground(String... data) {
            String[] rsp;
            try {
                URL url = new URL("https://donjoe81.000webhostapp.com/scripts/lister_pseudos.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                int rspCode = conn.getResponseCode();
                if (rspCode == 200) {
                    rsp = new String[2000];
                    InputStream inputStream = conn.getInputStream();
                    InputStreamReader inputSteamReader = new InputStreamReader(inputStream);
                    JsonReader jsonReader = new JsonReader(inputSteamReader);
                    jsonReader.beginObject();
                    jsonReader.nextName();
                    rsp[0] = String.valueOf(jsonReader.nextInt());
                    jsonReader.nextName();
                    jsonReader.beginArray();
                    int i = 1;
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        jsonReader.nextName();
                        rsp[i] = jsonReader.nextString();
                        jsonReader.endObject();
                        i++;
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
                rsp = new String[1];
                rsp[0] = ex.getMessage();
                return rsp;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {
            if(progress.isShowing())
                progress.dismiss();

            // Vérification du résultat
            if (!result[0].equals("0")) {

                // Déclaratoin des variables locales
                String msg;
                String title=MainDisplayUsers.this.getString(R.string.errTitle);

                TextView text = (TextView)findViewById(R.id.playerListTitle);
                text.setText(result[1]);

                // Mise en place du message personnalisé par erreur
                switch (result[0]) {
                    case "300":
                        msg = "moioioi1";
                        break;
                    default:
                        msg = "moioi3";
                        break;
                }

                // Création et affichage de l'alertDialog
                AlertDialog err = new AlertDialog.Builder(MainDisplayUsers.this).create();
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
                ShowResult(result);
            }
        }
    }


    protected void ShowResult(String [] noms) {
        final LinearLayout ll = (LinearLayout) findViewById(R.id.Layoutcontenair);
        int n= noms.length;
        for (int i = 1; i < n && noms[i] != null; i++) {
            TextView tv = new TextView(this);
            //tv.setTextColor(R.color.colorPrimary);
            //tv.setGravity(Gravity.CENTER);
            tv.setPadding(15,15,15,15);
            tv.setText("\u2022 " + noms[i]);
            ll.addView(tv, 1);
        }
    }
}
