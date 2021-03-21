package com.example.abhigrith.parent.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.abhigrith.databinding.ActivityParentSelectorBinding;

public class ParentSelectorActivity extends AppCompatActivity {

    private ActivityParentSelectorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentSelectorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}