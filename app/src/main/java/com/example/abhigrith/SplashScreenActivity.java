package com.example.abhigrith;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.abhigrith.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashScreenBinding binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        int secondsDelayed = 3;
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreenActivity.this, DashboardActivity.class));
            finish();
        }, secondsDelayed * 1000);
    }
}