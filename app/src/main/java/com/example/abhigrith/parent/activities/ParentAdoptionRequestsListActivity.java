package com.example.abhigrith.parent.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abhigrith.adoption.models.ChildModel;
import com.example.abhigrith.databinding.ActivityParentAdoptionRequestBinding;
import com.example.abhigrith.parent.adapters.ParentSelectedChildrenListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class ParentAdoptionRequestsListActivity extends AppCompatActivity {

    private static final String TAG = "AdoptionRequest";
    private static final String APP_SHARED_PREFERENCES = "APP-PREFERENCES";
    private static final String PARENT_EMAIL_ID = "parent-email-id";
    private static final String COLLECTION_PATH = "parents_info";
    private static final String PARENT_REQUESTED_CHILDREN = "adoptionRequestForChildren";
    private static final String PARENTS_FOR_CHILDREN = "childParentRequestForAdoption";

    private ActivityParentAdoptionRequestBinding binding;

    private List<DocumentReference> childrenDocuments;
    private DocumentReference parentsDocReference;
    private ListenerRegistration parentDocListener;

    private ParentSelectedChildrenListAdapter adapter;
    private ArrayList<ChildModel> children;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentAdoptionRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getSharedPreferences(APP_SHARED_PREFERENCES, MODE_PRIVATE);
        String emailId = preferences.getString(PARENT_EMAIL_ID, null);

        if (emailId != null) {
            parentsDocReference = firestore.collection(COLLECTION_PATH).document(emailId);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (parentsDocReference != null) {
            parentDocListener = parentsDocReference.addSnapshotListener((snapshot, error) -> {

                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());

                    childrenDocuments = (List<DocumentReference>) snapshot.get(PARENT_REQUESTED_CHILDREN);

                    if (childrenDocuments != null) {
                        Log.d(TAG, "82 : onComplete : " + childrenDocuments.toString());
                        children = new ArrayList<ChildModel>();

                        for (DocumentReference childDocument : childrenDocuments) {
                            childDocument.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot snapshot) {
                                    ChildModel child = snapshot.toObject(ChildModel.class);
                                    children.add(child);
                                    adapter.notifyItemInserted(children.size() - 1);
                                    Log.d(TAG, "92 : onSuccess: " + children.toString());
                                }
                            });
                        }

                        setupParentSelectedChildrenList(children);
                        Log.d(TAG, "96 : onSuccess: " + children.toString());
                    }
                } else {
                    Log.d(TAG, "No such document or Current data: null");
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (parentDocListener != null) {
            parentDocListener.remove();
        }
    }

    private void removeSelectedChild(DocumentReference selectedChildDocReference) {
        parentsDocReference.update(PARENT_REQUESTED_CHILDREN, FieldValue.arrayRemove(selectedChildDocReference));
        selectedChildDocReference.update(PARENTS_FOR_CHILDREN, FieldValue.arrayRemove(parentsDocReference));
    }

    private void setupParentSelectedChildrenList(List<ChildModel> children) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.rvParentSelectedChildrenList.setLayoutManager(linearLayoutManager);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvParentSelectedChildrenList);

        adapter = new ParentSelectedChildrenListAdapter(children);
        binding.rvParentSelectedChildrenList.setAdapter(adapter);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            RecyclerView.Adapter<? extends RecyclerView.ViewHolder> parentAdapter = viewHolder.getBindingAdapter();

            if (parentAdapter != null) {
                int position = viewHolder.getAbsoluteAdapterPosition();
                String childId = children.get(position).getChildId();

                for(DocumentReference selectedChildDocReference : childrenDocuments) {
                    Log.d(TAG, "onSwiped: " + selectedChildDocReference.getId());

                    if(childId.equals(selectedChildDocReference.getId())){
                        adapter.notifyItemRemoved(position);
                        removeSelectedChild(selectedChildDocReference);
                        childrenDocuments.remove(position);

                        Snackbar.make(viewHolder.itemView, "You have removed the selected child", Snackbar.LENGTH_LONG)
                                .setAnchorView(viewHolder.itemView)
                                .show();

                        return;
                    }
                }
            }
        }
    };
}
