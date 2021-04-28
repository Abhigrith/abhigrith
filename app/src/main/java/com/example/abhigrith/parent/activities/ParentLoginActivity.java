package com.example.abhigrith.parent.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.abhigrith.R;
import com.example.abhigrith.databinding.ActivityParentLoginBinding;
import com.example.abhigrith.parent.models.ParentsDetailModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ParentLoginActivity extends AppCompatActivity {

    private static final String TAG = "ParentLogin";
    private static final String APP_SHARED_PREFERENCES = "APP-PREFERENCES";
    private static final String PARENT_EMAIL_ID = "parent-email-id";
    private static final String COLLECTION_PATH = "parents_info";

    private ActivityParentLoginBinding binding;
    private FirebaseFirestore firestoreInstance;
    private CollectionReference parentInfoCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        binding = ActivityParentLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firestoreInstance = FirebaseFirestore.getInstance();
        parentInfoCollection = firestoreInstance.collection(COLLECTION_PATH);

        binding.btnParentLoginAction.setOnClickListener(v -> {
            String email = getEmail();
            String password = getPassword();

            boolean areAllFieldsFilled = checkWhetherAllFieldsAreFilledAndCorrect(email,password);
            if(!areAllFieldsFilled){
                return;
            }

            // And then allow to login them
            doesUserExistsAndGetUserLoggedIn(email,password);

//            SharedPreferences preferences = getSharedPreferences("APP-PREFERENCES",MODE_PRIVATE);
//            String id = preferences.getString("parent-email-id","null");
//            Log.d(TAG, "onCreate: " + id);
        });

        binding.btnParentLoginRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, ParentRegisterActivity.class);
            startActivity(intent);
        });
    }

    private void doesUserExistsAndGetUserLoggedIn(String email,String password){
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
                                    Toast.makeText(ParentLoginActivity.this,"You have entered the wrong password.Please enter the correct password.",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                SharedPreferences preferences = getSharedPreferences(APP_SHARED_PREFERENCES,MODE_PRIVATE);
                                preferences.edit().putString(PARENT_EMAIL_ID,model.getEmailAddress()).apply();

                                Toast.makeText(ParentLoginActivity.this,"Getting you logged in inside the app.",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ParentLoginActivity.this,ParentDashboardActivity.class));
                            }
                        } else {
                            Log.d(TAG, "No such document");
                            Toast.makeText(ParentLoginActivity.this,"You are not registered on this app.Please get yourself registered first to login",Toast.LENGTH_SHORT).show();
                            // startActivity(new Intent(ParentLoginActivity.this,ParentRegisterActivity.class));
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                        Toast.makeText(ParentLoginActivity.this,"There was some issue while login.So please try to login again.",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkWhetherAllFieldsAreFilledAndCorrect(String email, String password){
        if(email.isEmpty() && password.isEmpty()){
            Toast.makeText(ParentLoginActivity.this,"Your Email and Password fields are empty.Please fill them and then proceed",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.isEmpty()){
            Toast.makeText(ParentLoginActivity.this,"Your Email field is empty.Please fill it and then proceed",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isEmailValid(email)){
            Toast.makeText(ParentLoginActivity.this,"You have entered an invalid email.Please provide your valid email and then proceed and then proceed",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.isEmpty()){
            Toast.makeText(ParentLoginActivity.this,"Your Password field is empty.Please fill it and then proceed",Toast.LENGTH_SHORT).show();
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