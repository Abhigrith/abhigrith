package com.example.abhigrith.adoption.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.example.abhigrith.adoption.adapters.OrphanageListAdapter;
import com.example.abhigrith.databinding.ActivityOrphanageListBinding;
import com.example.abhigrith.orphanage.models.OrphanageModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class OrphanageListActivity extends AppCompatActivity {

    private static final String TAG = "OrphanageList";
    private static final String ORPAHANGE_COLLECTION_PATH = "orphanage_info";

    private ActivityOrphanageListBinding binding;
    private FirebaseFirestore firestore;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrphanageListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        Query query = firestore.collection(ORPAHANGE_COLLECTION_PATH);
        Log.d(TAG,"This is " + query.toString());

        FirestoreRecyclerOptions<OrphanageModel> options = new FirestoreRecyclerOptions.Builder<OrphanageModel>()
                .setQuery(query, OrphanageModel.class)
                .build();

        Log.d(TAG,"These are some events " + options.getSnapshots().toString());

        setupOrphanageList(options);
    }

    private void setupOrphanageList(FirestoreRecyclerOptions<OrphanageModel> options) {
        adapter = new OrphanageListAdapter(options);
        binding.rvActivityOrphanageListOrphanages.setLayoutManager(new LinearLayoutManager(OrphanageListActivity.this));
        binding.rvActivityOrphanageListOrphanages.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}