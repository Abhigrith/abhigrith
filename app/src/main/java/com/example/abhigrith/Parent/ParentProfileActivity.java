package com.example.abhigrith.Parent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.abhigrith.databinding.ActivityParentProfileBinding;

public class ParentProfileActivity extends AppCompatActivity {

    private ActivityParentProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }
}