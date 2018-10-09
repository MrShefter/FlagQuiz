package com.projet.seriousgame.flagquizseriousgame;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class main extends AppCompatActivity {

    int language;

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView rep2;
    private ImageView rep3;
    private ImageView rep1;
    private TextView question;

    // Size
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    // Position
    private int boxY;
    private int rep2X;
    private int rep2Y;
    private int rep3X;
    private int rep3Y;
    private int rep1X;
    private int rep1Y;

    // Speed
    private int boxSpeed;
    private int rep2Speed;
    private int rep3Speed;
    private int rep1Speed;

    // Score
    private int score = 0;

    // Initialize Class
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SoundPlayer sound;


    // Status Check
    private boolean action_flg = false;
    private boolean start_flg = false;

    //vies
    ImageView vie1 = null;
    ImageView vie2 = null;
    ImageView vie3 = null;

    int nbVies = 3;

    int tmp = 0;

    String id_question = "";
    ArrayList<HashMap<String, String>> flagList = new ArrayList<HashMap<String, String>>();

    int currentQuestion = 0;

    String id_reponse1 = "";
    String id_reponse2 = "";
    String id_reponse3 = "";

    String rep1Id = "";
    String rep2Id = "";
    String rep3Id = "";

    boolean response1 = false;
    boolean response2 = false;
    boolean response3 = false;

    String response = "";


    // JSON Node names
    private static final String TAG_QUESTION = "question";
    private static final String TAG_ID = "id";
    private static final String TAG_RESPONSE1 = "response1";
    private static final String TAG_RESPONSE2 = "response2";
    private static final String TAG_RESPONSE3 = "response3";
    private static final String TAG_RESPONSE = "response";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int languageInt = getIntent().getIntExtra("LANGUAGE", 0);
        language = languageInt;

        sound = new SoundPlayer(this);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);

        if(language == 0){
            startLabel.setText("Appuyer pour commencer");
        }
        box = (ImageView) findViewById(R.id.box);
        rep2 = (ImageView) findViewById(R.id.rep2);
        rep3 = (ImageView) findViewById(R.id.rep3);
        rep1 = (ImageView) findViewById(R.id.rep1);

        vie1 = (ImageView) findViewById(R.id.vie1);
        vie2 = (ImageView) findViewById(R.id.vie2);
        vie3 = (ImageView) findViewById(R.id.vie3);

        vie1.setVisibility(View.VISIBLE);
        vie2.setVisibility(View.VISIBLE);
        vie3.setVisibility(View.VISIBLE);

        question = (TextView) findViewById(R.id.textView);
        question.setVisibility(View.INVISIBLE);



        // Get screen size.
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;


        boxSpeed = Math.round(screenHeight / 80);

        rep2Speed = Math.round(screenWidth / 160);
        rep3Speed = Math.round(screenWidth / 160);
        rep1Speed = Math.round(screenWidth / 160);



        // Move to out of screen.
        rep2.setX(-800);
        rep2.setY(-800);
        rep3.setX(-800);
        rep3.setY(-800);
        rep1.setX(-800);
        rep1.setY(-800);

        scoreLabel.setText("Score : 0");

        box.setX(50);

        try {

            JSONObject jsonObj = new JSONObject(loadJSONFromAsset());

            // On récupère un tableau JSON
            JSONArray flags = jsonObj.getJSONArray(TAG_QUESTION);

            // boucle sur tous les flags du JSON
            for (int i = 0; i < flags.length(); i++) {
                JSONObject c = flags.getJSONObject(i);

                String id = c.getString(TAG_ID);
                String response1 = c.getString(TAG_RESPONSE1);
                String response2 = c.getString(TAG_RESPONSE2);
                String response3 = c.getString(TAG_RESPONSE3);
                String response = c.getString(TAG_RESPONSE);

                //Hashmap temporaire pour un simple flag
                HashMap<String, String> flag = new HashMap<String, String>();

                // adding every child node to HashMap key => value
                flag.put(TAG_ID, id);
                flag.put(TAG_RESPONSE1, response1);
                flag.put(TAG_RESPONSE2, response2);
                flag.put(TAG_RESPONSE3, response3);
                flag.put(TAG_RESPONSE, response);

                // On ajoute le flag simple à la flaglist
                flagList.add(flag);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        currentQuestion = (int)(Math.random() * (flagList.size()));

        id_question = flagList.get(currentQuestion).get("id");
        question.setText(id_question);

        id_reponse1 = flagList.get(currentQuestion).get("response1");
        id_reponse2 = flagList.get(currentQuestion).get("response2");
        id_reponse3 = flagList.get(currentQuestion).get("response3");

        int[] randomReps = new int[3];
        randomReps[0] = (int) (Math.random() * 2.9)+1;
        int tmpRandom = (int) (Math.random() * 2.9)+1;
        while(tmpRandom==randomReps[0]){
            tmpRandom = (int) (Math.random() * 2.9)+1;
        }
        randomReps[1] = tmpRandom;
        tmpRandom = (int) (Math.random() * 2.9)+1;
        while(tmpRandom==randomReps[0] || tmpRandom==randomReps[1]){
            tmpRandom = (int) (Math.random() * 2.9)+1;
        }
        randomReps[2] = tmpRandom;

        String tmpIdRep1 = "";
        String tmpIdRep2 = "";
        String tmpIdRep3 = "";

        //rep1
        if(randomReps[0] == 1){
            tmpIdRep1 = id_reponse1;
        }
        else if(randomReps[0] == 2){
            tmpIdRep1 = id_reponse2;
        }
        else{
            tmpIdRep1 = id_reponse3;
        }

        //rep2
        if(randomReps[1] == 1){
            tmpIdRep2 = id_reponse1;
        }
        else if(randomReps[1] == 2){
            tmpIdRep2 = id_reponse2;
        }
        else{
            tmpIdRep2 = id_reponse3;
        }

        //rep3
        if(randomReps[2] == 1){
            tmpIdRep3 = id_reponse1;
        }
        else if(randomReps[2] == 2){
            tmpIdRep3 = id_reponse2;
        }
        else{
            tmpIdRep3 = id_reponse3;
        }

        rep1.setImageResource(getResources().getIdentifier(tmpIdRep1, "mipmap", getPackageName()));
        rep1Id = tmpIdRep1;
        rep2.setImageResource(getResources().getIdentifier(tmpIdRep2, "mipmap", getPackageName()));
        rep2Id = tmpIdRep2;
        rep3.setImageResource(getResources().getIdentifier(tmpIdRep3, "mipmap", getPackageName()));
        rep3Id = tmpIdRep3;

        response = flagList.get(currentQuestion).get("response");

        if(response.equals("response1")){
            response1 = true;
        }
        else{
            response1 = false;
        }
        if(response.equals("response2")){
            response2 = true;
        }
        else{
            response2 = false;
        }
        if(response.equals("response3")){
            response3 = true;
        }
        else{
            response3 = false;
        }
    }


    public void changePos() {

        hitCheck();


        id_question = flagList.get(currentQuestion).get("id");
        question.setText(id_question);

        question.setVisibility(View.VISIBLE);


        // rep2
        rep2X -= rep2Speed;
        if (rep2X < 0) {

            if(tmp >0){
                nbVies = nbVies - 1;

                if (nbVies == 3) {
                    vie1.setVisibility(View.VISIBLE);
                    vie2.setVisibility(View.VISIBLE);
                    vie3.setVisibility(View.VISIBLE);
                    rep2X = screenWidth;
                    rep3X = screenWidth;
                    rep1X = screenWidth;
                } else if (nbVies == 2) {
                    vie1.setVisibility(View.INVISIBLE);
                    vie2.setVisibility(View.VISIBLE);
                    vie3.setVisibility(View.VISIBLE);
                    rep2X = screenWidth;
                    rep3X = screenWidth;
                    rep1X = screenWidth;
                } else if (nbVies == 1) {
                    vie1.setVisibility(View.INVISIBLE);
                    vie2.setVisibility(View.INVISIBLE);
                    vie3.setVisibility(View.VISIBLE);
                    rep2X = screenWidth;
                    rep3X = screenWidth;
                    rep1X = screenWidth;
                } else {
                    vie1.setVisibility(View.INVISIBLE);
                    vie2.setVisibility(View.INVISIBLE);
                    vie3.setVisibility(View.INVISIBLE);

                    //Stop Timer!!
                    timer.cancel();
                    timer = null;


                    // Show Result
                    Intent intent = new Intent(getApplicationContext(), result.class);
                    intent.putExtra("SCORE", score);
                    intent.putExtra("LANGUAGE", language);
                    startActivity(intent);
                }
                sound.playOverSound();

                currentQuestion = (int)(Math.random() * (flagList.size()));

                id_question = flagList.get(currentQuestion).get("id");
                question.setText(id_question);

                id_reponse1 = flagList.get(currentQuestion).get("response1");
                id_reponse2 = flagList.get(currentQuestion).get("response2");
                id_reponse3 = flagList.get(currentQuestion).get("response3");

                int[] randomReps = new int[3];
                randomReps[0] = (int) (Math.random() * 2.9)+1;
                int tmpRandom = (int) (Math.random() * 2.9)+1;
                while(tmpRandom==randomReps[0]){
                    tmpRandom = (int) (Math.random() * 2.9)+1;
                }
                randomReps[1] = tmpRandom;
                tmpRandom = (int) (Math.random() * 2.9)+1;
                while(tmpRandom==randomReps[0] || tmpRandom==randomReps[1]){
                    tmpRandom = (int) (Math.random() * 2.9)+1;
                }
                randomReps[2] = tmpRandom;

                String tmpIdRep1 = "";
                String tmpIdRep2 = "";
                String tmpIdRep3 = "";

                //rep1
                if(randomReps[0] == 1){
                    tmpIdRep1 = id_reponse1;
                }
                else if(randomReps[0] == 2){
                    tmpIdRep1 = id_reponse2;
                }
                else{
                    tmpIdRep1 = id_reponse3;
                }

                //rep2
                if(randomReps[1] == 1){
                    tmpIdRep2 = id_reponse1;
                }
                else if(randomReps[1] == 2){
                    tmpIdRep2 = id_reponse2;
                }
                else{
                    tmpIdRep2 = id_reponse3;
                }

                //rep3
                if(randomReps[2] == 1){
                    tmpIdRep3 = id_reponse1;
                }
                else if(randomReps[2] == 2){
                    tmpIdRep3 = id_reponse2;
                }
                else{
                    tmpIdRep3 = id_reponse3;
                }

                rep1.setImageResource(getResources().getIdentifier(tmpIdRep1, "mipmap", getPackageName()));
                rep1Id = tmpIdRep1;
                rep2.setImageResource(getResources().getIdentifier(tmpIdRep2, "mipmap", getPackageName()));
                rep2Id = tmpIdRep2;
                rep3.setImageResource(getResources().getIdentifier(tmpIdRep3, "mipmap", getPackageName()));
                rep3Id = tmpIdRep3;

                response = flagList.get(currentQuestion).get("response");

                if(response.equals("response1")){
                    response1 = true;
                }
                else{
                    response1 = false;
                }
                if(response.equals("response2")){
                    response2 = true;
                }
                else{
                    response2 = false;
                }
                if(response.equals("response3")){
                    response3 = true;
                }
                else{
                    response3 = false;
                }
            }
            tmp++;

            rep1X = screenWidth + 20;
            rep1Y = 0;

            rep2X = screenWidth + 20;
            rep2Y = frameHeight *5/11;

            rep3X = screenWidth + 20;
            rep3Y = frameHeight *9/10;
        }

        // rep3
        rep3X -= rep3Speed;

        // rep1
        rep1X -= rep1Speed;

        rep2.setX(rep2X);
        rep2.setY(rep2Y);

        rep1.setX(rep1X);
        rep1.setY(rep1Y);

        rep3.setX(rep3X);
        rep3.setY(rep3Y);


        // Move Box
        if (action_flg == true) {
            // Touching
            boxY -= boxSpeed;

        } else {
            // Releasing
            boxY += boxSpeed;
        }

        // Check box position.
        if (boxY < 0) boxY = 0;

        if (boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;

        box.setY(boxY);

        scoreLabel.setText("Score : " + score);

    }


    public void goodAnswer(){
        score += 10;
        rep2X = screenWidth;
        rep3X = screenWidth;
        rep1X = screenWidth;
        sound.playHitSound();

        currentQuestion = (int)(Math.random() * (flagList.size()));

        id_question = flagList.get(currentQuestion).get("id");
        question.setText(id_question);

        id_reponse1 = flagList.get(currentQuestion).get("response1");
        id_reponse2 = flagList.get(currentQuestion).get("response2");
        id_reponse3 = flagList.get(currentQuestion).get("response3");

        int[] randomReps = new int[3];
        randomReps[0] = (int) (Math.random() * 2.9)+1;
        int tmpRandom = (int) (Math.random() * 2.9)+1;
        while(tmpRandom==randomReps[0]){
            tmpRandom = (int) (Math.random() * 2.9)+1;
        }
        randomReps[1] = tmpRandom;
        tmpRandom = (int) (Math.random() * 2.9)+1;
        while(tmpRandom==randomReps[0] || tmpRandom==randomReps[1]){
            tmpRandom = (int) (Math.random() * 2.9)+1;
        }
        randomReps[2] = tmpRandom;

        String tmpIdRep1 = "";
        String tmpIdRep2 = "";
        String tmpIdRep3 = "";

        //rep1
        if(randomReps[0] == 1){
            tmpIdRep1 = id_reponse1;
        }
        else if(randomReps[0] == 2){
            tmpIdRep1 = id_reponse2;
        }
        else{
            tmpIdRep1 = id_reponse3;
        }

        //rep2
        if(randomReps[1] == 1){
            tmpIdRep2 = id_reponse1;
        }
        else if(randomReps[1] == 2){
            tmpIdRep2 = id_reponse2;
        }
        else{
            tmpIdRep2 = id_reponse3;
        }

        //rep3
        if(randomReps[2] == 1){
            tmpIdRep3 = id_reponse1;
        }
        else if(randomReps[2] == 2){
            tmpIdRep3 = id_reponse2;
        }
        else{
            tmpIdRep3 = id_reponse3;
        }

        rep1.setImageResource(getResources().getIdentifier(tmpIdRep1, "mipmap", getPackageName()));
        rep1Id = tmpIdRep1;
        rep2.setImageResource(getResources().getIdentifier(tmpIdRep2, "mipmap", getPackageName()));
        rep2Id = tmpIdRep2;
        rep3.setImageResource(getResources().getIdentifier(tmpIdRep3, "mipmap", getPackageName()));
        rep3Id = tmpIdRep3;

        response = flagList.get(currentQuestion).get("response");

        if(response.equals("response1")){
            response1 = true;
        }
        else{
            response1 = false;
        }
        if(response.equals("response2")){
            response2 = true;
        }
        else{
            response2 = false;
        }
        if(response.equals("response3")){
            response3 = true;
        }
        else{
            response3 = false;
        }
    }

    public void badAnswer(){
        nbVies = nbVies - 1;

        if (nbVies == 3) {
            vie1.setVisibility(View.VISIBLE);
            vie2.setVisibility(View.VISIBLE);
            vie3.setVisibility(View.VISIBLE);
            rep2X = screenWidth;
            rep3X = screenWidth;
            rep1X = screenWidth;
        } else if (nbVies == 2) {
            vie1.setVisibility(View.INVISIBLE);
            vie2.setVisibility(View.VISIBLE);
            vie3.setVisibility(View.VISIBLE);
            rep2X = screenWidth;
            rep3X = screenWidth;
            rep1X = screenWidth;
        } else if (nbVies == 1) {
            vie1.setVisibility(View.INVISIBLE);
            vie2.setVisibility(View.INVISIBLE);
            vie3.setVisibility(View.VISIBLE);
            rep2X = screenWidth;
            rep3X = screenWidth;
            rep1X = screenWidth;
        } else {
            vie1.setVisibility(View.INVISIBLE);
            vie2.setVisibility(View.INVISIBLE);
            vie3.setVisibility(View.INVISIBLE);

            //Stop Timer!!
            timer.cancel();
            timer = null;


            // Show Result
            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("SCORE", score);
            intent.putExtra("LANGUAGE", language);
            startActivity(intent);
        }
        sound.playOverSound();

        if(nbVies !=0){

            currentQuestion = (int)(Math.random() * (flagList.size()));

            id_question = flagList.get(currentQuestion).get("id");
            question.setText(id_question);

            id_reponse1 = flagList.get(currentQuestion).get("response1");
            id_reponse2 = flagList.get(currentQuestion).get("response2");
            id_reponse3 = flagList.get(currentQuestion).get("response3");

            int[] randomReps = new int[3];
            randomReps[0] = (int) (Math.random() * 2.9)+1;
            int tmpRandom = (int) (Math.random() * 2.9)+1;
            while(tmpRandom==randomReps[0]){
                tmpRandom = (int) (Math.random() * 2.9)+1;
            }
            randomReps[1] = tmpRandom;
            tmpRandom = (int) (Math.random() * 2.9)+1;
            while(tmpRandom==randomReps[0] || tmpRandom==randomReps[1]){
                tmpRandom = (int) (Math.random() * 2.9)+1;
            }
            randomReps[2] = tmpRandom;

            String tmpIdRep1 = "";
            String tmpIdRep2 = "";
            String tmpIdRep3 = "";

            //rep1
            if(randomReps[0] == 1){
                tmpIdRep1 = id_reponse1;
            }
            else if(randomReps[0] == 2){
                tmpIdRep1 = id_reponse2;
            }
            else{
                tmpIdRep1 = id_reponse3;
            }

            //rep2
            if(randomReps[1] == 1){
                tmpIdRep2 = id_reponse1;
            }
            else if(randomReps[1] == 2){
                tmpIdRep2 = id_reponse2;
            }
            else{
                tmpIdRep2 = id_reponse3;
            }

            //rep3
            if(randomReps[2] == 1){
                tmpIdRep3 = id_reponse1;
            }
            else if(randomReps[2] == 2){
                tmpIdRep3 = id_reponse2;
            }
            else{
                tmpIdRep3 = id_reponse3;
            }

            rep1.setImageResource(getResources().getIdentifier(tmpIdRep1, "mipmap", getPackageName()));
            rep1Id = tmpIdRep1;
            rep2.setImageResource(getResources().getIdentifier(tmpIdRep2, "mipmap", getPackageName()));
            rep2Id = tmpIdRep2;
            rep3.setImageResource(getResources().getIdentifier(tmpIdRep3, "mipmap", getPackageName()));
            rep3Id = tmpIdRep3;

            response = flagList.get(currentQuestion).get("response");

            if(response.equals("response1")){
                response1 = true;
            }
            else{
                response1 = false;
            }
            if(response.equals("response2")){
                response2 = true;
            }
            else{
                response2 = false;
            }
            if(response.equals("response3")){
                response3 = true;
            }
            else{
                response3 = false;
            }
        }


    }

    public void hitCheck() {

        // HITBOX


        // rep2

        int rep2Right = rep2X + rep2.getWidth();
        int rep2Left = rep2X;

        int rep2Bottom = rep2Y + rep2.getHeight();
        int rep2Top = rep2Y;

        boolean hit2 = false;


        if(0 <= rep2Left && rep2Left <= boxSize+50 && tmp!=0){

            ArrayList<Integer> rep2WidthTab = new ArrayList<Integer>();
            int tmpLeft = rep2Left;

            while(tmpLeft <= rep2Right){
                rep2WidthTab.add(tmpLeft);
                tmpLeft+=1;
            }

            ArrayList<Integer> rep2HeightTab = new ArrayList<Integer>();
            int tmpTop = rep2Top;
            while(tmpTop <= rep2Bottom){
                rep2HeightTab.add(tmpTop);
                tmpTop+=1;
            }

            for(int i=0; i < rep2WidthTab.size(); i++){
                if (0 <= rep2WidthTab.get(i) && rep2WidthTab.get(i) <= boxSize+50) {
                    for(int j=0; j < rep2HeightTab.size(); j++){
                        if(boxY+20 <= rep2HeightTab.get(j) && rep2HeightTab.get(j) <= boxY + boxSize-20){
                            hit2 = true;
                        }
                    }
                }
            }
            if(hit2){
                if(flagList.get(currentQuestion).get("response1") == rep2Id && response1){
                    goodAnswer();
                    if(score !=0 && score%50 == 0 && score < 300){
                        rep2Speed += 1;
                        rep3Speed += 1;
                        rep1Speed += 1;
                    }
                }
                else if(flagList.get(currentQuestion).get("response2") == rep2Id && response2){
                    goodAnswer();
                    if(score !=0 && score%50 == 0 && score < 300){
                        rep2Speed += 1;
                        rep3Speed += 1;
                        rep1Speed += 1;
                    }
                }
                else if(flagList.get(currentQuestion).get("response3") == rep2Id && response3){
                    goodAnswer();
                    if(score !=0 && score%50 == 0 && score < 300){
                        rep2Speed += 1;
                        rep3Speed += 1;
                        rep1Speed += 1;
                    }
                }
                else{
                    badAnswer();
                }
            }
        }


        // rep3
        int rep3Right = rep3X + rep3.getWidth();
        int rep3Left = rep3X;

        int rep3Bottom = rep3Y + rep3.getHeight();
        int rep3Top = rep3Y;

        boolean hit3 = false;


        if(0 <= rep3Left && rep3Left <= boxSize+50 && tmp!=0){

            ArrayList<Integer> rep3WidthTab = new ArrayList<Integer>();
            int tmpLeft = rep3Left;

            while(tmpLeft <= rep3Right){
                rep3WidthTab.add(tmpLeft);
                tmpLeft+=1;
            }

            ArrayList<Integer> rep3HeightTab = new ArrayList<Integer>();
            int tmpTop = rep3Top;
            while(tmpTop <= rep3Bottom){
                rep3HeightTab.add(tmpTop);
                tmpTop+=1;
            }

            for(int i=0; i < rep3WidthTab.size(); i++){
                if (0 <= rep3WidthTab.get(i) && rep3WidthTab.get(i) <= boxSize+50) {
                    for(int j=0; j < rep3HeightTab.size(); j++){
                        if(boxY+20 <= rep3HeightTab.get(j) && rep3HeightTab.get(j) <= boxY + boxSize-20){
                            hit3 = true;
                        }
                    }
                }
            }
            if(hit3){
                if(flagList.get(currentQuestion).get("response1") == rep3Id && response1){
                    goodAnswer();
                    if(score !=0 && score%50 == 0 && score < 300){
                        rep2Speed += 1;
                        rep3Speed += 1;
                        rep1Speed += 1;
                    }
                }
                else if(flagList.get(currentQuestion).get("response2") == rep3Id && response2){
                    goodAnswer();
                    if(score !=0 && score%50 == 0 && score < 300){
                        rep2Speed += 1;
                        rep3Speed += 1;
                        rep1Speed += 1;
                    }
                }
                else if(flagList.get(currentQuestion).get("response3") == rep3Id && response3){
                    goodAnswer();
                    if(score !=0 && score%50 == 0 && score < 300){
                        rep2Speed += 1;
                        rep3Speed += 1;
                        rep1Speed += 1;
                    }
                }
                else{
                    badAnswer();
                }
            }
        }


        // rep1
        int rep1Right = rep1X + rep1.getWidth();
        int rep1Left = rep1X;

        int rep1Bottom = rep1Y + rep1.getHeight();
        int rep1Top = rep1Y;

        boolean hit1 = false;


        if(0 <= rep1Left && rep1Left <= boxSize+50 && tmp!=0){

            ArrayList<Integer> rep1WidthTab = new ArrayList<Integer>();
            int tmpLeft = rep1Left;

            while(tmpLeft <= rep1Right){
                rep1WidthTab.add(tmpLeft);
                tmpLeft+=1;
            }

            ArrayList<Integer> rep1HeightTab = new ArrayList<Integer>();
            int tmpTop = rep1Top;
            while(tmpTop <= rep1Bottom){
                rep1HeightTab.add(tmpTop);
                tmpTop+=1;
            }

            for(int i=0; i < rep1WidthTab.size(); i++){
                if (0 <= rep1WidthTab.get(i) && rep1WidthTab.get(i) <= boxSize+50) {
                    for(int j=0; j < rep1HeightTab.size(); j++){
                        if(boxY+20 <= rep1HeightTab.get(j) && rep1HeightTab.get(j) <= boxY + boxSize-20){
                            hit1 = true;
                        }
                    }
                }
            }
            if(hit1){
                if(flagList.get(currentQuestion).get("response1") == rep1Id && response1){
                    goodAnswer();
                    if(score !=0 && score%50 == 0 && score < 300){
                        rep2Speed += 1;
                        rep3Speed += 1;
                        rep1Speed += 1;
                    }
                }
                else if(flagList.get(currentQuestion).get("response2") == rep1Id && response2){
                    goodAnswer();
                    if(score !=0 && score%50 == 0 && score < 300){
                        rep2Speed += 1;
                        rep3Speed += 1;
                        rep1Speed += 1;
                    }
                }
                else if(flagList.get(currentQuestion).get("response3") == rep1Id && response3){
                    goodAnswer();
                    if(score !=0 && score%50 == 0 && score < 300){
                        rep2Speed += 1;
                        rep3Speed += 1;
                        rep1Speed += 1;
                    }
                }
                else{
                    badAnswer();
                }
            }
        }
    }


    public boolean onTouchEvent(MotionEvent me) {

        if (start_flg == false) {

            start_flg = true;

            // Why get frame height and box height here?
            // Because the UI has not been set on the screen in OnCreate()!!

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int)box.getY();

            // The box is a square.(height and width are the same.)
            boxSize = box.getHeight();


            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);


        } else {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;

            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }

        return true;
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

    //Charge le fichier .json des questions
    public String loadJSONFromAsset() {
        String json = null;
        try {

            String jsonName = "questionsFr.json";

            if(language != 0){
                jsonName = "questionsEn.json";
            }

            InputStream is = getAssets().open(jsonName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

}