package com.example.abhigrith.ui.fragments.orphanage;

import android.content.SharedPreferences;
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

import com.example.abhigrith.databinding.FragmentOrphanageLoginBinding;
import com.example.abhigrith.models.OrphanageModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;

public class OrphanageLoginFragment extends Fragment {

    private static final String TAG = "OrphanageLogin";
    private static final String APP_PREFERENCES = "APP-PREFERENCES";
    private static final String ORPHANAGE_ID = "orphanageId";
    private static final String ORPHANAGE_COLLECTION_NAME = "orphanage_info";

    private FragmentOrphanageLoginBinding binding;
    private FirebaseFirestore firestoreInstance;
    private CollectionReference orphanageInfoCollection;

    private NavController navController;
    private NavDirections actionOrphanageRegistration;
    private NavDirections actionOrphanageDashboard;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        firestoreInstance = FirebaseFirestore.getInstance();
        orphanageInfoCollection = firestoreInstance.collection(ORPHANAGE_COLLECTION_NAME);

        binding = FragmentOrphanageLoginBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        actionOrphanageRegistration = OrphanageLoginFragmentDirections.actionOrphanageLoginFragmentToOrphanageRegisterFragment();
        actionOrphanageDashboard = OrphanageLoginFragmentDirections.actionOrphanageLoginFragmentToOrphanageDashboardFragment();

        binding.btnOrphanageLoginRegister.setOnClickListener(v -> navController.navigate(actionOrphanageRegistration));

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }

    private void doesOrphanageExistsAndGetOrphanageLoggedIn(String orphanageId, String password) {
        // Check whether orphanageId and password exist or not
        orphanageInfoCollection.document(orphanageId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            OrphanageModel model = document.toObject(OrphanageModel.class);
                            if (model != null) {
                                if (!password.equals(model.getOrphanagePassword())) {
                                    Toast.makeText(requireActivity().getApplicationContext(),"You have entered the wrong password.Please enter the correct password.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                SharedPreferences preferences = requireActivity().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
                                preferences.edit().putString(ORPHANAGE_ID,model.getOrphanageId()).apply();

                                Toast.makeText(requireActivity().getApplicationContext(),"Getting you logged in inside the app.", Toast.LENGTH_SHORT).show();
                                navController.navigate(actionOrphanageDashboard);
                            }
                        } else {
                            Log.d(TAG, "No such document");
                            Toast.makeText(requireActivity().getApplicationContext(),"Your orphanage account doesn't exist.Please get your orphanage registered on the app.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                        Toast.makeText(requireActivity().getApplicationContext(),"There was some issue while login.So please try to login again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkWhetherAllFieldsAreFilledAndCorrect(String orphanageId, String password) {
        if (orphanageId.isEmpty() && password.isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "Your Orphanage Id and Password fields are empty.Please fill them and then proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (orphanageId.isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "Your Orphanage Id field is empty.Please fill it and then proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(requireActivity().getApplicationContext(), "Your Password field is empty.Please fill it and then proceed", Toast.LENGTH_SHORT).show();
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