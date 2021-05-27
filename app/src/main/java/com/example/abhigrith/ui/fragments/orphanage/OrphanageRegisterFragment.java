package com.example.abhigrith.ui.fragments.orphanage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.abhigrith.databinding.FragmentOrphanageRegisterBinding;
import com.example.abhigrith.models.OrphanageAddressModel;
import com.example.abhigrith.models.OrphanageModel;
import com.example.abhigrith.util.enums.OrphanageListingStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class OrphanageRegisterFragment extends Fragment {

    private static final String TAG = "ORPHANAGE_REGISTER";
    private static final String COLLECTION_PATH = "orphanage_info";
    private static final String COLLECTION_FIELD = "orphanageId";

    private FragmentOrphanageRegisterBinding binding;
    private FirebaseFirestore firestoreDb;
    private CollectionReference orphanageCollection;

    private NavController navController;
    private NavDirections actionLogin;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        firestoreDb = FirebaseFirestore.getInstance();
        orphanageCollection = firestoreDb.collection(COLLECTION_PATH);

        binding = FragmentOrphanageRegisterBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        actionLogin = OrphanageRegisterFragmentDirections.actionOrphanageRegisterFragmentToOrphanageLoginFragment();

        binding.btnOrphanageRegisterAction.setOnClickListener(v -> {
            boolean areAllFieldsFilledAndCorrect = checkWhetherAllFieldsAreFilledAndCorrect();
            if(!areAllFieldsFilledAndCorrect){
                return;
            }

            OrphanageModel orphanageModel = getOrphanageData();
            checkWhetherOrphanageIsAlreadyRegistered(orphanageModel);
        });

        binding.btnOrphanageRegisterLogin.setOnClickListener(v -> navController.navigate(actionLogin));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }

    private void registerDataOfOrphanage(OrphanageModel orphangeModel) {
        orphanageCollection
                .document(orphangeModel.getOrphanageId())
                .set(orphangeModel)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    Toast.makeText(requireActivity().getApplicationContext(), "Your account have been registered with us.Redirecting you to login page.", Toast.LENGTH_LONG).show();
                    navController.navigate(actionLogin);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error writing document", e);
                    Toast.makeText(requireActivity().getApplicationContext(), "There was some problem occurred in getting you registered.So Please Try Again.", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(requireActivity().getApplicationContext(), "Your account already exists.Please try to login again with the previous credentials.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }

                            registerDataOfOrphanage(orphanageModel);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(requireActivity().getApplicationContext(), "There was problem occurred in getting you registered.So Please Try Again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean checkWhetherAllFieldsAreFilledAndCorrect(){
        if (binding.etOrphanageRegisterId.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "In first parent your Orphanage-Id field is empty.Please fill the Orphanage-Id field in first parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterNameOfOrphanage.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "In first parent your Fullname field is empty.Please fill the Fullname field in first parent to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterAddressLine01.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "Your Primary-Address field is empty.Please fill the Primary-Address field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterAddressLine02.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "Your Secondary-Address field is empty.Please fill the Secondary-Address field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterCity.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "Your City field is empty.Please fill the City field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterDistrict.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "Your District field is empty.Please fill the District field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterPincode.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "Your Pincode field is empty.Please fill the Pincode field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterState.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "Your State field is empty.Please fill the State field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterPrimaryPhone.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "Your Primary-Phone-Number field is empty.Please fill the Primary-Phone-Number field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterSecondaryPhone.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "Your Secondary-Phone-Number field is empty.Please fill the Secondary-Phone-Number field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterEmail.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "Your Email field is empty.Please fill the Email field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isEmailValid(binding.etOrphanageRegisterEmail.getText().toString())) {
            Toast.makeText(requireActivity().getApplicationContext(), "You have provided an Invalid Email Address.Please provide a Valid Email Address.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterPassword.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "Your Password field is empty.Please fill the Password field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageRegisterConfirmPassword.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "Your Confirm-Password field is empty.Please fill the Confirm-Password field to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!binding.etOrphanageRegisterConfirmPassword.getText().toString().equals(binding.etOrphanageRegisterPassword.getText().toString())) {
            Toast.makeText(requireActivity().getApplicationContext(), "The Confirm Password field doesn't matches with the Password field", Toast.LENGTH_SHORT).show();
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