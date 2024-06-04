package com.example.gestiondesyndicdecoproprit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.os.Handler;




public class SplashScreen extends AppCompatActivity {
    private static final int DELAY_MILLISECONDS = 3000; // 3 seconds delay
    ImageView image;
    Animation anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //get image View by id
        image=findViewById(R.id.logo);
        //get Progress Bar
        ProgressBar bar = findViewById(R.id.progressBar);
        //get Aimation
        anim = AnimationUtils.loadAnimation(SplashScreen.this,R.anim.logo_anim);
        //
        image.startAnimation(anim);
        // Create a Handler to delay the navigation
        new  Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to navigate to the other activity
                Intent intent = new Intent(SplashScreen.this,MainActivity .class);

                // Start the new activity
                startActivity(intent);

                // Finish the current activity if needed
                finish();
            }
        }, DELAY_MILLISECONDS);
    }

}

