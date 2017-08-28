package com.marius.projetandroid_v0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    Button btn_addScore = null;
    Button btn_displayTop10 = null;
    Button btn_displayGame = null;
    Button btn_displayUsers = null;
    Button btn_deconexion = null;

    private View.OnClickListener fct_addScore = new View.OnClickListener(){
        @Override
        public void onClick(View V){
            //recupération du pseudo passe dans activité
            Intent intent = getIntent();
            String pseudo = intent.getStringExtra("pseudo");

            //affectation
            Intent intentMenu = new Intent(MainMenu.this,MainAddScore.class);
            intentMenu.putExtra("pseudo",pseudo);
            startActivity(intentMenu);
            finish();
        }
    };

    private View.OnClickListener fct_displayTop10 = new View.OnClickListener(){
        @Override
        public void onClick(View V){
            Intent intentMenu = new Intent(MainMenu.this,Main2DisplayTop10.class);
            startActivity(intentMenu);
            finish();
        }
    };

    private View.OnClickListener fct_displayGame = new View.OnClickListener(){
        @Override
        public void onClick(View V){
            Intent intentMenu = new Intent(MainMenu.this,MainDisplayGame.class);
            startActivity(intentMenu);
            finish();
        }
    };

    private View.OnClickListener fct_displayUsers = new View.OnClickListener(){
        @Override
        public void onClick(View V){
            Intent intentMenu = new Intent(MainMenu.this,MainDisplayUsers.class);
            startActivity(intentMenu);
            finish();
        }
    };

    private View.OnClickListener fct_deconnextion = new View.OnClickListener(){
      @Override
        public void onClick(View V){
          Intent intentMenu = new Intent(MainMenu.this,welcome.class);
          startActivity(intentMenu);
          finish();
      }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        btn_addScore = (Button)findViewById(R.id.btnAddScore);
        btn_addScore.setOnClickListener(fct_addScore);

        btn_displayTop10 = (Button)findViewById(R.id.btnDisplayTop10);
        btn_displayTop10.setOnClickListener(fct_displayTop10);

        btn_displayGame = (Button)findViewById(R.id.btnDisplayGame);
        btn_displayGame.setOnClickListener(fct_displayGame);

        btn_displayUsers = (Button)findViewById(R.id.btnDisplayUsers);
        btn_displayUsers.setOnClickListener(fct_displayUsers);

        btn_deconexion=(Button)findViewById(R.id.btn_deconnection);
        btn_deconexion.setOnClickListener(fct_deconnextion);
    }

}
