package com.example.abhigrith.Parent;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.abhigrith.databinding.ActivityParentRegisterBinding;
import com.example.abhigrith.databinding.ActivityParentSelectionBinding;

public class ParentSelectionActivity extends AppCompatActivity {

    private ActivityParentSelectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentSelectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

}