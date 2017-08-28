package com.marius.projetandroid_v0;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainAddScore extends AppCompatActivity {

    Button btn_save = null;
    Button btn_home = null;

    EditText edt_nameGame = null;
    EditText edt_score = null;

    private View.OnClickListener fct_saveScore = new View.OnClickListener(){
        @Override
        public  void onClick(View V){
            edt_nameGame = (EditText)findViewById(R.id.edtNameGame);
            edt_score=(EditText)findViewById(R.id.edtScore);

            if(edt_nameGame.getText().toString().equals("") || edt_score.getText().toString().equals("")){
                dialigAlertP("nome du jeu ou  score manquant","erreur!");
            }
            else {
                Intent intent = getIntent();
                String pseudo = intent.getStringExtra("pseudo");
                asynNewScore newUser = new asynNewScore();
                newUser.execute(edt_nameGame.getText().toString(), edt_score.getText().toString(), pseudo);
            }

        }
    };

    /* fonction pour retourner au menu */
    private View.OnClickListener fct_home = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentMenu = new Intent(MainAddScore.this,MainMenu.class);
            startActivity(intentMenu);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add_score);

        Intent intent = getIntent();
        String pseudo = intent.getStringExtra("pseudo");

        btn_save = (Button)findViewById(R.id.btn_saveScore);
        btn_save.setOnClickListener(fct_saveScore);

        btn_home = (Button)findViewById(R.id.btn_HomeAddScore);
        btn_home.setOnClickListener(fct_home);
    }


    /******************************************************************/
    /*                                                                */
    /*      class asynchrone                                          */
    /*                                                                */
    /******************************************************************/
    private class asynNewScore extends AsyncTask<String, Void, String> {
        ProgressDialog progress = new ProgressDialog(MainAddScore.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle(MainAddScore.this.getString(R.string.titleBar));
            progress.setMessage(MainAddScore.this.getString(R.string.textBar));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Override
        protected void onProgressUpdate(Void... progress) {}

        @Override
        protected String doInBackground(String... data) {
            try {
                // Déclaration des variables locales
                String code;

                // Mise en place de l'appel au RPC
                URL url = new URL("https://donjoe81.000webhostapp.com/scripts/ajouter_score.php?jeu=" + data[0] + "&score=" + data[1]+"&pseudo="+data[2]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Déclaration de la méthode pour l'envoi
                conn.setRequestMethod("GET");
                conn.connect(); // Connection au RPC

                // Récupération et vérification du code de retour
                int rspCode = conn.getResponseCode();
                if (rspCode == 200) {
                    // Récupération du résultat
                    InputStream inputStream = conn.getInputStream();
                    InputStreamReader inputSteamReader = new InputStreamReader(inputStream);
                    Scanner scanner = new Scanner(inputSteamReader);
                    code = scanner.next();
                } else {
                    // Mise en place du rspCode comme réponse
                    code = String.valueOf(rspCode);
                }

                // Retour de la fonction
                conn.disconnect();
                return code;
            } catch (Exception ex) {

                // Retour de l'exception
                return ex.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if(progress.isShowing())
            {
                progress.dismiss();
            }
            if (!result.equals("0")) {

                String msg;
                String title=MainAddScore.this.getString(R.string.errTitle);
                edt_nameGame = (EditText)findViewById(R.id.edtNameGame);
                edt_nameGame.setText(result);

                switch (result) {
                    case "100":
                        msg = MainAddScore.this.getString(R.string.errLoginEmpty);
                        break;
                    case "110":
                        msg = MainAddScore.this.getString(R.string.errPwdEmpty);
                        break;
                    case "200":
                        msg = MainAddScore.this.getString(R.string.errPseudoExits);
                        break;
                    default:
                        msg = MainAddScore.this.getString(R.string.errOther);
                        break;
                }
                dialigAlertP(msg,title);

            } else {
                dialigAlertP("score add","succed!");
                Intent intentMenu = new Intent(MainAddScore.this,MainMenu.class);
                startActivity(intentMenu);
                finish();
            }

        }
    }
    /***********************************************************/
    /*                  fin class asyn                         */
    /***********************************************************/

        /* fonction boite de dialog  */
    public void dialigAlertP(String msg, String title){
        AlertDialog err = new AlertDialog.Builder(MainAddScore.this).create();
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
