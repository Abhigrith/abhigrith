package com.example.abhigrith.ui.fragments.adoption;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.abhigrith.databinding.FragmentAdoptionOrphanageListBinding;
import com.example.abhigrith.models.OrphanageModel;
import com.example.abhigrith.ui.adapters.AdoptionOrphanageListAdapter;
import com.example.abhigrith.util.interfaces.OnDocumentCheckListener;
import com.example.abhigrith.util.interfaces.OnListItemClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdoptionOrphanageListFragment extends Fragment implements OnListItemClickListener, OnDocumentCheckListener {

    private static final String TAG = "OrphanageList";
    private static final String ORPHANAGE_COLLECTION_PATH = "orphanage_info";

    private FragmentAdoptionOrphanageListBinding binding;
    private FirebaseFirestore firestore;
    private FirestoreRecyclerOptions<OrphanageModel> options;
    private AdoptionOrphanageListAdapter adapter;

    private NavController navController;
    private NavDirections actionChildrenList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        firestore = FirebaseFirestore.getInstance();

        binding = FragmentAdoptionOrphanageListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        setupOrphanageList();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        adapter = null;
    }

    private void setupOrphanageList() {
        Query query = firestore.collection(ORPHANAGE_COLLECTION_PATH);
        Log.d(TAG, "This is " + query.toString());

        options = new FirestoreRecyclerOptions.Builder<OrphanageModel>()
                .setQuery(query, OrphanageModel.class)
                .build();

        Log.d(TAG, "These are some events " + options.getSnapshots().toString());

        adapter = new AdoptionOrphanageListAdapter(this, this, options);
        binding.rvActivityOrphanageListOrphanages.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        binding.rvActivityOrphanageListOrphanages.setAdapter(adapter);
    }

    @Override
    public void onListItemClick(int position) {
        if (!options.getSnapshots().isEmpty() && options.getSnapshots().getSnapshot(position).exists()) {
            String orphanageDocumentName = options.getSnapshots().getSnapshot(position).getId();
            Log.d(TAG, "OrphanageDocumentName: " + orphanageDocumentName);
            String orphanageName = (String) options.getSnapshots().getSnapshot(position).get("orphanageName");
            Log.d(TAG, "OrphanageName: " + orphanageName);

            actionChildrenList = AdoptionOrphanageListFragmentDirections.actionAdoptionOrphanageListFragmentToAdoptionOrphanageChildrenListFragment(orphanageDocumentName,orphanageName);

            navController.navigate(actionChildrenList);
        } else {
            Toast.makeText(requireActivity().getApplicationContext(), "There was some problem in fetching the data of this orphanage", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void documentsDonotExist() {
        Snackbar.make(binding.llOrphanageList, "No orphanages are listed yet.", Snackbar.LENGTH_LONG)
                .show();
    }
}