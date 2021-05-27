package com.example.abhigrith.ui.fragments.parents;

import android.content.Context;
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

import com.example.abhigrith.databinding.FragmentParentLoginBinding;
import com.example.abhigrith.models.ParentsDetailModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ParentLoginFragment extends Fragment {

    private static final String TAG = "ParentLogin";
    private static final String APP_SHARED_PREFERENCES = "APP-PREFERENCES";
    private static final String PARENT_EMAIL_ID = "parent-email-id";
    private static final String COLLECTION_PATH = "parents_info";

    private FragmentParentLoginBinding binding;
    private FirebaseFirestore firestoreInstance;
    private CollectionReference parentInfoCollection;

    private NavController navController;
    private NavDirections actionParentRegistration;
    private NavDirections actionParentDashboard;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        firestoreInstance = FirebaseFirestore.getInstance();
        parentInfoCollection = firestoreInstance.collection(COLLECTION_PATH);

        binding = FragmentParentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        actionParentRegistration = ParentLoginFragmentDirections.actionParentLoginFragmentToParentRegisterFragment();
        actionParentDashboard = ParentLoginFragmentDirections.actionParentLoginFragmentToParentDashboardFragment();

        binding.btnParentLoginAction.setOnClickListener(v -> {
            String email = getEmail();
            String password = getPassword();

            boolean areAllFieldsFilled = checkWhetherAllFieldsAreFilledAndCorrect(email,password);
            if(!areAllFieldsFilled){
                return;
            }

            // And then allow to login them
            doesUserExistsAndGetUserLoggedIn(email,password);
        });

        binding.btnParentLoginRegister.setOnClickListener(v -> {
            navController.navigate(actionParentRegistration);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }

    private void doesUserExistsAndGetUserLoggedIn(String email, String password){
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
                                if(!password.equals(model.getPassword())){
                                    Toast.makeText(requireActivity().getApplicationContext(),"You have entered the wrong password.Please enter the correct password.",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                SharedPreferences preferences = requireContext().getApplicationContext().getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE);
                                preferences.edit().putString(PARENT_EMAIL_ID,model.getEmailAddress()).apply();

                                Toast.makeText(requireActivity().getApplicationContext(),"Getting you logged in inside the app.",Toast.LENGTH_SHORT).show();
                                navController.navigate(actionParentDashboard);
                            }
                        } else {
                            Log.d(TAG, "No such document");
                            Toast.makeText(requireActivity().getApplicationContext(),"You are not registered on this app.Please get yourself registered first to login",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                        Toast.makeText(requireActivity().getApplicationContext(),"There was some issue while login.So please try to login again.",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkWhetherAllFieldsAreFilledAndCorrect(String email, String password){
        if(email.isEmpty() && password.isEmpty()){
            Toast.makeText(requireActivity().getApplicationContext(),"Your Email and Password fields are empty.Please fill them and then proceed",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.isEmpty()){
            Toast.makeText(requireActivity().getApplicationContext(),"Your Email field is empty.Please fill it and then proceed",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isEmailValid(email)){
            Toast.makeText(requireActivity().getApplicationContext(),"You have entered an invalid email.Please provide your valid email and then proceed and then proceed",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.isEmpty()){
            Toast.makeText(requireActivity().getApplicationContext(),"Your Password field is empty.Please fill it and then proceed",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Methods to get email and password from user
    private String getEmail(){
        return binding.etParentLoginEmail.getText().toString().trim().toLowerCase();
    }

    private String getPassword(){
        return binding.etParentLoginPassword.getText().toString().trim();
    }
}