package com.marius.projetandroid_v0;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class welcome extends AppCompatActivity {

    Button btn_connexion = null;//
    Button btn_addNewUser=null;


    EditText edt_login = null;
    EditText edt_password = null;

    /* fonction appelée pour effectuer la connexion*/
    private View.OnClickListener fct_connexion = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            edt_login=(EditText)findViewById(R.id.edtLogin);
            edt_password = (EditText)findViewById(R.id.edtPass);

            welcome.asyncLogin asyncLog = new welcome.asyncLogin(); // Création de l'objet ASYNCHRONE
            asyncLog.execute(edt_login.getText().toString(), edt_password.getText().toString()); // Exécution avec les paramètres

        }
    };

    /* fonction appelée pour afficher le formulaire de saisi d'un nouveau utilisateur*/
    private View.OnClickListener fct_addNewUser = new View.OnClickListener(){
        @Override
        public  void onClick(View V){
          /*  edt_userName=(EditText)findViewById(R.id.edtNewUserName);
            edt_newPassword = (EditText)findViewById(R.id.edtNewUserPassword);
            edt_confirmNewPassword=(EditText)findViewById(R.id.edtNewUserConfirmPassword);
            btn_saveNewUser = (Button)findViewById(R.id.btn_saveNewUser);
            edt_userName.setVisibility(View.VISIBLE);
            edt_newPassword.setVisibility(View.VISIBLE);
            edt_confirmNewPassword.setVisibility(View.VISIBLE);
            btn_saveNewUser.setVisibility(View.VISIBLE);*/
            Intent intent = new Intent(welcome.this, MainAddNewUser.class);
            startActivityForResult(intent, 0);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String title="new user";
        String msg="account succed";
        // Vérification du code de résultat
        if (resultCode == RESULT_OK) {
            dialigAlertP(msg,title);
            edt_login = (EditText)findViewById(R.id.edtLogin);
            edt_login.setText(data.getStringExtra("pseudo"));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_connexion = (Button)findViewById(R.id.btn_connexion);
        btn_connexion.setOnClickListener(this.fct_connexion);

        btn_addNewUser=(Button)findViewById(R.id.btn_newUser);
        btn_addNewUser.setOnClickListener(fct_addNewUser);
    }

    /******************************************************************/
    /*                                                                */
    /*      class asynchrone                                          */
    /*                                                                */
    /******************************************************************/
    private class asyncLogin extends AsyncTask<String, Void, String> {
        ProgressDialog progress = new ProgressDialog(welcome.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle(welcome.this.getString(R.string.titleBar));
            progress.setMessage(welcome.this.getString(R.string.textBar));
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
                URL url = new URL("https://donjoe81.000webhostapp.com/scripts/se_connecter.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Déclaration de la méthode pour l'envoi
                conn.setRequestMethod("POST");

                // Mise en place d'un stream pour les paramètres d'envoi
                OutputStream os = conn.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String param = "pseudo=" + data[0] + "&mdp=" + data[1];
                bw.write(param);
                bw.flush(); // Vide le BufferedWriter
                bw.close(); // Ferme le BufferedWriter
                os.close(); // Ferme le OutputStream
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
                progress.dismiss();

            // Vérification du résultat
            if(!result.equals("0")) {
                // Déclaratoin des variables locales
                String msg;
                String title=welcome.this.getString(R.string.errTitle);
                final boolean closeApp;
                // Mise en place du message personnalisé par erreur
                switch (result) {
                    case "100": msg = welcome.this.getString(R.string.loginPassIncorrect); break;
                    case "110": msg = welcome.this.getString(R.string.loginPassIncorrect);break;
                    case "200": msg = welcome.this.getString(R.string.loginPassIncorrect);break;
                    default: msg = welcome.this.getString(R.string.errOther); break;
                }
                // Création et affichage de l'alertDialog
                dialigAlertP(msg,title);

            } else{
                edt_password.setText("");
                Intent intentMenu = new Intent(welcome.this,MainMenu.class);
                intentMenu.putExtra("pseudo",edt_login.getText().toString());
                startActivity(intentMenu);
                finish();
            }

        }
    }
    /*************************************************************************/
       /* fonction boite de dialog  */
    public void dialigAlertP(String msg, String title){
        AlertDialog err = new AlertDialog.Builder(welcome.this).create();
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
