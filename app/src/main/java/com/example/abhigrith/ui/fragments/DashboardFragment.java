package com.example.abhigrith.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abhigrith.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private NavController navController;
    private NavDirections actionOrphanage;
    private NavDirections actionParent;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        actionParent = DashboardFragmentDirections.actionDashboardFragmentToParentLoginFragment();
        actionOrphanage = DashboardFragmentDirections.actionDashboardFragmentToOrphanageLoginFragment();

        binding.btnDashboardOrganization.setOnClickListener(v -> {
            navController.navigate(actionOrphanage);
        });

        binding.btnDashboardParent.setOnClickListener(v -> {
            navController.navigate(actionParent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}