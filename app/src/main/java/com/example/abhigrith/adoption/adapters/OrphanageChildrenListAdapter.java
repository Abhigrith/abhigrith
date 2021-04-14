package com.example.abhigrith.adoption.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abhigrith.R;
import com.example.abhigrith.adoption.interfaces.OnDocumentCheckListener;
import com.example.abhigrith.adoption.interfaces.OnRecyclerViewItemClickListener;
import com.example.abhigrith.adoption.models.ChildModel;
import com.example.abhigrith.databinding.RecyclerViewChildListItemBinding;
import com.example.abhigrith.enums.Gender;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class OrphanageChildrenListAdapter extends FirestoreRecyclerAdapter<ChildModel, OrphanageChildrenListAdapter.OrphanageChildrenViewHolder> {

    private FirestoreRecyclerOptions<ChildModel> options;
    private RecyclerViewChildListItemBinding binding;
    private OnDocumentCheckListener onDocumentCheckListener;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public OrphanageChildrenListAdapter(OnDocumentCheckListener onDocumentCheckListener, OnRecyclerViewItemClickListener onRecyclerViewItemClickListener, @NonNull FirestoreRecyclerOptions<ChildModel> options) {
        super(options);
        this.onDocumentCheckListener = onDocumentCheckListener;
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        this.options = options;
    }

    @NonNull
    @Override
    public OrphanageChildrenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerViewChildListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrphanageChildrenViewHolder(binding);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrphanageChildrenViewHolder holder, int position, @NonNull ChildModel model) {
        holder.bindOrphanageChildView(model);
    }

    @Override
    public void onDataChanged() {
        // Called each time there is a new query snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        if (options.getSnapshots().isEmpty()) {
            onDocumentCheckListener.documentsDonotExist();
        }
    }

    public class OrphanageChildrenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String TAG = "ViewHolder";
        private RecyclerViewChildListItemBinding binding;

        public OrphanageChildrenViewHolder(@NonNull RecyclerViewChildListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecyclerViewItemClickListener.onOrphanageItemClick(getAbsoluteAdapterPosition());
        }

        public void bindOrphanageChildView(ChildModel model) {
            Log.d(TAG, "bindOrphanageChildView: " + model.getChildFullName());
            binding.tvChildFullName.setText(model.getChildFullName());
            Log.d(TAG, "bindOrphanageChildView: " + model.getChildDateOfBirth());
            binding.tvChildDateOfBirth.setText(model.getChildDateOfBirth());

            if(model.getChildGender().equals(Gender.MALE.getGender())){
                binding.ivChildAnimated.setImageResource(R.drawable.male_child_profile);
            } else if(model.getChildGender().equals(Gender.FEMALE.getGender())) {
                binding.ivChildAnimated.setImageResource(R.drawable.female_child_profile);
            }

            Log.d(TAG, "bindOrphanageChildView: " + model.getChildGender());
            Log.d(TAG, "bindOrphanageChildView: " + model.getChildId());
            Log.d(TAG, "bindOrphanageChildView: " + model.getChildImageUrl());
        }
    }
}
