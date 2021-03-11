package com.example.abhigrith;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.abhigrith.Parent.ParentDashboardActivity;
import com.example.abhigrith.Parent.ParentLoginActivity;
import com.example.abhigrith.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnSplashSkip.setOnClickListener(v -> {
            Intent intent = new Intent(this, ParentLoginActivity.class);
            startActivity(intent);
        });
    }
}