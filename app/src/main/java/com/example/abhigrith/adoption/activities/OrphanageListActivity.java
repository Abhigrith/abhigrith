package com.example.abhigrith.adoption.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.abhigrith.adoption.adapters.OrphanageListAdapter;
import com.example.abhigrith.adoption.interfaces.OnDocumentCheckListener;
import com.example.abhigrith.adoption.interfaces.OnRecyclerViewItemClickListener;
import com.example.abhigrith.databinding.ActivityOrphanageListBinding;
import com.example.abhigrith.orphanage.models.OrphanageModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class OrphanageListActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener, OnDocumentCheckListener {

    private static final String TAG = "OrphanageList";
    private static final String ORPHANAGE_COLLECTION_PATH = "orphanage_info";
    private static final String ORPHANAGE_DOCUMENT_NAME = "ORPHANAGE_DOCUMENT_NAME";
    private static final String ORPHANAGE_NAME_FIELD = "orphanageName";

    private ActivityOrphanageListBinding binding;
    private FirebaseFirestore firestore;
    private FirestoreRecyclerOptions<OrphanageModel> options;
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

        setupOrphanageList();
    }

    private void setupOrphanageList() {
        Query query = firestore.collection(ORPHANAGE_COLLECTION_PATH);
        Log.d(TAG, "This is " + query.toString());

        options = new FirestoreRecyclerOptions.Builder<OrphanageModel>()
                .setQuery(query, OrphanageModel.class)
                .build();

        Log.d(TAG, "These are some events " + options.getSnapshots().toString());

        adapter = new OrphanageListAdapter(this, this, options);
        binding.rvActivityOrphanageListOrphanages.setLayoutManager(new LinearLayoutManager(OrphanageListActivity.this));
        binding.rvActivityOrphanageListOrphanages.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onOrphanageItemClick(int position) {
        if (!options.getSnapshots().isEmpty() && options.getSnapshots().getSnapshot(position).exists()) {
            String orphanageDocumentName = options.getSnapshots().getSnapshot(position).getId();
            Log.d(TAG, "OrphanageDocumentName: " + orphanageDocumentName);
            String orphanageName = (String) options.getSnapshots().getSnapshot(position).get("orphanageName");
            Log.d(TAG, "OrphanageName: " + orphanageName);

            Intent intent = new Intent(OrphanageListActivity.this, OrphanageChildrenListActivity.class);
            intent.putExtra(ORPHANAGE_DOCUMENT_NAME, orphanageDocumentName);
            intent.putExtra(ORPHANAGE_NAME_FIELD,orphanageName);
            startActivity(intent);
        } else {
            Toast.makeText(this, "There was some problem in fetching the data of this orphanage", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void documentsDonotExist() {
        Snackbar.make(binding.llOrphanageList, "No orphanages are listed yet.", Snackbar.LENGTH_INDEFINITE)
                .setAction("Back", v -> finish())
                .show();
    }
}