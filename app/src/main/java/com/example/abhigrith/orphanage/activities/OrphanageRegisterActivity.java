package com.example.abhigrith.orphanage.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.abhigrith.databinding.ActivityOrphanageRegisterBinding;
import com.example.abhigrith.enums.OrphanageListingStatus;
import com.example.abhigrith.orphanage.models.OrphanageAddressModel;
import com.example.abhigrith.orphanage.models.OrphanageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class OrphanageRegisterActivity extends AppCompatActivity {

    private static final String TAG = "ORPHANAGE_REGISTER";
    private static final String COLLECTION_PATH = "orphanage_info";
    private static final String COLLECTION_FIELD = "orphanageId";

    private ActivityOrphanageRegisterBinding binding;
    private FirebaseFirestore firestoreDb;
    private CollectionReference orphanageCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrphanageRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestoreDb = FirebaseFirestore.getInstance();
        orphanageCollection = firestoreDb.collection(COLLECTION_PATH);

        binding.btnOrphanageRegisterAction.setOnClickListener(v -> {
            boolean areAllFieldsFilledAndCorrect = checkWhetherAllFieldsAreFilledAndCorrect();
            if(!areAllFieldsFilledAndCorrect){
                return;
            }

            OrphanageModel orphanageModel = getOrphanageData();
            checkWhetherOrphanageIsAlreadyRegistered(orphanageModel);
        });

        binding.btnOrphanageRegisterLogin.setOnClickListener(v -> {
            Intent intent = new Intent(OrphanageRegisterActivity.this, OrphanageLoginActivity.class);
            startActivity(intent);
        });
    }

    private void registerDataOfOrphanage(OrphanageModel orphangeModel) {
        orphanageCollection
                .document(orphangeModel.getOrphanageId())
                .set(orphangeModel)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    Toast.makeText(OrphanageRegisterActivity.this, "Your account have been registered with us.Redirecting you to login page.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(OrphanageRegisterActivity.this, OrphanageLoginActivity.class));
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error writing document", e);
                    Toast.makeText(OrphanageRegisterActivity.this, "There was some problem occurred in getting you registered.So Please Try Again.", Toast.LENGTH_SHORT).show();
                });
    }

    private void checkWhetherOrphanageIsAlreadyRegistered(OrphanageModel orphanageModel) {
        orphanageCollection.whereEqualTo(COLLECTION_FIELD, orphanageModel.getOrphanageId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult() != null) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    OrphanageModel orphanage = document.toObject(OrphanageModel.class);
                                    if (orphanage.getOrphanageId().equals(orphanageModel.getOrphanageId())) {
                                        Toast.makeText(OrphanageRegisterActivity.this, "Your account already exists.Please try to login again with the previous credentials.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }

                            registerDataOfOrphanage(orphanageModel);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(OrphanageRegisterActivity.this, "There was problem occurred in getting you registered.So Please Try Again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean checkWhetherAllFieldsAreFilledAndCorrect(){
        if (binding.etOrphanageRegisterId.getText().toString().isEmpty()) {
            Toast.makeText(OrphanageRegisterActivity.this, "In first parent your Orphanage-Id field is empty.Please fill the Orphanage-Id field in first parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterNameOfOrphanage.getText().toString().isEmpty()) {
            Toast.makeText(OrphanageRegisterActivity.this, "In first parent your Fullname field is empty.Please fill the Fullname field in first parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterAddressLine01.getText().toString().isEmpty()) {
            Toast.makeText(OrphanageRegisterActivity.this, "Your Primary-Address field is empty.Please fill the Primary-Address field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterAddressLine02.getText().toString().isEmpty()) {
            Toast.makeText(OrphanageRegisterActivity.this, "Your Secondary-Address field is empty.Please fill the Secondary-Address field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterCity.getText().toString().isEmpty()) {
            Toast.makeText(OrphanageRegisterActivity.this, "Your City field is empty.Please fill the City field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterDistrict.getText().toString().isEmpty()) {
            Toast.makeText(OrphanageRegisterActivity.this, "Your District field is empty.Please fill the District field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterPincode.getText().toString().isEmpty()) {
            Toast.makeText(OrphanageRegisterActivity.this, "Your Pincode field is empty.Please fill the Pincode field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterState.getText().toString().isEmpty()) {
            Toast.makeText(OrphanageRegisterActivity.this, "Your State field is empty.Please fill the State field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterPrimaryPhone.getText().toString().isEmpty()) {
            Toast.makeText(OrphanageRegisterActivity.this, "Your Primary-Phone-Number field is empty.Please fill the Primary-Phone-Number field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterSecondaryPhone.getText().toString().isEmpty()) {
            Toast.makeText(OrphanageRegisterActivity.this, "Your Secondary-Phone-Number field is empty.Please fill the Secondary-Phone-Number field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterEmail.getText().toString().isEmpty()) {
            Toast.makeText(OrphanageRegisterActivity.this, "Your Email field is empty.Please fill the Email field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isEmailValid(binding.etOrphanageRegisterEmail.getText().toString())) {
            Toast.makeText(OrphanageRegisterActivity.this, "You have provided an Invalid Email Address.Please provide a Valid Email Address.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterPassword.getText().toString().isEmpty()) {
            Toast.makeText(OrphanageRegisterActivity.this, "Your Password field is empty.Please fill the Password field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterConfirmPassword.getText().toString().isEmpty()) {
            Toast.makeText(OrphanageRegisterActivity.this, "Your Confirm-Password field is empty.Please fill the Confirm-Password field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!binding.etOrphanageRegisterConfirmPassword.getText().toString().equals(binding.etOrphanageRegisterPassword.getText().toString())) {
            Toast.makeText(OrphanageRegisterActivity.this, "The Confirm Password field doesn't matches with the Password field", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Else evrything else is correct
        return true;
    }

    private OrphanageModel getOrphanageData(){
        return new OrphanageModel(
                binding.etOrphanageRegisterId.getText().toString(),
                capitalize(binding.etOrphanageRegisterNameOfOrphanage.getText().toString()),
                new OrphanageAddressModel(
                        binding.etOrphanageRegisterAddressLine01.getText().toString(),
                        binding.etOrphanageRegisterAddressLine02.getText().toString(),
                        capitalize(binding.etOrphanageRegisterCity.getText().toString()),
                        capitalize(binding.etOrphanageRegisterDistrict.getText().toString()),
                        capitalize(binding.etOrphanageRegisterPincode.getText().toString()),
                        capitalize(binding.etOrphanageRegisterState.getText().toString())
                ),
                binding.etOrphanageRegisterPrimaryPhone.getText().toString(),
                binding.etOrphanageRegisterSecondaryPhone.getText().toString(),
                binding.etOrphanageRegisterEmail.getText().toString().toLowerCase(),
                binding.etOrphanageRegisterPassword.getText().toString(),
                OrphanageListingStatus.PENDING.getOrphangeListingStatus()
        );
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private String capitalize(String fieldValue){
        return fieldValue.substring(0,1).toUpperCase() + fieldValue.substring(1).toLowerCase();
    }

}