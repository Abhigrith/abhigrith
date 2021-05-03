package com.example.abhigrith.orphanage.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.abhigrith.adoption.models.ChildModel;
import com.example.abhigrith.databinding.ActivityOrphanageChildrenListBinding;
import com.example.abhigrith.orphanage.adapters.OrphanageChildrenListAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class OrphanageChildrenListActivity extends AppCompatActivity {

    private static final String TAG = "AdoptionRequests";
    private static final String APP_PREFERENCES = "APP-PREFERENCES";
    private static final String ORPHANAGE_ID = "orphanageId";
    private static final String ORPHANAGE_COLLECTION_NAME = "orphanage_info";
    private static final String CHILD_COLLECTION_NAME = "children_info";

    private ActivityOrphanageChildrenListBinding binding;
    private OrphanageChildrenListAdapter adapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrphanageChildrenListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
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
    protected void onStart() {
        super.onStart();

        if(adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(adapter != null) {
            adapter.stopListening();
        }
    }

    private void setupOrphanageChildrenList(FirestoreRecyclerOptions<ChildModel> options) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        binding.rvOrphanageChildrenList.setLayoutManager(linearLayoutManager);

        adapter = new OrphanageChildrenListAdapter(options);
        binding.rvOrphanageChildrenList.setAdapter(adapter);
    }
}