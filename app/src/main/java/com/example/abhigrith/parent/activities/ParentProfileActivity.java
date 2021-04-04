package com.example.abhigrith.parent.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.abhigrith.databinding.ActivityParentProfileBinding;
import com.example.abhigrith.enums.AdoptionRequestStatus;
import com.example.abhigrith.parent.models.ParentIndividualModel;
import com.example.abhigrith.parent.models.ParentsAddressModel;
import com.example.abhigrith.parent.models.ParentsDetailModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ParentProfileActivity extends AppCompatActivity {

    private static final String TAG = "ParentProfile";
    private static final String APP_SHARED_PREFERENCES = "APP-PREFERENCES";
    private static final String PARENT_EMAIL_ID = "parent-email-id";
    private static final String COLLECTION_PATH = "parents_info";

    private FirebaseFirestore firestoreInstance;
    private CollectionReference parentInfoCollection;
    private ActivityParentProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firestoreInstance = FirebaseFirestore.getInstance();
        parentInfoCollection = firestoreInstance.collection(COLLECTION_PATH);

        SharedPreferences preferences = getSharedPreferences(APP_SHARED_PREFERENCES, MODE_PRIVATE);
        String emailId = preferences.getString(PARENT_EMAIL_ID, "null");
        Log.d(TAG, "onCreate: " + emailId);

        setParentProfile(emailId);

        binding.btnParentProfileSaveChanges.setOnClickListener(v -> {
            if(!checkWhetherAllFieldsAreFilledAndCorrect()){
               return;
            }

            // If all the fields are properly filled by users
            ParentsDetailModel parentsDetailModel = getParentInfoData();
            updateParentData(parentsDetailModel);
        });
    }

    private void setParentProfile(String email) {
        // Check whether email and password exist or not
        parentInfoCollection.document(email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            ParentsDetailModel model = document.toObject(ParentsDetailModel.class);
                            if (model != null) {
                                setParentProfileData(model);
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                        Toast.makeText(ParentProfileActivity.this, "There was some issue to fetch the data.So please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setParentProfileData(ParentsDetailModel model) {
        ParentIndividualModel parent1 = model.getFirstParent();
        ParentIndividualModel parent2 = model.getSecondParent();
        ParentsAddressModel addressModel = model.getAddress();

        binding.tvParentProfileName01.setText(capitalize(parent1.getFullName()));
        binding.tvParentProfileGender01.setText(capitalize(parent1.getGender()));
        binding.tvParentProfileDob01.setText(parent1.getDateOfBirth());
        binding.tvParentProfileIncome01.setText(String.valueOf(parent1.getIncome()));
        binding.tvParentProfileAadhaarNumber01.setText(parent1.getAadhaarCardNumber());
        binding.tvParentProfilePanNumber01.setText(parent1.getPanCardNumber());

        binding.tvParentProfileName02.setText(capitalize(parent2.getFullName()));
        binding.tvParentProfileGender02.setText(capitalize(parent2.getGender()));
        binding.tvParentProfileDob02.setText(parent2.getDateOfBirth());
        binding.tvParentProfileIncome02.setText(String.valueOf(parent2.getIncome()));
        binding.tvParentProfileAadhaarNumber02.setText(parent2.getAadhaarCardNumber());
        binding.tvParentProfilePanNumber02.setText(parent2.getPanCardNumber());

        binding.etParentProfilePrimaryAddress.setText(capitalize(addressModel.getPrimaryAddress()));
        binding.etParentProfileSecondaryAddress.setText(capitalize(addressModel.getSecondaryAddress()));
        binding.etParentProfileAddressCity.setText(capitalize(addressModel.getCity()));
        binding.etParentProfileAddressDistrict.setText(capitalize(addressModel.getDistrict()));
        binding.etParentProfileAddressPincode.setText(addressModel.getPincode());
        binding.etParentProfileAddressState.setText(capitalize(addressModel.getState()));

        binding.tvParentProfileEmail.setText(model.getEmailAddress());
        binding.etParentProfilePrimaryPhone.setText(model.getPrimaryContactNumber());
        binding.etParentProfileSecondaryPhone.setText(model.getSecondaryContactNumber());
        binding.tvParentProfileEligibilityStatus.setText(model.getAdoptionRequestStatus());
    }

    private void updateParentData(ParentsDetailModel parentsDetailModel) {
        parentInfoCollection
                .document(parentsDetailModel.getEmailAddress())
                .set(parentsDetailModel)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    Toast.makeText(ParentProfileActivity.this, "The changes have been applied to your profile section.", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error writing document", e);
                    Toast.makeText(ParentProfileActivity.this, "There was some problem occurred while updating your ptofile.So kindly please try again", Toast.LENGTH_SHORT).show();
                });
    }

    private ParentsDetailModel getParentInfoData() {

        ParentIndividualModel parentOne = new ParentIndividualModel(
                binding.tvParentProfileName01.getText().toString().trim(),
                binding.tvParentProfileGender01.getText().toString().trim(),
                binding.tvParentProfileDob01.getText().toString().trim(),
                Float.parseFloat(binding.tvParentProfileIncome01.getText().toString().trim()),
                binding.tvParentProfileAadhaarNumber01.getText().toString().trim(),
                binding.tvParentProfilePanNumber01.getText().toString().trim()
        );
        ParentIndividualModel parentTwo = new ParentIndividualModel(
                binding.tvParentProfileName02.getText().toString().trim(),
                binding.tvParentProfileGender02.getText().toString().trim(),
                binding.tvParentProfileDob02.getText().toString().trim(),
                Float.parseFloat(binding.tvParentProfileIncome02.getText().toString().trim()),
                binding.tvParentProfileAadhaarNumber02.getText().toString().trim(),
                binding.tvParentProfilePanNumber02.getText().toString().trim()
        );
        ParentsAddressModel parentAddress = new ParentsAddressModel(
                binding.etParentProfilePrimaryAddress.getText().toString().trim(),
                binding.etParentProfileSecondaryAddress.getText().toString().trim(),
                binding.etParentProfileAddressCity.getText().toString().trim(),
                binding.etParentProfileAddressDistrict.getText().toString().trim(),
                binding.etParentProfileAddressPincode.getText().toString().trim(),
                binding.etParentProfileAddressState.getText().toString().trim()
        );

        String primaryContactNumber = binding.etParentProfilePrimaryPhone.getText().toString().trim();
        String secondaryContactNumber = binding.etParentProfileSecondaryPhone.getText().toString().trim();
        String email = binding.tvParentProfileEmail.getText().toString().trim().toLowerCase();
        String password = binding.etParentProfilePrimaryPhone.getText().toString().trim();
        String adoptionRequestStatus = AdoptionRequestStatus.PENDING.getAdoptionStatus();

        return new ParentsDetailModel(parentOne, parentTwo, parentAddress, primaryContactNumber, secondaryContactNumber, email, password, adoptionRequestStatus);
    }

    private boolean checkWhetherAllFieldsAreFilledAndCorrect() {
        if (binding.etParentProfilePrimaryAddress.getText().toString().isEmpty()) {
            Toast.makeText(ParentProfileActivity.this, "Your Primary-Address field is empty.Please fill the Primary-Address field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentProfileSecondaryAddress.getText().toString().isEmpty()) {
            Toast.makeText(ParentProfileActivity.this, "Your Secondary-Address field is empty.Please fill the Secondary-Address field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentProfileAddressCity.getText().toString().isEmpty()) {
            Toast.makeText(ParentProfileActivity.this, "Your City field is empty.Please fill the City field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentProfileAddressDistrict.getText().toString().isEmpty()) {
            Toast.makeText(ParentProfileActivity.this, "Your District field is empty.Please fill the District field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentProfileAddressPincode.getText().toString().isEmpty()) {
            Toast.makeText(ParentProfileActivity.this, "Your Pincode field is empty.Please fill the Pincode field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentProfileAddressState.getText().toString().isEmpty()) {
            Toast.makeText(ParentProfileActivity.this, "Your State field is empty.Please fill the State field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentProfilePrimaryPhone.getText().toString().isEmpty()) {
            Toast.makeText(ParentProfileActivity.this, "Your Primary-Phone-Number field is empty.Please fill the Primary-Phone-Number field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentProfileSecondaryPhone.getText().toString().isEmpty()) {
            Toast.makeText(ParentProfileActivity.this, "Your Secondary-Phone-Number field is empty.Please fill the Secondary-Phone-Number field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Else evrything else is correct
        return true;
    }

    private String capitalize(String fieldValue) {
        return fieldValue.substring(0, 1).toUpperCase() + fieldValue.substring(1).toLowerCase();
    }
}