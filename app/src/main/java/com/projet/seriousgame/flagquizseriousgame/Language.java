package com.projet.seriousgame.flagquizseriousgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;


public class Language extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

    }

    public void french(View view) {
        Intent intent = new Intent(getApplicationContext(), start.class);
        intent.putExtra("LANGUAGE", 0);
        startActivity(intent);
    }

    public void english(View view) {
        Intent intent = new Intent(getApplicationContext(), start.class);
        intent.putExtra("LANGUAGE", 1);
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
