package com.example.abhigrith.ui.fragments.adoption;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.abhigrith.databinding.FragmentAdoptionOrphanageChildrenListBinding;
import com.example.abhigrith.models.ChildModel;
import com.example.abhigrith.ui.adapters.AdoptionOrphanageChildrenListAdapter;
import com.example.abhigrith.util.interfaces.OnDocumentCheckListener;
import com.example.abhigrith.util.interfaces.OnListItemClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdoptionOrphanageChildrenListFragment extends Fragment implements OnDocumentCheckListener, OnListItemClickListener {

    private static final String TAG = "OrphanageChildrenList";
    private static final String ORPHANAGE_COLLECTION_PATH = "orphanage_info";
    private static final String ORPHANAGE_CHILDREN_COLLECTION_PATH = "children_info";

    private FragmentAdoptionOrphanageChildrenListBinding binding;
    private FirebaseFirestore firestore;
    private FirestoreRecyclerOptions<ChildModel> options;
    private AdoptionOrphanageChildrenListAdapter adapter;

    private String orphanageDocumentName;
    private String orphanageName;

    private NavController navController;
    private NavDirections actionChildDetail;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        firestore = FirebaseFirestore.getInstance();

        binding = FragmentAdoptionOrphanageChildrenListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        Bundle bundle = getArguments();
        AdoptionOrphanageChildrenListFragmentArgs args = AdoptionOrphanageChildrenListFragmentArgs.fromBundle(bundle);
        orphanageDocumentName = args.getOrphanageDocumentName();
        orphanageName = args.getOrphanageName();

        if (!orphanageDocumentName.equals("null") && !orphanageName.equals("null")) {
            setupOrphanageList(orphanageDocumentName);
        }
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

    @Override
    public void documentsDonotExist() {
        Snackbar.make(binding.llOrphanageChildrenList, "This " + orphanageName + " orphanage has not listed there childen.\nPlease look for some other orphanages.", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onListItemClick(int position) {
        ChildModel child = (ChildModel) adapter.getItem(position);
        Log.d(TAG, "Child : " + child.toString());

        actionChildDetail = AdoptionOrphanageChildrenListFragmentDirections.actionAdoptionOrphanageChildrenListFragmentToAdoptionOrphanageChildDetailFragment(orphanageDocumentName,child);

        navController.navigate(actionChildDetail);
    }

    private void setupOrphanageList(String orphanageDocumentName) {
        Query query = firestore.collection(ORPHANAGE_COLLECTION_PATH).document(orphanageDocumentName).collection(ORPHANAGE_CHILDREN_COLLECTION_PATH);
        Log.d(TAG, "This is " + query.toString());

        options = new FirestoreRecyclerOptions.Builder<ChildModel>()
                .setQuery(query, ChildModel.class)
                .build();

        Log.d(TAG, "These are some events " + options.getSnapshots().toString());

        adapter = new AdoptionOrphanageChildrenListAdapter(this, this, options);
        binding.rvOrphanageChildrenListActivityList.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        binding.rvOrphanageChildrenListActivityList.setAdapter(adapter);
    }
}