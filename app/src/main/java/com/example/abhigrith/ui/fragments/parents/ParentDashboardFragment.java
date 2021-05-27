package com.example.abhigrith.ui.fragments.parents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.abhigrith.databinding.FragmentParentDashboardBinding;

public class ParentDashboardFragment extends Fragment {

    private FragmentParentDashboardBinding binding;

    private NavController navController;
    private NavDirections actionParentAdoptionRequests;
    private NavDirections actionParentProfile;
    private NavDirections actionOrphanageList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentParentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        actionParentAdoptionRequests = ParentDashboardFragmentDirections.actionParentDashboardFragmentToParentAdoptionRequestsListFragment();
        actionParentProfile = ParentDashboardFragmentDirections.actionParentDashboardFragmentToParentProfileFragment();
        actionOrphanageList = ParentDashboardFragmentDirections.actionParentDashboardFragmentToAdoptionOrphanageListFragment();

        binding.toolbar.toolbarProfile.setOnClickListener(v -> {
            navController.navigate(actionParentProfile);
        });

        binding.btnDashboardSelection.setOnClickListener(v ->{
           navController.navigate(actionOrphanageList);
        });

        binding.btnDashboardSelector.setOnClickListener(v ->{
            navController.navigate(actionParentAdoptionRequests);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}