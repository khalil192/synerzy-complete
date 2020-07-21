package com.example.synerzy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.VideoView;

public class Splash_Screen extends AppCompatActivity {

    VideoView video2;
    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        ConstraintLayout layout1=(ConstraintLayout) findViewById(R.id.layout);
        AnimationDrawable animationDrawable=(AnimationDrawable) layout1.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onBoardingScreen = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                boolean isFirstTIme = onBoardingScreen.getBoolean( "firsttime", true);

                if(isFirstTIme) {

                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firsttime",false);
                    editor.commit();
                    Intent i = new Intent(Splash_Screen.this, Onboarding.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Intent i = new Intent(Splash_Screen.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        },5000);


    }
}
