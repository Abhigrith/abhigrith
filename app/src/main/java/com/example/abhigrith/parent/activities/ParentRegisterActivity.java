package com.example.abhigrith.parent.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.abhigrith.R;
import com.example.abhigrith.databinding.ActivityParentRegisterBinding;
import com.example.abhigrith.enums.AdoptionRequestStatus;
import com.example.abhigrith.parent.models.ParentIndividualModel;
import com.example.abhigrith.parent.models.ParentsAddressModel;
import com.example.abhigrith.parent.models.ParentsDetailModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class ParentRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PARENT_REGISTER";
    private static final String COLLECTION_PATH = "parents_info";
    private static final String COLLECTION_FIELD = "emailAddress";
    private static final Calendar myCalendar = Calendar.getInstance();

    private ActivityParentRegisterBinding binding;
    private FirebaseFirestore firestoreDb;
    private CollectionReference parentsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firestoreDb = FirebaseFirestore.getInstance();
        parentsCollection = firestoreDb.collection(COLLECTION_PATH);

        binding.etParentRegisterDateOfBirth01.setOnClickListener(this);
        binding.etParentRegisterDateOfBirth02.setOnClickListener(this);

        binding.btnParentRegisterLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, ParentLoginActivity.class);
            startActivity(intent);
        });

        binding.btnParentRegisterAction.setOnClickListener(v -> {
            boolean areAllFieldsFilledAndCorrect = checkWhetherAllFieldsAreFilledAndCorrect();
            // If all the fields are not properly filled by users
            if (!areAllFieldsFilledAndCorrect) {
                return;
            }

            // If all the fields are properly filled by users
            ParentsDetailModel parentsDetailModel = getParentInfoData();
            checkWhetherUserIsAlreadyRegistered(parentsDetailModel);
        });
    }

    private void registerDataOfParent(ParentsDetailModel parentsDetailModel) {
        parentsCollection
                .document(parentsDetailModel.getEmailAddress())
                .set(parentsDetailModel)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    Toast.makeText(ParentRegisterActivity.this, "Your account have been registered with us.Redirecting you to login page.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ParentRegisterActivity.this, ParentLoginActivity.class));
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error writing document", e);
                    Toast.makeText(ParentRegisterActivity.this, "There was some problem occurred in getting you registered.So Please Try Again.", Toast.LENGTH_SHORT).show();
                });
    }

    private void checkWhetherUserIsAlreadyRegistered(ParentsDetailModel parentsDetailModel) {
        parentsCollection.whereEqualTo(COLLECTION_FIELD, parentsDetailModel.getEmailAddress())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    ParentsDetailModel parent = document.toObject(ParentsDetailModel.class);
                                    if (parentsDetailModel.getEmailAddress().equals(parent.getEmailAddress())) {
                                        Toast.makeText(ParentRegisterActivity.this, "Your account already exists.Please try to login again with the previous credentials.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }

                            registerDataOfParent(parentsDetailModel);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(ParentRegisterActivity.this, "There was problem occurred in getting you registered.So Please Try Again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private boolean checkWhetherAllFieldsAreFilledAndCorrect() {
        if (binding.etParentRegisterName01.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "In first parent your Fullname field is empty.Please fill the Fullname field in first parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterGender01.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "In first parent your Gender field is empty.Please fill the Gender field in first parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterDateOfBirth01.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "In first parent your Date-Of-Birth field is empty.Please fill the Date-Of-Birth field in first parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterIncome01.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "In first parent your Income field is empty.Please fill the Income field in first parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterAadhaarNumber01.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "In first parent your Aadhaar-Card field is empty.Please fill the Aadhaar-Card field in first parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterPanNumber01.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "In first parent your Pan-Card field is empty.Please fill the Pan-Card field in first parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterName02.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "In second parent your Fullname field is empty.Please fill the Fullname field in second parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterGender02.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "In second parent your Gender field is empty.Please fill the Gender field in second parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterDateOfBirth02.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "In second parent your Date-Of-Birth field is empty.Please fill the Date-Of-Birth field in second parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterIncome02.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "In second parent your Income field is empty.Please fill the Income field in second parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterAadhaarNumber02.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "In second parent your Aadhaar-Card field is empty.Please fill the Aadhaar-Card field in second parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterPanNumber02.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "In second parent your Pan-Card field is empty.Please fill the Pan-Card field in second parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterPrimaryAddress.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "Your Primary-Address field is empty.Please fill the Primary-Address field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterSecondaryAddress.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "Your Secondary-Address field is empty.Please fill the Secondary-Address field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterAddressCity.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "Your City field is empty.Please fill the City field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterAddressDistrict.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "Your District field is empty.Please fill the District field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterAddressPincode.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "Your Pincode field is empty.Please fill the Pincode field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterAddressState.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "Your State field is empty.Please fill the State field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterPrimaryPhone.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "Your Primary-Phone-Number field is empty.Please fill the Primary-Phone-Number field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterSecondaryPhone.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "Your Secondary-Phone-Number field is empty.Please fill the Secondary-Phone-Number field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterEmail.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "Your Email field is empty.Please fill the Email field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isEmailValid(binding.etParentRegisterEmail.getText().toString())) {
            Toast.makeText(ParentRegisterActivity.this, "You have provided an Invalid Email Address.Please provide a Valid Email Address.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterPassword.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "Your Password field is empty.Please fill the Password field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etParentRegisterConfirmPassword.getText().toString().isEmpty()) {
            Toast.makeText(ParentRegisterActivity.this, "Your Confirm-Password field is empty.Please fill the Confirm-Password field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!binding.etParentRegisterPassword.getText().toString().equals(binding.etParentRegisterConfirmPassword.getText().toString())) {
            Toast.makeText(ParentRegisterActivity.this, "The Confirm Password field doesn't matches with the Password field", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Else evrything else is correct
        return true;
    }

    private ParentsDetailModel getParentInfoData() {

        ParentIndividualModel parentOne = new ParentIndividualModel(
                capitalize(binding.etParentRegisterName01.getText().toString().trim()),
                capitalize(binding.etParentRegisterGender01.getText().toString().trim()),
                capitalize(binding.etParentRegisterDateOfBirth01.getText().toString().trim()),
                Float.parseFloat(binding.etParentRegisterIncome01.getText().toString().trim()),
                binding.etParentRegisterAadhaarNumber01.getText().toString().trim(),
                capitalize(binding.etParentRegisterPanNumber01.getText().toString().trim())
        );
        ParentIndividualModel parentTwo = new ParentIndividualModel(
                capitalize(binding.etParentRegisterName02.getText().toString().trim()),
                capitalize(binding.etParentRegisterGender02.getText().toString().trim()),
                capitalize(binding.etParentRegisterDateOfBirth02.getText().toString().trim()),
                Float.parseFloat(binding.etParentRegisterIncome02.getText().toString().trim()),
                binding.etParentRegisterAadhaarNumber02.getText().toString().trim(),
                capitalize(binding.etParentRegisterPanNumber02.getText().toString().trim())
        );
        ParentsAddressModel parentAddress = new ParentsAddressModel(
                capitalize(binding.etParentRegisterPrimaryAddress.getText().toString().trim()),
                capitalize(binding.etParentRegisterSecondaryAddress.getText().toString().trim()),
                capitalize(binding.etParentRegisterAddressCity.getText().toString().trim()),
                capitalize(binding.etParentRegisterAddressDistrict.getText().toString().trim()),
                binding.etParentRegisterAddressPincode.getText().toString().trim(),
                capitalize(binding.etParentRegisterAddressState.getText().toString().trim())
        );

        String primaryContactNumber = binding.etParentRegisterPrimaryPhone.getText().toString().trim();
        String secondaryContactNumber = binding.etParentRegisterSecondaryPhone.getText().toString().trim();
        String email = binding.etParentRegisterEmail.getText().toString().trim().toLowerCase();
        String password = binding.etParentRegisterPassword.getText().toString().trim();
        String adoptionRequestStatus = AdoptionRequestStatus.PENDING.getAdoptionStatus();

        return new ParentsDetailModel(parentOne, parentTwo, parentAddress, primaryContactNumber, secondaryContactNumber, email, password, adoptionRequestStatus);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_parent_register_date_of_birth_01:
                DatePickerDialog datePicker = new DatePickerDialog(ParentRegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        binding.etParentRegisterDateOfBirth01.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

                datePicker.show();
                break;
            case R.id.et_parent_register_date_of_birth_02:
                datePicker = new DatePickerDialog(ParentRegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        binding.etParentRegisterDateOfBirth02.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

                datePicker.show();
                break;
        }
    }



    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private String capitalize(String fieldValue){
        return fieldValue.substring(0,1).toUpperCase() + fieldValue.substring(1).toLowerCase();
    }
}