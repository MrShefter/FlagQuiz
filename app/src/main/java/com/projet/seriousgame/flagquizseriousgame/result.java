package com.projet.seriousgame.flagquizseriousgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class result extends AppCompatActivity {

    int language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        TextView highScoreLabel = (TextView) findViewById(R.id.highScoreLabel);
        Button button = (Button) findViewById(R.id.button);
        TextView lost = (TextView) findViewById(R.id.lost);

        int languageInt = getIntent().getIntExtra("LANGUAGE", 0);
        language = languageInt;

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");

        if(language == 0){
            lost.setText("PERDU");
            button.setText("Recommencer");
        }

        SharedPreferences settings = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);

        if (score > highScore) {
            if(language == 0){
                highScoreLabel.setText("Meilleur score : " + score);
            }
            else{
                highScoreLabel.setText("High Score : " + score);
            }

            // Update High Score
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();

        } else {
            if(language == 0){
                highScoreLabel.setText("Meilleur score : " + highScore);
            }
            else{
                highScoreLabel.setText("High Score : " + highScore);
            }

        }

    }


    public void tryAgain(View view) {
        Intent intent = new Intent(getApplicationContext(), start.class);
        intent.putExtra("LANGUAGE", language);
        startActivity(intent);
    }
    

    // Disable Return Button
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }
}
