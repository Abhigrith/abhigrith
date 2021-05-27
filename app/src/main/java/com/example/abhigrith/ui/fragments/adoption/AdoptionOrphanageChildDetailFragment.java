package com.example.abhigrith.ui.fragments.adoption;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.abhigrith.R;
import com.example.abhigrith.databinding.FragmentAdoptionOrphanageChildDetailBinding;
import com.example.abhigrith.models.ChildModel;
import com.example.abhigrith.util.enums.Gender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;

public class AdoptionOrphanageChildDetailFragment extends Fragment {

    private static final String TAG = "OrphanChild";
    private static final String CHILD_DETAIL_MODEL = "CHILD_MODEL";
    private static final String ORPHANAGE_DOCUMENT_NAME = "ORPHANAGE_DOCUMENT_NAME";
    private static final String APP_SHARED_PREFERENCES = "APP-PREFERENCES";
    private static final String PARENT_DOCUMENT_NAME = "parent-email-id";

    private FragmentAdoptionOrphanageChildDetailBinding binding;
    private FirebaseFirestore firestore;
    private DocumentReference parentDocRef;
    private DocumentReference childDocRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        firestore = FirebaseFirestore.getInstance();

        binding = FragmentAdoptionOrphanageChildDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = requireActivity().getSharedPreferences(APP_SHARED_PREFERENCES, MODE_PRIVATE);

        Bundle bundle = getArguments();
        AdoptionOrphanageChildDetailFragmentArgs args = AdoptionOrphanageChildDetailFragmentArgs.fromBundle(bundle);

        ChildModel child = args.getChild();
        String orphanageDocumentName = args.getOrphanageDocumentName();

        String parentDocumentName = preferences.getString(PARENT_DOCUMENT_NAME, "null");
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }

    private void setChildDetailData(ChildModel child) {
        if (child.getChildGender().equals(Gender.MALE.getGender())) {
            Glide.with(AdoptionOrphanageChildDetailFragment.this)
                    .load(child.getChildImageUrl())
                    .error(R.drawable.male_child_profile)
                    .into(binding.ivOrphanageChildDetailProfileImage);
        } else {
            Glide.with(AdoptionOrphanageChildDetailFragment.this)
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
        childDocRef.update("childParentRequestForAdoption", FieldValue.arrayUnion(parentDocRef));
    }
}