package com.example.abhigrith;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.abhigrith.databinding.ActivityDashboardBinding;
import com.example.abhigrith.orphanage.activities.OrphanageLoginActivity;
import com.example.abhigrith.parent.activities.ParentLoginActivity;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnDashboardOrganization.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrphanageLoginActivity.class);
            startActivity(intent);
        });

        binding.btnDashboardParent.setOnClickListener(v -> {
            Intent intent = new Intent(this, ParentLoginActivity.class);
            startActivity(intent);
        });
    }
}