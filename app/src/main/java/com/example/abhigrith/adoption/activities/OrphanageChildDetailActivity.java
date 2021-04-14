package com.example.abhigrith.adoption.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.abhigrith.R;
import com.example.abhigrith.adoption.models.ChildModel;
import com.example.abhigrith.databinding.ActivityOrphanageChildDetailBinding;
import com.example.abhigrith.enums.Gender;
import com.google.android.material.snackbar.Snackbar;

public class OrphanageChildDetailActivity extends AppCompatActivity {

    private static final String CHILD_DETAIL_MODEL = "CHILD_MODEL";
    private ActivityOrphanageChildDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrphanageChildDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        ChildModel child = null;
        if (bundle != null) {
            child = bundle.getParcelable(CHILD_DETAIL_MODEL);
        }

        if (bundle != null && child != null) {
            setChildDetailData(child);
            binding.btnOrphanageChildDetailAdopt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            Snackbar.make(this, binding.btnOrphanageChildDetailAdopt, "Unable to display this child.Please look for some other child.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Back", v -> {
                        finish();
                    })
                    .show();
        }
    }

    private void setChildDetailData(ChildModel child) {
        if (child.getChildGender().equals(Gender.MALE.getGender())) {
            Glide.with(OrphanageChildDetailActivity.this)
                    .load(child.getChildImageUrl())
                    .error(R.drawable.male_child_profile)
                    .into(binding.ivOrphanageChildDetailProfileImage);
        } else {
            Glide.with(OrphanageChildDetailActivity.this)
                    .load(child.getChildImageUrl())
                    .error(R.drawable.female_child_profile)
                    .into(binding.ivOrphanageChildDetailProfileImage);
        }

        binding.tvOrphanageChildDetailFullName.setText(child.getChildFullName());
        binding.tvOrphanageChildDetailGender.setText(child.getChildGender());
        binding.tvOrphanageChildDetailDateOfBirth.setText(child.getChildDateOfBirth());
    }
}