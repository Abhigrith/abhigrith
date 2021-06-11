package com.example.abhigrith.orphanage.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.abhigrith.databinding.ActivityOrphanageLoginBinding;
import com.example.abhigrith.orphanage.models.OrphanageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class OrphanageLoginActivity extends AppCompatActivity {

    private static final String TAG = "OrphanageLogin";
    private static final String APP_PREFERENCES = "APP-PREFERENCES";
    private static final String ORPHANAGE_ID = "orphanageId";
    private static final String ORPHANAGE_NAME = "orphanageName";
    private static final String ORPHANAGE_COLLECTION_NAME = "orphanage_info";

    private ActivityOrphanageLoginBinding binding;
    private FirebaseFirestore firestoreInstance;
    private CollectionReference orphanageInfoCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrphanageLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestoreInstance = FirebaseFirestore.getInstance();
        orphanageInfoCollection = firestoreInstance.collection(ORPHANAGE_COLLECTION_NAME);

        binding.btnOrphanageLoginRegister.setOnClickListener(v -> {
            startActivity(new Intent(OrphanageLoginActivity.this, OrphanageRegisterActivity.class));
        });

        binding.btnOrphanageLoginAction.setOnClickListener(v -> {
            String orphanageId = getOrphanageId();
            String password = getOrphanagePassword();

            boolean areAllFieldsFilled = checkWhetherAllFieldsAreFilledAndCorrect(orphanageId, password);
            if (!areAllFieldsFilled) {
                return;
            }

            doesOrphanageExistsAndGetOrphanageLoggedIn(orphanageId, password);
        });
    }

    private void doesOrphanageExistsAndGetOrphanageLoggedIn(String orphanageId, String password) {
        // Check whether orphanageId and password exist or not
        orphanageInfoCollection.document(orphanageId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                OrphanageModel model = document.toObject(OrphanageModel.class);
                                if (model != null) {
                                    if (!password.equals(model.getOrphanagePassword())) {
                                        Toast.makeText(OrphanageLoginActivity.this, "You have entered the wrong password.Please enter the correct password.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
                                    preferences.edit().putString(ORPHANAGE_ID,model.getOrphanageId()).putString(ORPHANAGE_NAME,model.getOrphanageName()).apply();

                                    Toast.makeText(OrphanageLoginActivity.this, "Getting you logged in inside the app.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(OrphanageLoginActivity.this, OrphanageDashboardActivity.class));
                                }
                            } else {
                                Log.d(TAG, "No such document");
                                Toast.makeText(OrphanageLoginActivity.this, "Your orphanage account doesn't exist.Please get your orphanage registered on the app.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            Toast.makeText(OrphanageLoginActivity.this, "There was some issue while login.So please try to login again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean checkWhetherAllFieldsAreFilledAndCorrect(String orphanageId, String password) {
        if (orphanageId.isEmpty() && password.isEmpty()) {
            Toast.makeText(OrphanageLoginActivity.this, "Your Orphanage Id and Password fields are empty.Please fill them and then proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (orphanageId.isEmpty()) {
            Toast.makeText(OrphanageLoginActivity.this, "Your Orphanage Id field is empty.Please fill it and then proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(OrphanageLoginActivity.this, "Your Password field is empty.Please fill it and then proceed", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Methods to get email and password from user
    private String getOrphanageId() {
        return binding.etOrphanageLoginId.getText().toString().trim();
    }

    private String getOrphanagePassword() {
        return binding.etOrphanageLoginPassword.getText().toString().trim();
    }
}