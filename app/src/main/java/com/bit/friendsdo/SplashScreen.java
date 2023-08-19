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
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);
        ObjectAnimator fadeInAnimatorImg = ObjectAnimator.ofFloat(bitLogo, "alpha", 0f, 1f);
        fadeInAnimator.setDuration(1000);
        fadeInAnimatorImg.setDuration(1000);
        fadeInAnimator.setStartDelay(300);
        fadeInAnimatorImg.setStartDelay(800);
        fadeInAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                textView.setVisibility(View.VISIBLE);
                bitLogo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        fadeInAnimator.start();
        fadeInAnimatorImg.start();
    }
}
