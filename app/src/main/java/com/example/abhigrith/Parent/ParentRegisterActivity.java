package com.example.abhigrith.Parent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.abhigrith.databinding.ActivityParentRegisterBinding;

public class ParentRegisterActivity extends AppCompatActivity {

    private ActivityParentRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}