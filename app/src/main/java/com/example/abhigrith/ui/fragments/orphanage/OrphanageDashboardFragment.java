package com.example.abhigrith.ui.fragments.orphanage;

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

import com.example.abhigrith.databinding.FragmentOrphanageDashboardBinding;

public class OrphanageDashboardFragment extends Fragment {

    private FragmentOrphanageDashboardBinding binding;

    private NavController navController;
    private NavDirections actionChildrenList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentOrphanageDashboardBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        actionChildrenList = OrphanageDashboardFragmentDirections.actionOrphanageDashboardFragmentToOrphanageChildrenListFragment();

        binding.btnChildrenList.setOnClickListener(v -> navController.navigate(actionChildrenList));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}