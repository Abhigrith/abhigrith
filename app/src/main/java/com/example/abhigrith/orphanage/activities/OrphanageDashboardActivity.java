package com.example.abhigrith.orphanage.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.abhigrith.databinding.ActivityOrphanageDashboardBinding;

public class OrphanageDashboardActivity extends AppCompatActivity {

    private ActivityOrphanageDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrphanageDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnChildrenList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrphanageDashboardActivity.this, OrphanageChildrenListActivity.class));
            }
        });

        binding.btnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrphanageDashboardActivity.this, OrphanageAddChildActivity.class));
            }
        });
    }
}