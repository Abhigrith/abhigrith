package com.example.abhigrith.Parent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.abhigrith.databinding.ActivityParentRegisterBinding;

public class ParentRegisterActivity extends AppCompatActivity {

    private ActivityParentRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnParentRegisterLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, ParentLoginActivity.class);
            startActivity(intent);
        });

        binding.btnParentRegisterRegister.setOnClickListener(v -> {

        });
    }
}