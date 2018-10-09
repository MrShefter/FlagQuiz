package com.projet.seriousgame.flagquizseriousgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class start extends AppCompatActivity {

    int language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        int languageInt = getIntent().getIntExtra("LANGUAGE", 0);
        language = languageInt;

        TextView bonDrapeau = (TextView) findViewById(R.id.textView3);
        TextView mauvaisDrapeau = (TextView) findViewById(R.id.textView4);
        TextView vies = (TextView) findViewById(R.id.textView5);
        Button button = (Button) findViewById(R.id.button);

        if(language == 0){
            bonDrapeau.setText("Bon drapeau : 10 points");
            mauvaisDrapeau.setText("Mauvais drapeau : -1 vie");
            vies.setText("Vous avez 3 vies");
            button.setText("Commencer");
        }

    }

    public void startGame(View view) {
        Intent intent = new Intent(getApplicationContext(), main.class);
        intent.putExtra("LANGUAGE", language);
        startActivity(intent);
    }

}
