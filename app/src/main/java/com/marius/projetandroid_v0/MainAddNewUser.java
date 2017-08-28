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

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainAddNewUser extends AppCompatActivity {

    EditText edt_userName=null;
    EditText edt_newPassword = null;
    EditText edt_confirmNewPassword=null;

    Button btn_saveNewUser = null;
    Button btn_cancel = null;

    /* fonction appelée pour sauvegarder un nouveau utilisateur*/
    private View.OnClickListener fct_saveUser = new View.OnClickListener(){
        @Override
        public  void onClick(View V){
            edt_userName = (EditText)findViewById(R.id.edtNewUserName);
            edt_newPassword=(EditText)findViewById(R.id.edtNewUserPassword);
            edt_confirmNewPassword=(EditText)findViewById(R.id.edtNewUserConfirmPassword);
            /* test si les deux mots de pass sont iden */
            if(edt_newPassword.getText().toString().equals(edt_confirmNewPassword.getText().toString())) {
                asynNewUser newUser = new asynNewUser();
                newUser.execute(edt_userName.getText().toString(), edt_newPassword.getText().toString());
            }
            else
               dialigAlertP(MainAddNewUser.this.getString(R.string.passwordsNoSame),MainAddNewUser.this.getString(R.string.errTitle));

            /*Intent intent = getIntent();
            setResult(RESULT_OK,  intent);
            finish();*/
        }
    };

    /* fonction appelée pour annuler l'encodage d'un nouveau utilisateur */
    private View.OnClickListener fct_cancel = new View.OnClickListener(){
        @Override
        public  void onClick(View V){
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add_new_user);

        btn_saveNewUser = (Button)findViewById(R.id.btn_saveNewUser);
        btn_saveNewUser.setOnClickListener(fct_saveUser);

        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(fct_cancel);
    }

    /******************************************************************/
    /*                                                                */
    /*      class asynchrone                                          */
    /*                                                                */
    /******************************************************************/
    private class asynNewUser extends AsyncTask<String, Void, String> {
        ProgressDialog progress = new ProgressDialog(MainAddNewUser.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle(MainAddNewUser.this.getString(R.string.titleBar));
            progress.setMessage(MainAddNewUser.this.getString(R.string.textBar));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Override
        protected void onProgressUpdate(Void... progress) {}

        @Override
        protected String doInBackground(String... data) {

            try {
                String code;
                URL url = new URL("https://donjoe81.000webhostapp.com/scripts/creer_compte.php");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                OutputStream os = conn.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                String param = "pseudo=" + data[0] + "&mdp=" + data[1];
                bw.write(param);
                bw.flush();
                bw.close();
                os.close();
                conn.connect();
                int rspCode = conn.getResponseCode();
                if (rspCode == 200) {
                    InputStream inputStream = conn.getInputStream();
                    InputStreamReader inputSteamReader = new InputStreamReader(inputStream);
                    JsonReader jsonReader = new JsonReader(inputSteamReader);
                    jsonReader.beginObject();
                    jsonReader.nextName();
                    code = jsonReader.nextString();
                    jsonReader.endObject();
                } else {
                    code = String.valueOf(rspCode);
                }
                conn.disconnect();
                return code;
            } catch (Exception ex) {
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
                String title=MainAddNewUser.this.getString(R.string.errTitle);

                switch (result) {
                    case "100":
                        msg = MainAddNewUser.this.getString(R.string.errLoginEmpty);
                        break;
                    case "110":
                        msg = MainAddNewUser.this.getString(R.string.errPwdEmpty);
                        break;
                    case "200":
                        msg = MainAddNewUser.this.getString(R.string.errPseudoExits);
                        break;
                    default:
                        msg = MainAddNewUser.this.getString(R.string.errOther);
                        break;
                }
                dialigAlertP(msg,title);

            } else {
                callback();
            }

        }
    }
    /***********************************************************/
    /*                  fin class asyn                         */
    /***********************************************************/

    public void callback() {
        EditText etUsn = (EditText) findViewById(R.id.edtNewUserName);
        EditText etPwd = (EditText) findViewById(R.id.edtNewUserPassword);
        Intent intent = this.getIntent();
        intent.putExtra("pseudo", etUsn.getText().toString());
        setResult(RESULT_OK,  intent);
        finish();
    }

    /* fonction boite de dialog  */
    public void dialigAlertP(String msg, String title){
        AlertDialog err = new AlertDialog.Builder(MainAddNewUser.this).create();
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
