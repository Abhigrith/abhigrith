package com.example.abhigrith.adoption.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.abhigrith.adoption.adapters.AdoptionOrphanageChildrenListAdapter;
import com.example.abhigrith.adoption.interfaces.OnDocumentCheckListener;
import com.example.abhigrith.adoption.interfaces.OnRecyclerViewItemClickListener;
import com.example.abhigrith.adoption.models.ChildModel;
import com.example.abhigrith.databinding.ActivityAdoptionOrphanageChildrenListBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdoptionOrphanageChildrenListActivity extends AppCompatActivity implements OnDocumentCheckListener, OnRecyclerViewItemClickListener {

    private static final String TAG = "OrphanageChildrenList";
    private static final String ORPHANAGE_COLLECTION_PATH = "orphanage_info";
    private static final String ORPHANAGE_DOCUMENT_NAME = "ORPHANAGE_DOCUMENT_NAME";
    private static final String ORPHANAGE_NAME_FIELD = "orphanageName";
    private static final String ORPHANAGE_CHILDREN_COLLECTION_PATH = "children_info";
    private static final String CHILD_DETAIL_MODEL = "CHILD_MODEL";

    private ActivityAdoptionOrphanageChildrenListBinding binding;
    private FirebaseFirestore firestore;
    private FirestoreRecyclerOptions<ChildModel> options;
    private AdoptionOrphanageChildrenListAdapter adapter;

    private String orphanageDocumentName;
    private String orphanageName;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdoptionOrphanageChildrenListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        orphanageDocumentName = bundle.getString(ORPHANAGE_DOCUMENT_NAME, "null");
        orphanageName = bundle.getString(ORPHANAGE_NAME_FIELD, "null");

        if (!orphanageDocumentName.equals("null") && !orphanageName.equals("null")) {
            setupOrphanageList(orphanageDocumentName);
            // binding.tvOrphanageChildrenListActivityTitle.setText(getString(R.string.orphanage_title, orphanageName));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void documentsDonotExist() {
        Snackbar.make(binding.llOrphanageChildrenList, "This " + orphanageName + " orphanage has not listed there childen.\nPlease look for some other orphanages.", Snackbar.LENGTH_INDEFINITE)
                .setAction("Back", v -> finish())
                .show();
    }

    @Override
    public void onListItemClick(int position) {
        ChildModel child = (ChildModel) adapter.getItem(position);
        Log.d(TAG, "Child : " + child.toString());

        Bundle bundle = new Bundle();
        bundle.putParcelable(CHILD_DETAIL_MODEL,child);
        bundle.putString(ORPHANAGE_DOCUMENT_NAME,orphanageDocumentName);

        Intent intent = new Intent(AdoptionOrphanageChildrenListActivity.this, AdoptionOrphanageChildDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setupOrphanageList(String orphanageDocumentName) {
        Query query = firestore.collection(ORPHANAGE_COLLECTION_PATH).document(orphanageDocumentName).collection(ORPHANAGE_CHILDREN_COLLECTION_PATH);
        Log.d(TAG, "This is " + query.toString());

        options = new FirestoreRecyclerOptions.Builder<ChildModel>()
                .setQuery(query, ChildModel.class)
                .build();

        Log.d(TAG, "These are some events " + options.getSnapshots().toString());

        adapter = new AdoptionOrphanageChildrenListAdapter(this, this, options);
        binding.rvOrphanageChildrenListActivityList.setLayoutManager(new LinearLayoutManager(AdoptionOrphanageChildrenListActivity.this));
        binding.rvOrphanageChildrenListActivityList.setAdapter(adapter);
    }
}