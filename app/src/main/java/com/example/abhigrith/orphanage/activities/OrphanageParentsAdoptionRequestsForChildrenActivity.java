package com.example.abhigrith.orphanage.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.abhigrith.R;
import com.example.abhigrith.adoption.models.ChildModel;
import com.example.abhigrith.databinding.ActivityOrphanageParentsAdoptionRequestsForChildrenBinding;
import com.example.abhigrith.enums.Gender;
import com.example.abhigrith.orphanage.adapters.OrphanageParentsChildrenRequestsAdapter;
import com.example.abhigrith.parent.models.ParentsDetailModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class OrphanageParentsAdoptionRequestsForChildrenActivity extends AppCompatActivity {

    private static final String TAG = "ParentsAdoptionRequests";

    private static final String APP_SHARED_PREFERENCES = "APP-PREFERENCES";
    private static final String ORPHANAGE_ID = "orphanageId";
    private static final String CHILD_MODEL = "childData";
    private static final String ORPHANAGE_COLLECTION_PATH = "orphanage_info";
    private static final String CHILDREN_COLLECTION_PATH = "children_info";

    private static final String PARENT_REQUESTED_CHILDREN = "adoptionRequestForChildren";
    private static final String PARENTS_FOR_CHILDREN = "childParentRequestForAdoption";

    private ActivityOrphanageParentsAdoptionRequestsForChildrenBinding binding;
    private OrphanageParentsChildrenRequestsAdapter adapter;

    private FirebaseFirestore firestore;
    private List<DocumentReference> parentsDocuments;
    private DocumentReference orphanageDocReference;
    private ListenerRegistration orphanageDocListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrphanageParentsAdoptionRequestsForChildrenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getSharedPreferences(APP_SHARED_PREFERENCES, MODE_PRIVATE);
        String emailId = preferences.getString(ORPHANAGE_ID, null);

        ChildModel child = getIntent().getParcelableExtra(CHILD_MODEL);

        if (emailId != null && child != null) {
            binding.cvChildItemData.tvParentAdoptionRequestChildFullName.setText(child.getChildFullName());
            binding.cvChildItemData.tvParentAdoptionRequestChildDateOfBirth.setText(child.getChildDateOfBirth());
            binding.cvChildItemData.tvParentAdoptionRequestChildGender.setText(child.getChildGender());

            if (child.getChildGender().equals(Gender.MALE.getGender())) {
                Glide.with(this)
                        .load(child.getChildImageUrl())
                        .error(R.drawable.male_child_profile)
                        .into(binding.cvChildItemData.ivParentAdoptionRequestChildProfilePic);
            } else {
                Glide.with(this)
                        .load(child.getChildImageUrl())
                        .error(R.drawable.female_child_profile)
                        .into(binding.cvChildItemData.ivParentAdoptionRequestChildProfilePic);
            }

            orphanageDocReference = firestore.collection(ORPHANAGE_COLLECTION_PATH).document(emailId).collection(CHILDREN_COLLECTION_PATH).document(child.getChildId());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (orphanageDocReference != null) {
            orphanageDocListener = orphanageDocReference.addSnapshotListener((snapshot, error) -> {

                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());

                    parentsDocuments = (List<DocumentReference>) snapshot.get(PARENTS_FOR_CHILDREN);

                    if (parentsDocuments != null) {
                        Log.d(TAG, "99 : onComplete : " + parentsDocuments.toString());
                        ArrayList<ParentsDetailModel> parents = new ArrayList<ParentsDetailModel>();

                        for (DocumentReference parentDocument : parentsDocuments) {
                            parentDocument.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot snapshot) {
                                    ParentsDetailModel child = snapshot.toObject(ParentsDetailModel.class);
                                    parents.add(child);
                                    adapter.notifyItemInserted(parents.size() - 1);
                                    Log.d(TAG, "109 : onSuccess: " + parents.toString());
                                }
                            });
                        }

                        setupRequestedParentListForAdoption(parents);
                        Log.d(TAG, "96 : onSuccess: " + parents.toString());
                    }
                } else {
                    Log.d(TAG, "No such document or Current data: null");
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (orphanageDocListener != null) {
            orphanageDocListener.remove();
        }
    }

    private void setupRequestedParentListForAdoption(List<ParentsDetailModel> parents) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.rvActivityOrphanageParentAdoptionRequestsListForChild.setLayoutManager(linearLayoutManager);

        adapter = new OrphanageParentsChildrenRequestsAdapter(parents);
        binding.rvActivityOrphanageParentAdoptionRequestsListForChild.setAdapter(adapter);
    }
}