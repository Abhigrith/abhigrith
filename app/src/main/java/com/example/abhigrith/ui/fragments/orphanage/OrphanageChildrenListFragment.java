package com.example.abhigrith.ui.fragments.orphanage;

import android.content.SharedPreferences;
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

import com.example.abhigrith.databinding.FragmentOrphanageChildrenListBinding;
import com.example.abhigrith.models.ChildModel;
import com.example.abhigrith.ui.adapters.OrphanageChildrenListAdapter;
import com.example.abhigrith.util.interfaces.OnChildItemClickListener;
import com.example.abhigrith.util.interfaces.OnListItemClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static android.content.Context.MODE_PRIVATE;

public class OrphanageChildrenListFragment extends Fragment implements OnChildItemClickListener {

    private static final String TAG = "AdoptionRequests";
    private static final String APP_PREFERENCES = "APP-PREFERENCES";
    private static final String ORPHANAGE_ID = "orphanageId";
    private static final String ORPHANAGE_COLLECTION_NAME = "orphanage_info";
    private static final String CHILD_COLLECTION_NAME = "children_info";

    private FragmentOrphanageChildrenListBinding binding;
    private OrphanageChildrenListAdapter adapter;
    private FirebaseFirestore firestore;

    private NavController navController;
    private NavDirections actionParentAdoptionRequests;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        firestore = FirebaseFirestore.getInstance();

        binding = FragmentOrphanageChildrenListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        SharedPreferences preferences = requireActivity().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        String orphanageId = preferences.getString(ORPHANAGE_ID,null);

        Log.d(TAG, "onCreate: " + orphanageId);

        if(orphanageId != null && !orphanageId.isEmpty()) {
            Query query = firestore.collection(ORPHANAGE_COLLECTION_NAME).document(orphanageId).collection(CHILD_COLLECTION_NAME);

            FirestoreRecyclerOptions<ChildModel> options = new FirestoreRecyclerOptions.Builder<ChildModel>()
                    .setQuery(query, ChildModel.class)
                    .build();

            Log.d(TAG, "onCreate: " + options.getSnapshots().size());

            setupOrphanageChildrenList(options);
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
    }

    private void setupOrphanageChildrenList(FirestoreRecyclerOptions<ChildModel> options) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        binding.rvOrphanageChildrenList.setLayoutManager(linearLayoutManager);

        adapter = new OrphanageChildrenListAdapter(options, this);
        binding.rvOrphanageChildrenList.setAdapter(adapter);
    }

    @Override
    public void onChildItemClick(ChildModel child) {
        actionParentAdoptionRequests = OrphanageChildrenListFragmentDirections.actionOrphanageChildrenListFragmentToOrphanageParentsAdoptionRequestsForChildrenFragment(child);

        navController.navigate(actionParentAdoptionRequests);

        Toast.makeText(requireActivity().getApplicationContext(), "You are currently viewing child : " + child.getChildFullName(), Toast.LENGTH_LONG).show();
    }
}