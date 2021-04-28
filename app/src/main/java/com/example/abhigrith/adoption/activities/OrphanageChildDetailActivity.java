package com.example.abhigrith.adoption.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.abhigrith.R;
import com.example.abhigrith.adoption.models.ChildModel;
import com.example.abhigrith.databinding.ActivityOrphanageChildDetailBinding;
import com.example.abhigrith.enums.Gender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrphanageChildDetailActivity extends AppCompatActivity {

    private static final String TAG = "OrphanChild";
    private static final String CHILD_DETAIL_MODEL = "CHILD_MODEL";
    private static final String ORPHANAGE_DOCUMENT_NAME = "ORPHANAGE_DOCUMENT_NAME";
    private static final String APP_SHARED_PREFERENCES = "APP-PREFERENCES";
    private static final String PARENT_DOCUMENT_NAME = "parent-email-id";

    private ActivityOrphanageChildDetailBinding binding;
    private FirebaseFirestore firestore;
    private DocumentReference parentDocRef;
    private DocumentReference childDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrphanageChildDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getSharedPreferences(APP_SHARED_PREFERENCES,MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        ChildModel child = null;
        String orphanageDocumentName = null;
        if (bundle != null) {
            child = bundle.getParcelable(CHILD_DETAIL_MODEL);
            orphanageDocumentName = bundle.getString(ORPHANAGE_DOCUMENT_NAME);
        }

        if (bundle != null && child != null && orphanageDocumentName != null) {

            String parentDocumentName = preferences.getString(PARENT_DOCUMENT_NAME,"null");
            parentDocRef = firestore.collection("parents_info").document(parentDocumentName);

            childDocRef = firestore.collection("orphanage_info").document(orphanageDocumentName).collection("children_info").document(child.getChildId());

            setChildDetailData(child);

            binding.btnOrphanageChildDetailAdopt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendRequestToAdopt();
                    Snackbar.make(binding.btnOrphanageChildDetailAdopt, "Your request has been is got registered in database.\nYou will get notified soon.", Snackbar.LENGTH_INDEFINITE).show();
                }
            });
        } else {
            Snackbar.make(this, binding.btnOrphanageChildDetailAdopt, "Unable to display this child.Please look for some other child.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Back", v -> {
                        finish();
                    })
                    .show();
        }
    }

    private void setChildDetailData(ChildModel child) {
        if (child.getChildGender().equals(Gender.MALE.getGender())) {
            Glide.with(OrphanageChildDetailActivity.this)
                    .load(child.getChildImageUrl())
                    .error(R.drawable.male_child_profile)
                    .into(binding.ivOrphanageChildDetailProfileImage);
        } else {
            Glide.with(OrphanageChildDetailActivity.this)
                    .load(child.getChildImageUrl())
                    .error(R.drawable.female_child_profile)
                    .into(binding.ivOrphanageChildDetailProfileImage);
        }

        binding.tvOrphanageChildDetailFullName.setText(child.getChildFullName());
        binding.tvOrphanageChildDetailGender.setText(child.getChildGender());
        binding.tvOrphanageChildDetailDateOfBirth.setText(child.getChildDateOfBirth());
    }

    private void sendRequestToAdopt() {
        parentDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        ChildModel child = document.toObject(ChildModel.class);
                        if (child != null) {
                            Log.d(TAG, "onComplete: " + child.toString());
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        Log.d(TAG, "sendRequestToAdopt: " + childDocRef.getPath());
        parentDocRef.update("adoptionRequestForChildren", FieldValue.arrayUnion(childDocRef));
        Log.d(TAG, "sendRequestToAdopt: " + parentDocRef.getPath());
        childDocRef.update("childParentRequestForAdoption",FieldValue.arrayUnion(parentDocRef));
    }
}