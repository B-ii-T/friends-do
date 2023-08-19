package com.bit.friendsdo;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        LottieAnimationView animationView = findViewById(R.id.animated_logo);
        TextView textView = findViewById(R.id.label_textview);
        ImageView bitLogo = findViewById(R.id.bit_logo);

        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                // Animation started
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // Animation ended, navigate to the next screen
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity if needed
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Animation canceled
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // Animation repeated
            }
        });

        // Create an ObjectAnimator to fade in the TextView
        ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);
        ObjectAnimator fadeInAnimatorImg = ObjectAnimator.ofFloat(bitLogo, "alpha", 0f, 1f);
        fadeInAnimator.setDuration(1000); // Set the duration of the fade-in animation
        fadeInAnimatorImg.setDuration(1000); // Set the duration of the fade-in animation
        fadeInAnimator.setStartDelay(300); // Set a delay before the animation starts
        fadeInAnimatorImg.setStartDelay(800); // Set a delay before the animation starts
        fadeInAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                // Make the TextView visible when the animation starts
                textView.setVisibility(View.VISIBLE);
                bitLogo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // Animation ended, no additional action needed
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Animation canceled, no additional action needed
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // Animation repeated, no additional action needed
            }
        });
        fadeInAnimator.start(); // Start the animation
        fadeInAnimatorImg.start(); // Start the animation
    }
}
