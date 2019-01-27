package com.example.hp.akura;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class Logo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        EasySplashScreen config = new EasySplashScreen(Logo.this)
        .withFullScreen()
        .withTargetActivity( LogOrReg .class )
        .withSplashTimeOut( 5000 );

        //Set to view
        View view=config.create();

        //Set view to content view
     //  setContentView(view);


    }
}
