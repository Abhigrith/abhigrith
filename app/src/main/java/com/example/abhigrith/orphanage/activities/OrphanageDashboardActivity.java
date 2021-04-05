package com.example.abhigrith.orphanage.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.abhigrith.databinding.ActivityOrphanageDashboardBinding;

public class OrphanageDashboardActivity extends AppCompatActivity {

    private ActivityOrphanageDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrphanageDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}