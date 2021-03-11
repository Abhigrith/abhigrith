package com.example.abhigrith.Parent;

import androidx.appcompat.app.AppCompatActivity;
import com.example.abhigrith.R;
import com.example.abhigrith.databinding.ActivityParentLoginBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ParentLoginActivity extends AppCompatActivity {

    private ActivityParentLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        binding = ActivityParentLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnLogin.setOnClickListener(v -> {

        });

        binding.btnParentLoginSkip.setOnClickListener(v ->{
            Intent intent = new Intent(this, ParentDashboardActivity.class);
            startActivity(intent);
        });

        binding.btnParentLoginRegister.setOnClickListener(v ->{
            Intent intent = new Intent(this, ParentRegisterActivity.class);
            startActivity(intent);
        });
    }
}