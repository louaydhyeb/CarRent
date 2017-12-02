package com.esprit.carrent.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

import com.esprit.carrent.R;

public class Splashscreen extends Activity {
    private static int SPLASH_TIME_OUT = 6000;
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    ImageView img ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        img =  (ImageView)findViewById(R.id.imageCar);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent LoginIntent = new Intent (getBaseContext(),LoginActivity.class);
                startActivity(LoginIntent);
                finish();

            }
        },SPLASH_TIME_OUT);
    }}