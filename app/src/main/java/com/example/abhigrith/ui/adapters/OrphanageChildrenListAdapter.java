package com.example.abhigrith.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.abhigrith.R;
import com.example.abhigrith.databinding.RecyclerViewParentChildAdoptionListItemBinding;
import com.example.abhigrith.models.ChildModel;
import com.example.abhigrith.util.enums.Gender;
import com.example.abhigrith.util.interfaces.OnChildItemClickListener;
import com.example.abhigrith.util.interfaces.OnListItemClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class OrphanageChildrenListAdapter extends FirestoreRecyclerAdapter<ChildModel, OrphanageChildrenListAdapter.ChildViewHolder> {

    private final FirestoreRecyclerOptions<ChildModel> options;
    private final OnChildItemClickListener onChildItemClickListener;

    public OrphanageChildrenListAdapter(@NonNull FirestoreRecyclerOptions<ChildModel> options, OnChildItemClickListener onChildItemClickListener) {
        super(options);
        this.options = options;
        this.onChildItemClickListener = onChildItemClickListener;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewParentChildAdoptionListItemBinding binding = RecyclerViewParentChildAdoptionListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChildViewHolder(binding, options, onChildItemClickListener);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChildViewHolder holder, int position, @NonNull ChildModel model) {
        holder.bindChildData(model);
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RecyclerViewParentChildAdoptionListItemBinding binding;
        private final FirestoreRecyclerOptions<ChildModel> options;
        private final OnChildItemClickListener onChildItemClickListener;

        public ChildViewHolder(@NonNull RecyclerViewParentChildAdoptionListItemBinding binding, FirestoreRecyclerOptions<ChildModel> options, OnChildItemClickListener onChildItemClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.options = options;
            this.onChildItemClickListener = onChildItemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ChildModel child = options.getSnapshots().get(getAbsoluteAdapterPosition());
            onChildItemClickListener.onChildItemClick(child);
        }

        private void bindChildData(ChildModel model) {
            binding.tvParentAdoptionRequestChildFullName.setText(model.getChildFullName());
            binding.tvParentAdoptionRequestChildGender.setText(model.getChildGender());
            binding.tvParentAdoptionRequestChildDateOfBirth.setText(model.getChildDateOfBirth());

            if (model.getChildGender().equals(Gender.MALE.getGender())) {
                Glide.with(itemView)
                        .load(model.getChildImageUrl())
                        .error(R.drawable.male_child_profile)
                        .into(binding.ivParentAdoptionRequestChildProfilePic);
            } else {
                Glide.with(itemView)
                        .load(model.getChildImageUrl())
                        .error(R.drawable.female_child_profile)
                        .into(binding.ivParentAdoptionRequestChildProfilePic);
            }
        }
    }
}
