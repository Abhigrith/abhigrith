package com.example.abhigrith.parent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.abhigrith.adoption.activities.OrphanageListActivity;
import com.example.abhigrith.databinding.ActivityParentDashboardBinding;

public class ParentDashboardActivity extends AppCompatActivity {

    private ActivityParentDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.toolbarProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ParentProfileActivity.class);
            startActivity(intent);
        });

        binding.btnDashboardSelection.setOnClickListener(v ->{
            Intent intent = new Intent(this, OrphanageListActivity.class);
            startActivity(intent);
        });

        binding.btnDashboardSelector.setOnClickListener(v ->{
            Intent intent = new Intent(this,ParentAdoptionRequestActivity.class);
            startActivity(intent);
        });
    }
}