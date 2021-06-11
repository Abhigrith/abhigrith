package com.example.abhigrith.orphanage.activities;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.abhigrith.R;
import com.example.abhigrith.adoption.models.ChildModel;
import com.example.abhigrith.databinding.ActivityOrphanageAddChildBinding;
import com.example.abhigrith.enums.Gender;
import com.example.abhigrith.orphanage.models.OrphanageModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class OrphanageAddChildActivity extends AppCompatActivity {

    private static final String TAG = "ADD_CHILD";
    private static final String ORPHANAGE_COLLECTION = "orphanage_info";
    private static final String CHILDREN_COLLECTION = "children_info";
    private static final String ORPHANAGE_ID = "orphanageId";
    private static final String ORPHANAGE_NAME = "orphanageName";
    private static final String CHILD_ID_FIELD = "childId";
    private static final String CHILD_IMAGE_FIELD = "childImageUrl";

    private static final String APP_PREFERENCES = "APP-PREFERENCES";

    private static final int IMAGE_PICK_CODE = 999;

    private ActivityOrphanageAddChildBinding binding;

    private FirebaseFirestore firestore;
    private CollectionReference children;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private final OrphanageModel[] orphanageModel = new OrphanageModel[0];

    private String orphanageId;
    private String orphanageName;

    private Uri imageUri = null;
    private String imageUriAsString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrphanageAddChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        orphanageId = preferences.getString(ORPHANAGE_ID, null);
        orphanageName = preferences.getString(ORPHANAGE_NAME, null);

        firestore = FirebaseFirestore.getInstance();
        if (orphanageId != null && orphanageName != null) {
            children = firestore.collection(ORPHANAGE_COLLECTION).document(orphanageId).collection(CHILDREN_COLLECTION);
        }

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        //Calender
        Calendar calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_MONTH);
        int month = calender.get(Calendar.MONTH);
        int year = calender.get(Calendar.YEAR);

        binding.btnChooseChildImg.setOnClickListener(v -> launchGallery());

        String[] genders = new String[]{Gender.MALE.getGender(), Gender.FEMALE.getGender(), Gender.OTHERS.getGender()};
        ArrayAdapter adapter = new ArrayAdapter(OrphanageAddChildActivity.this, R.layout.list_gender, genders);
        AutoCompleteTextView actv = (AutoCompleteTextView) binding.dropdownMenuGender.getEditText();
        if (actv != null) {
            actv.setAdapter(adapter);
        }

        binding.etOrphanageAddChildDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(
                        OrphanageAddChildActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String dobDay = String.valueOf(dayOfMonth);
                                String dobMonth = String.valueOf(month + 1);
                                String dobYear = String.valueOf(year);

                                if (dayOfMonth >= 1 && dayOfMonth <= 9) {
                                    dobDay = "0" + dobDay;
                                }

                                if ((month + 1) >= 1 && (month + 1) <= 9) {
                                    dobMonth = "0" + dobMonth;
                                }

                                String dob = dobDay + "-" + dobMonth + "-" + dobYear;
                                binding.etOrphanageAddChildDob.setText(dob);
                            }
                        },
                        year,
                        month,
                        day
                );

                dpd.show();
            }
        });

        binding.btnOrphanageChildAdd.setOnClickListener(v -> {
            boolean areAllFieldsFilledAndCorrect = checkWhetherAllFieldsAreFilledAndCorrect();
            if (!areAllFieldsFilledAndCorrect) {
                return;
            }

            ChildModel childModel = getChildData();
            Log.d(TAG, "onCreate: Show child data " + childModel.toString());
            checkWhetherChildIsAlreadyAdded(childModel);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            if (data != null) {
                imageUri = data.getData();

                if (imageUri != null) {
                    binding.ivOrphanageAddChildProfileImg.setImageURI(imageUri);
                    imageUriAsString = data.getDataString();
                }
            }
        }
    }

    private void addChildToOrphanage(ChildModel childModel) {
        children
                .document(childModel.getChildId())
                .set(childModel)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    Toast.makeText(OrphanageAddChildActivity.this, "Your child with child Child-ID " + childModel.getChildId() + " and child name " + childModel.getChildFullName() + " has been successfully added to the orphanage", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error writing document", e);
                    Toast.makeText(OrphanageAddChildActivity.this, "There was some problem occurred in adding the child with child Child-ID " + childModel.getChildId() + " and child name " + childModel.getChildFullName() + " to the orphanage.So please try again.", Toast.LENGTH_SHORT).show();
                });
    }

    private void checkWhetherChildIsAlreadyAdded(ChildModel childModel) {
        children.whereEqualTo(CHILD_ID_FIELD, childModel.getChildId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    ChildModel child = document.toObject(ChildModel.class);
                                    if (child.getChildId().equals(childModel.getChildId())) {
                                        Toast.makeText(OrphanageAddChildActivity.this, "This child already exists", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                }
                            }

                            addChildToOrphanage(childModel);

                            uploadImageToFirebaseStorage(childModel);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(OrphanageAddChildActivity.this, "There was problem occurred in getting you registered.So Please Try Again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadImageToFirebaseStorage(ChildModel childModel) {
        String childFolderName = childModel.getChildId() + "-" + childModel.getChildFullName().toLowerCase();
        String orphanageFolderName = orphanageId + "-" + orphanageName.toLowerCase();

        if (imageUri != null) {
            String ref = "orphanages/" + orphanageFolderName + "/children/" + childFolderName + "/images/" + childModel.getChildFullName().toLowerCase() + "-profile-img" + "." + getFileExtension(imageUri);
            StorageReference childImageRef = storageRef.child(ref);

            String path = childImageRef.getPath();
            String imageName = childImageRef.getName();
            Log.d(TAG, "path is " + path + " and image name is " + imageName);

            UploadTask uploadTask = childImageRef.putFile(imageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(TAG, "uploadImageToFirebaseStorage " + exception.getMessage());
                    Toast.makeText(
                            OrphanageAddChildActivity.this,
                            "Your image did not got successfully uploaded.Please contact the administration team",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "uploadImageToFirebaseStorage: Your image got successfully uploaded");
                }
            });

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return childImageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null) {
                            // We are here updating the event
                            updateChildDataWithImageUrl(childModel.getChildId(), downloadUri);
                        }
                    } else {
                        Log.d(TAG,"Here error occurred is: " + task.getException().getMessage());
                    }
                }
            });
        }
    }

    private void updateChildDataWithImageUrl(String childId,Uri downloadUri) {
        children.document(childId)
                .update(CHILD_IMAGE_FIELD, downloadUri.toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated! with path name is ${downloadUri}");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    private void launchGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeType = MimeTypeMap.getSingleton();
        return mimeType.getExtensionFromMimeType(contentResolver.getType(uri)).toString();
    }

    private boolean checkWhetherAllFieldsAreFilledAndCorrect() {
        if (binding.etOrphanageAddChildName.getText().toString().isEmpty() && binding.etOrphanageAddChildName.getText().toString().trim().isEmpty()) {
            Toast.makeText(OrphanageAddChildActivity.this, "Child Name field is empty or blank.Please fill this field.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageAddChildOrphanageId.getText().toString().isEmpty() && binding.etOrphanageAddChildOrphanageId.getText().toString().trim().isEmpty()) {
            Toast.makeText(OrphanageAddChildActivity.this, "Child Orphanage-ID field is empty or blank.Please fill this field.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (imageUri == null) {
            Toast.makeText(OrphanageAddChildActivity.this, "Child Image is not choosen.Please choose a child image.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.dropdownMenuGender.getEditText().getText().toString().isEmpty() && binding.dropdownMenuGender.getEditText().getText().toString().trim().isEmpty()) {
            Toast.makeText(OrphanageAddChildActivity.this, "Child Gender field is empty or blank.Please fill this field.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOrphanageAddChildDob.getText().toString().isEmpty() && binding.etOrphanageAddChildDob.getText().toString().trim().isEmpty()) {
            Toast.makeText(OrphanageAddChildActivity.this, "Child Date-Of-Birth field is empty or blank.Please fill this field.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private ChildModel getChildData() {
        return new ChildModel(
                binding.etOrphanageAddChildDob.getText().toString(),
                capitalize(binding.etOrphanageAddChildName.getText().toString()),
                binding.dropdownMenuGender.getEditText().getText().toString(),
                binding.etOrphanageAddChildOrphanageId.getText().toString(),
                null,
                null
        );
    }

    private String capitalize(String fieldValue) {
        return fieldValue.substring(0, 1).toUpperCase() + fieldValue.substring(1).toLowerCase();
    }
}